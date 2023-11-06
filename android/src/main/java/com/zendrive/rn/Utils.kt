package com.zendrive.rn

import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import com.facebook.react.ReactApplication
import com.facebook.react.ReactInstanceManager
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.zendrive.sdk.*
import com.zendrive.sdk.ZendriveAccidentConfidence.HIGH
import com.zendrive.sdk.ZendriveAccidentConfidence.LOW
import com.zendrive.sdk.ZendriveDriveDetectionMode.AUTO_OFF
import com.zendrive.sdk.ZendriveDriveDetectionMode.AUTO_ON
import com.zendrive.sdk.ZendriveDriveDetectionMode.INSURANCE
import com.zendrive.sdk.ZendriveDriverAttributes.ServiceLevel
import com.zendrive.sdk.ZendriveDriverAttributes.ServiceLevel.LEVEL_1
import com.zendrive.sdk.ZendriveDriverAttributes.ServiceLevel.LEVEL_DEFAULT
import com.zendrive.sdk.ZendriveErrorCode.ACCIDENT_DETECTION_NOT_AVAILABLE_FOR_APPLICATION
import com.zendrive.sdk.ZendriveErrorCode.ACCIDENT_DETECTION_NOT_AVAILABLE_FOR_DEVICE
import com.zendrive.sdk.ZendriveErrorCode.ANDROID_VERSION_NOT_SUPPORTED
import com.zendrive.sdk.ZendriveErrorCode.GOOGLE_AND_HUAWEI_MOBILE_SERVICES_UNAVAILABLE
import com.zendrive.sdk.ZendriveErrorCode.GOOGLE_PLAY_SERVICES_UNAVAILABLE
import com.zendrive.sdk.ZendriveErrorCode.GOOGLE_PLAY_SERVICES_UPDATE_REQUIRED
import com.zendrive.sdk.ZendriveErrorCode.HUAWEI_MOBILE_SERVICES_UPDATE_REQUIRED
import com.zendrive.sdk.ZendriveErrorCode.INVALID_DRIVER_ID
import com.zendrive.sdk.ZendriveErrorCode.INVALID_SDK_KEY
import com.zendrive.sdk.ZendriveErrorCode.INVALID_SESSION_ID
import com.zendrive.sdk.ZendriveErrorCode.INVALID_TRACKING_ID
import com.zendrive.sdk.ZendriveErrorCode.INVALID_VALUE_FOR_TRIP_PARAMETER
import com.zendrive.sdk.ZendriveErrorCode.LOCATION_ACCURACY_NOT_AVAILABLE
import com.zendrive.sdk.ZendriveErrorCode.LOCATION_PERMISSION_DENIED
import com.zendrive.sdk.ZendriveErrorCode.MOCK_ACCIDENT_ERROR
import com.zendrive.sdk.ZendriveErrorCode.NETWORK_NOT_AVAILABLE
import com.zendrive.sdk.ZendriveErrorCode.NOTIFICATION_PROVIDER_ERROR
import com.zendrive.sdk.ZendriveErrorCode.NO_MANUAL_DRIVE
import com.zendrive.sdk.ZendriveErrorCode.REGION_SWITCH_ERROR
import com.zendrive.sdk.ZendriveErrorCode.REGION_UNSUPPORTED
import com.zendrive.sdk.ZendriveErrorCode.SDK_NOT_SETUP
import com.zendrive.sdk.ZendriveErrorCode.UNSUPPORTED_DEVICE
import com.zendrive.sdk.ZendriveErrorCode.UNSUPPORTED_OPERATION
import com.zendrive.sdk.ZendriveErrorCode.UNSUPPORTED_VEHICLE_TYPE
import com.zendrive.sdk.ZendriveErrorCode.USER_DEPROVISIONED
import com.zendrive.sdk.ZendriveErrorCode.ZENDRIVE_CONFIGURATION_ERROR
import com.zendrive.sdk.ZendriveErrorCode.ZENDRIVE_DEBUG_UPLOAD_ERROR
import com.zendrive.sdk.ZendriveErrorCode.ZENDRIVE_SDK_ERROR
import com.zendrive.sdk.ZendriveErrorCode.ZENDRIVE_SDK_NOT_TORN_DOWN
import com.zendrive.sdk.ZendriveErrorCode.ZENDRIVE_SDK_SETUP_IN_PROGRESS
import com.zendrive.sdk.ZendriveErrorCode.ZENDRIVE_SDK_TEAR_DOWN_IN_PROGRESS
import com.zendrive.sdk.ZendriveEventType.AGGRESSIVE_ACCELERATION
import com.zendrive.sdk.ZendriveEventType.COLLISION
import com.zendrive.sdk.ZendriveEventType.HARD_BRAKE
import com.zendrive.sdk.ZendriveEventType.HARD_TURN
import com.zendrive.sdk.ZendriveEventType.PHONE_HANDLING
import com.zendrive.sdk.ZendriveEventType.PHONE_SCREEN_INTERACTION
import com.zendrive.sdk.ZendriveEventType.SPEEDING
import com.zendrive.sdk.ZendriveEventType.STOP_SIGN_VIOLATION
import com.zendrive.sdk.ZendrivePauseAutoTrackingResult.EXPIRED_RESUMPTION_TIME_STAMP
import com.zendrive.sdk.ZendriveRegion.EU
import com.zendrive.sdk.ZendriveRegion.US
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.ASSOCIATED_VEHICLES_LIMIT_EXCEEDED
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.ASSOCIATED_VEHICLE_CONFLICT
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.BEACON_SCAN_ALREADY_IN_PROGRESS
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.BLE_SCAN_INTERNAL_ERROR
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.BLE_SCAN_NOT_SUPPORTED
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.BLUETOOTH_NOT_AVAILABLE
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.BLUETOOTH_PERMISSION_REVOKED
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.INVALID_VEHICLE_ID
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.INVALID_ZENDRIVE_VEHICLE_INFO
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.LOCATION_ERROR
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.SUCCESS
import com.zendrive.sdk.ZendriveVehicleTaggingOperationResult.VEHICLE_NOT_ASSOCIATED
import com.zendrive.sdk.ZendriveBusinessHoursOperationResult.BUSINESS_HOURS_NOT_ENABLED
import com.zendrive.sdk.ZendriveBusinessHoursOperationResult.NO_NETWORK
import com.zendrive.sdk.ZendriveBusinessHoursOperationResult.REQUEST_TIMEOUT
import com.zendrive.sdk.feedback.ZendriveFeedback
import java.util.*

