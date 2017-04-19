import org.mule.jmh.report.elasticsearch.ElasticsearchConnectionProperties;
import org.mule.jmh.report.elasticsearch.ElasticsearchReporter;

import java.io.IOException;

import org.json.simple.parser.ParseException;


public class SimpleTest {

  public static final String DOMAIN = "34.205.215.69";
  public static final String REPORT_PATH =
      "/Users/lucianogandini/Documents/ESB/jmh-elasticsearch-report/jmh-to-elasticsearch/src/main/resources/results.json";
  public static final String INDEX = "/data_weave/jmh/";

  public static void main(String[] args) {
    ElasticsearchReporter reporter = new ElasticsearchReporter();
    ElasticsearchConnectionProperties connectionProperties =
        new ElasticsearchConnectionProperties(DOMAIN, "user", "password");
    try
    {
      reporter.createReport(REPORT_PATH, INDEX, connectionProperties);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}
