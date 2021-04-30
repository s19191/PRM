package pl.edu.pja.covid_19

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val appFeature: AppFeature by lazy { AppFeatureImpl() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate")
        setContentView(setupLayout())
    }

    fun setupLayout() = LinearLayout(this).apply {
        orientation =  LinearLayout.VERTICAL

        addView(makeLabel(resources.getString(R.string.prevention), true))

        val steps = resources.getStringArray(R.array.prevention_steps)
        steps.map { makeLabel(it) }.forEach { addView(it) }

        if (appFeature.isAvaliable) {
            addView(appFeature.inject(context))
        }
    }

    fun makeLabel(content: String, bold: Boolean = false) = TextView(this).apply {
        text = content
        if (bold) typeface = Typeface.DEFAULT_BOLD
    }

    override fun onStart() {
        super.onStart()
        println("onStart")
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }

    override fun onPause() {
        super.onPause()
        println("onPause")
    }

    override fun onStop() {
        super.onStop()
        println("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
    }
}