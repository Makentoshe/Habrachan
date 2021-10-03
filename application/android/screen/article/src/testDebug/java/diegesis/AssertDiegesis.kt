import android.view.View
import android.widget.TextView
import org.junit.Assert

infix fun View.`visibility should be`(visibility: Int) {
    Assert.assertEquals(visibility, this.visibility)
}

infix fun TextView.`text should be`(resource: Int) {
    Assert.assertEquals(resources.getString(resource), text)
}

infix fun TextView.`text should be`(string: String) {
    Assert.assertEquals(string, text)
}
