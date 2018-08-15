package li.x1ang.remotesms.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log

import cz.msebera.android.httpclient.util.TextUtils

import li.x1ang.remotesms.App
import li.x1ang.remotesms.PhoneMessage

import li.x1ang.remotesms.service.SMSService
import li.x1ang.remotesms.utils.*

/**
 * 短信接收处理
 */
class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action?.equals(SMSService.ACTION_SMS_RECEIVED) == true) {
            val bundle = intent.extras
            val pdus = bundle?.get("pdus") as? Array<*>

            if (null != pdus && !pdus.isEmpty()) {
                val messages = pdus.map {
                    SmsMessage.createFromPdu(it as? ByteArray, bundle.getString("format"))
                }
                onSmsReceived(messages)
            }
        }
    }

    private fun onSmsReceived(messages: List<SmsMessage>) {
        Log.d("SMSReceiver","onReceive list $messages.size")
        val smsHelper = SmsHelper()
        val msgBody = smsHelper.getMsgBody(messages)
        Log.d("SMSReceiver", "onReceive message $msgBody")

        val message = messages[0]
        message.let {
            val sender_id = it.displayOriginatingAddress ?: ""
//            val contact = getContactName(context?.contentResolver, it.displayOriginatingAddress) ?: ""
            val timestamp = it.timestampMillis
            val phoneMessage = PhoneMessage(timestamp, sender_id, msgBody)

            val phoneFilter = App.inputPhone
            val contentFilter = App.inputContent

            val isNoFilter = TextUtils.isEmpty(phoneFilter) && TextUtils.isEmpty(contentFilter)
            val isSenderIDFilter = !TextUtils.isEmpty(phoneFilter) && sender_id.contains(phoneFilter, true)
            val isContentFilter = !TextUtils.isEmpty(contentFilter) && msgBody.contains(contentFilter, true)

            Log.d("SMSReceiver","转发规则 $isNoFilter | $isSenderIDFilter | $isContentFilter")

            if (isNoFilter || isSenderIDFilter || isContentFilter) {
                val msgMarkDown = smsHelper.genMsgMarkdown(phoneMessage)
                smsHelper.sendMsg(msgMarkDown)
                log("转发内容:\n $msgMarkDown)}")
            } else {
                log("不符合转发规则 $phoneFilter | $contentFilter")
            }
        }
    }
}
