package com.zendrive.rn

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule
import com.zendrive.sdk.insurance.ZendriveInsurance

@ReactModule(name = RNZendriveInsuranceModule.MODULE_NAME, needsEagerInit = false)
class RNZendriveInsuranceModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  override fun getName(): String {
    return MODULE_NAME
  }

  @ReactMethod
  fun startDriveWithPeriod1(promise: Promise) {
    ZendriveInsurance.startDriveWithPeriod1(reactApplicationContext) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun startDriveWithPeriod2(trackingId: String, promise: Promise) {
    ZendriveInsurance.startDriveWithPeriod2(reactApplicationContext, trackingId) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun startDriveWithPeriod3(trackingId: String, promise: Promise) {
    ZendriveInsurance.startDriveWithPeriod3(reactApplicationContext, trackingId) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun stopPeriod(promise: Promise) {
    ZendriveInsurance.stopPeriod(reactApplicationContext) {
      promise.resolve(it.toRnObject())
    }
  }

  companion object {
    const val MODULE_NAME = "ZendriveInsurance"
  }
}
