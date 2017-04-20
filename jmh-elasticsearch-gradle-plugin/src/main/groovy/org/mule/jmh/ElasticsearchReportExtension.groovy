package org.mule.jmh


class ElasticsearchReportExtension {

    String reportPath = "reports/jmh/results.json";
    String index;
    String host = "localhost";
    int port = 9200;
    String version;
    String userName;
    String userPassword;


    @Override
    public String toString() {
        return "InfluxReportExtension{" +
                "reportPath='" + reportPath + '\'' +
                ", index='" + index + '\'' +
                ", version='" + version + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}

