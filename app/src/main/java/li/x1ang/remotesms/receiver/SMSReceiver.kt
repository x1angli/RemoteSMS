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
import java.text.SimpleDateFormat
import java.util.*

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
        val size = messages.size
        Log.d("SMSReceiver","onReceive list $size")
        val messageBody = StringBuilder()
        val message: SmsMessage = messages[0]

        if (size > 1) {
            //长短信拼接处理
            messages.map {
                messageBody.append(it.messageBody)
            }
        } else {
            messageBody.append(message.messageBody)
        }

        Log.d("SMSReceiver", "onReceive message $messageBody")

        message.let {
            val receiver_id = getLocalPhoneNumber()
            val sender_id = it.displayOriginatingAddress ?: ""
//            val contact = getContactName(context?.contentResolver, it.displayOriginatingAddress) ?: ""
            val timestamp = it.timestampMillis
            val phoneMessage = PhoneMessage(timestamp, sender_id, messageBody.toString())

            val phoneFilter = App.inputPhone
            val contentFilter = App.inputContent

            val isNoFilter = TextUtils.isEmpty(phoneFilter) && TextUtils.isEmpty(contentFilter)
            val isSenderIDFilter = !TextUtils.isEmpty(phoneFilter) && sender_id.contains(phoneFilter, true)
            val isContentFilter = !TextUtils.isEmpty(contentFilter) && messageBody.contains(contentFilter, true)

            Log.d("SMSReceiver","转发规则 $isNoFilter | $isSenderIDFilter | $isContentFilter")

            if (isNoFilter || isSenderIDFilter || isContentFilter) {
                val dateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA)
                val msgMD = genMsgMarkdown(receiver_id, dateFormat.format(phoneMessage.timestamp), phoneMessage.sender_id, phoneMessage.content)
                sendMsg(msgMD)
                log("转发内容:\n $msgMD)}")
            } else {
                log("不符合转发规则 $phoneFilter | $contentFilter")
            }
        }
    }
}
