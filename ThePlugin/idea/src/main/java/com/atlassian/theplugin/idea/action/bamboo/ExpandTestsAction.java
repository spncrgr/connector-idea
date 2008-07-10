package com.atlassian.theplugin.idea.action.bamboo;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.atlassian.theplugin.idea.bamboo.TestResultsToolWindow;

public class ExpandTestsAction extends AnAction {
	public void actionPerformed(AnActionEvent event) {
		TestResultsToolWindow.getTestTree(event.getPlace()).expand();
	}
}
