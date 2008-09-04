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

package com.atlassian.theplugin.idea.crucible;

import com.atlassian.theplugin.commons.cfg.CrucibleServerCfg;
import com.atlassian.theplugin.commons.crucible.CrucibleServerFacade;
import com.atlassian.theplugin.commons.crucible.api.model.PermId;
import com.atlassian.theplugin.commons.crucible.api.model.PredefinedFilter;
import com.atlassian.theplugin.commons.crucible.api.model.Review;
import com.atlassian.theplugin.commons.crucible.api.model.SvnRepository;
import com.atlassian.theplugin.commons.exception.ServerPasswordNotProvidedException;
import com.atlassian.theplugin.commons.remoteapi.RemoteApiException;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import static com.intellij.openapi.ui.Messages.showMessageDialog;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

enum AddMode
{
    ADDREVISION,
    ADDPATCH
}

public class CrucibleHelperForm extends DialogWrapper
{
    private JPanel rootComponent;
    private JComboBox reviewComboBox;
    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField moderatorField;
    private JTextArea descriptionArea;
    private JTextField statusField;

    private CrucibleServerFacade crucibleServerFacade;
    private ChangeList[] changes;
    private final com.intellij.openapi.project.Project project;
    private CrucibleServerCfg cfg;
    private PermId permId;
    private String patch;
    private AddMode mode;
    private SvnRepository repo;

    protected CrucibleHelperForm(com.intellij.openapi.project.Project project,
            CrucibleServerCfg cfg,
            CrucibleServerFacade crucibleServerFacade,
            ChangeList[] changes)
    {
        this(project, cfg, crucibleServerFacade);
        this.changes = changes;
        this.mode = AddMode.ADDREVISION;
        setTitle("Add revision to review... ");
        getOKAction().putValue(Action.NAME, "Add revision...");
    }

    protected CrucibleHelperForm(com.intellij.openapi.project.Project project,
            CrucibleServerCfg cfg,
            CrucibleServerFacade crucibleServerFacade,
            String patch)
    {
        this(project, cfg, crucibleServerFacade);
        this.patch = patch;
        this.mode = AddMode.ADDPATCH;
        setTitle("Add patch");
        getOKAction().putValue(Action.NAME, "Add patch...");
    }

    private CrucibleHelperForm(com.intellij.openapi.project.Project project, CrucibleServerCfg cfg,
            CrucibleServerFacade crucibleServerFacade)
    {
        super(false);
        this.cfg = cfg;
        this.crucibleServerFacade = crucibleServerFacade;
        this.project = project;
        $$$setupUI$$$();
        init();

        reviewComboBox.addActionListener(new ActionListener()
        {

            public void actionPerformed(final ActionEvent event)
            {
                if (reviewComboBox.getSelectedItem() != null)
                {
                    if (reviewComboBox.getSelectedItem() instanceof ReviewComboBoxItem)
                    {
                        ReviewComboBoxItem item = (ReviewComboBoxItem) reviewComboBox.getSelectedItem();
                        if (item != null)
                        {
                            final Review review = item.getReview();
                            permId = review.getPermId();
                            idField.setText(review.getPermId().getId());
                            statusField.setText(review.getState().value());
                            titleField.setText(review.getName());
                            authorField.setText(review.getAuthor().getDisplayName());
                            moderatorField.setText(review.getModerator().getDisplayName());
                            descriptionArea.setText(review.getDescription());
                            getOKAction().setEnabled(true);
                        }
                        else
                        {
                            getOKAction().setEnabled(false);
                        }
                    }
                    else
                    {
                        getOKAction().setEnabled(false);
                    }
                }
                else
                {
                    getOKAction().setEnabled(false);
                }
            }
        });

        fillReviewCombos();
    }

