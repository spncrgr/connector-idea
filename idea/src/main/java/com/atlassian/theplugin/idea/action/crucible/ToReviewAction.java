package com.atlassian.theplugin.idea.action.crucible;

import com.atlassian.theplugin.commons.crucible.api.PredefinedFilter;

public class ToReviewAction extends PredefinedFilterAction {
    public ToReviewAction() {
        super(PredefinedFilter.ToReview);
    }
}
