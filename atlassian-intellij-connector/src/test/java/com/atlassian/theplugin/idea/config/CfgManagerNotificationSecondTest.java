/**
 * Copyright (C) 2008 Atlassian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atlassian.theplugin.idea.config;

import com.atlassian.theplugin.commons.cfg.*;
import com.atlassian.theplugin.commons.remoteapi.ServerData;
import com.atlassian.theplugin.configuration.WorkspaceConfigurationBean;
import junit.framework.TestCase;
import org.easymock.EasyMock;

/**
 * @author Jacek Jaroczynski
 */
public class CfgManagerNotificationSecondTest extends TestCase {

	//	private CfgManager cfgManager;
	private ConfigurationListener listener;
	private ProjectConfiguration newConf;
	private static final String SUFFIX = "SUFFIX";
	private ProjectCfgManagerImpl projectCfgManager;

	@Override
	public void setUp() throws Exception {
		super.setUp();
//		cfgManager = new CfgManagerImpl();
		projectCfgManager = new ProjectCfgManagerImpl(new WorkspaceConfigurationBean(), null);

		listener = EasyMock.createMock(ConfigurationListener.class);
		projectCfgManager.addProjectConfigurationListener(listener);

		populateServerCfgs();
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	private final BambooServerCfg bamboo1 = new BambooServerCfg("bamboo1", new ServerIdImpl());
	private final BambooServerCfg bamboo2 = new BambooServerCfg("bamboo2", new ServerIdImpl());
	private final JiraServerCfg jira1 = new JiraServerCfg("jira1", new ServerIdImpl(), true);
	private final JiraServerCfg jira2 = new JiraServerCfg("jira2", new ServerIdImpl(), true);

	private void populateServerCfgs() {

		projectCfgManager.addServer(bamboo1);
		projectCfgManager.addServer(jira1);

		newConf = new ProjectConfiguration(projectCfgManager.getProjectConfiguration());
	}

	public void testServerAdded() {

		newConf.getServers().add(bamboo2);
		newConf.getServers().add(jira2);

		// record
		listener.configurationUpdated(newConf);
		listener.serverAdded(getServerData(bamboo2));
		listener.serverAdded(getServerData(jira2));
		listener.jiraServersChanged(newConf);
		listener.bambooServersChanged(newConf);

		// test
		EasyMock.replay(listener);
		projectCfgManager.updateProjectConfiguration(newConf);

		EasyMock.verify(listener);
	}

	private ServerData getServerData(final ServerCfg serverCfg) {
		return new ServerData(serverCfg, new UserCfg(serverCfg.getUsername(), serverCfg.getPassword()));
	}

	public void testServerRemoved() {

		newConf.getServers().remove(bamboo1);
		newConf.getServers().remove(jira1);

		// record
		listener.configurationUpdated(newConf);
		listener.serverRemoved(getServerData(bamboo1));
		listener.serverRemoved(getServerData(jira1));
		listener.jiraServersChanged(newConf);
		listener.bambooServersChanged(newConf);

		// test
		EasyMock.replay(listener);
		projectCfgManager.updateProjectConfiguration(newConf);

		EasyMock.verify(listener);
	}

	public void testServerDisabledEnabled() {

		newConf.getServerCfg(bamboo1.getServerId()).setEnabled(false);
		newConf.getServerCfg(jira1.getServerId()).setEnabled(false);

		// record
		listener.configurationUpdated(newConf);
		listener.serverDisabled(bamboo1.getServerId());
		listener.serverDisabled(jira1.getServerId());
		listener.serverDataChanged(getServerData(bamboo1));
		listener.serverDataChanged(getServerData(jira1));
		listener.jiraServersChanged(newConf);
		listener.bambooServersChanged(newConf);

		// test disabled
		EasyMock.replay(listener);
		projectCfgManager.updateProjectConfiguration(newConf);

		EasyMock.verify(listener);

		// reset
		EasyMock.reset(listener);

		ProjectConfiguration conf = new ProjectConfiguration(newConf);

		conf.getServerCfg(bamboo1.getServerId()).setEnabled(true);
		conf.getServerCfg(jira1.getServerId()).setEnabled(true);

		// record
		listener.configurationUpdated(conf);
		listener.serverEnabled(getServerData(bamboo1));
		listener.serverEnabled(getServerData(jira1));
		listener.serverDataChanged(getServerData(bamboo1));
		listener.serverDataChanged(getServerData(jira1));
		listener.jiraServersChanged(conf);
		listener.bambooServersChanged(conf);

		// test enabled
		EasyMock.replay(listener);
		projectCfgManager.updateProjectConfiguration(conf);

		EasyMock.verify(listener);
	}

	public void testServerLabelChanged() {

		newConf.getServerCfg(bamboo1.getServerId()).setName(bamboo1.getName() + SUFFIX);
		newConf.getServerCfg(jira1.getServerId()).setName(jira1.getName() + SUFFIX);

		// record
		listener.configurationUpdated(newConf);
		listener.serverNameChanged(bamboo1.getServerId());
		listener.serverNameChanged(jira1.getServerId());
		listener.serverDataChanged(getServerData(bamboo1));
		listener.serverDataChanged(getServerData(jira1));
		listener.jiraServersChanged(newConf);
		listener.bambooServersChanged(newConf);

		// test
		EasyMock.replay(listener);
		projectCfgManager.updateProjectConfiguration(newConf);

		EasyMock.verify(listener);
	}

	public void testServerConnectionDataChanged() {

		newConf.getServerCfg(bamboo1.getServerId()).setUrl(bamboo1.getUrl() + SUFFIX);
		newConf.getServerCfg(jira1.getServerId()).setPassword(jira1.getPassword() + SUFFIX);

		// record
		listener.configurationUpdated(newConf);
		listener.serverConnectionDataChanged(bamboo1.getServerId());
		listener.serverConnectionDataChanged(jira1.getServerId());
		listener.serverDataChanged(getServerData(bamboo1));
		listener.serverDataChanged(getServerData(jira1));
		listener.jiraServersChanged(newConf);
		listener.bambooServersChanged(newConf);

		// test
		EasyMock.replay(listener);
		projectCfgManager.updateProjectConfiguration(newConf);

		EasyMock.verify(listener);
	}

	public void testConfigurationMixedUpdate() {

		newConf.getServerCfg(bamboo1.getServerId()).setUrl(bamboo1.getUrl() + SUFFIX);
		newConf.getServerCfg(bamboo1.getServerId()).setName(bamboo1.getName() + SUFFIX);
		newConf.getServerCfg(bamboo1.getServerId()).setEnabled(false);

		newConf.getServers().remove(jira1);
		newConf.getServers().add(bamboo2);

		// record
		listener.configurationUpdated(newConf);
		listener.serverConnectionDataChanged(bamboo1.getServerId());
		listener.serverNameChanged(bamboo1.getServerId());
		listener.serverDisabled(bamboo1.getServerId());
		listener.serverDataChanged(getServerData(bamboo1));
		listener.serverRemoved(getServerData(jira1));
		listener.serverAdded(getServerData(bamboo2));
		listener.bambooServersChanged(newConf);
		listener.jiraServersChanged(newConf);

		// test
		EasyMock.replay(listener);
		projectCfgManager.updateProjectConfiguration(newConf);

		EasyMock.verify(listener);
	}

	public void testServerDataChange() {
		newConf.getServerCfg(bamboo1.getServerId()).setPasswordStored(!bamboo1.isPasswordStored());

		// record
		listener.configurationUpdated(newConf);
		listener.serverDataChanged(getServerData(bamboo1));
		listener.bambooServersChanged(newConf);

		// test
		EasyMock.replay(listener);
		projectCfgManager.updateProjectConfiguration(newConf);

		EasyMock.verify(listener);

	}

	public void testDefaultCredentials() {
		UserCfg userCfg = new UserCfg("userName", "secretPAssword");
		ServerData srvData1 = projectCfgManager.getServerr(bamboo1.getServerId());
		assertEquals(srvData1.getUsername(), bamboo1.getUsername());
		assertEquals(srvData1.getPassword(), bamboo1.getPassword());

		assertFalse(projectCfgManager.isDefaultCredentialsAsked());
		projectCfgManager.setDefaultCredentials(userCfg);
		assertFalse(bamboo1.isUseDefaultCredentials());
		srvData1 = projectCfgManager.getServerr(bamboo1.getServerId());
		assertTrue(projectCfgManager.isDefaultCredentialsAsked());
		assertEquals(srvData1.getUsername(), bamboo1.getUsername());
		assertEquals(srvData1.getPassword(), bamboo1.getPassword());

		bamboo1.setUseDefaultCredentials(true);
		assertTrue(bamboo1.isUseDefaultCredentials());
		srvData1 = projectCfgManager.getServerr(bamboo1.getServerId());
		assertEquals(srvData1.getUsername(), userCfg.getUsername());
		assertEquals(srvData1.getPassword(), userCfg.getPassword());


	}
}