import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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

internal infix fun Toolbar.`tag should be`(@DrawableRes resource: Int) {
    Assert.assertEquals(resource, tag)
}

internal infix fun <VH: RecyclerView.ViewHolder>ViewPager2.`should have adapter`(adapter: RecyclerView.Adapter<VH>) {
    Assert.assertEquals(adapter, this.adapter)
}
