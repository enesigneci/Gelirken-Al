package com.enesigneci.gelirkenal

import android.app.Application
import android.content.Context
import java.util.*

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null
        var appContext: Context? = null
            private set
        var uuid: UUID? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        uuid = UUID.randomUUID()
    }
}