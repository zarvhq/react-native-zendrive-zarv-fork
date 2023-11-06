package com.zendrive.rn

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.zendrive.sdk.*
import com.zendrive.sdk.DriveInfo.Warning.UnexpectedTripDuration
import com.zendrive.sdk.PhonePosition.MOUNT
import com.zendrive.sdk.PhonePosition.UNKNOWN
import com.zendrive.sdk.ZendriveDriveType.DRIVE
import com.zendrive.sdk.ZendriveDriveType.INVALID
import com.zendrive.sdk.ZendriveDriveType.NON_DRIVING
import com.zendrive.sdk.ZendriveEvent.SpeedingInfo
import com.zendrive.sdk.ZendriveEventSeverity.HIGH
import com.zendrive.sdk.ZendriveEventSeverity.LOW
import com.zendrive.sdk.ZendriveEventType.AGGRESSIVE_ACCELERATION
import com.zendrive.sdk.ZendriveEventType.COLLISION
import com.zendrive.sdk.ZendriveEventType.HARD_BRAKE
import com.zendrive.sdk.ZendriveEventType.HARD_TURN
import com.zendrive.sdk.ZendriveEventType.PHONE_HANDLING
import com.zendrive.sdk.ZendriveEventType.PHONE_SCREEN_INTERACTION
import com.zendrive.sdk.ZendriveEventType.SPEEDING
import com.zendrive.sdk.ZendriveEventType.STOP_SIGN_VIOLATION
import com.zendrive.sdk.ZendriveEventType.HANDS_FREE_PHONE_CALL
import com.zendrive.sdk.ZendriveEventType.PASSIVE_DISTRACTION
import com.zendrive.sdk.ZendriveInsurancePeriod.Period1
import com.zendrive.sdk.ZendriveInsurancePeriod.Period2
import com.zendrive.sdk.ZendriveInsurancePeriod.Period3
import com.zendrive.sdk.ZendriveIssueType.*
import com.zendrive.sdk.ZendriveStarRating.FIVE
import com.zendrive.sdk.ZendriveStarRating.FOUR
import com.zendrive.sdk.ZendriveStarRating.NOT_AVAILABLE
import com.zendrive.sdk.ZendriveStarRating.ONE
import com.zendrive.sdk.ZendriveStarRating.THREE
import com.zendrive.sdk.ZendriveStarRating.TWO
import com.zendrive.sdk.ZendriveTurnDirection.LEFT
import com.zendrive.sdk.ZendriveTurnDirection.RIGHT
import com.zendrive.sdk.ZendriveUserMode.DRIVER
import com.zendrive.sdk.ZendriveUserMode.PASSENGER
import com.zendrive.sdk.ZendriveUserMode.UNAVAILABLE

fun ActiveDriveInfo.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  this.currentLocation?.let {
    map.putMap("currentLocation", this.currentLocation?.toRnObject())
  }
  map.putDouble("currentSpeed", this.currentSpeed)
  map.putDouble("distanceMeters", this.distanceMeters)
  map.putString("driveId", this.driveId)
  map.putString("insurancePeriod", this.insurancePeriod?.toRnObject())
  map.putDouble("startTimeMillis", this.startTimeMillis.toDouble())
  this.sessionId.let {
    map.putString("sessionId", this.sessionId)
  }
  this.trackingId.let {
    map.putString("trackingId", this.trackingId)
  }
  return map
}

fun LocationPoint.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putDouble("latitude", this.latitude)
  map.putDouble("longitude", this.longitude)
  return map
}

fun ZendriveInsurancePeriod.toRnObject(): String {
  return when (this) {
    Period1 -> "period-1"
    Period2 -> "period-2"
    Period3 -> "period-3"
  }
}

