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

package com.atlassian.theplugin.idea.action.bamboo;

import com.atlassian.theplugin.commons.bamboo.BambooStatusChecker;
import com.atlassian.theplugin.commons.util.Logger;
import com.atlassian.theplugin.idea.IdeaHelper;
import com.atlassian.theplugin.idea.ProgressAnimationProvider;
import com.atlassian.theplugin.idea.ThePluginProjectComponent;
import com.atlassian.theplugin.util.PluginUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class RefreshBambooPanelAction extends AnAction {

	@Override
	public void actionPerformed(final AnActionEvent e) {
		final ThePluginProjectComponent currentProject = IdeaHelper.getCurrentProjectComponent(e);
		if (currentProject == null) {
			return;
		}

		final BambooStatusChecker checker = currentProject.getBambooStatusChecker();

		if (checker.canSchedule()) {

			final ProgressAnimationProvider animator =
					IdeaHelper.getBambooToolWindowPanel(e).getProgressAnimation();

			final Logger log = PluginUtil.getLogger();

			new Thread(new Runnable() {
				public void run() {

					Thread t = new Thread(checker.newTimerTask(), "Manual Bamboo panel refresh (checker)");

					animator.startProgressAnimation();

					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
						log.warn(e.toString());
					} finally {
						animator.stopProgressAnimation();
					}


				}
			}, "Manual Bamboo panel refresh").start();

		}
	}
}
