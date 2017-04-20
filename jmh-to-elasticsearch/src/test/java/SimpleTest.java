import org.mule.jmh.report.elasticsearch.ElasticsearchConnectionProperties;
import org.mule.jmh.report.elasticsearch.ElasticsearchReporter;

import java.io.IOException;

import org.json.simple.parser.ParseException;


public class SimpleTest {

  public static final String DOMAIN = "localhost";
  public static final String REPORT_PATH =
      "/Users/lucianogandini/Documents/ESB/jmh-elasticsearch-report/jmh-to-elasticsearch/src/main/resources/results.json";
  public static final String INDEX = "/test/jmh/";
  private static final String VERSION = "Testing";
  private static final int PORT = 9200;

  public static void main(String[] args) {
    ElasticsearchReporter reporter = new ElasticsearchReporter();
    ElasticsearchConnectionProperties connectionProperties =
        new ElasticsearchConnectionProperties(DOMAIN, PORT, "user", "pwd");
    try
    {
      System.out.println("Creating report.");
      reporter.createReport(REPORT_PATH, INDEX, VERSION, connectionProperties);
      System.out.println("Finished uploading report.");
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
    System.out.println("Exiting");
    return;
  }
}
