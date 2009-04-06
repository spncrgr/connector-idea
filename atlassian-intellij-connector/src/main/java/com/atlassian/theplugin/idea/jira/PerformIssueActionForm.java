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

import com.atlassian.theplugin.jira.JiraActionFieldType;
import com.atlassian.theplugin.jira.api.JIRAActionField;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Jacek Jaroczynski
 */
public class PerformIssueActionForm extends DialogWrapper {
	private JTextField textField1;
	private JPanel root;
	private JPanel contentPanel;
	private JTextArea textArea2;
	private JTextArea textArea1;

	public PerformIssueActionForm(final Project project, final String name, final List<JIRAActionField> fields) {
		super(project, true);

		$$$setupUI$$$();

		createContent(fields);

		init();
		pack();

		setTitle(name);
//		getOKAction().putValue(Action.NAME, name);
	}

	private void createContent(final List<JIRAActionField> fields) {

////		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		String columns = "3dlu, right:pref, 3dlu, fill:pref:grow, 3dlu";
		String rows = "3dlu"; //, p, 3dlu, fill:pref:grow, 3dlu";

		for (JIRAActionField field : fields) {
			switch (JiraActionFieldType.getFiledTypeForFieldId(field)) {
				case TEXT_FIELD:
					rows += ", p, 3dlu";
					break;
				case TEXT_AREA:
					rows += ", fill:pref:grow, 3dlu";
					break;
				case TIME_SPENT:
				case UNSUPPORTED:
				case USER:
				case VERSIONS:
				case CALENDAR:
				case COMPONENTS:
				case ISSUE_TYPE:
				case PRIORITY:
				case RESOLUTION:
				default:
					break;
			}
		}

		int y = 2;

		contentPanel.setLayout(new FormLayout(columns, rows));
		final CellConstraints cc = new CellConstraints();

		for (JIRAActionField field : fields) {


			Component component = null;

			switch (JiraActionFieldType.getFiledTypeForFieldId(field)) {
				case TEXT_FIELD:
					component = new JTextField();
					break;
				case TEXT_AREA:
					JTextArea textArea = new JTextArea();
					textArea.setRows(6);
					textArea.setColumns(20);
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
					component = new JScrollPane(textArea,
							ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					break;
				case TIME_SPENT:
				case UNSUPPORTED:
				case USER:
				case VERSIONS:
				case CALENDAR:
				case COMPONENTS:
				case ISSUE_TYPE:
				case PRIORITY:
				case RESOLUTION:
				default:
					break;
			}

			if (component != null) {
				final JLabel label = new JLabel(field.getFieldId() + ":");
				contentPanel.add(label, cc.xy(2, y, CellConstraints.RIGHT, CellConstraints.TOP));

				contentPanel.add(component, cc.xy(4, y));
				y += 2;
			}


		}

//		final JLabel label1 = new JLabel("sfsfssdfssd");
//		contentPanel.add(label1, cc.xy(2, 2));
////
//		textField1 = new JTextField();
//		contentPanel.add(textField1, cc.xy(4, 2));
////
//		final JLabel label2 = new JLabel("aaa");
//		contentPanel.add(label2, cc.xy(2, 4, CellConstraints.RIGHT, CellConstraints.TOP));
//
//		textArea1 = new JTextArea();
//		textArea1.setRows(8);
//		textArea1.setColumns(20);
////		textArea1.setMinimumSize(new Dimension(120, 80));
//		textArea1.setLineWrap(true);
//		textArea1.setWrapStyleWord(true);
////		textArea1.setBorder(new BlockBorder());
//		JScrollPane scroll = new JScrollPane(textArea1,
//				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		contentPanel.add(scroll, cc.xy(4, 4));
//		contentPanel.add(textArea1, cc.xy(4, 4));
	}

	protected void doOKAction() {
		super.doOKAction();
	}

	@Nullable
	protected JComponent createCenterPanel() {
		return root;
	}

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!

	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		root = new JPanel();
		root.setLayout(new BorderLayout(0, 0));
		final JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setHorizontalScrollBarPolicy(31);
		root.add(scrollPane1, BorderLayout.CENTER);
		contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
		scrollPane1.setViewportView(contentPanel);
		final JLabel label1 = new JLabel();
		label1.setHorizontalAlignment(10);
		label1.setText("Labeldfasfasfsdf");
		contentPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL,
				GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		textField1 = new JTextField();
		contentPanel.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST,
				GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
				new Dimension(150, -1), null, 0, false));
		final JScrollPane scrollPane2 = new JScrollPane();
		contentPanel.add(scrollPane2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
				GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
				GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		textArea2 = new JTextArea();
		scrollPane2.setViewportView(textArea2);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return root;
	}
}
