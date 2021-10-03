import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import com.makentoshe.habrachan.application.android.screen.article.R

fun <F : Fragment> FragmentScenario<F>.`then resume and execute`(
    action: (F) -> Unit
): FragmentScenario<F> {
    return moveToState(Lifecycle.State.RESUMED).onFragment(action)
}

fun <F : Fragment> FragmentScenario<F>.`and release at finish`() {
    moveToState(Lifecycle.State.DESTROYED)
}

inline fun <reified F : Fragment> F.`launch this fragment`(): FragmentScenario<F> {
    return launchFragmentInContainer(requireArguments(), R.style.Theme_MaterialComponents) {
        return@launchFragmentInContainer this
    }
}

inline fun <reified F : Fragment> F.`launch this fragment and execute`(crossinline action: F.() -> Unit): FragmentScenario<F> {
    return launchFragmentInContainer(requireArguments(), R.style.Theme_MaterialComponents) {
        return@launchFragmentInContainer this.apply(action)
    }
}
