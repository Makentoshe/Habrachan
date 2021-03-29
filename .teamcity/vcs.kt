import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

object GithubVcsRoot : GitVcsRoot({
    name = "Github"
    url = "git@github.com:Makentoshe/Habrachan.git"
    authMethod = uploadedKey {
        uploadedKey = "id_rsa"
        passphrase = "credentialsJSON:211c06d0-dd07-42a8-8344-211453523bdc"
    }
})

object SpaceVcsRoot : GitVcsRoot({
    name = "Space"
})

/** Contains sensitive metadata. Keystores for signing android application, for example */
object MetadataVcsRoot : GitVcsRoot({
    name = "Metadata"
    url = "ssh://git@git.jetbrains.space/makentoshe/habrachan/Habrachan-metadata.git"
    authMethod = uploadedKey {
        uploadedKey = "id_rsa"
        passphrase = "credentialsJSON:211c06d0-dd07-42a8-8344-211453523bdc"
    }
})
