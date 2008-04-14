/**
 * Copyright (C) 2008 Atlassian
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atlassian.theplugin.idea.jira;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
import java.awt.*;

public class ChangesetCreate extends DialogWrapper {
	private JPanel contentPane;
	private JTextField changesetName;
	private JTextArea changesetComment;
	private JCheckBox isActive;

	public ChangesetCreate(String issueKey) {
		super(false);
		init();
		setTitle("Create Changelist for " + issueKey);
		changesetName.setDragEnabled(false);
		getOKAction().putValue(Action.NAME, "Create");
	}

	public void setChangesetName(String txt) {
		changesetName.setText(txt);
		changesetName.setCaretPosition(0);
	}

	public void setChangestComment(String txt) {
		changesetComment.setText(txt);
	}

	public void setActive(boolean active) {
		isActive.setSelected(active);
	}

	public String getChangesetName() {
		return changesetName.getText();
	}

	public String getChangesetComment() {
		return changesetComment.getText();
	}

	public boolean isActive() {
		return isActive.isSelected();
	}

	protected JComponent createCenterPanel() {
		return contentPane;
	}

	public JComponent getPreferredFocusedComponent() {
		return changesetComment;
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
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
		contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		contentPane.setMaximumSize(new Dimension(-1, -1));
		contentPane.setMinimumSize(new Dimension(-1, -1));
		contentPane.setOpaque(false);
		contentPane.setPreferredSize(new Dimension(500, 200));
		contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12), null));
		changesetName = new JTextField();
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(changesetName, gbc);
		final JScrollPane scrollPane1 = new JScrollPane();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(12, 0, 12, 0);
		contentPane.add(scrollPane1, gbc);
		changesetComment = new JTextArea();
		scrollPane1.setViewportView(changesetComment);
		final JLabel label1 = new JLabel();
		label1.setText("Name:");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(label1, gbc);
		final JLabel label2 = new JLabel();
		label2.setText("Comment:");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 0, 12);
		contentPane.add(label2, gbc);
		isActive = new JCheckBox();
		isActive.setSelected(false);
		isActive.setText("Make this changelist active");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		contentPane.add(isActive, gbc);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}
}
