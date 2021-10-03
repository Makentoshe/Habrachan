import android.view.View

interface Actions

object ShortClick: Actions

infix fun View.`perform`(action: Actions) {
    when(action) {
        is ShortClick -> performClick()
    }
}
