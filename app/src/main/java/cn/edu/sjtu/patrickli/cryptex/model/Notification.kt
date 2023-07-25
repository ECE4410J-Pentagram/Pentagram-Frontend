package cn.edu.sjtu.patrickli.cryptex.model

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cn.edu.sjtu.patrickli.cryptex.MainActivity
import cn.edu.sjtu.patrickli.cryptex.R

object Notification {
    @SuppressLint("MissingPermission")
    fun pushNewInvitation(context: Context) {
        val mainIntent = Intent(context, MainActivity::class.java)
        val mainPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(mainIntent)
            getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = NotificationCompat.Builder(
            context, context.getString(R.string.mainNotificationChannelId)
        ).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(context.getString(R.string.newInvitationNotificationTitle))
            setContentText(context.getString(R.string.newInvitationNotificationContent))
            setAutoCancel(true)
            setContentIntent(mainPendingIntent)
        }.build()
        with(NotificationManagerCompat.from(context)) {
            notify(0, notification)
        }
    }
    @SuppressLint("MissingPermission")
    fun pushNewContact(context: Context) {
        val mainIntent = Intent(context, MainActivity::class.java)
        val mainPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(mainIntent)
            getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        val notification = NotificationCompat.Builder(
            context, context.getString(R.string.mainNotificationChannelId)
        ).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(context.getString(R.string.newContactNotificationTitle))
            setContentText(context.getString(R.string.newContactNotificationContent))
            setAutoCancel(true)
            setContentIntent(mainPendingIntent)
        }.build()
        with(NotificationManagerCompat.from(context)) {
            notify(1, notification)
        }
    }
}