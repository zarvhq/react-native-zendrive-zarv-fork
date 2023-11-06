package com.zendrive.rn

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.zendrive.sdk.debug.ZendriveDebug

@ReactModule(name = RNZendriveDebugModule.MODULE_NAME, needsEagerInit = false)
class RNZendriveDebugModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  override fun getName(): String {
    return MODULE_NAME
  }

  @ReactMethod
  fun uploadAllZendriveData(
    zendriveConfiguration: ReadableMap,
    notificationConfiguration: ReadableMap?,
    promise: Promise
  ) {
    val conf = zendriveConfigurationFromRnObject(zendriveConfiguration)!!
    ZendriveDebug.uploadAllZendriveData(
      reactApplicationContext,
      conf,
      zendriveNotificationContainer(reactApplicationContext, fromMap = notificationConfiguration)
    ) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun isZendriveSessionIdentifier(
    @Suppress("UNUSED_PARAMETER") sessionId: String,
    promise: Promise
  ) {
    val resolved = Arguments.createMap()
    resolved.putString("errorCode", "zendrive-invalid-params")
    resolved.putString("errorMessage", "Not applicable in android")
    resolved.putBoolean("isSuccess", false)
    promise.resolve(resolved)
  }

  @ReactMethod
  fun handleEventsForBackgroundURLSession(
    @Suppress("UNUSED_PARAMETER") sessionId: String,
    promise: Promise
  ) {
    val resolved = Arguments.createMap()
    resolved.putString("errorCode", "zendrive-invalid-params")
    resolved.putString("errorMessage", "Not applicable in android")
    resolved.putBoolean("isSuccess", false)
    promise.resolve(resolved)
  }

  companion object {
    const val MODULE_NAME = "ZendriveDebug"
  }
}
