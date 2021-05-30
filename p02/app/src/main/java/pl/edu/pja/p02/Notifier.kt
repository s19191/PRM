package pl.edu.pja.p02

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.GeofencingEvent

class Notifier  : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("Poza domem!")
        val event = intent?.let {
            GeofencingEvent.fromIntent(it)
        }
        event?.errorCode

        event?.triggeringGeofences?.first()?.requestId
        context?.let {

            val notification = NotificationCompat.Builder(it, "pl.edu.pja.kwarantanna13.Geofence")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Wyszedłeś z kwarantanny")
                .build()
            it.getSystemService(NotificationManager::class.java)
                ?.notify(1, notification)
        }
    }
}