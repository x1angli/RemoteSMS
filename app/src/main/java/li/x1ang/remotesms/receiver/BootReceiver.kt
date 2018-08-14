package li.x1ang.remotesms.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import li.x1ang.remotesms.service.SMSService

/**
 * 用于重启短信监听服务
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startService(Intent(context, SMSService::class.java))
    }
}