const val LOG_TAG = "RNZendrive"
fun driveDetectionMode(from: String): ZendriveDriveDetectionMode {
  return when (from) {
    "auto-off" -> AUTO_OFF
    "auto-on" -> AUTO_ON
    "auto-insurance" -> INSURANCE
    else -> AUTO_OFF
  }
}

fun driveDetectionModeString(from: ZendriveDriveDetectionMode): String {
  return when (from) {
    AUTO_OFF -> "auto-off"
    AUTO_ON -> "auto-on"
    INSURANCE -> "auto-insurance"
  }
}

fun regionString(from: ZendriveRegion): String {
  return when (from) {
    US -> "US"
    EU -> "EU"
  }
}

fun serviceLevelAttribute(from: String): ServiceLevel {
  return when (from) {
    "level-1" -> LEVEL_1
    "default" -> LEVEL_DEFAULT
    else -> LEVEL_DEFAULT
  }
}

fun serviceLevelAttributeString(from: ServiceLevel): String {
  return when (from) {
    LEVEL_1 -> "level-1"
    LEVEL_DEFAULT -> "default"
  }
}

fun eventType(from: String): ZendriveEventType {
  return when (from) {
    "aggressive-acceleration" -> AGGRESSIVE_ACCELERATION
    "collision" -> COLLISION
    "hard-brake" -> HARD_BRAKE
    "hard-turn" -> HARD_TURN
    "phone-handling" -> PHONE_HANDLING
    "phone-screen-interaction" -> PHONE_SCREEN_INTERACTION
    "speeding" -> SPEEDING
    "stop-sign-violation" -> STOP_SIGN_VIOLATION
    else -> AGGRESSIVE_ACCELERATION
  }
}

