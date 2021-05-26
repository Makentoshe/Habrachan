package build

// This is a base build attached to the Github VCS
abstract class BaseBuild(name: String, init: BuildType.() -> Unit): BuildType({
    this.name = name

    // A VCS Root is a set of settings defining how TeamCity communicates with a version control system to
    // monitor changes and get sources of a build
    vcs { root(GithubVcsRoot) }

    // All requirements that build agents should meet to run this build.
    requirements {
        matches("teamcity.agent.jvm.os.family", "Linux")
    }

    init()
})