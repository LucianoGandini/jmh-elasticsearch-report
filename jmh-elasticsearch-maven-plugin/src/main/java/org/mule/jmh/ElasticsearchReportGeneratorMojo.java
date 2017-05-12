package org.mule.jmh;


import org.mule.jmh.report.elasticsearch.ElasticsearchConnectionProperties;
import org.mule.jmh.report.elasticsearch.ElasticsearchReporter;

import java.io.FileNotFoundException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.json.simple.parser.ParseException;


@Mojo(name = "generate-report", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@Execute(goal = "generate-report")
public class ElasticsearchReportGeneratorMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project.build.outputDirectory}/jmh-result.json")
  private String reportPath;

  @Parameter(defaultValue = "/${project.artifactId}/jmh/")
  private String index;

  @Parameter(defaultValue = "localhost")
  private String host;

  @Parameter(defaultValue = "9200")
  private int port;

  @Parameter
  private String userName;

  @Parameter
  private String userPassword;

  @Parameter
  private String version = "${project.version}" ;

  public void execute() throws MojoExecutionException, MojoFailureException {
    Log log = getLog();
    try {
      log.info(String.format("Creating report from %s and uploading to index: %s.",reportPath,index));
      new ElasticsearchReporter().createReport(reportPath, index, version, new ElasticsearchConnectionProperties(host, port, userName, userPassword));
      log.info("Uploaded report succesfully.");
    } catch (FileNotFoundException e) {
      throw new MojoExecutionException("Report : " + reportPath + " was not found.", e);
    }
    catch (ParseException e)
    {
      throw new MojoExecutionException("Report : " + reportPath + " cannot be parse correclty.", e);
    }
    catch (Exception e)
    {
      throw new MojoExecutionException("Error creating report for elasticsearch with: " + reportPath + " .", e);
    }
  }
}
