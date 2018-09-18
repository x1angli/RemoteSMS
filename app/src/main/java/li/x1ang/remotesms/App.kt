package li.x1ang.remotesms


import android.app.Application
import android.content.Context
import android.text.TextUtils

class App : Application() {

    companion object {
        var inputPhone = ""
        var inputContent = ""
        var msgReceived = 0
        var msgSend = 0
        val shouldForwardMessage = {sender_id:String, msg_content:String ->

            val senderIdFilterExists = !TextUtils.isEmpty(inputPhone)
            val contentFilterExists = !TextUtils.isEmpty(inputContent)

            (!senderIdFilterExists && !contentFilterExists)
            || (senderIdFilterExists && sender_id.contains(inputPhone, true))
            || (contentFilterExists && msg_content.contains(inputContent, true))

        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    lateinit var context: Context
}