package li.x1ang.remotesms


import android.Manifest
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.support.design.widget.TextInputEditText

import li.x1ang.remotesms.service.SMSService

private const val REQUEST_CODE_ASK_PERMISSIONS = 123

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.appbar))

        val serverButton = findViewById<Button>(R.id.button_server_toggle)
        val serverUpdate = findViewById<Button>(R.id.button_update)
        val inputPhone = findViewById<TextInputEditText>(R.id.input_phone)
        val inputContent = findViewById<TextInputEditText>(R.id.input_content)

        serverUpdate.setOnClickListener {
            App.inputPhone = inputPhone.text.toString()
            App.inputContent = inputContent.text.toString()
        }

        /**点击事件处理*/
        serverButton.setOnClickListener {
            if (SMSService.isRunning) {
                stopServices()
                serverButton.text = getText(R.string.server_start)
            } else {
                App.inputPhone = inputPhone.text.toString()
                App.inputContent = inputContent.text.toString()
                startServices()
                serverButton.text = getText(R.string.server_stop)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CONTACTS
                )
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS)
    }

    private fun startServices() {
        startService(Intent(this, SMSService::class.java))
    }

    private fun stopServices() {
        stopService(Intent(this, SMSService::class.java))
    }
}
