package com.universe.touchpoint.config.transport;

public abstract class RPCConfig {

    protected String applicationName;
    protected String registryAddress;

    public RPCConfig(String applicationName, String registryAddress) {
        this.applicationName = applicationName;
        this.registryAddress = registryAddress;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

}
