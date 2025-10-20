package com.example.app_kotlin.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

fun showNotification(context: Context, doctor: String, fecha: String, hora: String) {
    val channelId = "consulta_channel"
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Crear canal solo una vez (Android 8+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Consultas",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificaciones de nuevas consultas"
        }
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Consulta agendada")
        .setContentText("Doctor: $doctor, Fecha: $fecha, Hora: $hora")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .build()

    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}
