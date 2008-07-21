package com.atlassian.theplugin.idea.action.crucible;


import com.atlassian.theplugin.idea.IdeaHelper;
import com.atlassian.theplugin.idea.crucible.events.ShowReviewEvent;
import com.atlassian.theplugin.idea.crucible.comments.CrucibleReviewActionListener;
import com.atlassian.theplugin.idea.crucible.ReviewData;


public class GetCommentsAction extends TableSelectedAction  {

	protected void itemSelected(Object row) {
		IdeaHelper.getReviewActionEventBroker().trigger(new ShowReviewEvent(
				CrucibleReviewActionListener.ANONYMOUS, (ReviewData) row));
	}
}
