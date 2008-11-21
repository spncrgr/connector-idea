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

package com.atlassian.theplugin.jira.api;

import com.atlassian.theplugin.commons.remoteapi.RemoteApiMalformedUrlException;
import com.atlassian.theplugin.commons.remoteapi.RemoteApiSessionExpiredException;
import junit.framework.TestCase;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JIRARssClientTest extends TestCase
{
    private String mostRecentUrl;

    public void testAssignedIssues() throws Exception {
        JIRARssClient rss = getClasspathJIRARssClient("http://www.server.com", null, null, "/jira/api/assignedIssues.xml");
/*
        // first try unauthenticated and test the URL is correct
        rss.getAssignedIssues("anyone");
        assertEquals("http://www.server.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?resolution=-1&assignee=anyone&sorter/field=updated&sorter/order=DESC&tempMax=100", mostRecentUrl);

        // now try authenticated
        rss = getClasspathJIRARssClient("http://www.server.com", "user", "pass", "/jira/api/assignedIssues.xml");
        List list = rss.getAssignedIssues("anyone");
        assertEquals("http://www.server.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?resolution=-1&assignee=anyone&sorter/field=updated&sorter/order=DESC&tempMax=100&os_username=user&os_password=pass", mostRecentUrl);
        assertEquals(7, list.size());

        JIRAIssueBean firstIssue = new JIRAIssueBean();
        firstIssue.setServerUrl("http://www.server.com");
        firstIssue.setKey("PL-94");
        firstIssue.setSummary("NullPointerException on wrong URL to Bamboo server");
        assertEquals(firstIssue, list.get(0));
*/        
    }

	// for testing PL-863
	public void testBugPl863() throws Exception {
		JIRARssClient c = new JIRARssClient("file://test") {
			protected Document retrieveGetResponse(String urlString)
					throws IOException, JDOMException, RemoteApiSessionExpiredException {
				SAXBuilder builder = new SAXBuilder();
				builder.setErrorHandler(new ErrorHandler() {
					public void warning(SAXParseException exception) throws SAXException {
						System.out.println(exception.getMessage());
					}

					public void error(SAXParseException exception) throws SAXException {
						System.out.println(exception.getMessage());
					}

					public void fatalError(SAXParseException exception) throws SAXException {
						System.out.println(exception.getMessage());
					}
				});
				InputStream is = JIRARssClientTest.class.getResourceAsStream("/jira/api/PL-863.xml");
				Document doc = builder.build(is);
				preprocessResult(doc);
				return doc;
			}
		};
		List<JIRAQueryFragment> l = new ArrayList<JIRAQueryFragment>();
		l. add(new JIRAProjectBean());

		c.getIssues(l, "ASC", "prio", 0, 1);
	}

	// make a simple mock rss client that overrides URL loading with loading from a file
    private JIRARssClient getClasspathJIRARssClient(String url, String userName, String password, final String file) throws RemoteApiMalformedUrlException {
        return new JIRARssClient(url, userName, password) {
            // protected so that we can easily write tests by simply returning XML from a file instead of a URL!
            protected InputStream getUrlAsStream(String url) throws IOException {
                mostRecentUrl = url;
                return JIRARssClientTest.class.getResourceAsStream(file);
            }
        };
    }
}
