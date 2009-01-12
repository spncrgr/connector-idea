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

import com.atlassian.theplugin.commons.crucible.CrucibleServerFacadeImpl;
import com.atlassian.theplugin.commons.crucible.api.model.*;
import com.atlassian.theplugin.commons.exception.ServerPasswordNotProvidedException;
import com.atlassian.theplugin.commons.remoteapi.RemoteApiException;
import com.atlassian.theplugin.idea.IdeaHelper;
import com.atlassian.theplugin.idea.VcsIdeaHelper;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diff.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.VcsBundle;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CrucibleHelper {
	static final Document EMPTY_DOCUMENT = new DocumentImpl("");

	private CrucibleHelper() {
	}

	/**
	 * Shows virtual file taken from repository in Idea Editor.
	 * Higlights all versioned comments for given file
	 * Adds StripeMark on the right side of file window with set tool tip text that corresponde
	 * to VersionedComment.getMessage content
	 * Note: must be run from event dispatch thread or inside read-action only!
	 *
	 * @param project	project
	 * @param review	 review data
	 * @param reviewItem review item
	 */
	public static void showVirtualFileWithComments(final Project project,
			final ReviewAdapter review,
			final CrucibleFileInfo reviewItem) {

		int line = 1;

		java.util.List<VersionedComment> fileComments;
		fileComments = reviewItem.getVersionedComments();

		if (fileComments != null && !fileComments.isEmpty()) {
			line = fileComments.iterator().next().getFromStartLine();
		}

		VcsIdeaHelper.openFileWithDiffs(project
				, true
				, reviewItem.getFileDescriptor().getAbsoluteUrl()
				, reviewItem.getOldFileDescriptor().getRevision()
				, reviewItem.getFileDescriptor().getRevision()
				, reviewItem.getCommitType()
				, line
				, 1
				, new VcsIdeaHelper.OpenDiffAction() {

					public void run(OpenFileDescriptor displayFile, VirtualFile referenceFile, CommitType commitType) {
						if (referenceFile == null) {
							Messages.showErrorDialog(project,
									"Cannot fetch " + reviewItem.getOldFileDescriptor().getAbsoluteUrl()
											+ ".\nAnnotated file cannot be displayed.", "Error");
							return;
						}
						FileEditorManager fem = FileEditorManager.getInstance(project);
						Editor editor = fem.openTextEditor(displayFile, true);
						if (editor == null) {
							return;
						}
					}
				});
	}

	@NotNull
	private static DiffContent createDiffContent(@NotNull final Project project, @NotNull final VirtualFile virtualFile) {
		if (!FileTypeManager.getInstance().getFileTypeByFile(virtualFile).isBinary()) {
			return new FileContent(project, virtualFile);
		} else {
			try {
				return new BinaryContent(virtualFile.contentsToByteArray(), null,
						FileTypeManager.getInstance().getFileTypeByFile(virtualFile));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public static void showRevisionDiff(final Project project, final CrucibleFileInfo reviewItem) {

		VcsIdeaHelper.openFileWithDiffs(project
				, true
				, reviewItem.getFileDescriptor().getAbsoluteUrl()
				, reviewItem.getOldFileDescriptor().getRevision()
				, reviewItem.getFileDescriptor().getRevision()
				, reviewItem.getCommitType()
				, 1
				, 1
				, new MyOpenDiffAction(project, reviewItem));
	}

	public static List<CustomFieldDef> getMetricsForReview(@NotNull final Project project,
			@NotNull final ReviewAdapter review) {
		java.util.List<CustomFieldDef> metrics = new ArrayList<CustomFieldDef>();
		try {
			metrics = CrucibleServerFacadeImpl.getInstance()
					.getMetrics(review.getServer(), review.getMetricsVersion());
		} catch (RemoteApiException e) {
			IdeaHelper.handleRemoteApiException(project, e);
		} catch (ServerPasswordNotProvidedException e) {
			IdeaHelper.handleMissingPassword(e);
		}
		return metrics;
	}

	public static Editor getEditorForCrucibleFile(ReviewAdapter review, CrucibleFileInfo file) {
		Editor[] editors = EditorFactory.getInstance().getAllEditors();
		for (Editor editor : editors) {
			final Document document = editor.getDocument();
			final VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
			final ReviewAdapter mr = virtualFile.getUserData(CommentHighlighter.REVIEW_DATA_KEY);
			final CrucibleFileInfo mf = virtualFile.getUserData(CommentHighlighter.REVIEWITEM_DATA_KEY);
			if (mr != null && mf != null) {
				if (review.getPermId().equals(mr.getPermId()) && file.getPermId().equals(mf.getPermId())) {
					return editor;
				}
			}
		}
		return null;
	}

	public static void openFileOnComment(final Project project, final ReviewAdapter review, final CrucibleFileInfo file,
			final VersionedComment comment) {

		ApplicationManager.getApplication().runReadAction(new Runnable() {
			public void run() {
				VcsIdeaHelper.openFileWithDiffs(project
						, true
						, file.getFileDescriptor().getAbsoluteUrl()
						, file.getOldFileDescriptor().getRevision()
						, file.getFileDescriptor().getRevision()
						, file.getCommitType()
						, comment.getToStartLine() - 1
						, 0
						, new VcsIdeaHelper.OpenDiffAction() {

							public void run(OpenFileDescriptor displayFile, VirtualFile referenceFile, CommitType commitType) {
								FileEditorManager fem = FileEditorManager.getInstance(project);
								// @todo temporary - should be handled when opening file
								if (displayFile != null) {
									displayFile.getFile().putUserData(CommentHighlighter.VERSIONED_COMMENT_DATA_KEY, comment);
									Editor editor = fem.openTextEditor(displayFile, false);
									if (editor == null) {
										return;
									}
								}
							}
						});
			}
		});

	}

	private static class MyOpenDiffAction implements VcsIdeaHelper.OpenDiffAction {
		private final Project project;
		private final CrucibleFileInfo reviewItem;

		public MyOpenDiffAction(final Project project, final CrucibleFileInfo reviewItem) {
			this.project = project;
			this.reviewItem = reviewItem;
		}

		public void run(OpenFileDescriptor displayFile, VirtualFile referenceFile, CommitType commitType) {
			Document displayDocument = EMPTY_DOCUMENT;
			Document referenceDocument = EMPTY_DOCUMENT;
			DiffContent displayFileContent = null;
			if (displayFile != null) {
				displayFileContent = createDiffContent(project, displayFile.getFile());
				displayDocument = displayFileContent.getDocument();
			}

			DiffContent referenceFileContent = null;
			if (referenceFile != null) {
				referenceFileContent = createDiffContent(project, referenceFile);
				referenceDocument = referenceFileContent.getDocument();
			}

			if ((displayFileContent != null && displayFileContent.isBinary())
					|| (referenceFileContent != null && referenceFileContent.isBinary())) {
				Messages.showInfoMessage(project, "Files are binary. Diff not available.", "Information");
				return;
			}

			final DocumentContent displayDocumentContentFinal = new DocumentContent(project, displayDocument);
			final DocumentContent referenceDocumentContentFinal = new DocumentContent(project, referenceDocument);

			DiffRequest request = new DiffRequest(project) {
				@Override
				public DiffContent[] getContents() {
					return (new DiffContent[]{
							referenceDocumentContentFinal,
							displayDocumentContentFinal
					});
				}

				@Override
				public String[] getContentTitles() {
					return (new String[]{
							VcsBundle.message("diff.content.title.repository.version",
									reviewItem.getOldFileDescriptor().getRevision()),
							VcsBundle.message("diff.content.title.repository.version",
									reviewItem.getFileDescriptor().getRevision())
					});
				}

				@Override
				public String getWindowTitle() {
					return reviewItem.getFileDescriptor().getAbsoluteUrl();
				}
			};
			DiffManager.getInstance().getDiffTool().show(request);
		}
	}
}
