package li.x1ang.remotesms.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationManagerCompat
import li.x1ang.remotesms.App
import li.x1ang.remotesms.service.SMSService

fun notify(context: Context){
    val notificationId = 0x1234
    val channelId = "1"
    val info = "服务状态：${SMSService.isRunning} 收到短信：${App.msgReceived} 转发短信: ${App.msgSend}"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = (context).getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        val mChannel = NotificationChannel(channelId, "my_channel_01" as CharSequence, NotificationManager.IMPORTANCE_DEFAULT)
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        notificationManager!!.createNotificationChannel(mChannel)
        val builder = Notification.Builder(context, channelId)
        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentText(info)
                .setNumber(3) //久按桌面图标时允许的此条通知的数量
        notificationManager.notify(notificationId, builder.build())
    } else {
        val notificationManager = NotificationManagerCompat.from(context)
        val builder = Notification.Builder(context)
        builder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentText(info)
                .setNumber(3) //久按桌面图标时允许的此条通知的数量
        notificationManager!!.notify(notificationId, builder.build())
    }
}