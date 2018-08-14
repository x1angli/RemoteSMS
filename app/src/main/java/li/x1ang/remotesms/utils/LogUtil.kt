package li.x1ang.remotesms.utils

import android.util.Log
import li.x1ang.remotesms.BuildConfig

const val tag = "remotesms"

fun log(info: String){
    if(BuildConfig.DEBUG){
        Log.i(tag, info)
    }
}