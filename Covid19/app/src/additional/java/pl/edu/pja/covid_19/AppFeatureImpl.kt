package pl.edu.pja.covid_19

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button

class AppFeatureImpl : AppFeature {
    override val isAvaliable: Boolean = true

    override fun inject(context: Context): View = Button(context).apply {
        text = context.resources.getString(R.string.button_text)
        setOnClickListener {
            val intent = Intent(context, AdditionalActivity::class.java)
            context.startActivity(intent)
        }
    }
}