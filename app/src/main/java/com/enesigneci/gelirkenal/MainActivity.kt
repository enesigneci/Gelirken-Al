package com.enesigneci.gelirkenal

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.ads.MobileAds
import com.google.firebase.crashlytics.FirebaseCrashlytics


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseCrashlytics.getInstance().sendUnsentReports()
        setContentView(R.layout.main_activity)

        setupToolbar()

        MobileAds.initialize(this)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.logo =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_twotone_shopping_cart_24, theme)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.btnShare) {
            // do something here
        }
        return super.onOptionsItemSelected(item)
    }
}