    @Override
    public JComponent getPreferredFocusedComponent()
    {
        return this.reviewComboBox;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your code!
     */
    private void $$$setupUI$$$()
    {
        rootComponent = new JPanel();
        rootComponent.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootComponent.setBackground(UIManager.getColor("Button.background"));
        rootComponent.setEnabled(false);
        rootComponent.setMinimumSize(new Dimension(450, 200));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(1, 1, 1, 1), -1, -1));
        rootComponent.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        reviewComboBox = new JComboBox();
        panel1.add(reviewComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Review");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        rootComponent.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
                1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootComponent.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Id");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Title");
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Author");
        panel2.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Moderator");
        panel2.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Description");
        panel2.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        idField = new JTextField();
        idField.setBackground(UIManager.getColor("Button.background"));
        idField.setEnabled(false);
        panel2.add(idField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0,
                false));
        titleField = new JTextField();
        titleField.setBackground(UIManager.getColor("Button.background"));
        titleField.setEnabled(false);
        panel2.add(titleField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0,
                false));
        authorField = new JTextField();
        authorField.setBackground(UIManager.getColor("Button.background"));
        authorField.setEnabled(false);
        panel2.add(authorField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0,
                false));
        moderatorField = new JTextField();
        moderatorField.setBackground(UIManager.getColor("Button.background"));
        moderatorField.setEnabled(false);
        panel2.add(moderatorField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0,
                false));
        descriptionArea = new JTextArea();
        descriptionArea.setBackground(UIManager.getColor("Button.background"));
        descriptionArea.setEnabled(false);
        panel2.add(descriptionArea, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null,
                0, false));
        statusField = new JTextField();
        statusField.setBackground(UIManager.getColor("Button.background"));
        statusField.setEnabled(false);
        panel2.add(statusField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0,
                false));
        final JLabel label7 = new JLabel();
        label7.setText("State");
        panel2.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return rootComponent;
    }

    private static final class ReviewComboBoxItem
    {
        private final Review review;

        private ReviewComboBoxItem(Review review)
        {
            this.review = review;
        }

        @Override
        public String toString()
        {
            return review.getPermId().getId();
        }

        public Review getReview()
        {
            return review;
        }
    }


    private void fillReviewCombos()
    {
        reviewComboBox.removeAllItems();
        getOKAction().setEnabled(false);

        new Thread(new Runnable()
        {
            public void run()
            {
                List<Review> drafts = new ArrayList<Review>();
                List<Review> outForReview = new ArrayList<Review>();

                try
                {
                    drafts = crucibleServerFacade.getReviewsForFilter(cfg, PredefinedFilter.Drafts);
                    outForReview = crucibleServerFacade.getReviewsForFilter(cfg, PredefinedFilter.OutForReview);
                    repo = crucibleServerFacade.getRepository(cfg, cfg.getRepositoryName());
                }
                catch (RemoteApiException e)
                {
                    // nothing can be done here
                }
                catch (ServerPasswordNotProvidedException e)
                {
                    // nothing can be done here
                }
                final List<Review> reviews = new ArrayList<Review>();
                reviews.addAll(drafts);
                reviews.addAll(outForReview);
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        updateReviewCombo(reviews);
                    }
                });
            }
        }, "atlassian-idea-plugin crucible patch upload combos refresh").start();
    }

    private void updateReviewCombo(List<Review> reviews)
    {
        reviewComboBox.addItem("");
        if (!reviews.isEmpty())
        {
            for (Review review : reviews)
            {
                reviewComboBox.addItem(new ReviewComboBoxItem(review));
            }
        }
    }

    public JComponent getRootComponent()
    {
        return rootComponent;
    }

    @Override
    @Nullable
    protected JComponent createCenterPanel()
    {
        return getRootComponent();
    }


    protected void doOKAction()
    {
        switch (mode)
        {
            case ADDREVISION:
                try
                {
                    List<String> revisions = new ArrayList<String>();
                    for (ChangeList change : changes)
                    {
                        for (Change change1 : change.getChanges())
                        {
                            revisions.add(change1.getAfterRevision().getRevisionNumber().asString());
                            break;
                        }
                    }
                    crucibleServerFacade.addRevisionsToReview(cfg, permId, repo.getName(), revisions);
                    super.doOKAction();

                }
                catch (RemoteApiException e)
                {
                    showMessageDialog(e.getMessage(),
                            "Error creating review: " + cfg.getUrl(), Messages.getErrorIcon());
                }
                catch (ServerPasswordNotProvidedException e)
                {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                break;

            case ADDPATCH:
                try
                {
                    crucibleServerFacade.addPatchToReview(cfg, permId, repo.getName(), patch);
                    super.doOKAction();

                }
                catch (RemoteApiException e)
                {
                    showMessageDialog(e.getMessage(),
                            "Error creating review: " + cfg.getUrl(), Messages.getErrorIcon());
                }
                catch (ServerPasswordNotProvidedException e)
                {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                break;
        }
    }

    private void createUIComponents()
    {
	}
}