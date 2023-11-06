package com.zendrive.rn

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.Settings
import java.util.Locale

object BrandHelper {
  /**
   * Xiaomi
   */
  private const val BRAND_XIAOMI = "xiaomi"
  private const val BRAND_XIAOMI_REDMI = "redmi"
  private const val PACKAGE_XIAOMI_MAIN = "com.miui.securitycenter"
  private const val PACKAGE_XIAOMI_COMPONENT =
    "com.miui.permcenter.autostart.AutoStartManagementActivity"

  /**
   * Huawei
   */
  private const val BRAND_HUAWEI = "huawei"
  private const val PACKAGE_HUAWEI_MAIN = "com.huawei.systemmanager"
  private const val PACKAGE_HUAWEI_COMPONENT =
    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
  private const val PACKAGE_HUAWEI_COMPONENT_FALLBACK =
    "com.huawei.systemmanager.optimize.process.ProtectActivity"

  /**
   * Samsumg
   */
  private const val BRAND_SAMSUNG = "samsung"

  /**
   * Oneplus
   */
  private const val BRAND_ONEPLUS = "oneplus"

  fun needsAdvancedBatteryChecks(): Boolean {
    return when (Build.BRAND.lowercase(Locale.ROOT)) {
      BRAND_HUAWEI, BRAND_ONEPLUS, BRAND_SAMSUNG, BRAND_XIAOMI, BRAND_XIAOMI_REDMI -> {
        true
      }
      else -> {
        false
      }
    }
  }

  fun startIntentAdvancedBatterySettings(context: Context): Intent? {
    var intent: Intent? = null
    when (Build.BRAND.lowercase(Locale.ROOT)) {
      BRAND_XIAOMI, BRAND_XIAOMI_REDMI -> {
        intent =
          Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + context.getPackageName())
      }
      BRAND_HUAWEI -> {
        intent = startIntent(context, PACKAGE_HUAWEI_MAIN, PACKAGE_HUAWEI_COMPONENT)
        if (intent == null) {
          intent = startIntent(context, PACKAGE_HUAWEI_MAIN, PACKAGE_HUAWEI_COMPONENT_FALLBACK)
        }
      }
      BRAND_SAMSUNG, BRAND_ONEPLUS -> {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
          intent = Intent()
          intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
          intent.data = Uri.parse("package:" + context.packageName)
        }
      }
    }
    if (intent != null && isActivityIntentResolvable(context, intent)) {
      return intent
    }
    return null
  }

  private fun startIntent(
    context: Context,
    packageName: String,
    componentName: String
  ): Intent? {
    val intent = Intent()
    intent.component = ComponentName(packageName, componentName)
    intent.data = Uri.parse("package:" + context.packageName)
    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
    return if (isActivityIntentResolvable(context, intent)) {
      intent
    } else {
      null
    }
  }

  fun isActivityIntentResolvable(
    context: Context,
    intent: Intent
  ): Boolean {
    return context.packageManager.resolveActivity(intent, 0) != null
  }
}
