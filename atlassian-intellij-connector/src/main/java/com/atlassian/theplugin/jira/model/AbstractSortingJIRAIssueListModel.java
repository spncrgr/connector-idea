package com.atlassian.theplugin.jira.model;

import com.atlassian.theplugin.jira.api.JIRAIssue;

import java.util.*;

public abstract class AbstractSortingJIRAIssueListModel	
		extends JIRAIssueListModelListenerHolder implements JIRAIssueListModel {

	private final JIRAIssueListModel parent;

	public AbstractSortingJIRAIssueListModel(JIRAIssueListModel parent) {
		this.parent = parent;
		parent.addModelListener(this);
	}

	public void clear() {
		parent.clear();
	}

	public void addIssue(JIRAIssue issue) {
		parent.addIssue(issue);
	}

	public void addIssues(Collection<JIRAIssue> issues) {
		parent.addIssues(issues);
	}

	protected abstract Comparator<JIRAIssue> getComparator();

	private Collection<JIRAIssue> sort(Collection<JIRAIssue> col) {
		List<JIRAIssue> list = new ArrayList<JIRAIssue>();
		for (JIRAIssue i : col) {
			list.add(i);
		}
		Collections.sort(list, getComparator());
		return list;
	}

	public Collection<JIRAIssue> getIssues() {
		return sort(parent.getIssues());
	}

	public Collection<JIRAIssue> getIssuesNoSubtasks() {
		return sort(parent.getIssuesNoSubtasks());
	}

	public Collection<JIRAIssue> getSubtasks(JIRAIssue p) {
		return sort(parent.getSubtasks(p));
	}

	public void addModelListener(JIRAIssueListModelListener listener) {
		addListener(listener);
	}

	public void removeModelListener(JIRAIssueListModelListener listener) {
		removeListener(listener);
	}

	public void setSeletedIssue(JIRAIssue issue) {
		parent.setSeletedIssue(issue);
	}

	public JIRAIssue getSelectedIssue() {
		return parent.getSelectedIssue();
	}

	public void setIssue(JIRAIssue issue) {
		parent.setIssue(issue);
	}

	public void fireModelChanged() {
		modelChanged(this);
	}

	public void fireIssuesLoaded(int numberOfLoadedIssues) {
		issuesLoaded(this, numberOfLoadedIssues);
	}

	public boolean isModelFrozen() {
		return parent.isModelFrozen();
	}

	public void setModelFrozen(boolean frozen) {
		parent.setModelFrozen(frozen);
	}

	public void addFrozenModelListener(FrozenModelListener listener) {
		parent.addFrozenModelListener(listener);
	}

	public void removeFrozenModelListener(FrozenModelListener listener) {
		parent.removeFrozenModelListener(listener);
	}
}