fun errorCodeString(from: ZendriveErrorCode): String {
  return when (from) {
    ACCIDENT_DETECTION_NOT_AVAILABLE_FOR_APPLICATION
    -> "accident-detection-not-available-for-application"
    ACCIDENT_DETECTION_NOT_AVAILABLE_FOR_DEVICE
    -> "accident-detection-not-available-for-device"
    ANDROID_VERSION_NOT_SUPPORTED -> "android-version-not-supported"
    GOOGLE_PLAY_SERVICES_UNAVAILABLE -> "google-play-services-unavailable"
    GOOGLE_PLAY_SERVICES_UPDATE_REQUIRED -> "google-play-services-update-required"
    INVALID_DRIVER_ID -> "invalid-driver-id"
    INVALID_SDK_KEY -> "invalid-sdk-key"
    INVALID_SESSION_ID -> "invalid-session-id"
    INVALID_TRACKING_ID -> "invalid-tracking-id"
    LOCATION_ACCURACY_NOT_AVAILABLE -> "location-accuracy-not-available"
    LOCATION_PERMISSION_DENIED -> "location-permission-denied"
    NETWORK_NOT_AVAILABLE -> "network-not-available"
    NO_MANUAL_DRIVE -> "no-manual-drive"
    NOTIFICATION_PROVIDER_ERROR -> "notification-provider-error"
    SDK_NOT_SETUP -> "sdk-not-setup"
    UNSUPPORTED_OPERATION -> "unsupported-operation"
    ZENDRIVE_CONFIGURATION_ERROR -> "configuration-error"
    ZENDRIVE_DEBUG_UPLOAD_ERROR -> "debug-upload-error"
    ZENDRIVE_SDK_ERROR -> "sdk-error"
    ZENDRIVE_SDK_NOT_TORN_DOWN -> "sdk-not-torn-down"
    ZENDRIVE_SDK_SETUP_IN_PROGRESS -> "sdk-setup-in-progress"
    ZENDRIVE_SDK_TEAR_DOWN_IN_PROGRESS -> "teardown-in-progress"
    MOCK_ACCIDENT_ERROR -> "mock-accident-error"
    REGION_UNSUPPORTED -> "region-unsupported"
    REGION_SWITCH_ERROR -> "region-switch-error"
    USER_DEPROVISIONED -> "user-deprovisioned"
    UNSUPPORTED_VEHICLE_TYPE -> "unsupported-vehicle-type"
    INVALID_VALUE_FOR_TRIP_PARAMETER -> "invalid-value-for-trip-parameter"
    GOOGLE_AND_HUAWEI_MOBILE_SERVICES_UNAVAILABLE -> "google-and-huawei-mobile-services-unavailable"
    HUAWEI_MOBILE_SERVICES_UPDATE_REQUIRED -> "huawei-mobile-services-update-required"
    UNSUPPORTED_DEVICE -> "unsupported-device"
  }
}

fun errorCodeString(from: ZendriveVehicleTaggingOperationResult): String {
  return when (from) {
    SUCCESS -> "success"
    ZendriveVehicleTaggingOperationResult.SDK_NOT_SETUP -> "sdk-not-setup"
    BLUETOOTH_PERMISSION_REVOKED -> "bluetooth-permission-revoked"
    INVALID_ZENDRIVE_VEHICLE_INFO -> "invalid-zendrive-vehicle-info"
    ASSOCIATED_VEHICLE_CONFLICT -> "associated-vehicle-conflict"
    ASSOCIATED_VEHICLES_LIMIT_EXCEEDED -> "associated-vehicle-limit-exceeded"
    INVALID_VEHICLE_ID -> "invalid-vehicle-id"
    VEHICLE_NOT_ASSOCIATED -> "vehicle-not-associated"
    BLE_SCAN_NOT_SUPPORTED -> "ble-scan-not-supported"
    BEACON_SCAN_ALREADY_IN_PROGRESS -> "beacon-scan-already-in-progress"
    BLUETOOTH_NOT_AVAILABLE -> "bluetooth-not-available"
    BLE_SCAN_INTERNAL_ERROR -> "ble-scan-internal-error"
    LOCATION_ERROR -> "location-error"
  }
}

