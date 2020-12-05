package com.enesigneci.gelirkenal

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.enesigneci.gelirkenal.data.AppDatabase
import com.google.android.gms.ads.MobileAds
import com.google.firebase.crashlytics.FirebaseCrashlytics


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseCrashlytics.getInstance().sendUnsentReports()
        setContentView(R.layout.main_activity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.logo = ResourcesCompat.getDrawable(resources, R.drawable.ic_twotone_shopping_cart_24, theme)
        setSupportActionBar(toolbar)
        MobileAds.initialize(this)
    }
}