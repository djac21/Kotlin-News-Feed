package com.djac21.kotlin.customTabs

import android.support.customtabs.CustomTabsIntent
import android.content.Context
import android.net.Uri

class CustomTabActivityHelper {
    interface CustomTabFallback {
        fun openUri(activity: Context, uri: Uri)
    }

    companion object

    fun openCustomTab(activity: Context, customTabsIntent: CustomTabsIntent,
                      uri: Uri, fallback: CustomTabFallback?) {
        val packageName = CustomTabsHelper().getPackageNameToUse(activity)

        if (packageName == null) {
            fallback?.openUri(activity, uri)
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(activity, uri)
        }
    }
}