fun ZendriveEventType.toRnObject(): String {
  return when (this) {
    AGGRESSIVE_ACCELERATION -> "aggressive-acceleration"
    COLLISION -> "collision"
    HARD_BRAKE -> "hard-brake"
    HARD_TURN -> "hard-turn"
    PHONE_HANDLING -> "phone-handling"
    PHONE_SCREEN_INTERACTION -> "phone-screen-interaction"
    SPEEDING -> "speeding"
    STOP_SIGN_VIOLATION -> "stop-sign-violation"
    HANDS_FREE_PHONE_CALL -> "hands-free-phone-call"
    PASSIVE_DISTRACTION -> "passive-distraction"
  }
}

fun ZendriveExtrapolationDetails.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putMap("estimatedStartLocation", this.estimatedStartLocation?.toRnObject())
  map.putDouble("extrapolatedDistance", this.extrapolatedDistance)
  return map
}

fun ZendriveSettings.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  val errors = Arguments.createArray()
  this.errors.forEach {
    errors.pushMap(it.toRnObject())
  }
  val warnings = Arguments.createArray()
  this.warnings.forEach {
    warnings.pushMap(it.toRnObject())
  }
  map.putArray("errors", errors)
  map.putArray("warnings", warnings)
  return map
}

fun ZendriveSettingError.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("type", zendriveSettingsFromRnObject(this.type.toString()))
  return map
}

fun ZendriveSettingWarning.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("type", zendriveSettingsFromRnObject(this.type.toString()))
  return map
}

fun ZendriveIssueType.toRnObject(): String {
  return when (this) {
    POWER_SAVER_MODE_ENABLED -> "power-saver-mode-enabled"
    BACKGROUND_RESTRICTION_ENABLED -> "background-restriction-enabled"
    LOCATION_PERMISSION_DENIED -> "location-permission-denied"
    LOCATION_SETTINGS_ERROR -> "location-settings-error"
    WIFI_SCANNING_DISABLED -> "wifi-scanning-disabled"
    GOOGLE_PLAY_CONNECTION_ERROR -> "google-play-connection-error"
    GOOGLE_PLAY_SETTINGS_ERROR -> "google-play-settings-error"
    OVERLAY_PERMISSION_DENIED -> "overlay-permission-denied"
    ACTIVITY_RECOGNITION_PERMISSION_DENIED -> "activity-recognition-permission-denied"
    BATTERY_OPTIMIZATION_ENABLED -> "battery-optimization-enabled"
    ONE_PLUS_DEEP_OPTIMIZATION -> "one-plus-deep-optimization"
    AIRPLANE_MODE_ENABLED -> "airplane-mode-enabled"
    BLUETOOTH_DISABLED -> "bluetooth-disabled"
    LOCATION_UNAVAILABLE_WHILE_DRIVE_RESUME -> "location-unavailable-while-drive-resume"
    PRECISE_LOCATION_DENIED -> "precise-location-denied"
    LOCATION_MODE_HIGH_ACCURACY_DENIED -> "location-mode-high-accuracy-denied"
    BLUETOOTH_PERMISSION_DENIED -> "bluetooth-permission-denied"
    BLE_PERMISSION_DENIED -> "ble-permission-denied"
  }
}

fun ZendriveDriveType.toRnObject(): String {
  return when (this) {
    INVALID -> "invalid"
    NON_DRIVING -> "non-driving"
    DRIVE -> "drive"
  }
}

fun ZendriveVehicleType.toRnObject(): String {
  return when (this) {
    ZendriveVehicleType.CAR -> "car"
    ZendriveVehicleType.MOTORCYCLE -> "motorcycle"
  }
}

fun ZendriveVehicleTaggingDetails.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("vehicleId",this.vehicleId)
  map.putBoolean("isTaggedByBeacon",this.isTaggedByBeacon)
  map.putBoolean("isTaggedByBluetoothStereo",this.isTaggedByBluetoothStereo)
  return map
}

fun ZendriveOperationResult.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putBoolean("isSuccess", this.isSuccess)
  if (!this.isSuccess) {
    map.putString("errorCode", errorCodeString(from = this.errorCode))
    map.putString("errorMessage", this.errorMessage)
  }
  return map
}

