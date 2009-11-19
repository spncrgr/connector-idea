package com.atlassian.theplugin.jira.model.presetfilters;

import com.atlassian.theplugin.jira.model.JiraPresetFilter;
import com.atlassian.theplugin.commons.jira.JiraServerData;

/**
 * User: kalamon
 * Date: 2009-11-18
 * Time: 14:42:34
 *
 * Runs query for: "reporterSelect=issue_current_user&sorter/field=updated"
 *
 * note that on teh web this preset sorts by priority. I have changed it because it seems to make a bit more sense
 *
 */
public class ReportedByMePresetFilter extends JiraPresetFilter {
    public ReportedByMePresetFilter(JiraServerData jiraServer) {
        super(jiraServer);
    }

    public String getName() {
        return "Reported by me";
    }

    public String getQueryString() {
        return "reporterSelect=issue_current_user";
    }

    public String getSortBy() {
        return "updated";
    }
}