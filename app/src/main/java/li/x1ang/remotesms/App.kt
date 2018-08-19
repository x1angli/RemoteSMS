package li.x1ang.remotesms


import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        lateinit var context: Context
        var inputPhone = ""
        var inputContent = ""
        var msgReceived = 0
        var msgSend = 0
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}