package com.djac21.kotlin.customTabs

import android.widget.Toast
import android.content.Intent
import android.content.Context
import android.content.BroadcastReceiver
import com.djac21.kotlin.R

class ChromeTabActionBroadcastReceiver: BroadcastReceiver() {
    private val keyActionSource = "org.chromium.customtabsdemos.ACTION_SOURCE"
    private val actionMenuItem1 = 1
    private val actionMenuItem2 = 2
    private val actionButton = 3

    override fun onReceive(context: Context, intent: Intent) {
        val data = intent.dataString

        if (data != null) {
            val toastText = getToastText(context, intent.getIntExtra(keyActionSource, -1), data)

            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getToastText(context: Context, actionSource: Int, message: String): String {
        return when (actionSource) {
            actionMenuItem1 -> context.getString(R.string.action_back)
            actionMenuItem2 -> context.getString(R.string.action_forward)
            actionButton -> context.getString(R.string.action_bookmark)
            else -> context.getString(R.string.action_settings)
        }
    }
}