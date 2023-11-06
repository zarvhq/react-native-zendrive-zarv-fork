package com.zendrive.rn

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.module.annotations.ReactModule
import com.zendrive.sdk.*
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.INVALID_ZENDRIVE_VEHICLE_INFO
import java.util.UUID

@ReactModule(name = RNZendriveModule.MODULE_NAME, needsEagerInit = true)
class RNZendriveModule(private val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return MODULE_NAME
  }

  @ReactMethod
  fun getActiveDriveInfo(promise: Promise) {
    val driveInfo = Zendrive.getActiveDriveInfo(reactApplicationContext)
    if (driveInfo != null) {
      promise.resolve(driveInfo.toRnObject())
    } else {
      promise.resolve(null)
    }
  }

  @ReactMethod
  fun getBuildVersion(promise: Promise) {
    promise.resolve(Zendrive.getBuildVersion())
  }

  @ReactMethod
  fun setup(configuration: ReadableMap, promise: Promise) {
    val conf = zendriveConfigurationFromRnObject(configuration)
    val notificationSettings = notificationSettingsFromRnObject(reactContext, configuration)
    if (conf != null) {
      Zendrive.setup(
        reactApplicationContext,
        conf,
        RNZendriveBroadcastReceiver::class.java,
        RNZendriveNotificationProvider::class.java
      ) { res ->
        notificationSettings.saveToPrefs(reactContext)
        promise.resolve(res.toRnObject())
      }
    } else {
      val resolved = Arguments.createMap()
      resolved.putString("errorCode", "zendrive-invalid-params")
      resolved.putString("errorMessage", "Could not resolve zendrive configuration")
      resolved.putBoolean("isSuccess", false)
      promise.resolve(resolved)
    }
  }

  @ReactMethod
  fun getEventSupportForDevice(promise: Promise) {
    val data = Zendrive.getEventSupportForDevice(reactApplicationContext)
    val map = Arguments.createMap()
    for ((k, v) in data) {
      map.putBoolean(k.toRnObject(), v)
    }
    promise.resolve(map)
  }

  @ReactMethod
  fun getZendriveSettings(promise: Promise) {
    Zendrive.getZendriveSettings(reactApplicationContext) {
      if (it != null) {
        promise.resolve(it.toRnObject())
      } else {
        promise.resolve(null)
      }
    }
  }

  @ReactMethod
  fun isAccidentDetectionSupported(promise: Promise) {
    promise.resolve(Zendrive.isAccidentDetectionSupported(reactApplicationContext))
  }

  @ReactMethod
  fun isSDKSetup(promise: Promise) {
    promise.resolve(Zendrive.isSDKSetup(reactApplicationContext))
  }

  @ReactMethod
  fun isValidInputParameter(input: String, promise: Promise) {
    promise.resolve(Zendrive.isValidInputParameter(input))
  }

  @ReactMethod
  fun setZendriveDriveDetectionMode(mode: String, promise: Promise) {
    Zendrive.setZendriveDriveDetectionMode(
      reactApplicationContext,
      driveDetectionMode(from = mode)
    ) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun startDrive(trackingId: String, promise: Promise) {
    val tID: String? = if (trackingId == "") null else trackingId
    Zendrive.startDrive(reactApplicationContext, tID) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun startSession(sessionId: String, promise: Promise) {
    promise.resolve(Zendrive.startSession(reactApplicationContext, sessionId).toRnObject())
  }

  @ReactMethod
  fun stopManualDrive(promise: Promise) {
    Zendrive.stopManualDrive(reactApplicationContext) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun stopSession(promise: Promise) {
    promise.resolve(Zendrive.stopSession(reactApplicationContext).toRnObject())
  }

  @ReactMethod
  fun teardown(promise: Promise) {
    Zendrive.teardown(reactApplicationContext) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun triggerMockPotentialAccident(confidence: ReadableMap, promise: Promise) {
    val accidentConfiguration: ZendriveMockAccidentConfiguration =
      ZendriveMockAccidentConfiguration()
    accidentConfiguration.potentialCallbackConfidence = accidentConfidence(
      from = confidence.getString("potentialCallbackConfidence")!!
    )
    accidentConfiguration.potentialCallbackConfidenceNumber = confidence.getInt(
      "potentialCallbackConfidenceNumber"
    )
    accidentConfiguration.finalCallbackConfidence = accidentConfidence(
      from = confidence.getString("finalCallbackConfidence")!!
    )
    accidentConfiguration.finalCallbackConfidenceNumber = confidence.getInt(
      "finalCallbackConfidenceNumber"
    )
    accidentConfiguration.delayBetweenCallbacksSeconds = confidence.getInt(
      "delayBetweenCallbacksSeconds"
    )
    Zendrive.triggerMockAccident(reactApplicationContext, accidentConfiguration) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun triggerMockAccident(confidence: String, promise: Promise) {
    Zendrive.triggerMockAccident(reactApplicationContext, accidentConfidence(from = confidence)) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun uploadAllDebugDataAndLogs(promise: Promise) {
    promise.resolve(Zendrive.uploadAllDebugDataAndLogs(reactContext).toRnObject())
  }

  @ReactMethod
  fun wipeOut(promise: Promise) {
    Zendrive.wipeOut(reactContext) {
      promise.resolve(it.toRnObject())
    }
  }

  @ReactMethod
  fun getZendriveState(promise: Promise) {
    Zendrive.getZendriveState(reactApplicationContext) {
      if (it != null) {
        promise.resolve(it.toRnObject())
      } else {
        promise.resolve(null)
      }
    }
  }

  @ReactMethod
  fun refreshBusinessHours(promise: Promise) {
    Log.d("Here", "Testing if method is getting called")
    Zendrive.refreshBusinessHours(reactApplicationContext) {
        zendriveShiftDetail: ZendriveShiftDetail?,
        zendriveBusinessHoursOperationResult: ZendriveBusinessHoursOperationResult? ->
      if (zendriveShiftDetail != null && zendriveBusinessHoursOperationResult != null) {
        promise.resolve(zendriveBusinessHoursOperationResult.toRnObject())
      }
      else {
        promise.resolve(null)
      }
    }
  }


  @ReactMethod
  fun getZendriveDriverCustomAttributes(array: ReadableArray, promise: Promise) {
    Zendrive.getZendriveState(reactApplicationContext) {
      if (it != null) {
        val attributes = arrayListOf<String>()
        for (i in 0 until array.size()) {
          if (array.getType(i) == ReadableType.String) {
            array.getString(i)?.let { attribute ->
              attributes.add(attribute)
            }
          }
        }
        val readableMap = Arguments.createMap()
        attributes.forEach { attribute ->
          val attrVal = it.zendriveConfiguration.driverAttributes.getCustomAttribute(attribute)
          readableMap.putString(attribute, attrVal)
        }
        promise.resolve(readableMap)
      } else {
        promise.resolve(null)
      }
    }
  }

  @ReactMethod
  fun autoTrackingPausedReason(promise: Promise){
    val response = Zendrive.getAutoTrackingPauseReason(reactApplicationContext)
    if (response == null){
      promise.resolve(null)
    }else {
      promise.resolve(response.toRnObject())
    }
  }
  @ReactMethod
  fun associateVehicle(info: ReadableMap, promise: Promise) {
    val vehicleInfo = zendriveVehicleInfoFromRnObject(info)
    val response = Arguments.createMap()
    if (vehicleInfo != null) {
      val result = ZendriveVehicleTagging.associateVehicle(reactApplicationContext, vehicleInfo)
      if (result == ZendriveVehicleTaggingOperationResult.SUCCESS) {
        response.putBoolean("isSuccess", true)
      } else {
        response.putBoolean("isSuccess", false)
        response.putString("errorMessage", result.message)
        response.putString("errorCode", errorCodeString(from = result))
      }
      promise.resolve(response)
    } else {
      response.putBoolean("isSuccess", false)
      response.putString("errorMessage", INVALID_ZENDRIVE_VEHICLE_INFO.message)
      response.putString("errorCode", errorCodeString(from = INVALID_ZENDRIVE_VEHICLE_INFO))
      promise.resolve(response)
    }
  }

  @ReactMethod
  fun dissociateVehicle(vehicleId: String?, promise: Promise) {
    val response = Arguments.createMap()
    if (vehicleId != null) {
      val result = ZendriveVehicleTagging.dissociateVehicle(reactApplicationContext, vehicleId)
      if (result == ZendriveVehicleTaggingOperationResult.SUCCESS) {
        response.putBoolean("isSuccess", true)
      } else {
        response.putBoolean("isSuccess", false)
        response.putString("errorMessage", result.message)
      }
      promise.resolve(response)
    } else {
      response.putBoolean("isSuccess", false)
      response.putString("errorMessage", "Not a valid context - activity not available")
      promise.resolve(response)
    }
  }

  @ReactMethod
  fun getAssociatedVehicles(promise: Promise) {
    val data = ZendriveVehicleTagging.getAssociatedVehicles(reactApplicationContext)
    var arr = data?.fold(Arguments.createArray()) { acc, elem ->
      acc.pushMap(elem.toRnObject())
      acc
    }
    promise.resolve(arr ?: Arguments.createArray())
  }

  @ReactMethod
  fun getBluetoothPairedDevices(promise: Promise) {
    val data = ZendriveVehicleTagging.getBluetoothPairedDevices(reactApplicationContext)
    var arr = data?.fold(Arguments.createArray()) { acc, elem ->
      var rnObject = Arguments.createMap()
      if (ActivityCompat.checkSelfPermission(
          reactApplicationContext,
          Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
      }
      rnObject.putString("name", elem.name)
      rnObject.putString("address", elem.address)
      acc.pushMap(rnObject)
      acc
    }
    promise.resolve(arr ?: Arguments.createArray())
  }

  @ReactMethod
  fun getNearbyBeaconsWithRange(uuidString: String, major: Int, minor: Int, promise: Promise) {
    try {
      val uuid = UUID.fromString(uuidString)
      ZendriveVehicleTagging.getNearbyBeacons(
        reactApplicationContext, uuid, major, minor
      ) { _, beaconsInfo ->
        var arr = beaconsInfo?.fold(Arguments.createArray()) { acc, elem ->
          acc.pushMap(elem.toRnObject())
          acc
        }
        promise.resolve(arr ?: Arguments.createArray())
      }
    } catch (err: Exception) {
      promise.resolve(err.localizedMessage)
    }
  }

  @ReactMethod
  fun getNearbyBeacons(uuidString: String, promise: Promise) {
    try {
      val uuid = UUID.fromString(uuidString)
      ZendriveVehicleTagging.getNearbyBeacons(
        reactApplicationContext, uuid, null, null
      ) { _, beaconsInfo ->
        var arr = beaconsInfo?.fold(Arguments.createArray()) { acc, elem ->
          acc.pushMap(elem.toRnObject())
          acc
        }
        promise.resolve(arr ?: Arguments.createArray())
      }
    } catch (err: Exception) {
      promise.resolve(err.localizedMessage)
    }
  }



  @ReactMethod
  fun pauseAutoDriveTracking(pausedTillTimestamp: Double, promise: Promise) {
    var result = Zendrive.pauseAutoTracking(reactApplicationContext, pausedTillTimestamp.toLong())
    val response = Arguments.createMap()
    if (result == ZendrivePauseAutoTrackingResult.SUCCESS) {
      response.putBoolean("isSuccess", true)
    } else {
      response.putBoolean("isSuccess", false)
      response.putString("errorMessage", result.message)
      response.putString("errorCode", errorCodeString(from = result))
    }
    promise.resolve(response)
  }

  @ReactMethod
  fun resumeAutoDriveTracking(promise: Promise) {
    var result = Zendrive.resumeAutoTracking(reactApplicationContext)
    val response = Arguments.createMap()
    if (result == ZendriveResumeAutoTrackingResult.SUCCESS) {
      response.putBoolean("isSuccess", true)
    } else {
      response.putBoolean("isSuccess", false)
      response.putString("errorMessage", result.message)
      response.putString("errorCode", errorCodeString(from = result))
    }
    promise.resolve(response)
  }

  @ReactMethod
  fun isAutoTripTrackingPaused(promise: Promise) {
    promise.resolve(Zendrive.isAutoTripTrackingPaused(reactApplicationContext))
  }

  companion object {
    const val MODULE_NAME = "Zendrive"
  }
}
