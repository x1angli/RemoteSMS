package li.x1ang.remotesms


import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        var inputPhone = ""
        var inputContent = ""
        var msgReceived = 0
        var msgSend = 0
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    lateinit var context: Context
}