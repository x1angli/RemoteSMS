package li.x1ang.remotesms


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import li.x1ang.remotesms.service.SMSService
import android.view.View

import li.x1ang.remotesms.utils.hideKeyboard


private const val REQUEST_CODE_ASK_PERMISSIONS = 123

class MainActivity : AppCompatActivity() {
    lateinit var tvInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.appbar))

        tvInfo = findViewById(R.id.tv_info)
        val serverButton = findViewById<Button>(R.id.button_server_toggle)
        val serverUpdate = findViewById<Button>(R.id.button_update)
        val inputPhone = findViewById<TextInputEditText>(R.id.input_phone)
        val inputContent = findViewById<TextInputEditText>(R.id.input_content)

        serverUpdate.setOnClickListener {
            App.inputPhone = inputPhone.text.toString()
            App.inputContent = inputContent.text.toString()
            refreshInfo(SMSService.isRunning)
        }

        /**点击事件处理*/
        serverButton.setOnClickListener {
            if (SMSService.isRunning) {
                stopServices()
                serverButton.text = getText(R.string.server_start)
                refreshInfo(false)
            } else {
                App.inputPhone = inputPhone.text.toString()
                App.inputContent = inputContent.text.toString()
                startServices()
                serverButton.text = getText(R.string.server_stop)
                refreshInfo(true)
            }
        }

        val contentView = findViewById<View>(R.id.content)
        contentView.setOnClickListener {
            if (currentFocus != null) {
                /*
                 hide keyboard if tap outside of text inputs
                 当用户触摸输入框以外部分市，隐藏软键盘
                 */
                hideKeyboard()
                currentFocus.clearFocus()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_ctx_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("SetTextI18n")
    private fun refreshInfo(running: Boolean) {
        if(!isFinishing){
            val info = "服务状态：$running \n号码过滤：${App.inputPhone} \n内容过滤: ${App.inputContent}"
            tvInfo.text = info
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null && item!!.itemId == R.id.ctx_settings){
            //show remotesms settings
            startActivity(Intent(this, SMSPreferenceActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
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
