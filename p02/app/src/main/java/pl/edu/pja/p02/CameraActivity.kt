package pl.edu.pja.p02

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import pl.edu.pja.p02.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    val cam by lazy {
        CameraUtil(getSystemService(Context.CAMERA_SERVICE) as CameraManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.surfaceView.holder.addCallback(this)

        binding.shutterButton.setOnClickListener {
//            cam.acquire()
        }
    }

    override fun onResume() {
        super.onResume()
        cam.openCamera()
    }

    override fun onPause() {
        cam.closeCamera()
        super.onPause()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        holder.surface?.let { cam.setupPreviewSession(it) }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }
}