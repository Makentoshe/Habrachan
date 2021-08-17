import org.gradle.api.Project
import org.gradle.kotlin.dsl.support.delegates.ProjectDelegate

val Project.dependency: Dependency
    get() = Dependency

val ProjectDelegate.dependency
    get() = Dependency
