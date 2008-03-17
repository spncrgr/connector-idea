package com.atlassian.theplugin.idea.autoupdate;

import com.atlassian.theplugin.exception.ThePluginException;
import com.atlassian.theplugin.util.InfoServer;

/**
 * Created by IntelliJ IDEA.
 * User: lguminski
 * Date: Mar 13, 2008
 * Time: 11:17:21 AM
 * To change this template use File | Settings | File Templates.
 */
public interface UpdateActionHandler {
	void doAction(InfoServer.VersionInfo versionInfo) throws ThePluginException;
}