fun businessHoursResult(from: ZendriveBusinessHoursOperationResult): String {
  return when(from) {
    ZendriveBusinessHoursOperationResult.SUCCESS -> "success"
    BUSINESS_HOURS_NOT_ENABLED -> "business-hours-not-enabled"
    NO_NETWORK -> "no-network"
    REQUEST_TIMEOUT -> "request-timeout"
    ZendriveBusinessHoursOperationResult.SDK_NOT_SETUP -> "sdk-not-setup"
  }
}

fun errorCodeString(from: ZendrivePauseAutoTrackingResult): String {
  return when (from) {
    ZendrivePauseAutoTrackingResult.SDK_NOT_SETUP -> "sdk-not-setup"
    EXPIRED_RESUMPTION_TIME_STAMP -> "expired-resumption-time-stamp"
    ZendrivePauseAutoTrackingResult.SUCCESS -> "success"
  }
}

fun errorCodeString(from: ZendriveResumeAutoTrackingResult): String {
  return when (from) {
    ZendriveResumeAutoTrackingResult.SDK_NOT_SETUP -> "sdk-not-setup"
    ZendriveResumeAutoTrackingResult.SUCCESS -> "success"
  }
}

fun accidentConfidence(from: String): ZendriveAccidentConfidence {
  return when (from) {
    "high" -> HIGH
    "low" -> LOW
    else -> LOW
  }
}

fun feedbackCategory(from: String): ZendriveFeedback.DriveCategory {
  return when (from) {
    "bicycle" -> ZendriveFeedback.DriveCategory.BICYCLE
    "bus" -> ZendriveFeedback.DriveCategory.BUS
    "car" -> ZendriveFeedback.DriveCategory.CAR
    "car-driver" -> ZendriveFeedback.DriveCategory.CAR_DRIVER
    "car-passenger" -> ZendriveFeedback.DriveCategory.CAR_PASSENGER
    "foot" -> ZendriveFeedback.DriveCategory.FOOT
    "invalid" -> ZendriveFeedback.DriveCategory.INVALID
    "motorcycle" -> ZendriveFeedback.DriveCategory.MOTORCYCLE
    "not-car" -> ZendriveFeedback.DriveCategory.NOT_CAR
    "other" -> ZendriveFeedback.DriveCategory.OTHER
    "train" -> ZendriveFeedback.DriveCategory.TRAIN
    "transit" -> ZendriveFeedback.DriveCategory.TRANSIT
    else -> ZendriveFeedback.DriveCategory.OTHER
  }
}

fun zendriveConfigurationFromRnObject(rnObject: ReadableMap): ZendriveConfiguration? {
  var driverId: String? = null
  var sdkKey: String? = null
  var driveDetectionModeStr: String? = null
  var regionStr: String? = null
  var implementsMultipleAccidentCallbacks: Boolean = false
  var enabledBluetoothTripStart = false

  if (rnObject.hasKey("driverId")) {
    rnObject.getString("driverId").let {
      driverId = it
    }
  }

  if (rnObject.hasKey("sdkKey")) {
    rnObject.getString("sdkKey").let {
      sdkKey = it
    }
  }
  var zendriveConfig: ZendriveConfiguration? = null

  if (rnObject.hasKey("driveDetectionMode")) {
    rnObject.getString("driveDetectionMode").let {
      driveDetectionModeStr = it
    }
  }

  if (rnObject.hasKey("region")) {
    rnObject.getString("region").let {
      regionStr = it
    }
  }

  if (rnObject.hasKey("implementsMultipleAccidentCallbacks")) {
    implementsMultipleAccidentCallbacks = rnObject.getBoolean("implementsMultipleAccidentCallbacks")
  }

  if (rnObject.hasKey("enabledBluetoothTripStart")) {
    enabledBluetoothTripStart = rnObject.getBoolean("enabledBluetoothTripStart")
  }

  if (sdkKey != null && driverId != null) {
    zendriveConfig = ZendriveConfiguration(sdkKey, driverId)
    if (driveDetectionModeStr != null) {
      when (driveDetectionModeStr) {
        "auto-on" -> {
          zendriveConfig.setDriveDetectionMode(AUTO_ON)
        }
        "auto-off" -> {
          zendriveConfig.setDriveDetectionMode(AUTO_OFF)
        }
        "insurance" -> {
          zendriveConfig.setDriveDetectionMode(INSURANCE)
        }
      }
    }

    if (regionStr != null) {
      when (regionStr) {
        "US" -> {
          zendriveConfig.region = US
        }
        "EU" -> {
          zendriveConfig.region = EU
        }
      }
    }
  }
  if (zendriveConfig != null) {
    if (rnObject.hasKey("attributes")) {
      rnObject.getMap("attributes")?.let {
        val attrs = zendriveDriverAttributesFromRnObject(it)
        zendriveConfig.driverAttributes = attrs
      }
    }
    zendriveConfig.implementsMultipleAccidentCallbacks = implementsMultipleAccidentCallbacks
    zendriveConfig.enabledBluetoothDriveStart = enabledBluetoothTripStart
  }
  return zendriveConfig
}

