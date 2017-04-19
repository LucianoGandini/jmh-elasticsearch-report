package org.mule.jmh


class ElasticsearchReportExtension {

    String reportPath = "reports/jmh/results.json";
    String index;
    String host = "localhost";
    String userName;
    String userPassword;


    @Override
    public String toString() {
        return "InfluxReportExtension{" +
                "reportPath='" + reportPath + '\'' +
                ", index='" + index + '\'' +
                ", host='" + host + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}

