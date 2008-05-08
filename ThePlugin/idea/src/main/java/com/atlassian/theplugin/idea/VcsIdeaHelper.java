package com.atlassian.theplugin.idea;

import com.intellij.openapi.diff.DiffContent;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcsUtil.VcsUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class VcsIdeaHelper {

	private VcsIdeaHelper() {
	}

	public static String getRepositoryUrlForFile(VirtualFile vFile) {
		AbstractVcs vcs = ProjectLevelVcsManager.getInstance(IdeaHelper.getCurrentProject()).getVcsFor(vFile);
		return vcs.getCommittedChangesProvider().getLocationFor(VcsUtil.getFilePath(vFile.getPath())).toPresentableString();
	}

	public static List<VcsFileRevision> getFileHistory(VirtualFile vFile) throws VcsException {
		return ProjectLevelVcsManager.getInstance(IdeaHelper.getCurrentProject()).getVcsFor(vFile)
				.getVcsHistoryProvider().createSessionFor(VcsUtil.getFilePath(vFile.getPath())).getRevisionList();
	}

	public static VcsFileRevision getFileRevision(VirtualFile vFile, String revision) {
		try {
			List<VcsFileRevision> revisions = getFileHistory(vFile);
			for (VcsFileRevision vcsFileRevision : revisions) {
				if (vcsFileRevision.getRevisionNumber().asString().equals((revision))) {
					return vcsFileRevision;
				}
			}
		} catch (VcsException e) {
			// nothing to do
		}
		return null;
	}

	public static List<VcsFileRevision> getFileRevisions(VirtualFile vFile, List<String> revisions) {
		List<VcsFileRevision> allRevisions;
		try {
			allRevisions = getFileHistory(vFile);
		} catch (VcsException e) {
			return Collections.EMPTY_LIST;
		}
		List<VcsFileRevision> returnRevision = new ArrayList<VcsFileRevision>(revisions.size());
		for (VcsFileRevision allRevision : allRevisions) {
			String rev = allRevision.getRevisionNumber().asString();
			for (String revision : revisions) {
				if (revision.equals(rev)) {
					returnRevision.add(allRevision);
				}
			}
		}
		return returnRevision;
	}

	public static DiffContent getFileRevisionContent(VirtualFile vFile, VcsFileRevision revNumber) {
		AbstractVcs vcs = ProjectLevelVcsManager.getInstance(IdeaHelper.getCurrentProject()).getVcsFor(vFile);
		VcsRevisionNumber rev = revNumber.getRevisionNumber();
		try {
			return com.intellij.openapi.diff.SimpleContent.fromBytes(vcs.getDiffProvider()
					.createFileContent(rev, vFile).getContent().getBytes(), vFile.getCharset().name(), vFile.getFileType());
		} catch (UnsupportedEncodingException e) {
			// nothing to do
		} catch (VcsException e) {
			// nothing to do
		}
		return null;
	}
}
