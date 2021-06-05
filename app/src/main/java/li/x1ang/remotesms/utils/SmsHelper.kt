package li.x1ang.remotesms.utils

import android.content.Context
import android.support.v7.preference.PreferenceManager
import android.telephony.SmsMessage
import android.text.TextUtils
import android.util.Log
import cz.msebera.android.httpclient.HttpStatus
import cz.msebera.android.httpclient.client.methods.HttpPost
import cz.msebera.android.httpclient.entity.StringEntity
import cz.msebera.android.httpclient.impl.client.HttpClients
import cz.msebera.android.httpclient.util.EntityUtils
import li.x1ang.remotesms.App
import li.x1ang.remotesms.PhoneMessage
import org.yaml.snakeyaml.Yaml
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class SmsHelper(context: Context) {
    private val _dateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA)

    private var config: Map<String, Map<String, Any>>

    private var webhook_endpoint: String

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    init {
        //val yamlConfigText = this.javaClass::class.java.getResource("config.yaml")?.readText() ?: ""
        //val yamlConfigText = (context.getString(R.string.yaml_config))
        val yamlConfigText = context.assets.open("config.yaml").bufferedReader().use {
            it.readText()
        }
        Log.i("SMSReceiver", yamlConfigText)

        config = Yaml().load(yamlConfigText)

        webhook_endpoint = config.get("webhook")?.get("endpoint") as String? ?: ""

    }

    fun getPhoneNumByIccId(iccid: String): String {
        if (TextUtils.isEmpty(iccid))
            return ""
        val iccid_map = config.get("simcards")?.get("by_iccid") as Map<String, String>
        val iccid_val = iccid_map.get(iccid)
        return iccid_val ?: ""
    }

    fun getPreferredNumberBySlotId(slotid: String): String {
        val preferredNumber = sharedPreferences.getString("sim${slotid}num", "")
        return preferredNumber!!
    }

    fun getPhoneNumBySlotId(slotid: String): String {
        if (TextUtils.isEmpty(slotid))
            return ""

        val slotid_map = config.get("simcards")?.get("by_slotid") as Map<String, String>
        val slotid_val = slotid_map.get(slotid)
        return slotid_val ?: ""
    }


    fun postWebhook(msg: String) {

        val webhook_type = config.get("webhook")?.get("type") as String

        when (webhook_type) {
            "dingtalk" -> {
                thread {
                    val httpclient = HttpClients.createDefault()

                    var endpoint = sharedPreferences.getString("dingtalk_endpoint", webhook_endpoint)
                    if(TextUtils.isEmpty(endpoint)){
                        endpoint = webhook_endpoint
                    }

                    val httpPost = HttpPost(endpoint)
                    httpPost.addHeader("Content-Type", "application/json; charset=utf-8")

                    val se = StringEntity(msg, "utf-8")
                    httpPost.entity = se

                    val response = httpclient.execute(httpPost)
                    if (response.statusLine.statusCode == HttpStatus.SC_OK) {
                        val result = EntityUtils.toString(response.entity, "utf-8")
                        Log.i("SmsHelper", "postWebhook = $result")
                        App.msgSend++
                    }

                    //
                } // end of mini thread
            } // end of when-case lambda
            else -> {
                Log.w("SMSService", "Unrecognized webhook's type: $webhook_type")
            }
        }// end of when
    }


    fun genMsgMarkdown(phoneMessage: PhoneMessage): String {
        var toReturn: String
        with(phoneMessage) {
            val receiverNumStr = if (receiverNum != null) "#### 终端: $receiverNum\\n" else ""
            toReturn = """
                {
                    "msgtype" : "markdown",
                    "markdown" : {
                        "title" : "转发短信",
                        "text" : "#### 时间: ${_dateFormat.format(timestamp)}\n #### 来自: $senderNum\n $receiverNumStr #### 正文: \n > $content "
                    }
                }
            """.trimIndent()
        }
        return toReturn
    }

    fun getMsgBody(messages: List<SmsMessage>): String {

        return messages.fold("") { body, msg -> body + msg.messageBody}
    }

    fun shouldForwardMessage(sender_id: String, msg_content: String): Boolean {

        val senderIdFilterExists = !TextUtils.isEmpty(App.inputPhone)
        val contentFilterExists = !TextUtils.isEmpty(App.inputContent)

        return (!senderIdFilterExists && !contentFilterExists)
                || (senderIdFilterExists && sender_id.contains(App.inputPhone, true))
                || (contentFilterExists && msg_content.contains(App.inputContent, true))

    }
}


/*
val nameCache = HashMap<String, String>()

fun getContactName(contentResolver: ContentResolver?, number: String,
                   cache: MutableMap<String, String>? = nameCache): String? {
    if (cache?.containsKey(number) == true) {
        return cache[number]
    }

    val uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
    contentResolver?.query(uri, arrayOf(PhoneLookup._ID, PhoneLookup.DISPLAY_NAME), null, null, null).use { cursor ->
        if (cursor != null && cursor.count > 0) {
            cursor.moveToNext()
            val name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME))
            cursor.close()
            cache?.set(number, name)
            return name
        }
    }

    return null
}
*/