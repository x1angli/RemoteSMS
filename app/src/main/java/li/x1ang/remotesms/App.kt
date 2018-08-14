package li.x1ang.remotesms


import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        lateinit var context: Context
        var inputPhone = ""
        var inputContent = ""
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}