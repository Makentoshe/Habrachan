package com.makentoshe.habrachan.model.main.posts

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.model.post.PostScreen
import ru.terrakok.cicerone.Router

class PostsEpoxyController(private val modelFactory: PostModelFactory) : EpoxyController() {

    private val posts = ArrayList<Data>()

    fun append(posts: List<Data>) {
        this.posts.addAll(posts)
    }

    fun clear() = posts.clear()

    override fun buildModels() {
        var index = 0
        val chunkedPosts = this.posts.chunked(20)
        chunkedPosts.forEachIndexed { page, posts ->
            if (page >= 1) {
                addPageDivide(index, page + 1)
                index += 1
            }
            addPage(index, posts)
            index += posts.size * 2
        }
    }

    private fun addPage(start: Int, posts: List<Data>) {
        var position = 0
        val end = start + posts.size * 2
        for (index in start until end step 2) {
            // add post model and divider
            modelFactory.build(index, posts[position]).addTo(this)
            position += 1
            // avoid default divide for the last model in page
            // because there will be page divider instead
            if (index + 1 == end - 1) break
            addModelDivide(index + 1)
        }
    }

    private fun addModelDivide(position: Int) {
        DividerEpoxyModel_().id(position).addTo(this)
    }

    private fun addPageDivide(index: Int, page: Int) {
        println("Divide at position $index for page $page")
        val model = PageDividerEpoxyModel_()
        model.id(index)
        model.text(page.toString())
        model.addTo(this)
    }
}

class PostModelFactory(private val router: Router) {

    fun build(id: Int, post: Data): PostEpoxyModel {
        println("Model at position $id")
        val model = PostEpoxyModel_()
        model.id(id)
        model.title = post.title
        model.author = post.author.login
        model.timePublished = post.timePublished
        model.hubs = post.hubs.joinToString(", ") { it.title }
        model.score = post.score
        model.readingsCount = post.readingCount
        model.commentsCount = post.commentsCount
        model.clickListener = View.OnClickListener {
            val screen = PostScreen(post.id)
            router.navigateTo(screen)
        }
        return model
    }
}