package com.atlassian.theplugin.jira.model;

import com.atlassian.theplugin.commons.cfg.JiraServerCfg;
import com.atlassian.theplugin.jira.JIRAServerFacade;
import com.atlassian.theplugin.jira.JIRAServerFacadeImpl;
import com.atlassian.theplugin.jira.api.JIRAException;
import com.atlassian.theplugin.jira.api.JIRAIssue;
import com.atlassian.theplugin.jira.api.JIRAQueryFragment;
import com.atlassian.theplugin.jira.api.JIRASavedFilter;

import java.util.ArrayList;
import java.util.List;

public final class JIRAIssueListModelBuilderImpl implements JIRAIssueListModelBuilder {
	private JiraServerCfg server;
	private JIRASavedFilter savedFilter;
	private List<JIRAQueryFragment> customFilter;
	private JIRAServerFacade facade;

	private static final String SORT_BY = "priority";
	private static final String SORT_ORDER = "DESC";

	private int startFrom;
	private JIRAIssueListModel model;

	public JIRAIssueListModelBuilderImpl() {
		facade = JIRAServerFacadeImpl.getInstance();
		startFrom = 0;
	}

	// for testing
	public void setFacade(JIRAServerFacade newFacade) {
		facade = newFacade;
	}

	public void setModel(final JIRAIssueListModel model) {
		this.model = model;
	}

	public JIRAIssueListModel getModel() {
		return model;
	}

	public void setServer(JiraServerCfg server) {
		this.server = server;
	}

	public JiraServerCfg getServer() {
		return server;
	}

	public void setSavedFilter(JIRASavedFilter filter) {
		savedFilter = filter;
		customFilter = null;
		startFrom = 0;
	}

	public void setCustomFilter(List<JIRAQueryFragment> query) {
		customFilter = query;
		savedFilter = null;
		startFrom = 0;
	}

	public synchronized void addIssuesToModel(int size, boolean reload) throws JIRAException {
		List<JIRAIssue> l = null;
		try {
			model.setModelFrozen(true);
			if (server == null || model == null || !(customFilter != null || savedFilter != null)) {
				if (model != null) {
					model.clear();
					model.fireModelChanged();
				}
				return;
			}

			if (reload) {
				startFrom = 0;
				model.clear();
			}

			if (customFilter != null && customFilter.size() > 0) {
				l = facade.getIssues(server, customFilter, SORT_BY, SORT_ORDER, startFrom, size);
				model.addIssues(l);
			}
			if (savedFilter != null) {
				List<JIRAQueryFragment> query = new ArrayList<JIRAQueryFragment>();
				query.add(savedFilter);
				l = facade.getSavedFilterIssues(server, query, SORT_BY, SORT_ORDER, startFrom, size);
				model.addIssues(l);
			}
			startFrom += l != null ? l.size() : 0;
		} finally {
			if (model != null) {
				model.fireModelChanged();
				model.fireIssuesLoaded(l != null ? l.size() : 0);
				model.setModelFrozen(false);
			}
		}
	}

	public synchronized void updateIssue(final JIRAIssue issue) throws JIRAException {
		model.setModelFrozen(true);
		if (model == null || server == null) {
			return;
		}

		JIRAIssue updatedIssue = facade.getIssueUpdate(server, issue);
		try {
			model.setModelFrozen(true);
			model.setIssue(updatedIssue);
		} finally {
			model.setModelFrozen(false);
		}

		model.fireModelChanged();

	}

	public synchronized void reset() {
		int size = model.getIssues().size();
		if (size > 0) {
			model.clear();
			startFrom = 0;
			model.fireModelChanged();
		}
	}

	public boolean isModelFrozen() {
		return model.isModelFrozen();
	}

	public void setModelFrozen(boolean frozen) {
		this.model.setModelFrozen(frozen);
	}

	public void addFrozenModelListener(FrozenModelListener listener) {
		this.model.addFrozenModelListener(listener);
	}

	public void removeFrozenModelListener(FrozenModelListener listener) {
		this.model.removeFrozenModelListener(listener);
	}
}