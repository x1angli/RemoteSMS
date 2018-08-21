package li.x1ang.remotesms.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

import li.x1ang.remotesms.receiver.SMSReceiver
import li.x1ang.remotesms.utils.log
import li.x1ang.remotesms.utils.notify

/**
 * 短信监听服务，开启后会自动重启
 */
class SMSService : Service() {
    private val smsReceiver = SMSReceiver()

    companion object {
        val ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
        var isRunning = false
    }

    override fun onBind(p0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_SMS_RECEIVED)
        registerReceiver(smsReceiver, intentFilter)

        isRunning = true
        log("SMSService  onCreate")
        notify(this)
    }

    override fun onDestroy() {
        isRunning = false
        super.onDestroy()

        unregisterReceiver(smsReceiver)

        val intent = Intent("li.x1ang.sms.permanent")
        sendBroadcast(intent)
        log("SMSService  onDestroy")
        notify(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("SMSService  onStartCommandc")
        return START_STICKY
    }
}