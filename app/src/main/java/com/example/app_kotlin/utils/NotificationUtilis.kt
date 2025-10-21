package com.example.app_kotlin.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.app_kotlin.R

fun showAgendaNotification(context: Context, message: String) {
    val channelId = "agenda_channel"

    // Crear canal de notificación (solo Android 8+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Recordatorios de Agenda",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificaciones para recordatorios de consultas médicas"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Construir la notificación
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Consulta agendada")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    // Mostrarla
    with(NotificationManagerCompat.from(context)) {
        notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
