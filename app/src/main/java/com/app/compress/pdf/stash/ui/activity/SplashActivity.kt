package com.app.compress.pdf.stash.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.app.compress.pdf.stash.R
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash__screen)
        MobileAds.initialize(this) {}
        Handler().postDelayed({ // This method will be executed once the timer is over
            // Start your app main activity
            val onBoard: Intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(onBoard)
            finish()
        }, 4000)
    }
}
