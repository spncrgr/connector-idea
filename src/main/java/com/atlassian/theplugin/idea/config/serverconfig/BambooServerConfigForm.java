package com.atlassian.theplugin.idea.config.serverconfig;

import com.atlassian.theplugin.bamboo.BambooServerFacade;
import com.atlassian.theplugin.bamboo.api.BambooLoginException;
import com.atlassian.theplugin.configuration.Server;
import com.atlassian.theplugin.exception.ThePluginException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.log4j.Category;

import javax.swing.*;
import java.awt.*;

/**
 * Plugin configuration form.
 */
public class BambooServerConfigForm extends JComponent implements ServerPanel {

	private static final Category LOG = Category.getInstance(BambooServerConfigForm.class);

	private JPanel rootComponent;

	private BambooPlansForm planList;
	private transient GenericServerConfigForm genericServerConfigForm;
	private final transient BambooServerFacade bambooServerFacade;

	public BambooServerConfigForm(BambooServerFacade bambooServerFacadeInstance) {
		this.bambooServerFacade = bambooServerFacadeInstance;

		$$$setupUI$$$();
	}

	public void setData(Server aServer) {
		genericServerConfigForm.setData(aServer);
		planList.setEnabled(!aServer.getUseFavourite());
		planList.setData(aServer);

	}

	public Server getData() {
		Server server = genericServerConfigForm.getData();

		Server s = planList.getData();
		server.setSubscribedPlans(s.getSubscribedPlans());
		server.setUseFavourite(s.getUseFavourite());

		return server;
	}

	public boolean isModified() {
		return genericServerConfigForm.isModified() || planList.isModified();
	}


	 public JComponent getRootComponent() {
		 return rootComponent;
	 }

	public void setVisible(boolean visible) {
		rootComponent.setVisible(visible);
	}

	private void createUIComponents() {
		genericServerConfigForm = new GenericServerConfigForm(new ConnectionTester() {
			public void testConnection(String username, String password, String server)
					throws ThePluginException {
				try {
					bambooServerFacade.testServerConnection(server, username, password);
				} catch (BambooLoginException e) {
					throw new ThePluginException(e.getMessage());
				}
			}
		});
		planList = new BambooPlansForm(bambooServerFacade, genericServerConfigForm);
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		rootComponent = new JPanel();
		rootComponent.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		rootComponent.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panel1.add(genericServerConfigForm.$$$getRootComponent$$$(), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, 1, 1, null, null, null, 0, false));
		panel1.add(planList.$$$getRootComponent$$$(), new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return rootComponent;
	}
}
