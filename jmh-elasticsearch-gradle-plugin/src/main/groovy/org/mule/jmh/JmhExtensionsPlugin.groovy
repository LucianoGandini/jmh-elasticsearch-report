package org.mule.jmh

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.mule.jmh.report.elasticsearch.ElasticsearchConnectionProperties
import org.mule.jmh.report.elasticsearch.ElasticsearchReporter


class JmhExtensionsPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create('elasticsearchReport', ElasticsearchReportExtension)
        def task = project.task('generateJmhInfluxReport') << {
            ElasticsearchReportExtension configuration = project.elasticsearchReport
            configuration.index = configuration.index ?: "/" + project.name + "/jmh/"
            project.logger.info 'Using this configuration:\n{}', configuration
            new ElasticsearchReporter().createReport(project.buildDir.absolutePath + "/" + configuration.reportPath, configuration.index, version, new ElasticsearchConnectionProperties(configuration.host, configuration.port, configuration.userName, configuration.userPassword))
        }
        task.group = "jmh"
        task.description = "Parse the json result and inserts it inside an elasticsearch."

    }
}
