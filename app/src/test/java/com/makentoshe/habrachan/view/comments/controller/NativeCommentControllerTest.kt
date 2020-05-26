package com.makentoshe.habrachan.view.comments.controller

import android.content.Intent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class NativeCommentControllerTest {

    private val activityController = Robolectric.buildActivity(AppActivity::class.java, Intent())

    private val mockPopupFactory = mockk<CommentPopupFactory>(relaxed = true)
    private val mockAvatarControllerFactory = mockk<NativeCommentAvatarController.Factory>(relaxed = true)
    private val controller = NativeCommentController(mockPopupFactory, mockAvatarControllerFactory)

    @Test
    fun testShouldSetPositiveScore() {
        val activity = activityController.setup().get()
        val container = activity.findViewById<ViewGroup>(R.id.main_view_pager)
        val textView = TextView(activity).also(container::addView)
        controller.scoreFactory().build(textView).setCommentScore(10)

        assertEquals("+10", textView.text)
        assertEquals(activity.getColor(R.color.positive), textView.currentTextColor)
    }

    @Test
    fun testShouldSetNegativeScore() {
        val activity = activityController.setup().get()
        val container = activity.findViewById<ViewGroup>(R.id.main_view_pager)
        val textView = TextView(activity).also(container::addView)
        controller.scoreFactory().build(textView).setCommentScore(-10)

        assertEquals("-10", textView.text)
        assertEquals(activity.getColor(R.color.negative), textView.currentTextColor)
    }

    @Test
    fun testShouldSetDefaultScore() {
        val activity = activityController.setup().get()
        val container = activity.findViewById<ViewGroup>(R.id.main_view_pager)
        val textView = TextView(activity).also(container::addView)
        controller.scoreFactory().build(textView).setCommentScore(0)

        assertEquals("0", textView.text)
        assertNotEquals(activity.getColor(R.color.negative), textView.currentTextColor)
        assertNotEquals(activity.getColor(R.color.positive), textView.currentTextColor)
    }

    @Test
    fun testShouldSetLevel() {
        val activity = activityController.setup().get()
        val container = activity.findViewById<ViewGroup>(R.id.main_view_pager)
        val levelContainer = LinearLayout(activity).apply {
            orientation = LinearLayout.HORIZONTAL
        }.also(container::addView)
        controller.levelFactory().build(levelContainer).setCommentLevel(5)

        assertEquals(5, levelContainer.childCount)
    }

    @Test
    fun testShouldIgnoreMoreThan10Level() {
        val activity = activityController.setup().get()
        val container = activity.findViewById<ViewGroup>(R.id.main_view_pager)
        val levelContainer = LinearLayout(activity).apply {
            orientation = LinearLayout.HORIZONTAL
        }.also(container::addView)
        controller.levelFactory().build(levelContainer).setCommentLevel(112233)

        assertEquals(10, levelContainer.childCount)
    }

    @Test
    fun testShouldDisplayCommentMessage() {
        val activity = activityController.setup().get()
        val container = activity.findViewById<ViewGroup>(R.id.main_view_pager)
        val textView = TextView(activity).also(container::addView)
        controller.messageFactory().build(textView).setCommentText("<sas>html should be parsed</sas>")

        assertEquals("html should be parsed", textView.text.toString())
    }

    @Test
    fun testShouldDisplayAvatar() {
        val activity = activityController.setup().get()
        val container = activity.findViewById<ViewGroup>(R.id.main_view_pager)
        val imageView = ImageView(activity).also(container::addView)

        controller.avatarFactory().build(imageView).setCommentAvatar("")

        verify { mockAvatarControllerFactory.build(imageView).setCommentAvatar("") }
    }
}