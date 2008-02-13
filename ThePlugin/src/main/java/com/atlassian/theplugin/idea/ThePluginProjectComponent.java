package com.atlassian.theplugin.idea;

import com.atlassian.theplugin.bamboo.BuildStatus;
import com.atlassian.theplugin.bamboo.HtmlBambooStatusListener;
import com.atlassian.theplugin.crucible.HtmlCrucibleStatusListener;
import com.atlassian.theplugin.idea.bamboo.BambooStatusChecker;
import com.atlassian.theplugin.idea.bamboo.BambooStatusIcon;
import com.atlassian.theplugin.idea.bamboo.BambooToolWindowPanel;
import com.atlassian.theplugin.idea.crucible.CruciblePatchSubmitExecutor;
import com.atlassian.theplugin.idea.crucible.CrucibleStatusChecker;
import com.atlassian.theplugin.idea.crucible.CrucibleToolWindowPanel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.wm.*;
import com.intellij.peer.PeerFactory;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Per-project plugin component.
 */
public class ThePluginProjectComponent implements ProjectComponent {
	private final Project project;
	private StatusBar statusBar;
	private BambooStatusIcon statusBarIcon;
	private BambooStatusChecker bambooStatusChecker;
    private HtmlBambooStatusListener iconBambooStatusListener;
    private HtmlBambooStatusListener toolWindowBambooListener;
	private BambooStatusListenerImpl simpleBambooStatusListener;

    private CrucibleStatusChecker crucibleStatusChecker;
    private HtmlCrucibleStatusListener toolWindowCrucibleListener;

	private ToolWindowManager toolWindowManager;
	public static final String TOOL_WINDOW_NAME = "Atlassian";
	private ToolWindow toolWindow;
	private static final String THE_PLUGIN_TOOL_WINDOW_ICON = "/icons/thePlugin_15x10.png";
	private boolean enabled;

    public ThePluginProjectComponent(Project project) {
		this.project = project;

		// make findBugs happy
		bambooStatusChecker = null;
        crucibleStatusChecker = null;
        statusBar = null;
		statusBarIcon = null;
		enabled = false;
	}

	public void initComponent() {
		System.out.println("Start: Init ThePlugin project component.");
		System.out.println("End: Init ThePlugin project component.");
		ChangeListManager.getInstance(project).registerCommitExecutor(new CruciblePatchSubmitExecutor(project));
	}

	public void disposeComponent() {
		System.out.println("Start: Dispose ThePlugin project component");
		System.out.println("End: Dispose ThePlugin project component");
	}

	@NotNull
	public String getComponentName() {
		return "ThePluginProjectComponent";
	}

	public void enablePlugin() {
		ThePluginApplicationComponent appComponent =
				ApplicationManager.getApplication().getComponent(ThePluginApplicationComponent.class);

		if (appComponent.getState().isPluginEnabled() && !enabled) {

			// create tool window on the right
			toolWindowManager = ToolWindowManager.getInstance(project);
			toolWindow = toolWindowManager.registerToolWindow(TOOL_WINDOW_NAME, true, ToolWindowAnchor.RIGHT);
			Icon toolWindowIcon = IconLoader.getIcon(THE_PLUGIN_TOOL_WINDOW_ICON);
			toolWindow.setIcon(toolWindowIcon);

			// create tool window content
			BambooToolWindowPanel bambooToolWindowPanel = new BambooToolWindowPanel();
			PeerFactory peerFactory = PeerFactory.getInstance();

            Content bambooToolWindow = peerFactory.getContentFactory().createContent(
					bambooToolWindowPanel, "Bamboo", false);
            bambooToolWindow.setIcon(IconLoader.getIcon("/icons/bamboo-blue-16.png"));
			bambooToolWindow.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);
			toolWindow.getContentManager().addContent(bambooToolWindow);

            CrucibleToolWindowPanel crucibleToolWindowPanel = new CrucibleToolWindowPanel();
            Content crucibleToolWindow = peerFactory.getContentFactory().createContent(
                    crucibleToolWindowPanel, "Crucible", false);
            crucibleToolWindow.setIcon(IconLoader.getIcon("/icons/crucible-blue-16.png"));
			crucibleToolWindow.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);
			toolWindow.getContentManager().addContent(crucibleToolWindow);

			bambooStatusChecker = appComponent.getBambooStatusChecker();

            // add tool window bamboo content listener to bamboo checker thread
			toolWindowBambooListener = new HtmlBambooStatusListener(bambooToolWindowPanel.getBambooContent());
			bambooStatusChecker.registerListener(toolWindowBambooListener);

			// create status bar icon
			statusBarIcon = new BambooStatusIcon(this);
			statusBarIcon.updateBambooStatus(BuildStatus.UNKNOWN,
					"<div style=\"font-size:12pt ; font-family: arial, helvetica, sans-serif\">"
					+ "Waiting for Bamboo build statuses."
					+ "</div>");

			// add icon listener to bamboo checker thread
			iconBambooStatusListener = new HtmlBambooStatusListener(statusBarIcon);
			bambooStatusChecker.registerListener(iconBambooStatusListener);

			// add simple bamboo listener to bamboo checker thread
			// this listener shows idea tooltip when buld failed
			simpleBambooStatusListener = new BambooStatusListenerImpl(project);
			bambooStatusChecker.registerListener(simpleBambooStatusListener);

			// add icon to status bar
			statusBar = WindowManager.getInstance().getStatusBar(project);
			statusBar.addCustomIndicationComponent(statusBarIcon);

            // setup Crucible status checker and listeners
            crucibleStatusChecker = appComponent.getCrucibleStatusChecker();
            toolWindowCrucibleListener = new HtmlCrucibleStatusListener(crucibleToolWindowPanel.getCrucibleContent());
            crucibleStatusChecker.registerListener(toolWindowCrucibleListener);
			crucibleStatusChecker.registerListener(appComponent.getUserDataContext());

			// now fire all status checkers
            appComponent.triggerStatusCheckers();
            enabled = true;
		}
	}

	public void disablePlugin() {
		if (enabled) {
			// remove icon from status bar
			statusBar.removeCustomIndicationComponent(statusBarIcon);
			statusBarIcon = null;

			// remove tool window
			toolWindowManager.unregisterToolWindow(TOOL_WINDOW_NAME);
			toolWindow = null;

			// unregister listeners
			bambooStatusChecker.unregisterListener(iconBambooStatusListener);
			bambooStatusChecker.unregisterListener(toolWindowBambooListener);
			bambooStatusChecker.unregisterListener(simpleBambooStatusListener);
			crucibleStatusChecker.unregisterListener(toolWindowCrucibleListener);
			enabled = false;
		}
	}

	public void projectOpened() {
		System.out.println("Start: Project open");
		enablePlugin();
		System.out.println("End: Project open");



		//WindowManager.getInstance().getStatusBar(project).fireNotificationPopup(panel, null);
	}

	public void projectClosed() {
		System.out.println("Start: Project close");
		disablePlugin();
		System.out.println("End: Project close");
	}

	public Project getProject() {
		return project;
	}

	public ToolWindow getToolWindow() {
		return toolWindow;
	}
}
