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

object Keystores : GitVcsRoot({
    name = "https://github.com/Makentoshe/Keystores"
    url = "https://github.com/Makentoshe/Keystores"
    authMethod = password {
        userName = "Makentoshe"
        password = "credentialsJSON:7e96df72-acd5-43a7-a77b-8ccc97804767"
    }
})
