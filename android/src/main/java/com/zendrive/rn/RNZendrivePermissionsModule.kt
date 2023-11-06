package com.zendrive.rn

import android.app.Activity
import android.app.AppOpsManager
import android.app.AppOpsManager.OnOpChangedListener
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.Settings
import android.util.Log
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = RNZendrivePermissionsModule.MODULE_NAME, needsEagerInit = false)
class RNZendrivePermissionsModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext), ActivityEventListener {
  private var overlayPromise: Promise? = null
  private var hasOverlayPermission: Boolean = false
  private lateinit var onOpChangedListener: OnOpChangedListener

  override fun onNewIntent(intent: Intent?) {
    // no-op
  }

  override fun onActivityResult(
    activity: Activity?,
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    if (SETTINGS_OVERLAY_DRAW_REQUEST_CODE == requestCode) {
      if (VERSION.SDK_INT >= VERSION_CODES.M) {
        if (currentActivity != null) {
          val appOpsManager = reactApplicationContext.getSystemService(AppOpsManager::class.java)
          appOpsManager!!.stopWatchingMode(onOpChangedListener)
          reactApplicationContext.removeActivityEventListener(this)
          overlayPromise?.resolve(hasOverlayPermission)
          overlayPromise = null
        }
      }
    }
  }

  override fun getName(): String {
    return MODULE_NAME
  }

  @ReactMethod
  fun checkOverlay(promise: Promise) {
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      promise.resolve(Settings.canDrawOverlays(currentActivity))
    } else {
      promise.resolve(true)
    }
  }

  @ReactMethod
  fun requestOverlay(
    promise: Promise
  ) {
    if (overlayPromise != null) {
      Log.i(LOG_TAG, "Another request overlay permission already in progress")
      promise.resolve(false)
      return
    }
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      if (Settings.canDrawOverlays(currentActivity)) {
        promise.resolve(true)
      } else {
        if (currentActivity != null) {
          reactApplicationContext.addActivityEventListener(this)
          overlayPromise = promise
          hasOverlayPermission = Settings.canDrawOverlays(currentActivity)
          onOpChangedListener = OnOpChangedListener { op, packageName ->
            if (AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW == op &&
              reactApplicationContext.packageName == packageName
            ) {
              hasOverlayPermission = !hasOverlayPermission
            }
          }
          val appOpsManager = reactApplicationContext.getSystemService(AppOpsManager::class.java)
          appOpsManager!!.startWatchingMode(
            AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
            null, onOpChangedListener
          )
          val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + reactApplicationContext.packageName)
          )
          currentActivity?.startActivityForResult(intent, SETTINGS_OVERLAY_DRAW_REQUEST_CODE)
        }
      }
    } else {
      promise.resolve(true)
    }
  }

  @ReactMethod
  fun openAdvancedBatterySettings(
    promise: Promise
  ) {
    if (BrandHelper.needsAdvancedBatteryChecks() && currentActivity != null) {
      val startIntent = BrandHelper.startIntentAdvancedBatterySettings(currentActivity!!)
      if (startIntent != null) {
        currentActivity!!.startActivityForResult(startIntent, SETTINGS_BATTERY_OPTIMIZATIONS)
        promise.resolve(true)
      } else {
        promise.resolve(false)
      }
    }
    promise.resolve(false)
  }

  companion object {
    const val MODULE_NAME = "ZendrivePermissions"
    const val SETTINGS_OVERLAY_DRAW_REQUEST_CODE = 0x990
    const val SETTINGS_BATTERY_OPTIMIZATIONS = 0x991
  }
}
