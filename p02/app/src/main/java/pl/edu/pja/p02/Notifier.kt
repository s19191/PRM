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
        println("Poza domem!")
        val event = intent?.let {
            GeofencingEvent.fromIntent(it)
        }
        event?.errorCode
        val descriptionActivity = Intent(context, PhotoLookUpActivity::class.java)
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, descriptionActivity, 0)
        val cos = event?.triggeringGeofences?.first()?.requestId
        println(cos)
        context?.let {
            val notification = NotificationCompat.Builder(it, "pl.edu.pja.p02.Geofence")
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Przeżyj to jeszcze raz!")
                .addAction(R.mipmap.ic_launcher_foreground, "Aaaa",
                    snoozePendingIntent)
                .setFullScreenIntent(snoozePendingIntent, true)
                .build()
            it.getSystemService(NotificationManager::class.java)
                ?.notify(1, notification)
        }
    }
}