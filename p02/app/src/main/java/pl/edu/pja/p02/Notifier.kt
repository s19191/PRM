package pl.edu.pja.p02

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.GeofencingEvent

class Notifier : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val event = intent?.let {
            GeofencingEvent.fromIntent(it)
        }
        event?.errorCode
        val descriptionActivity = Intent(context, PhotoLookUpActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("photoUri", event?.triggeringGeofences?.first()?.requestId)
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context, 0, descriptionActivity, PendingIntent.FLAG_UPDATE_CURRENT
            )
        context?.let {
            val notification = NotificationCompat.Builder(it, "pl.edu.pja.p02.Geofence")
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Przeżyj to jeszcze raz!")
                .addAction(R.mipmap.ic_launcher_foreground, "Podejrzyj swoje zdjęcie wraz z notatką",
                    snoozePendingIntent)
                .setFullScreenIntent(snoozePendingIntent, true)
                .build()
            it.getSystemService(NotificationManager::class.java)
                ?.notify(1, notification)
        }
    }
}