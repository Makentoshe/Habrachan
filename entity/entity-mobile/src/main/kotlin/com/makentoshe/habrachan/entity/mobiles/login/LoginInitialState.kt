package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class LoginInitialState(
    @SerializedName("adblock")
    val adblock: Adblock,
    @SerializedName("articlesList")
    val articlesList: ArticlesList,
    @SerializedName("authorContribution")
    val authorContribution: AuthorContribution,
    @SerializedName("authorStatistics")
    val authorStatistics: AuthorStatistics,
    @SerializedName("betaTest")
    val betaTest: BetaTest,
    @SerializedName("comments")
    val comments: Comments,
    @SerializedName("companies")
    val companies: Companies,
    @SerializedName("companiesContribution")
    val companiesContribution: CompaniesContribution,
    @SerializedName("companyHubsContribution")
    val companyHubsContribution: CompanyHubsContribution,
    @SerializedName("conversation")
    val conversation: Conversation,
    @SerializedName("conversations")
    val conversations: Conversations,
    @SerializedName("desktopState")
    val desktopState: DesktopState,
    @SerializedName("dfp")
    val dfp: Dfp,
    @SerializedName("docs")
    val docs: Docs,
    @SerializedName("flows")
    val flows: Flows,
    @SerializedName("global")
    val global: Global,
    @SerializedName("hubs")
    val hubs: Hubs,
    @SerializedName("hubsBlock")
    val hubsBlock: HubsBlock,
    @SerializedName("i18n")
    val i18n: I18n,
    @SerializedName("info")
    val info: Info,
    @SerializedName("location")
    val location: Location,
    @SerializedName("me")
    val me: Me,
    @SerializedName("mostReadingList")
    val mostReadingList: MostReadingList,
    @SerializedName("myFeedSettings")
    val myFeedSettings: MyFeedSettings,
    @SerializedName("newsBlock")
    val newsBlock: NewsBlock,
    @SerializedName("pinnedPost")
    val pinnedPost: PinnedPost,
    @SerializedName("ppa")
    val ppa: Ppa,
    @SerializedName("projectsBlocks")
    val projectsBlocks: ProjectsBlocks,
    @SerializedName("promoBlock")
    val promoBlock: PromoBlock,
    @SerializedName("pullRefresh")
    val pullRefresh: PullRefresh,
    @SerializedName("rootClassName")
    val rootClassName: RootClassName,
    @SerializedName("sandbox")
    val sandbox: Sandbox,
    @SerializedName("settingsOther")
    val settingsOther: SettingsOther,
    @SerializedName("similarList")
    val similarList: SimilarList,
    @SerializedName("ssr")
    val ssr: Ssr,
    @SerializedName("tracker")
    val tracker: Tracker,
    @SerializedName("userHubsContribution")
    val userHubsContribution: UserHubsContribution,
    @SerializedName("userInvites")
    val userInvites: UserInvites,
    @SerializedName("users")
    val users: Users,
    @SerializedName("viewport")
    val viewport: Viewport
)