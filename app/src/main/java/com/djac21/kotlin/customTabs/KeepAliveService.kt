package com.djac21.kotlin.customTabs

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class KeepAliveService : Service() {
    private val sBinder = Binder()

    override fun onBind(intent: Intent): IBinder? {
        return sBinder
    }
}
