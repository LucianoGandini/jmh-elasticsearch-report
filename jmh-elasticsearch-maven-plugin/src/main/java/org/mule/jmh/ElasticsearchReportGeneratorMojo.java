package org.mule.jmh;


import org.mule.jmh.report.elasticsearch.ElasticsearchConnectionProperties;
import org.mule.jmh.report.elasticsearch.ElasticsearchReporter;

import java.io.FileNotFoundException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.json.simple.parser.ParseException;


@Mojo(name = "generate-report", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@Execute(goal = "generate-report")
public class ElasticsearchReportGeneratorMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project.build.outputDirectory}/reports/jmh/results.json")
  private String reportPath;

  @Parameter(defaultValue = "/${project.artifactId}/jmh/")
  private String index;

  @Parameter(defaultValue = "localhost")
  private String host;

  @Parameter(defaultValue = "9200")
  private String port;

  @Parameter
  private String userName;

  @Parameter
  private String userPassword;

  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      new ElasticsearchReporter().createReport(reportPath, index, new ElasticsearchConnectionProperties(host, userName, userPassword));
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
