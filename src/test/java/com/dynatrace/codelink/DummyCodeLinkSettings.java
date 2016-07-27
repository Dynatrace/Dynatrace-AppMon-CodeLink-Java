package com.dynatrace.codelink;


public class DummyCodeLinkSettings implements CodeLinkSettings {
    private boolean enabled = true;
    private String host = "localhost";
    private int port = 8031;
    private boolean ssl = true;

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean isSSL() {
        return this.ssl;
    }

    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }
}
