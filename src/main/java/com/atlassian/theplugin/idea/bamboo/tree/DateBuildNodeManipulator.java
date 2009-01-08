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
package com.atlassian.theplugin.idea.bamboo.tree;

import com.atlassian.theplugin.idea.bamboo.BuildModel;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Jacek Jaroczynski
 */
public class DateBuildNodeManipulator extends BuildNodeManipulator {
	public DateBuildNodeManipulator(final BuildModel buildModel, final DefaultMutableTreeNode root) {
		super(buildModel, root);
	}

	public int getChildCount(final Object parent) {
		return 0;
	}

	public Object getChild(final Object parent, final int index) {
		return null;
	}

//	@Override
//	public int getChildCount(Object parent) {
//		if (parent == rootNode) {
//			return getDistinctDates().size();
//		} else if (parent instanceof BuildDateTreeNode) {
//			BuildDateTreeNode stateNode = (BuildDateTreeNode) parent;
//			return gentNumOfBuildsForDate(stateNode.getDate());
//		}
//
//		return 0;
//	}
//
//	@Override
//	public Object getChild(Object parent, int index) {
//		if (parent == rootNode) {
//
//			DefaultMutableTreeNode p = (DefaultMutableTreeNode) parent;
//
//			if (index < p.getChildCount()) {
//				return p.getChildAt(index);
//			}
//
//			String project = getDistinctDates().get(index);
//
//			BuildProjectTreeNode projectNode = new BuildProjectTreeNode(project);
//			p.add(projectNode);
//
//			return projectNode;
//
//		} else if (parent instanceof BuildProjectTreeNode) {
//			BuildProjectTreeNode p = (BuildProjectTreeNode) parent;
//
//			if (index < p.getChildCount()) {
//				return p.getChildAt(index);
//			}
//
//			BambooBuildAdapterIdea build = getBuildForProject(p.getProject(), index);
//			BuildTreeNode node = new BuildTreeNode(build);
//			p.add(node);
//
//			return node;
//		}
//
//		return null;
//	}
//
//	private List<String> getDistinctDates() {
//		Set<String> projects = new LinkedHashSet<String>();	// ordered set
//
//		for (BambooBuildAdapterIdea build : buildModel.getBuilds()) {
//			projects.add(build.getProjectName());
//		}
//
//		return new ArrayList<String>(projects);
//	}
//
//	private int gentNumOfBuildsForDate(BuildDate projectName) {
//		int ret = 0;
//		for (BambooBuildAdapterIdea build : buildModel.getBuilds()) {
//			if (build.getBuildTime()) {
//				++ret;
//			}
//		}
//
//		return ret;
//	}
//
//	private BambooBuildAdapterIdea getBuildForProject(String projectName, int index) {
//		List<BambooBuildAdapterIdea> array = new ArrayList<BambooBuildAdapterIdea>();
//
//		// get all builds for server
//		for (BambooBuildAdapterIdea build : buildModel.getBuilds()) {
//			if (build.getProjectName().equals(projectName)) {
//				array.add(build);
//			}
//		}
//
//		return array.get(index);
//	}
}