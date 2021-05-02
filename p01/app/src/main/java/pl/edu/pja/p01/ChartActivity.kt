package pl.edu.pja.p01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.pja.p01.databinding.ActivityChartBinding

class ChartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChartBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}