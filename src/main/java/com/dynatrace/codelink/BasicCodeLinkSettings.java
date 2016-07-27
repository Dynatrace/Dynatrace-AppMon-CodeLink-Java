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

/**
 * Provides a basic implementation of {@link CodeLinkSettings}
 * You may want to extend this class in order to implement {@link #setEnabled(boolean)} method.
 */
public class BasicCodeLinkSettings implements CodeLinkSettings {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8031;
    public static final boolean DEFAULT_SSL = true;

    private final String host;
    private final int port;
    private final boolean ssl;
    private boolean enabled = true;


    /**
     * Constructs basic settings and populates them with default values:
     * <dl>
     * <dt>{@code host}</dt>
     * <dd>{@value DEFAULT_HOST}</dd>
     * <dt>{@code port}</dt>
     * <dd>{@value DEFAULT_PORT}</dd>
     * <dt>{@code ssl}</dt>
     * <dd>{@value DEFAULT_SSL}</dd>
     * </dl>
     */
    public BasicCodeLinkSettings() {
        this(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_SSL);
    }

    /**
     * Constructs basic settings
     *
     * @param host dynatrace client host
     * @param port dynatrace client port
     * @param ssl  whether to use https
     */
    public BasicCodeLinkSettings(String host, int port, boolean ssl) {
        this.host = host;
        this.port = port;
        this.ssl = ssl;
    }

    @Override
    public synchronized boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public synchronized void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public boolean isSSL() {
        return this.ssl;
    }
}
