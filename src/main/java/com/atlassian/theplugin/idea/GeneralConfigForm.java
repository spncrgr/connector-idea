package com.atlassian.theplugin.idea;

import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: lguminski
 * Date: Feb 26, 2008
 * Time: 9:39:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralConfigForm {
	private JCheckBox chkAutoUpdateEnabled;
	private JPanel mainPanel;
	private boolean autoUpdateEnabled;

	public Component getRootPane() {
		return mainPanel;
	}

	public boolean getIsAutoUpdateEnabled() {
		return autoUpdateEnabled;
	}

	public void setAutoUpdateEnabled(boolean autoUpdateEnabled) {
		this.autoUpdateEnabled = autoUpdateEnabled;
	}

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		chkAutoUpdateEnabled = new JCheckBox();
		chkAutoUpdateEnabled.setText("Auto Update Enabled");
		mainPanel.add(chkAutoUpdateEnabled, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		mainPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return mainPanel;
	}
}
