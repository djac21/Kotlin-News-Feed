package com.djac21.kotlin.customTabs

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import java.util.ArrayList

class CustomTabsHelper {

    fun getPackageNameToUse(context: Context): String? {
        val stablePackage = "com.android.chrome"
        val betaPackage = "com.chrome.beta"
        val devPackage = "com.chrome.dev"
        val localPackage = "com.google.android.apps.chrome"
        val actionCustomTabsConnection = "android.support.customtabs.action.CustomTabsService"
        var sPackageNameToUse: String? = null
        if (sPackageNameToUse != null) return sPackageNameToUse

        val pm = context.packageManager
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
        val defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0)
        var defaultViewHandlerPackageName: String? = null
        if (defaultViewHandlerInfo != null)
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName

        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs = ArrayList<String>()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = actionCustomTabsConnection
            serviceIntent.setPackage(info.activityInfo.packageName)
            if (pm.resolveService(serviceIntent, 0) != null)
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
        }

        if (packagesSupportingCustomTabs.isEmpty())
            sPackageNameToUse = null
        else if (packagesSupportingCustomTabs.size == 1)
            sPackageNameToUse = packagesSupportingCustomTabs.get(0)
        else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
                && !hasSpecializedHandlerIntents(context, activityIntent)
                && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName))
            sPackageNameToUse = defaultViewHandlerPackageName
        else if (packagesSupportingCustomTabs.contains(stablePackage))
            sPackageNameToUse = stablePackage
        else if (packagesSupportingCustomTabs.contains(betaPackage))
            sPackageNameToUse = betaPackage
        else if (packagesSupportingCustomTabs.contains(devPackage))
            sPackageNameToUse = devPackage
        else if (packagesSupportingCustomTabs.contains(localPackage))
            sPackageNameToUse = localPackage

        return sPackageNameToUse
    }

    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        val tag = "CustomTabsHelper"

        try {
            val pm = context.packageManager
            val handlers = pm.queryIntentActivities(
                    intent,
                    PackageManager.GET_RESOLVED_FILTER)
            if (handlers == null || handlers.size == 0)
                return false

            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter ?: continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
                if (resolveInfo.activityInfo == null) continue
                return true
            }
        } catch (e: RuntimeException) {
            Log.e(tag, "Runtime exception while getting specialized handlers")
        }

        return false
    }
}