package pl.edu.pja.covid_19

import android.content.Context
import android.view.View

interface AppFeature {
    val isAvaliable: Boolean
    fun inject(context: Context): View
}