fun ZendriveBusinessHoursOperationResult.toRnObject(): WritableMap{
  val map = Arguments.createMap()
  map.putBoolean("isSuccess", true)
  map.putString("name",this.name)
  map.putString("message", this.message)
  return map
}

fun ZendrivePauseAutoTrackingReason.toRnObject(): WritableMap{
  val map = Arguments.createMap()
  map.putString("paused_reason", this.name)
  return map
}

fun ZendriveState.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putBoolean("isDriveInProgress", this.isDriveInProgress)
  map.putBoolean("isForegroundService", this.isForegroundService)
  map.putMap("zendriveConfiguration", this.zendriveConfiguration.toRnObject())
  return map
}

fun ZendriveConfiguration.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("driverId", this.driverId)
  map.putString("sdkKey", this.sdkKey)
  map.putString(
    "driveDetectionMode",
    driveDetectionModeString(from = this.zendriveDriveDetectionMode)
  )
  map.putString(
    "region",
    regionString(from = this.region)
  )
  map.putMap("attributes", this.driverAttributes.toRnObject())
  map.putBoolean("implementsMultipleAccidentCallbacks", this.implementsMultipleAccidentCallbacks)
  return map
}

fun ZendriveDriverAttributes.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("alias", this.alias)
  map.putString("group", this.group)
  map.putString("serviceLevel", serviceLevelAttributeString(from = this.serviceLevel))
  return map
}

fun ZendriveStarRating.toRnObject(): String {
  return when (this) {
    FIVE -> "5"
    FOUR -> "4"
    THREE -> "3"
    TWO -> "2"
    ONE -> "1"
    NOT_AVAILABLE -> "not-available"
  }
}

fun ZendriveEventRatings.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("aggressiveAccelerationRating", this.aggressiveAccelerationRating.toRnObject())
  map.putString("hardBrakeRating", this.hardBrakeRating.toRnObject())
  map.putString("hardTurnRating", this.hardTurnRating.toRnObject())
  map.putString("phoneHandlingRating", this.phoneHandlingRating.toRnObject())
  map.putString("speedingRating", this.speedingRating.toRnObject())
  return map
}

fun DriveStartInfo.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("driveId", this.driveId)
  map.putString("sessionId", this.sessionId)
  map.putString("trackingId", this.trackingId)
  map.putString("insurancePeriod", this.insurancePeriod?.toRnObject())
  map.putMap("startLocation", this.startLocation?.toRnObject())
  map.putDouble("startTimeMillis", this.startTimeMillis.toDouble())
  return map
}

fun DriveResumeInfo.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("driveId", this.driveId)
  map.putString("sessionId", this.sessionId)
  map.putString("trackingId", this.trackingId)
  map.putString("insurancePeriod", this.insurancePeriod?.toRnObject())
  map.putDouble("startTimeMillis", this.startTimeMillis.toDouble())
  map.putDouble("driveGapEndTimestampMillis", this.driveGapEndTimestampMillis.toDouble())
  map.putDouble("driveGapStartTimestampMillis", this.driveGapStartTimestampMillis.toDouble())
  return map
}

fun ZendriveEventSeverity.toRnObject(): String {
  return when (this) {
    HIGH -> "high"
    LOW -> "low"
    ZendriveEventSeverity.NOT_AVAILABLE -> "not-available"
  }
}

fun SpeedingInfo.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putDouble("avgSpeed", this.avgSpeed)
  map.putDouble("maxSpeed", this.maxSpeed)
  map.putDouble("speedLimit", this.speedLimit)
  return map
}

fun ZendriveTurnDirection.toRnObject(): String {
  return when (this) {
    LEFT -> "left"
    RIGHT -> "right"
  }
}

fun PhonePosition.toRnObject(): String {
  return when (this) {
    MOUNT -> "mount"
    UNKNOWN -> "unknown"
  }
}

fun DriveScore.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putInt("zendriveScore", this.zendriveScore)
  return map
}

