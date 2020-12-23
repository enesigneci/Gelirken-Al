package com.enesigneci.gelirkenal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    lateinit var mainActivityIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mainActivityIntent = Intent(this, MainActivity::class.java)
        var uri = intent.data
        uri?.let {
            mainActivityIntent.putExtra("deeplink", it.host)
        }
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(mainActivityIntent)
            finish()
        }, 2000)
    }
}