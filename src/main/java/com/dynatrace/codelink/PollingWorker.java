/*
 * Dynatrace CodeLink Wrapper
 * Copyright (c) 2008-2016, DYNATRACE LLC
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  Neither the name of the dynaTrace software nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package com.dynatrace.codelink;

import com.dynatrace.codelink.exceptions.CodeLinkConnectionException;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;

class PollingWorker implements Runnable {
    private final ProjectDescriptor project;
    private final CodeLinkEndpoint endpoint;
    private final CodeLinkSettings clSettings;
    private final IDEDescriptor ide;

    private String prevErrorMessage = null;
    private int suppress = 0;
    private long sessionId = -1;

    PollingWorker(IDEDescriptor ide, CodeLinkSettings clSettings, ProjectDescriptor project) {
        this.project = project;
        this.endpoint = new CodeLinkEndpoint(project, ide, clSettings);
        this.clSettings = clSettings;
        this.ide = ide;
    }

    @Override
    public void run() {
        if (!this.clSettings.isEnabled()) {
            return;
        }

        // Not a perfect solution.
        if (this.suppress > 0) {
            this.suppress--;
            return;
        }

        try {
            final CodeLinkLookupResponse response = this.endpoint.connect(this.sessionId);
            this.sessionId = response.sessionId;
            //if the response times out it means there's no codelink request
            if (response.timedOut) {
                return;
            }
            final long sid = this.sessionId;
            //try to jump to class
            this.project.jumpToClass(response, new Callback<Boolean>() {
                @Override
                public void call(Boolean success) {
                    if (!success) {
                        PollingWorker.this.ide.log(Level.WARNING, "CodeLink Error", "Unsuccessful lookup", String.format("Method: %s(%s) in class %s could not be found", response.getMethodName(), Arrays.toString(response.getArguments()), response.getClassName()), false);
                    }
                    try {
                        PollingWorker.this.endpoint.respond(success ? CodeLinkEndpoint.ResponseStatus.FOUND : CodeLinkEndpoint.ResponseStatus.NOT_FOUND, sid);
                    } catch (CodeLinkConnectionException e) {
                    	String errorMessage = "Could not send response to CodeLink: " + e.getMessage();
                    	if (prevErrorMessage != errorMessage) {
	                        PollingWorker.this.prevErrorMessage = errorMessage;
                    		PollingWorker.this.ide.log(Level.WARNING, "CodeLink Error", "Could not send response", errorMessage, false);
	                        CodeLinkClient.LOGGER.warning(errorMessage);
                    	}
                    }
                }
            });

            this.prevErrorMessage = null;
        } catch (CodeLinkConnectionException e) {
            // if the host can't be found disable codelink to not disturb user with future notifications
            if (e.getCause() instanceof UnknownHostException) {
                this.clSettings.setEnabled(false);
                String errorMessage = "Dynatrace client host not found. Please verify your configuration"
                		+ " under Eclipse preferences\\Dynatrace AppMon\\CodeLink\\Client host\nCodeLink has been disabled.";
                if (prevErrorMessage != errorMessage) {
                	this.prevErrorMessage = errorMessage;
	                this.ide.log(Level.WARNING, "CodeLink Error", "Check your configuration", errorMessage, true);
                }
            } else {
            	String errorMessage = "Failed connecting to Dynatrace AppMon Client to poll for CodeLink jump requests.";
            	if (prevErrorMessage != errorMessage) {
            		this.prevErrorMessage = errorMessage;
            		this.ide.log(Level.WARNING, "CodeLink Error", "Check your configuration", errorMessage, false);
            	}
                this.suppress = 5;
            }
        } catch (Exception e) {
            String errorMessage = "Error occured in codelink worker " + e.getMessage();
            if (this.prevErrorMessage != errorMessage) {
            	this.prevErrorMessage = errorMessage;
            	CodeLinkClient.LOGGER.warning(errorMessage);
                this.ide.log(Level.WARNING, "CodeLink Error", "Check your configuration", errorMessage, true);
            }
            this.suppress = 5; // skip 5 connections
        }
    }
}


