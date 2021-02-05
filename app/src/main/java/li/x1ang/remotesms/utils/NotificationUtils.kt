package li.x1ang.remotesms.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationManagerCompat
import li.x1ang.remotesms.App
import li.x1ang.remotesms.MainActivity
import li.x1ang.remotesms.service.SMSService

fun notify(context: Context): Notification{
    val info = "服务状态：${SMSService.isRunning} 收到短信：${App.msgReceived} 转发短信: ${App.msgSend}"
    val notification: Notification

    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 1,
            Intent(context, MainActivity::class.java).apply { putExtra(MainActivity.SERVICE_STARTED, true) },
            PendingIntent.FLAG_UPDATE_CURRENT)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = (context).getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        val mChannel = NotificationChannel(SMSService.channelId, "my_channel_01" as CharSequence, NotificationManager.IMPORTANCE_HIGH)
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(false)
        notificationManager!!.createNotificationChannel(mChannel)
        val builder = Notification.Builder(context, SMSService.channelId)
        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentText(info)
                .setContentIntent(pendingIntent)
                .setNumber(3) //久按桌面图标时允许的此条通知的数量
        notification = builder.build()
       // notificationManager.notify( SMSService.notificationId, notification)
    } else {
        val notificationManager = NotificationManagerCompat.from(context)
        val builder = Notification.Builder(context)
        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentText(info)
                .setContentIntent(pendingIntent)
                .setNumber(3) //久按桌面图标时允许的此条通知的数量
        notification = builder.build()
       // notificationManager!!.notify( SMSService.notificationId, notification)
    }
    return notification
}