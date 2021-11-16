package com.makentoshe.habrachan.entity.mobile.whois

data class InternalWhoIs(
    val aboutHtml: String,
    val alias: String, // Makentoshe
    val badgets: List<Badget>,
    val contacts: List<Contact>,
    val invitedBy: InvitedBy
)