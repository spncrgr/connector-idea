package com.atlassian.theplugin.idea.jira;

public enum JiraIssueGroupBy {
	NONE("None"),
	PROJECT("Project"),
	TYPE("Type"),
	STATUS("Status"),
	PRIORITY("Priority");

	private String name;

	JiraIssueGroupBy(String name) {
	    this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