fun zendriveDriverAttributesFromRnObject(rnObject: ReadableMap): ZendriveDriverAttributes {
  val zendriveDriverAttributes = ZendriveDriverAttributes()
  val keyIterator = rnObject.keySetIterator()
  while (keyIterator.hasNextKey()) {
    val key = keyIterator.nextKey()
    val readableType = rnObject.getType(key)
    if (readableType == ReadableType.String) {
      val value = rnObject.getString(key)
      when (key) {
        "alias" -> {
          zendriveDriverAttributes.alias = value
        }
        "groupId" -> {
          zendriveDriverAttributes.group = value
        }
        "serviceLevel" -> {
          when (value) {
            "default" -> zendriveDriverAttributes.serviceLevel = LEVEL_DEFAULT
            "level-1" -> zendriveDriverAttributes.serviceLevel = LEVEL_1
          }
        }
        "vehicleType" -> {
          when (value) {
            "car" -> zendriveDriverAttributes.vehicleType = ZendriveVehicleType.CAR
            "motorcycle" -> zendriveDriverAttributes.vehicleType = ZendriveVehicleType.MOTORCYCLE
          }
        }
        else -> {
          zendriveDriverAttributes.setCustomAttribute(key, value)
        }
      }
    }
  }
  return zendriveDriverAttributes
}

fun zendriveVehicleInfoFromRnObject(rnObject: ReadableMap): ZendriveVehicleInfo? {
  var vehicleId: String? = null
  var bluetoothAddress: String? = null
  if (rnObject.hasKey("vehicleId")) {
    vehicleId = rnObject.getString("vehicleId")
  }
  if (rnObject.hasKey("bluetoothAddress")) {
    bluetoothAddress = rnObject.getString("bluetoothAddress")
  }

  if (vehicleId != null && bluetoothAddress != null) {
    return ZendriveVehicleInfo(vehicleId, bluetoothAddress)
  }

  return null
}

fun zendriveStarRatingFromString(rating: String): ZendriveStarRating {
  return when (rating) {
    "5" -> ZendriveStarRating.FIVE
    "4" -> ZendriveStarRating.FOUR
    "3" -> ZendriveStarRating.THREE
    "2" -> ZendriveStarRating.TWO
    "1" -> ZendriveStarRating.ONE
    else -> ZendriveStarRating.NOT_AVAILABLE
  }
}

fun zendriveVehicleTypeFromString(type: String): ZendriveVehicleType {
  return when (type) {
    "car" -> ZendriveVehicleType.CAR
    "motorcycle" -> ZendriveVehicleType.MOTORCYCLE
    else -> ZendriveVehicleType.CAR
  }
}

