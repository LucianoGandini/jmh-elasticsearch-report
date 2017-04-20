package org.mule.jmh.report.elasticsearch;


public class ElasticsearchConnectionProperties {

  private String userName;
  private String password;
  private int port;
  private String hostname;

  public ElasticsearchConnectionProperties(String hostname, int port, String userName, String password) {
    this.hostname = hostname;
    this.port = port;
    this.userName = userName;
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public int getPort() {
    return port;
  }

  public String getHostname() {
    return hostname;
  }
}
