package com.atlassian.theplugin.idea.action.jira;

import com.atlassian.theplugin.jira.api.JIRAIssue;

@Deprecated
public class CopyIssueKeySummaryAction extends AbstractClipboardAction {

	protected String getCliboardText(final JIRAIssue issue) {
		return issue.getKey() + " - " + issue.getSummary();
	}
}
