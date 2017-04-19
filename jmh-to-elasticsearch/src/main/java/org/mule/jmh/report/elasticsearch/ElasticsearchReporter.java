package org.mule.jmh.report.elasticsearch;


import java.io.*;
import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ElasticsearchReporter {

  /**
   * Inserts the repo inside the
   *
   * @param jsonReport JMH json file path
   * @param index The index name where the measurements are being stored
   * @param connectionProperties The elastisearch connection properties
   * @throws FileNotFoundException
   */
  public void createReport(String jsonReport, String index, ElasticsearchConnectionProperties connectionProperties)
      throws IOException, ParseException, InterruptedException {
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY,
                                       new UsernamePasswordCredentials(connectionProperties.getUserName(),
                                                                       connectionProperties.getPassword()));

    RestClient restClient = RestClient.builder(
        new HttpHost(connectionProperties.getHostname(), connectionProperties.getPort()))
        .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

          @Override
          public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
            // disable preemptive authentication
            httpClientBuilder.disableAuthCaching();
            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
          }
        })
        .build();

    JSONParser parser = new JSONParser();

    Object obj = parser.parse(new FileReader(jsonReport));

    JSONArray jsonArray = (JSONArray) obj;

    String hash = calculateGitHash();
    String date = Instant.now().toString();

    Iterator<JSONObject> it = jsonArray.iterator();

    while (it.hasNext()) {
      JSONObject jsonObject = it.next();

      // Add git hash commit
      jsonObject.put("git", hash);
      jsonObject.put("timestamp", date);
      String benchmkark = (String) jsonObject.get("benchmark");
      String[] all = benchmkark.split("[.]");
      jsonObject.put("benchmark_full_name", String.format("%s.%s", all[all.length - 2], all[all.length - 1]));
      jsonObject.put("benchmark_class", String.format(all[all.length - 2]));
      jsonObject.put("benchmark_test", String.format(all[all.length - 1]));

      // index the document
      HttpEntity entity = new NStringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
      restClient.performRequest("POST", index, Collections.<String, String>emptyMap(), entity);
    }
  }

  private String calculateGitHash() throws IOException, InterruptedException {
    Process exec = Runtime.getRuntime().exec("git rev-parse HEAD");
    exec.waitFor();
    InputStream output = exec.getInputStream();
    return new BufferedReader(new InputStreamReader(output)).readLine();
  }

  public static void main(String[] args) throws IOException, ParseException, InterruptedException {
    if (args.length == 5) {
      final String reportPath = args[0];
      final String index = args[1];
      final String host = args[2];
      final String port = args[3];
      final String username = args[4];
      final String password = args[5];
      new ElasticsearchReporter()
          .createReport(reportPath, "/" + index.toLowerCase() + "/jmh/",
                        new ElasticsearchConnectionProperties(host, Integer.getInteger(port), username, password));
      System.out.print("Result inserted successfully.");
    } else {
      System.err.println("Expecting 6 parameters <reportPath> <indexName> <host> <port> <userName> <userPassword>");
    }

  }
}


