package com.universe.touchpoint.config.transport;

public class MQTTConfig {

  public String brokerUri = "tcp://localhost:1883";

  public String getBrokerUri() {
    return brokerUri;
  }

  public void setBrokerUri(String brokerUri) {
    this.brokerUri = brokerUri;
  }
}
