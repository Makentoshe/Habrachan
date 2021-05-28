import src.build.AndroidBuild
import src.build.AndroidRelease
import jetbrains.buildServer.configs.kotlin.v2019_2.project
import jetbrains.buildServer.configs.kotlin.v2019_2.version
import src.GithubVcsRoot
import src.MetadataVcsRoot
import src.Parameters
import src.build.NetworkNativeNetworkTestBuild

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.2"

project {

    vcsRoot(GithubVcsRoot)
    vcsRoot(MetadataVcsRoot)

    buildType(AndroidBuild)
    buildType(AndroidRelease)
    buildType(NetworkNativeNetworkTestBuild)

    params {
        add(Parameters.Configuration.AndroidSdkUrl)
        add(Parameters.Environment.AndroidHome)
        add(Parameters.Configuration.AndroidBuildTools29)
    }
}