fun ZendriveUserMode.toRnObject(): String {
  return when (this) {
    DRIVER -> "driver"
    PASSENGER -> "passenger"
    UNAVAILABLE -> "unavailable"
  }
}

fun DriveInfo.Warning.toRnObject(): String {
  return when (this) {
    UnexpectedTripDuration -> "unexpected-trip-duration"
  }
}

fun ZendriveAccidentConfidence.toRnObject(): String {
  return when (this) {
    ZendriveAccidentConfidence.HIGH -> "high"
    ZendriveAccidentConfidence.LOW -> "low"
    ZendriveAccidentConfidence.INVALID -> "invalid"
  }
}

fun LocationPointWithTimestamp.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putDouble("timestampMillis", this.timestampMillis.toDouble())
  map.putMap("location", this.location.toRnObject())
  return map
}

fun ZendriveEvent.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putMap("endLocation", this.endLocation?.toRnObject())
  map.putDouble("endTimestampMillis", this.endTimestampMillis.toDouble())
  map.putString("eventType", this.eventType.toRnObject())
  map.putString("severity", this.severity.toRnObject())
  map.putMap("speedingInfo", this.speedingInfo?.toRnObject())
  map.putMap("startLocation", this.startLocation?.toRnObject())
  map.putDouble("startTimestampMillis", this.startTimestampMillis.toDouble())
  map.putString("turnDirection", this.turnDirection?.toRnObject())
  return map
}

fun DriveInfo.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("driveId", this.driveId)
  map.putString("vehicleType", this.vehicleType?.toRnObject())
  map.putString("sessionId", this.sessionId)
  map.putString("trackingId", this.trackingId)
  map.putString("insurancePeriod", this.insurancePeriod?.toRnObject())
  map.putDouble("startTimeMillis", this.startTimeMillis.toDouble())
  map.putDouble("averageSpeed", this.averageSpeed)
  map.putDouble("distanceMeters", this.distanceMeters)
  map.putDouble("maxSpeed", this.maxSpeed)
  map.putString("driveType", this.driveType.toRnObject())
  map.putDouble("endTimeMillis", this.endTimeMillis.toDouble())
  map.putMap("eventRatings", this.eventRatings.toRnObject())
  map.putMap("extrapolationDetails", this.extrapolationDetails?.toRnObject())
  map.putMap("vehicleTaggingDetails", this.vehicleTaggingDetails?.toRnObject())

  map.putArray(
    "events",
    this.events.fold(Arguments.createArray()) { acc, elem ->
      acc.pushMap(elem.toRnObject())
      acc
    }
  )
  map.putString("phonePosition", this.phonePosition.toRnObject())
  map.putMap("score", this.score.toRnObject())
  map.putString("userMode", this.userMode?.toRnObject())
  map.putArray(
    "warnings",
    this.warnings.fold(Arguments.createArray()) { acc, elem ->
      acc.pushString(elem.toRnObject())
      acc
    }
  )
  map.putArray(
    "waypoints",
    this.waypoints.fold(Arguments.createArray()) { acc, elem ->
      acc.pushMap(elem.toRnObject())
      acc
    }
  )
  return map
}

fun AccidentInfo.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("accidentId", this.accidentId)
  map.putString("confidence", this.confidence.toRnObject())
  map.putString("driveId", this.driveId)
  map.putString("sessionId", this.sessionId)
  map.putDouble("timestampMillis", this.timestampMillis.toDouble())
  map.putString("trackingId", this.trackingId)
  map.putMap("location", this.location?.toRnObject())
  return map
}

fun ZendriveVehicleInfo.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("vehicleId", this.vehicleId)
  map.putString("bluetoothAddress", this.bluetoothAddress)
  return map
}


fun ZendriveScannedBeaconInfo.toRnObject(): WritableMap {
  val map = Arguments.createMap()
  map.putString("uuid", this.uuid.toString())
  map.putInt("major", this.major)
  map.putInt("minor", this.minor)
  map.putInt("rssi", this.rssi)
  return map
}
