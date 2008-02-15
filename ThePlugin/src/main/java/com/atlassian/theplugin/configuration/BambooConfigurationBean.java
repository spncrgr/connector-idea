package com.atlassian.theplugin.configuration;

import com.intellij.util.xmlb.annotations.Transient;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * Bean storing information about Bamboo servers etc.<p>
 * The class serves both as a configuration provider for plugin logic and Bean for persistence.
 */
public class BambooConfigurationBean extends AbstractConfigurationBean {
	public BambooConfigurationBean() {
        super();
    }

	@Override
	@Transient
	public void storeServer(Server server) {
		ServerBean foundServer = (ServerBean) getServer(server);
		if (foundServer == null) {
			servers.add((ServerBean) server);
		} else {
			foundServer.setName(server.getName());
			foundServer.setPasswordString(server.getPasswordString(), server.getShouldPasswordBeStored());
			foundServer.setUrlString(server.getUrlString());
			foundServer.setUserName(server.getUserName());
			foundServer.setEnabled(server.getEnabled());
			List<SubscribedPlanBean> subscribedPlans = new ArrayList<SubscribedPlanBean>();
			for (SubscribedPlan subPlan : server.getSubscribedPlans()) {
				subscribedPlans.add((SubscribedPlanBean) subPlan);
			}
			foundServer.setSubscribedPlansData(subscribedPlans);
		}
	}

	public BambooConfigurationBean(ProductServerConfiguration cfg) {
		super(cfg);
	}
}
