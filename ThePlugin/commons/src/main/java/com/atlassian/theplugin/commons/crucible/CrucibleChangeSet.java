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

package com.atlassian.theplugin.commons.crucible;

import com.atlassian.theplugin.commons.Server;
import com.atlassian.theplugin.commons.VirtualFileSystem;
import com.atlassian.theplugin.commons.crucible.api.model.Review;
import com.atlassian.theplugin.commons.crucible.api.model.Reviewer;
import com.atlassian.theplugin.commons.crucible.api.model.GeneralComment;
import com.atlassian.theplugin.commons.crucible.api.model.Transition;
import com.atlassian.theplugin.commons.crucible.api.model.CrucibleFileInfo;
import com.atlassian.theplugin.commons.crucible.api.model.VersionedComment;

import java.util.List;

public interface CrucibleChangeSet extends Review {
	String getReviewUrl();

	List<Reviewer> getReviewers() throws ValueNotYetInitialized;

	Server getServer();

	List<GeneralComment> getGeneralComments() throws ValueNotYetInitialized;

    List<VersionedComment> getVersionedComments() throws ValueNotYetInitialized;

    List<CrucibleFileInfo> getFiles() throws ValueNotYetInitialized;

	List<Transition> getTransitions() throws ValueNotYetInitialized;

	VirtualFileSystem getVirtualFileSystem();
}