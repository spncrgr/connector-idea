package com.atlassian.theplugin.idea.action.reviews;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.atlassian.theplugin.commons.crucible.api.model.CrucibleAction;
import com.atlassian.theplugin.commons.crucible.api.model.ReviewAdapter;
import com.atlassian.theplugin.commons.crucible.ValueNotYetInitialized;
import com.atlassian.theplugin.commons.util.LoggerImpl;
import com.atlassian.theplugin.idea.crucible.CrucibleChangeStateWorker;
import com.atlassian.theplugin.idea.IdeaHelper;
import com.atlassian.theplugin.idea.Constants;

/**
 * User: jgorycki
 * Date: Dec 3, 2008
 * Time: 2:36:22 PM
 */
public abstract class AbstractReviewAction extends AnAction {
	protected abstract CrucibleAction getRequestedAction();

	@Override
	public void actionPerformed(final AnActionEvent event) {
		ReviewAdapter review = event.getData(Constants.REVIEW_KEY);
		if (review != null) {
			new CrucibleChangeStateWorker(IdeaHelper.getCurrentProject(event), review, getRequestedAction()).run();
		}
	}

	@Override
	public void update(AnActionEvent event) {
		super.update(event);
		final ReviewAdapter review = event.getData(Constants.REVIEW_KEY);
		if (review == null) {
			event.getPresentation().setEnabled(false);
		} else {
			try {
				final boolean isThere = review.getActions().contains(getRequestedAction());
				event.getPresentation().setEnabled(isThere);
				event.getPresentation().setVisible(isThere);
			} catch (ValueNotYetInitialized valueNotYetInitialized) {
				LoggerImpl.getInstance().error(valueNotYetInitialized);
			}
		}

	}
}