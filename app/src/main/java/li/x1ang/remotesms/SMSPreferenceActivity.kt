package li.x1ang.remotesms

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import li.x1ang.remotesms.fragments.SMSPreferenceFragment

class SMSPreferenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        setSupportActionBar(findViewById(R.id.appbar))
        if(supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            val toolbar = findViewById<Toolbar>(R.id.appbar)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, SMSPreferenceFragment())
                .commit()
    }
}