export type ZendriveSettingError = {
  /**
   * The type of the error.
   */
  type: ZendriveIssueType;
};
/**
 * This class represents a warning that may impact the normal functioning of the Zendrive SDK.
 */
export type ZendriveSettingWarning = {
  /**
   * The type of the error.
   */
  type: ZendriveIssueType;
};

/**
 * error that occurred when attempting to connect to google play services.
 */
export type GooglePlayConnectionError = GooglePlayConnectionResult &
  ZendriveSettingError;

export type GooglePlayConnectionResult = {
  /**
   * Errors found when attempting to connect to google play services.
   * See [ConnectionResult](https://developers.google.com/android/reference/com/google/android/gms/common/ConnectionResult)
   */
  connectionResult: string;
};

export type GooglePlaySettingsResult = {
  /**
   * Errors found when determining location settings status on the device.
   * See [LocationSettingsResult](https://developers.google.com/android/reference/com/google/android/gms/location/LocationSettingsResult)
   */
  googlePlaySettingsResult: string;
};

/**
 * error that occurred when attempting to connect to google play services.
 */
export type GooglePlaySettingsError = GooglePlaySettingsResult &
  ZendriveSettingError;

/**
 * The type of a [[ZendriveSettingError]] or [[ZendriveSettingWarning]]
 */
export enum ZendriveIssueType {
  /**
   * User has enabled background restrictions on the application.
   */
  BACKGROUND_RESTRICTION_ENABLED = 'background-restriction-enabled',
  /**
   * Zendrive SDK was unable to connect to Google Play services.
   */
  GOOGLE_PLAY_CONNECTION_ERROR = 'google-play-connection-error',
  /**
   * There is a settings error from Google play services.
   */
  GOOGLE_PLAY_SETTINGS_ERROR = 'google-play-settings-error',
  /**
   * User has denied location permission to the application.
   */
  LOCATION_PERMISSION_DENIED = 'location-permission-denied',
  /**
   * Device location settings is not set to high accuracy.
   */
  LOCATION_SETTINGS_ERROR = 'location-settings-error',
  /**
   * Device location accuracy reduced.
   */
  LOCATION_ACCURACY_AUTHORIZATION_REDUCED = 'location-accuracy-authorization-reduced',
  /**
   * Power saver mode is enabled.
   */
  POWER_SAVER_MODE_ENABLED = 'power-saver-mode-enabled',
  /**
   * Wifi scanning is disabled on the device and is affecting location accuracy.
   */
  WIFI_SCANNING_DISABLED = 'wifi-scanning-disabled',
  /**
   * User has denied activity recognition permission to the application and the current drive detection mode is [[ZendriveDriveDetectionMode#AUTO_ON]].
   */
  ACTIVITY_RECOGNITION_PERMISSION_DENIED = 'activity-recognition-permission-denied',
  /**
   * Draw over other apps is disabled.
   */
  OVERLAY_PERMISSION_DENIED = 'overlay-permission-denied',
  /**
   * Battery optimization is enabled. This can happen when:
   * 1) Device specific Adaptive battery is turned on and the app is not exempted from this setting.
   * 2) User has explicitly turned on Battery Optimization for the app.
   * This will be shown as an error on Samsung devices only. For more details, see: Optimize for Doze and App Standby
   */
  BATTERY_OPTIMIZATION_ENABLED = 'battery-optimization-enabled',

  /**
   * OnePlus OEM specific Deep Optimization battery setting is enabled on device.
   * This error indicates that trip detection of Zendrive SDK is heavily restricted and drives may not be properly recorded.
   */
  ONE_PLUS_DEEP_OPTIMIZATION = 'one-plus-deep-optimization',

  /**
   * Airplane mode is enabled on the device and is affecting location accuracy.
   */
  AIRPLANE_MODE_ENABLED = 'airplane-mode-enabled',

  /**
   * Bluetooth is disabled when there are some vehicles already associated by using associateVehicle.
   */
  BLUETOOTH_DISABLED = 'bluetooth-disabled',
}

/**
 * An error code that indicates the type of error that occured during a Zendrive SDK operation.
 */
