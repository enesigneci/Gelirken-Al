package com.enesigneci.gelirkenal

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.enesigneci.gelirkenal.databinding.ContentMainBinding
import com.enesigneci.gelirkenal.databinding.MainActivityBinding
import com.google.android.gms.ads.MobileAds
import com.google.firebase.crashlytics.FirebaseCrashlytics


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseCrashlytics.getInstance().sendUnsentReports()
        binding = MainActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setupToolbar()

        MobileAds.initialize(this)
    }

    private fun setupToolbar() {
        val toolbar = ContentMainBinding.bind(binding.root).toolbar
        toolbar.logo =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_twotone_shopping_cart_24, theme)
        setSupportActionBar(toolbar)
    }
}