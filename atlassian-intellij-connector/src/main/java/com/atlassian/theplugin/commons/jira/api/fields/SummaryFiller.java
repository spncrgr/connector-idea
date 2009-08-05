package com.atlassian.theplugin.commons.jira.api.fields;

import com.atlassian.theplugin.commons.jira.api.JIRAIssue;
import com.atlassian.theplugin.commons.jira.api.soap.axis.RemoteIssue;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jgorycki
 * Date: Apr 6, 2009
 * Time: 4:21:26 PM
 */
public class SummaryFiller implements FieldFiller {
	public List<String> getFieldValues(String field, JIRAIssue detailedIssue) {
		RemoteIssue ri = (RemoteIssue) detailedIssue.getRawSoapIssue();
		if (ri == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		result.add(ri.getSummary());
		return result;
	}
}
