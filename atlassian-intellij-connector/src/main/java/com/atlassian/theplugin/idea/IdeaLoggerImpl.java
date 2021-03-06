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

package com.atlassian.theplugin.idea;

import com.atlassian.theplugin.commons.util.LoggerImpl;

/**
 * Created by IntelliJ IDEA.
 * User: pmaruszak
 * Date: Mar 17, 2008
 * Time: 4:40:34 PM
 */
public class IdeaLoggerImpl extends LoggerImpl {

    private com.intellij.openapi.diagnostic.Logger ideaLog;

    public IdeaLoggerImpl(com.intellij.openapi.diagnostic.Logger ideaLog) {
        super();
        this.ideaLog = ideaLog;
        ideaLog.isDebugEnabled();
		setInstance(this);
	}

    @Override
    public void info(String msg) {
        this.log(LoggerImpl.LOG_INFO, msg, null);
    }

    @Override
    public void error(String msg) {
        log(LoggerImpl.LOG_ERR, msg, null);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(LoggerImpl.LOG_ERR, msg, t);
    }

    @Override
    public void error(Throwable t) {
        log(LoggerImpl.LOG_ERR, "", t);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(LoggerImpl.LOG_WARN, msg, t);
    }

    @Override
    public void warn(String msg) {
        log(LoggerImpl.LOG_WARN, msg, null);
    }

    @Override
    public void warn(Throwable t) {
        log(LoggerImpl.LOG_WARN, "", t);
    }

    @Override
    public void info(String msg, Throwable t) {
       log(LoggerImpl.LOG_INFO, msg, t);
    }

    @Override
    public void info(Throwable t) {
        log(LoggerImpl.LOG_INFO, "", t);
    }

    public void log(int level, String aMsg, Throwable t) {

        switch (level) {
            case LoggerImpl.LOG_VERBOSE:
            case LoggerImpl.LOG_DEBUG:
                ideaLog.debug(aMsg);
                if (t != null) {
                    ideaLog.debug(t);
                }

				break;

            case LoggerImpl.LOG_INFO:
                ideaLog.info(aMsg);
                if (t != null) {
                    ideaLog.info(t);
                }
                break;

            case LoggerImpl.LOG_ERR:
                if (t != null) {
                    ideaLog.info("ERROR:" + aMsg, t);
                } else {
                    ideaLog.info("ERROR:" + aMsg);
                }
                break;

            default:
                ideaLog.debug("<unknown log level> " + aMsg);
                break;
        }
    }
}
