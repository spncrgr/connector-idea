package com.atlassian.theplugin.crucible;

import com.atlassian.theplugin.ServerType;
import com.atlassian.theplugin.util.PluginUtil;
import com.atlassian.theplugin.configuration.Server;
import com.atlassian.theplugin.configuration.ServerPasswordNotProvidedException;
import com.atlassian.theplugin.crucible.api.*;
import com.atlassian.theplugin.crucible.api.rest.CrucibleSessionImpl;
import com.atlassian.theplugin.remoteapi.RemoteApiException;
import com.atlassian.theplugin.remoteapi.RemoteApiLoginFailedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrucibleServerFacadeImpl implements CrucibleServerFacade {
	private Map<String, CrucibleSession> sessions = new HashMap<String, CrucibleSession>();

	public CrucibleServerFacadeImpl() {
	}

	public ServerType getServerType() {
		return ServerType.CRUCIBLE_SERVER;
	}

	private synchronized CrucibleSession getSession(Server server) throws RemoteApiException {
		String key = server.getUrlString() + server.getUserName() + server.getPasswordString();
		CrucibleSession session = sessions.get(key);
		if (session == null) {
			session = new CrucibleSessionImpl(server.getUrlString());
			sessions.put(key, session);
		}
		return session;
	}

	/**
	 * @param serverUrl @see com.atlassian.theplugin.crucible.remoteapi.soap.CrucibleSessionImpl#constructor(String baseUrl)
	 * @param userName
	 * @param password
	 * @throws com.atlassian.theplugin.crucible.api.CrucibleException
	 *
	 */
	public void testServerConnection(String serverUrl, String userName, String password) throws RemoteApiException {
		CrucibleSession session = null;		
		session = new CrucibleSessionImpl(serverUrl);
		session.login(userName, password);
		session.logout();
	}

	/**
	 * Creates new review in Crucible
	 *
	 * @param server
	 * @param reviewData data for new review to create (some fields have to be set e.g. projectKey)
	 * @return created revew date
	 * @throws CrucibleException in case of createReview error or CrucibleLoginException in case of login error
	 */
	public ReviewData createReview(Server server, ReviewData reviewData) throws RemoteApiException {
		CrucibleSession session = getSession(server);

		session.login(server.getUserName(), server.getPasswordString());
		return session.createReview(reviewData);
	}

	/**
	 * Creates new review in Crucible
	 *
	 * @param server
	 * @param reviewData data for new review to create (some fields have to be set e.g. projectKey)
	 * @param patch	  patch to assign with the review
	 * @return created revew date
	 * @throws CrucibleException in case of createReview error or CrucibleLoginException in case of login error
	 */
	public ReviewData createReviewFromPatch(Server server, ReviewData reviewData, String patch) throws RemoteApiException {
		CrucibleSession session = getSession(server);
		session.login(server.getUserName(), server.getPasswordString());
		return session.createReviewFromPatch(reviewData, patch);
	}

	/**
	 * Retrieves list of projects defined on Crucible server
	 *
	 * @param server
	 * @return
	 * @throws CrucibleException
	 * @throws ServerPasswordNotProvidedException
	 */
	public List<ProjectData> getProjects(Server server) throws RemoteApiException, ServerPasswordNotProvidedException {
		CrucibleSession session = getSession(server);
		session.login(server.getUserName(), server.getPasswordString());
		return session.getProjects();
	}


	/**
	 * Retrieves list of repositories defined on Crucible server
	 *
	 * @param server
	 * @return
	 * @throws CrucibleException
	 * @throws ServerPasswordNotProvidedException
	 */
	public List<RepositoryData> getRepositories(Server server) throws RemoteApiException, ServerPasswordNotProvidedException {
		CrucibleSession session = getSession(server);
		session.login(server.getUserName(), server.getPasswordString());
		return session.getRepositories();
	}

	/**
	 * @param server server object with Url, Login and Password to connect to
	 * @return List of reviews (empty list in case there is no review)
	 */
	public List<ReviewDataInfo> getAllReviews(Server server) throws RemoteApiException {
		CrucibleSession session = getSession(server);

		session.login(server.getUserName(), server.getPasswordString());

		List<ReviewData> res = session.getAllReviews();
		List<ReviewDataInfo> result = new ArrayList<ReviewDataInfo>(res.size());
		for (ReviewData review : res) {
			List<String> reviewers = session.getReviewers(review.getPermaId());
			result.add(new ReviewDataInfoImpl(review, reviewers, server));
		}
		return result;
	}

	public List<ReviewDataInfo> getActiveReviewsForUser(Server server)
			throws RemoteApiException, ServerPasswordNotProvidedException {
		CrucibleSession session = getSession(server);


		try {
			session.login(server.getUserName(), server.getPasswordString());
		} catch (RemoteApiLoginFailedException e) {
			if (server.getIsConfigInitialized()) {
				PluginUtil.getLogger().error("Crucible login exception: " + e.getMessage());
			} else {
				throw new ServerPasswordNotProvidedException();
			}
		} catch (RemoteApiException e) {
			PluginUtil.getLogger().error("Crucible exception: " + e.getMessage());
		}

		List<State> states = new ArrayList<State>();
		states.add(State.REVIEW);

		List<ReviewData> reviews = session.getReviewsInStates(states);
		List<ReviewDataInfo> result = new ArrayList<ReviewDataInfo>(reviews.size());

		for (ReviewData reviewData : reviews) {
			List<String> reviewers = session.getReviewers(reviewData.getPermaId());

			if (reviewers.contains(server.getUserName())) {
				result.add(new ReviewDataInfoImpl(reviewData, reviewers, server));
			}
		}
		return result;
	}

}
