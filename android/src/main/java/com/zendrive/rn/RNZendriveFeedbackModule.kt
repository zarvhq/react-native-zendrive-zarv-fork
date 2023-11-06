package com.zendrive.rn

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule
import com.zendrive.sdk.feedback.ZendriveFeedback

@ReactModule(name = RNZendriveInsuranceModule.MODULE_NAME, needsEagerInit = false)
class RNZendriveFeedbackModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  override fun getName(): String {
    return MODULE_NAME
  }

  @ReactMethod
  fun addDriveCategory(driveId: String, category: String, promise: Promise) {
    ZendriveFeedback.addDriveCategory(
      reactApplicationContext,
      driveId,
      feedbackCategory(from = category)
    )
    promise.resolve(true)
  }

  @ReactMethod
  fun addEventOccurrence(
    driveId: String,
    eventTimestamp: Double,
    eveType: String,
    occurrence: Boolean,
    promise: Promise
  ) {
    ZendriveFeedback.addEventOccurrence(
      reactApplicationContext, driveId,
      eventTimestamp.toLong(), eventType(from = eveType), occurrence
    )
    promise.resolve(true)
  }

  companion object {
    const val MODULE_NAME = "ZendriveFeedback"
  }
}
