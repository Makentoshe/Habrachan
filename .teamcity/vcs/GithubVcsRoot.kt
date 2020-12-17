package vcs

import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

object GithubVcsRoot : GitVcsRoot({
    name = "Github"
    url = "git@github.com:Makentoshe/Habrachan.git"
    authMethod = uploadedKey {
        uploadedKey = "id_rsa"
        passphrase = "credentialsJSON:51c3ef83-e5b1-405a-9573-dbabef655e0c"
    }
})

object SpaceVcsRoot: GitVcsRoot({
    name = "Space"
})