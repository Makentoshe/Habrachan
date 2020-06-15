package com.makentoshe.habrachan.view.comments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.ui.softkeyboard.SoftKeyboardController
import com.makentoshe.habrachan.navigation.StackNavigator
import com.makentoshe.habrachan.navigation.comments.CommentsDisplayFragmentScreen
import com.makentoshe.habrachan.navigation.comments.CommentsFlowFragmentArguments
import com.makentoshe.habrachan.ui.comments.CommentsFlowFragmentUi
import com.makentoshe.habrachan.ui.comments.CommentsInputFragmentUi
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Navigator
import toothpick.ktp.delegate.inject

class CommentsFlowFragment : CommentsInputFragment() {

    override val commentsInputFragmentUi by inject<CommentsInputFragmentUi>()
    override val sendCommentViewModel by inject<SendCommentViewModel>()
    override val disposables by inject<CompositeDisposable>()
    override val softKeyboardController = SoftKeyboardController()

    private val arguments by inject<CommentsFlowFragmentArguments>()
    private val commentsFlowFragmentUi by inject<CommentsFlowFragmentUi>()

    override val articleId: Int
        get() = arguments.articleId

    private lateinit var navigator: Navigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = StackNavigator(requireActivity(), R.id.main_container, childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return commentsFlowFragmentUi.inflateView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val containedFragment = childFragmentManager.findFragmentById(R.id.comments_fragment_container)
        if (containedFragment == null) {
            val commentsFragment = CommentsDisplayFragmentScreen(
                arguments.articleId, null, arguments.commentActionsEnabled
            ).fragment
            childFragmentManager.beginTransaction().add(R.id.comments_fragment_container, commentsFragment).commitNow()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val COMMENTS_DISPLAY_FRAGMENT_RESULT_CODE = 1
    }
}