fun zendriveDriveTypeFromRnObject(type: String): ZendriveDriveType {
  return when (type) {
    "invalid" -> ZendriveDriveType.INVALID
    "drive" -> ZendriveDriveType.DRIVE
    "non-driving" -> ZendriveDriveType.NON_DRIVING
    else -> ZendriveDriveType.INVALID
  }
}

fun phonePositionFromRnObject(position: String): PhonePosition {
  return when (position) {
    "mount" -> PhonePosition.MOUNT
    "unknown" -> PhonePosition.UNKNOWN
    else -> PhonePosition.UNKNOWN
  }
}

fun zendriveInsurancePeriodFromRnObject(insurance: String): ZendriveInsurancePeriod {
  return when (insurance) {
    "period-1" -> ZendriveInsurancePeriod.Period1
    "period-2" -> ZendriveInsurancePeriod.Period2
    "period-3" -> ZendriveInsurancePeriod.Period3
    else -> ZendriveInsurancePeriod.Period1
  }
}

fun zendriveSettingsFromRnObject(type: String): String {
  return when (type) {
    "POWER_SAVER_MODE_ENABLED" -> "power-saver-mode-enabled"
    "BACKGROUND_RESTRICTION_ENABLED" -> "background-restriction-enabled"
    "LOCATION_PERMISSION_DENIED" -> "location-permission-denied"
    "LOCATION_SETTINGS_ERROR" -> "location-settings-error"
    "WIFI_SCANNING_DISABLED" -> "wifi-scanning-disabled"
    "GOOGLE_PLAY_CONNECTION_ERROR" -> "google-play-connection-error"
    "GOOGLE_PLAY_SETTINGS_ERROR" -> "google-play-settings-error"
    "OVERLAY_PERMISSION_DENIED" -> "overlay-permission-denied"
    "ACTIVITY_RECOGNITION_PERMISSION_DENIED" -> "activity-recognition-permission-denied"
    "BATTERY_OPTIMIZATION_ENABLED" -> "battery-optimization-enabled"
    "ONE_PLUS_DEEP_OPTIMIZATION" -> "one-plus-deep-optimization"
    "AIRPLANE_MODE_ENABLED" -> "airplane-mode-enabled"
    "BLUETOOTH_DISABLED" -> "bluetooth-disabled"
    "LOCATION_UNAVAILABLE_WHILE_DRIVE_RESUME" -> "location-unavailable-while-drive-resume"
    "PRECISE_LOCATION_DENIED" -> "precise-location-denied"
    "LOCATION_MODE_HIGH_ACCURACY_DENIED" -> "location-mode-high-accuracy-denied"
    else -> "sdk-error"
  }
}

fun zendriveEventRatingsFromRnObject(rnObject: ReadableMap): ZendriveEventRatings {
  val eventRatings = ZendriveEventRatings()
  if (rnObject.hasKey("aggressiveAccelerationRating")) {
    val ratingString = rnObject.getString("aggressiveAccelerationRating")!!
    eventRatings.aggressiveAccelerationRating = zendriveStarRatingFromString(ratingString)
  }
  if (rnObject.hasKey("hardBrakeRating")) {
    val ratingString = rnObject.getString("hardBrakeRating")!!
    eventRatings.hardBrakeRating = zendriveStarRatingFromString(ratingString)
  }
  if (rnObject.hasKey("hardTurnRating")) {
    val ratingString = rnObject.getString("hardTurnRating")!!
    eventRatings.hardTurnRating = zendriveStarRatingFromString(ratingString)
  }
  if (rnObject.hasKey("phoneHandlingRating")) {
    val ratingString = rnObject.getString("phoneHandlingRating")!!
    eventRatings.phoneHandlingRating = zendriveStarRatingFromString(ratingString)
  }
  if (rnObject.hasKey("speedingRating")) {
    val ratingString = rnObject.getString("speedingRating")!!
    eventRatings.speedingRating = zendriveStarRatingFromString(ratingString)
  }
  return eventRatings
}