export enum ZendriveErrorCode {
  /**
   * This application does not have Accident Detection enabled.
   */
  ACCIDENT_DETECTION_NOT_AVAILABLE_FOR_APPLICATION = 'accident-detection-not-available-for-application',
  /**
   * This device does not have sensors that support Accident Detection.
   */
  ACCIDENT_DETECTION_NOT_AVAILABLE_FOR_DEVICE = 'accident-detection-not-available-for-device',
  /**
   * The Android version on the device is not supported by the SDK.
   */
  ANDROID_VERSION_NOT_SUPPORTED = 'android-version-not-supported',
  /**
   * Google play services unavailable on the device.
   */
  GOOGLE_PLAY_SERVICES_UNAVAILABLE = 'google-play-services-unavailable',
  /**
   * The version of Google Play Services installed on the device needs to be updated for use with the Zendrive SDK.
   */
  GOOGLE_PLAY_SERVICES_UPDATE_REQUIRED = 'google-play-services-update-required',
  /**
   * The driver id provided during SDK setup is invalid.
   */
  INVALID_DRIVER_ID = 'invalid-driver-id',
  /**
   * The SDK key provided during SDK setup is invalid.
   */
  INVALID_SDK_KEY = 'invalid-sdk-key',
  /**
   * The session id provided to startSession call is invalid.
   */
  INVALID_SESSION_ID = 'invalid-session-id',
  /**
   * The tracking id provided to [[Zendrive.startDrive]] or [[Zendrive.stopManualDrive]] call is invalid.
   */
  INVALID_TRACKING_ID = 'invalid-tracking-id',
  /**
   * High accuracy location updates is not enabled on the device.
   */
  LOCATION_ACCURACY_NOT_AVAILABLE = 'location-accuracy-not-available',
  /**
   * This indicates error conditions encountered when triggering mock accidents.
   */
  MOCK_ACCIDENT_ERROR = 'mock-accident-error',
  /**
   * This device is not connected to network.
   */
  NETWORK_NOT_AVAILABLE = 'network-not-available',
  /**
   * Called [[Zendrive.stopManualDrive]] when no manual drive in progress.
   */
  NO_MANUAL_DRIVE = 'no-manual-drive',
  /**
   * Operation failed because ZendriveNotificationProvider class was NULL.
   */
  NOTIFICATION_PROVIDER_ERROR = 'notification-provider-error',
  /**
   * The operation failed because the SDK is not yet setup.
   */
  SDK_NOT_SETUP = 'sdk-not-setup',
  /**
   * Operation failed because it is not supported by ZendriveSDK-testing Library.
   */
  UNSUPPORTED_OPERATION = 'unsupported-operation',
  /**
   * Operation failed because [[ZendriveConfiguration]] was invalid.
   */
  CONFIGURATION_ERROR = 'configuration-error',
  /**
   * Operation failed because Zendrive debug data failed to upload.
   */
  DEBUG_UPLOAD_ERROR = 'debug-upload-error',
  /**
   * This indicates an internal SDK error which caused a SDK operation to fail.
   */
  SDK_ERROR = 'sdk-error',
  /**
   * This operation failed because it should not be called when the sdk is running.
   */
  SDK_NOT_TORN_DOWN = 'sdk-not-torn-down',
  /**
   *This operation failed because it should not be called when [[Zendrive.setup]] is in progress.
   */
  SDK_SETUP_IN_PROGRESS = 'sdk-setup-in-progress',
  /**
   * This operation failed because it should not be called when [[Zendrive.teardown]] is in progress.
   */
  SDK_TEAR_DOWN_IN_PROGRESS = 'teardown-in-progress',
  /**
   * Zendrive SDK does not support the OS version of the device.
   */
  UNSUPPORTED_OS_VERSION = 'unsupported-os-version',
  /**
   * Zendrive SDK does not support the device type.
   */
  UNSUPPORTED_DEVICE = 'unsupported-device',
  /**
   * Invalid parameter was passed to the API.
   */
  INVALID_PARAMS = 'invalid-params',
  /**
   * Insurance Period hasn’t changed from the previously active period, action ignored. This error may be returned from startPeriod1, startDriveWithPeriod2, startDriveWithPeriod3 APIs of ZendriveInsurance.
   */
  INSURANCE_PERIOD_SAME = 'insurance-period-same',

  /**
   * Some IO error occured while doing the operation. Refer to error description for more info.
   */
  IO_ERROR = 'io-error',

  /**
   * Operation failed because [[ZendriveRegion]] provided was unsupported.
   */
  REGION_UNSUPPORTED = 'region-unsupported',
  /**
   * Changing a region post setup is NOT allowed, until wipeout is called.
   */
  REGION_SWITCH_ERROR = 'region-switch-error',
  /**
   * User is not authorized to use this application.
   */
  USER_DEPROVISIONED = 'user-deprovisioned',
}

/**
 * An error code that indicates the type of error that occured during a Zendrive Vehicle Tagging Operation.
 */
export enum ZendriveVehicleTaggingErrorCode {
  ASSOCIATED_VEHICLE_CONFLICT = 'associated-vehicle-conflict',
  ASSOCIATED_VEHICLES_LIMIT_EXCEEDED = 'associated-vehicle-limit-exceeded',
  INVALID_VEHICLE_ID = 'invalid-vehicle-id',
  INVALID_ZENDRIVE_VEHICLE_INFO = 'invalid-zendrive-vehicle-info',
  SDK_NOT_SETUP = 'sdk-not-setup',
  SUCCESS = 'success',
  VEHICLE_NOT_ASSOCIATED = 'vehicle-not-associated',
  INVALID_ZENDRIVE_VEHICLE_BEACON = 'invalid-zendrive-vehicle-beacon',
  BEACON_NOT_ASSOCIATED = 'beacon-not-associated',
  ASSOCIATED_VEHICLE_BEACON_CONFLICT = 'associated-vehicle-beacon-conflict',
  MULTIPLE_UUID_ASSOCIATION_ERROR = 'multiple-uuid-association-error',
  BLE_SCAN_NOT_SUPPORTED = 'ble-scan-not-supported',
  BEACON_SCAN_ALREADY_IN_PROGRESS = 'beacon-scan-already-in-progress',
  BLUETOOTH_NOT_AVAILABLE = 'bluetooth-not-available',
  BLE_SCAN_INTERNAL_ERROR = 'ble-scan-internal-error',
  LOCATION_ERROR = 'location-error',
}
