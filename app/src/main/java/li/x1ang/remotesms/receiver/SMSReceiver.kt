package li.x1ang.remotesms.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.telephony.SmsMessage
import android.telephony.SubscriptionManager
import android.text.TextUtils
import android.util.Log
import li.x1ang.remotesms.App
import li.x1ang.remotesms.PhoneMessage
import li.x1ang.remotesms.service.SMSService
import li.x1ang.remotesms.utils.SmsHelper
import li.x1ang.remotesms.utils.notify

/**
 * 短信接收处理
 */
class SMSReceiver(context: Context) : BroadcastReceiver() {
    //    @SuppressLint("MissingPermission", "HardwareIds")
//    fun getLocalPhoneNumber(): String {
//        val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        return tm.line1NumberonCreate
//    }
    val smsHelper = SmsHelper(context)

    private fun getReceiverNum(context: Context, bundle: Bundle): String? {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED) {
            val manager = context.getSystemService(SubscriptionManager::class.java)

            val slotIdx = bundle.getInt("slot")

            manager.getActiveSubscriptionInfoForSimSlotIndex(slotIdx)?.let {
                if (it.number?.length ?: 0 > 0)
                    return it.number
                val numByIccId = smsHelper.getPhoneNumByIccId(it.iccId)
                if (!TextUtils.isEmpty(numByIccId))
                    return numByIccId
            }
            return smsHelper.getPhoneNumBySlotId(slotIdx.toString())
        }
        return null
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == SMSService.ACTION_SMS_RECEIVED) {
            val bundle = intent.extras

            bundle?.let {
                val receiverNum = getReceiverNum(context, it)

                val pdus = it.get("pdus") as Array<*>

                App.msgReceived++
                val messages = pdus.map {
                    SmsMessage.createFromPdu(it as? ByteArray, bundle.getString("format"))
                }
                onSmsReceived(context, messages, receiverNum)
            }
        }
    }

    private fun onSmsReceived(context: Context, messages: List<SmsMessage>, receiverNum: String?) {
        Log.d("SMSReceiver","onReceive list $messages.size")

        val msgBody = smsHelper.getMsgBody(messages)
        Log.d("SMSReceiver", "onReceive message $msgBody")

        val message = messages[0]

        val senderNum = message.displayOriginatingAddress ?: ""
        // val contact = getContactName(context?.contentResolver, it.displayOriginatingAddress) ?: ""
        val phoneMessage = PhoneMessage(message.timestampMillis, senderNum, receiverNum, msgBody)

        if (smsHelper.shouldForwardMessage(senderNum, msgBody)) {
            val msgMarkDown = smsHelper.genMsgMarkdown(phoneMessage)
            smsHelper.postWebhook(msgMarkDown)
            Log.i("SMSReceiver", "Forwarded Message:\n $msgMarkDown)}")
            notify(context)
        } else {
            Log.i("SMSReceiver", "Canceled Message:\n $msgBody)}")
        }
    }


}