fun zendriveDriveInfoFromRnObject(rnObject: ReadableMap): DriveInfo? {
  var driveInfo = DriveInfo()
  if (rnObject.hasKey("averageSpeed")) {
    driveInfo.averageSpeed = rnObject.getDouble("averageSpeed")
  }
  if (rnObject.hasKey("distanceMeters")) {
    driveInfo.distanceMeters = rnObject.getDouble("distanceMeters")
  }
  if (rnObject.hasKey("driveId")) {
    driveInfo.driveId = rnObject.getString("driveId")
  }
  if (rnObject.hasKey("insurancePeriod")) {
    val insurancePeriodString = rnObject.getString("insurancePeriod")
    if (insurancePeriodString != null) {
      val insurancePeriod = zendriveInsurancePeriodFromRnObject(insurancePeriodString)
      driveInfo.insurancePeriod = insurancePeriod
    }
  }
  if (rnObject.hasKey("sessionId")) {
    driveInfo.sessionId = rnObject.getString("sessionId")
  }
  if (rnObject.hasKey("startTimeMillis")) {
    driveInfo.startTimeMillis = rnObject.getDouble("startTimeMillis").toLong()
  }
  if (rnObject.hasKey("endTimeMillis")) {
    driveInfo.endTimeMillis = rnObject.getDouble("endTimeMillis").toLong()
  }
  if (rnObject.hasKey("trackingId")) {
    driveInfo.trackingId = rnObject.getString("trackingId")
  }
  if (rnObject.hasKey("eventRatings")) {
    driveInfo.eventRatings = zendriveEventRatingsFromRnObject(rnObject.getMap("eventRatings")!!)
  }
  if (rnObject.hasKey("driveType")) {
    val driveTypeString = rnObject.getString("driveType")!!
    val driveType = zendriveDriveTypeFromRnObject(driveTypeString)
    driveInfo.driveType = driveType
  }
  if (rnObject.hasKey("vehicleType")) {
    val vehicleTypeString = rnObject.getString("vehicleType")!!
    val vehicleType = zendriveVehicleTypeFromString(vehicleTypeString)
    driveInfo.vehicleType = vehicleType
  }
  if (rnObject.hasKey("phonePosition")) {
    val phonePositionString = rnObject.getString("phonePosition")!!
    val phonePosition = phonePositionFromRnObject(phonePositionString)
    driveInfo.phonePosition = phonePosition
  }
  if (rnObject.hasKey("maxSpeed")) {
    driveInfo.maxSpeed = rnObject.getDouble("maxSpeed")
  }
  if (rnObject.hasKey("score")) {
    val score = rnObject.getMap("score")
    if (score?.hasKey("zendriveScore") == true) {
      val driveScore = DriveScore()
      driveScore.zendriveScore = score.getInt("zendriveScore")
      driveInfo.score = driveScore
    }
  }
  return driveInfo
}

fun notifyJS(
  context: Context?,
  receiver: BroadcastReceiver,
  eventName: String,
  data: WritableMap?
) {
  val reactApplication = context?.applicationContext as? ReactApplication
  val instanceManager = reactApplication?.reactNativeHost?.reactInstanceManager
  val reactContext = instanceManager?.currentReactContext
  if (reactContext != null) {
    Log.d(
      LOG_TAG,
      "React context is available, sending event $eventName"
    )
    reactApplication.reactNativeHost?.reactInstanceManager?.currentReactContext?.getJSModule(
      DeviceEventManagerModule.RCTDeviceEventEmitter::class.java
    )?.emit(eventName, data)
  } else {
    Log.d(LOG_TAG, "React context is not available")
    val pendingResult = receiver.goAsync()
    instanceManager!!.addReactInstanceEventListener(
      object : ReactInstanceManager.ReactInstanceEventListener {
        override fun onReactContextInitialized(reactContext: ReactContext) {
          Log.v(
            LOG_TAG,
            "React context is initialized sending js event $eventName"
          )
          reactContext.getJSModule(
            DeviceEventManagerModule.RCTDeviceEventEmitter::class.java
          )?.emit(eventName, data)
          instanceManager.removeReactInstanceEventListener(this)
          pendingResult.finish()
        }
      })
    instanceManager.createReactContextInBackground()
    Log.d(LOG_TAG, "Called to create context in the background")
  }
}
