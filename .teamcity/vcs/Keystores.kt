package vcs

import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

object Keystores : GitVcsRoot({
    name = "https://github.com/Makentoshe/Keystores"
    url = "https://github.com/Makentoshe/Keystores"
    authMethod = password {
        userName = "Makentoshe"
        password = "credentialsJSON:7e96df72-acd5-43a7-a77b-8ccc97804767"
    }
})