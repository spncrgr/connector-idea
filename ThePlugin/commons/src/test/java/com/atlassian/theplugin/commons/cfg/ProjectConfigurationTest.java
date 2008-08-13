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
package com.atlassian.theplugin.commons.cfg;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import com.atlassian.theplugin.commons.util.MiscUtil;

import java.util.Iterator;

/**
 * ProjectConfiguration Tester.
 *
 * @author wseliga
 */
public class ProjectConfigurationTest extends TestCase {

	private static final BambooServerCfg BAMBOO_1 = new BambooServerCfg("mybamboo", new ServerId());
	private static final CrucibleServerCfg CRUCIBLE_1 = new CrucibleServerCfg("mycrucible", new ServerId());
	ProjectConfiguration projectCfg = new ProjectConfiguration(MiscUtil.buildArrayList(BAMBOO_1, CRUCIBLE_1));
	ProjectConfiguration anotherCfg = new ProjectConfiguration(projectCfg);

    public void testCopyConstructor() {
		assertEquals(projectCfg, anotherCfg);
	}

    public void testHashCode() {
		assertEquals(projectCfg.hashCode(), anotherCfg.hashCode());
    }

	public void testGetClone() {
		final ProjectConfiguration clone = projectCfg.getClone();
		assertNotSame(clone, projectCfg);
		assertEquals(projectCfg, clone);
		assertEquals(projectCfg.getServers(), clone.getServers());

		final Iterator<ServerCfg> oIt = projectCfg.getServers().iterator();
		final Iterator<ServerCfg> cIt = clone.getServers().iterator();
		while (oIt.hasNext() && cIt.hasNext()) {
			final ServerCfg oServer = oIt.next();
			final ServerCfg cServer = cIt.next();
			assertNotSame(oServer, cServer);
			assertEquals(oServer, cServer);
		}
	}

}
