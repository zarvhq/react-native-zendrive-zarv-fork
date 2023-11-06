import { ActiveDriveInfo, ZendriveEventType, ZendriveSettings, ZendriveConfiguration, ZendriveOperationResult, ZendriveDriveDetectionMode, ZendriveAccidentConfidence, ZendriveState, NotificationConfig, ZendriveVehicleInfo, ZendriveVehicleTaggingOperationResult, ZendriveBluetoothDevice, ZendriveScannedBeaconInfo, ZendrivePauseAutoTrackingReason, ZendriveSDKHealthReason } from './Infos';
import { ZendriveCallbackEventHandler } from './Events';
import { ZendriveInsurance } from './Insurance';
import { ZendriveFeedback } from './Feedback';
/**
 * Rationale for permission, applies only to android
 */
export type PermissionRationale = {
    title: string;
    message: string;
    buttonPositive: string;
    buttonNegative: string;
    buttonNeutral: string;
};
/**
 * Permissions required by Zendrive.
 */
export type ZendriveRequiredPermission = 'location' | 'motion' | 'overlay';
/**
 * A utility module to verify & validate permissions that zendrive will require.
 */
export interface ZendrivePermissions {
    /**
     * Check whether the given kind of permissions is granted
     * @param kind The kind permission required to check
     */
    check(kind: ZendriveRequiredPermission): Promise<boolean>;
    /**
     * Request the user to grant permission
     * @param kind The kind permission required to check
     */
    request(kind: ZendriveRequiredPermission, rationale?: PermissionRationale): Promise<boolean>;
    /**
     * Open settings for iOS.
     * This should be used when location permission is denied from settings.
     */
    openSettings(): Promise<boolean>;
    /**
     * Open battery settings page for a given model.
     * Android only.
     * See https://docs.zendrive.com/android/phone-restrictions for more information
     */
    openAdvancedBatterySettings(): Promise<boolean>;
}
/**
 * Debuggin related utility.
 */
export interface ZendriveDebug {
    /**
     * Upload all zendrive related data that will help in debugging
     * This will make the app go in foreground and start uploading data related to Zendrive. It doesn't require SDK to be setup to make this call and should work correctly irrespective of whether the SDK is setup or not.
     * Whenever this method is called, it uploads 10s of MBs of data to the Zendrive server.
     * @param zendriveConfiguration The [[ZendriveConfiguration]] properties which was used to setup the Zendrive SDK
     * @param notificationConfiguration Foreground notification that is used to display notification when the sdk goes foreground while upload. Ignored for iOS
     */
    uploadAllZendriveData(zendriveConfiguration: ZendriveConfiguration, notificationConfiguration?: NotificationConfig): Promise<ZendriveOperationResult>;
    /**
     * **iOS only**
     * Check using a session identifier if the corresponding session was started for data upload.
     * @param identifier session identifier as given in [[ZendriveDebug#handleEventsForBackgroundURLSession]]
     */
    isZendriveSessionIdentifier(identifier: string): Promise<ZendriveOperationResult>;
    /**
     * **iOS only**
     * Tell the SDK that events for a NSUrlSession are waiting to be processed.
     * @param identifier session identifier
     */
    handleEventsForBackgroundURLSession(identifier: string): Promise<ZendriveOperationResult>;
}
/**
 * Zendrive SDK interface
 */
export interface Zendrive {
    /**x
     * Get info on the currently active drive.
     * Could be undefined/null when sdk is not setup
     */
    getActiveDriveInfo(): Promise<ActiveDriveInfo | undefined>;
    /**
     * Returns an identifier which can be used to identify this SDK build.
     */
    getBuildVersion(): Promise<string>;
    /**
     * Check whether the zendrive event types are supported or not on this device
     * See [[ZendriveEventType]]
     */
    getEventSupportForDevice(): Promise<Map<ZendriveEventType, boolean>>;
    /**
     * Get the current state of settings affecting the Zendrive SDK's normal operation.
     * Could be undefined/null when sdk is not setup
     */
    getZendriveSettings(): Promise<ZendriveSettings | undefined>;
    /**
     * Is Zendrive collision detection supported on this device?
     */
    isAccidentDetectionSupported(): Promise<boolean>;
    /**
     * Is the Zendrive SDK already setup?
     */
    isSDKSetup(): Promise<boolean>;
    /**
     * Checks whether the string passed to the SDK is valid
     * String parameters cannot be null or empty. Unless documented otherwise, strings parameters to the SDK cannot contain any of the following characters - "?", " ", "&", "/", "\", ";", "#"
     * @param input The string to validate
     */
    isValidInputParameter(input: string): Promise<boolean>;
    /**
     * iOS Only
     * Restart any paused or pending tasks while application is inactive
     * Will refresh the UI if application is in background
     */
    resume(): Promise<ZendriveOperationResult>;
    /**
     * Setup the Zendrive SDK with a configuration and a intent service for SDK callbacks.
     * This will start tracking the phone's movements to track any driving activity. The application should call this method before anything else in the Zendrive SDK.
     * Calling this method multiple times with the same 'sdkKey' and 'driverId' is no-op.
     * If either the 'sdkKey' or 'driverId' in the [[ZendriveConfiguration]] changes between the previous setup call and this one, it will be the same as calling [[Zendrive.teardown]] followed by calling setup with the new parameters.
     * @param configuration The configuration properties to setup the Zendrive SDK.
     */
    setup(configuration: ZendriveConfiguration): Promise<ZendriveOperationResult>;
    /**
     * Use this method to change the [[ZendriveDriveDetectionMode]] after SDK is already setup. Applications which do not want the SDK to continuously track drives in background should set this value to [[AUTO_OFF]]. With this, the application needs to call [[Zendrive.startDrive]] method to record drives. In case the application wants to enable auto drive detection only for a fixed duration (like when the driver is on-duty), use this method to change the mode to AUTO_ON for that period and set it back to AUTO_OFF (once the driver goes off-duty).
     * Once setup finishes, the ZendriveOperationCallback is called (if provided) with the result of the setup operation.
  
     * @param driveDetectionMode The new drive detection mode. If this is the same as the current [[ZendriveDriveDetectionMode]], then this call is a no-op.
     */
    setZendriveDriveDetectionMode(driveDetectionMode: ZendriveDriveDetectionMode): Promise<ZendriveOperationResult>;
    /**
     * Manually start recording a drive.
     * For applications which want to manually start recording drives(as opposed to automatic detection that the framework does) this method may be called.
     *
     * The 'trackingId' will eventually show up in the reports that Zendrive generates.
     *
     * When this method is called it is the client code's responsibility to call [[Zendrive.stopManualDrive]] to indicate the end of the drive. These methods should be used when the application has explicit knowledge of the start and end of a drive. In such applications, using this call is recommended).
     *
     *When startDrive is called with a manually recorded drive in progress, if trackingId matches the ongoing drive's trackingId, this call is a no-op. If the trackingIds do not match, [[Zendrive.stopManualDrive]] is called implicitly before a new drive is started.
     *
     * @param trackingId Optional identifier which allows identifying this drive uniquely. If this value is null, the SDK will supply a default trackingId with value com.zendrive.sdk.internal. It may not be an empty string. It will be truncated to 64 characters if it is longer than 64 characters. Passing invalid trackingId is an error.
     */
    startDrive(trackingId?: string): Promise<ZendriveOperationResult>;
    /**
     * Start a session in the SDK.
     *
     * Applications which want to record several user's drives as a session may use this call. All drives (automatically detected or manually started) when a session is in progress will be tagged with the session id. If a drive is already on when this call is made, that drive will not belong to this session. This session id will be made available in the reports and API that Zendrive provides.
     *
     * The application must call [[Zendrive.stopSession]] when it wants to end the session.
     *
     * Only one session may be active at a time. Calling startSession when a session is already active will terminate the ongoing session and start a new one with the given id.
     *
     * @param sessionId an identifier that identifies this session uniquely. It may not be null or an empty string. It may not be longer than 64 characters. Passing invalid sessionId is a no-op.
     * See [[Zendrive.isValidInputParameter]]
     */
    startSession(sessionId: string): Promise<ZendriveOperationResult>;
    /**
     * Stop the currently active manually started drive.
     * The [[Zendrive.startDrive]] and stopManualDrive calls are not counted. i.e stopManualDrive will stop a manually started drive irrespective of how many times [[Zendrive.startDrive]] is called.
     */
    stopManualDrive(): Promise<ZendriveOperationResult>;
    /**
     * Stop currently ongoing session. No-op if no session is ongoing. Trips that start after this call do not belong to the session.
     * Ongoing trips at the time of this call will continue to belong to the session that was just stopped.
     */
    stopSession(): Promise<ZendriveOperationResult>;
    /**
     * Shuts down the Zendrive framework. This may be called if the client code wishes to turn off the Zendrive framework to isolate the operations that the application is doing. This is a no-op if [[Zendrive.setup]] has not been called.
     * the library is typically very conservative about what modules of the phone it uses for tracking and how much memory it uses etc. and as such is very conservative about memory and battery impact. So this method should not really be called to save power/memory for the application.
     */
    teardown(): Promise<ZendriveOperationResult>;
    /**
     * Triggers a fake accident.
     *
     * A fake accident will be invoked on the ZendriveListener after this function is called. Requires that the Zendrive SDK is setup and a trip is in progress.
     *
     * This function can be used to test the accident detection integration of your application.
     * @param confidence confidence level of the accident to trigger. See [[ZendriveAccidentConfidence]]
     */
    triggerMockAccident(confidence: ZendriveAccidentConfidence): Promise<ZendriveOperationResult>;
    /**
     * Triggers a fake with a potential accident as per configuration provided.
     *
     * A fake accident along with prior potential accident will be invoked on the ZendriveListener after this function is called. Requires that the Zendrive SDK is setup and a trip is in progress.
     *
     * This function can be used to test the accident detection integration of your application.
     * @param mockConfig configuration for potential accident.
     */
    triggerMockPotentialAccident(mockConfig: {
        delayBetweenCallbacksSeconds: number;
        finalCallbackConfidence: ZendriveAccidentConfidence;
        finalCallbackConfidenceNumber: number;
        potentialCallbackConfidence: ZendriveAccidentConfidence;
        potentialCallbackConfidenceNumber: number;
    }): Promise<ZendriveOperationResult>;
    /**
     * Allows an application to trigger a bulk upload of all SDK data on the device to Zendrive for debugging purposes. This uploads historical data and logs on the device.
     *
     * **WARNING**: This will cause significant battery drain on the device. It will also use significant upload bandwidth to upload this data.
     *
     * This is meant to be used sparingly by applications. This should be used only when you have raised a ticket with Zendrive and Zendrive support recommends this approach to debug the issue.
     */
    uploadAllDebugDataAndLogs(): Promise<ZendriveOperationResult>;
    /**
     * Wipe out all the data that zendrive keeps locally on the device.
     *
     * When Zendrive SDK is torn down, trip data that is locally persisted continues to remain persisted. The data will be uploaded when SDK setup is called at a later time. Wipeout should be used when the application wants to remove all traces of Zendrive on the device. Data cannot be recovered after this call.
     *
     * **NOTE**: This call can be made when the SDK is not running. Call [[Zendrive.teardown]] to tear down a live SDK before making this call.
     */
    wipeOut(): Promise<ZendriveOperationResult>;
    /**
     * Get the current state of the Zendrive SDK.
     * Could be undefined/null when sdk is not setup
     */
    getZendriveState(): Promise<ZendriveState | undefined>;
    /**
     * Refreshes business hours set by fleet manager instantly
     */
    refreshBusinessHours(): Promise<any>;
    /**
     * Use this method to get the ZendrivePauseAutoTrackingReason when the auto tracking is paused.
     */
    autoTrackingPausedReason(): Promise<ZendrivePauseAutoTrackingReason | undefined>;
    /**
     * Provide insurance module of Zendrive SDK
     */
    insurance(): ZendriveInsurance;
    /**
     * Provide feedback module of Zendrive SDK
     */
    feedback(): ZendriveFeedback;
    /**
     * Provide debug module of Zendrive SDK
     */
    debug(): ZendriveDebug;
    /**
     * Provide permissions module
     */
    permissions(): ZendrivePermissions;
    /**
     * Android only
     * Provide zendrive driver custom attributes
     */
    getZendriveDriverCustomAttributes(attributes: [string]): Promise<{
        [key in string]: string;
    } | null>;
    /**
     * Associate a vehicle with the SDK.
     */
    associateVehicle(vehicleInfo: ZendriveVehicleInfo): Promise<ZendriveVehicleTaggingOperationResult>;
    /**
     * Dissociate an associated vehicle from the SDK.
     */
    dissociateVehicle(vehicleId: string): Promise<ZendriveVehicleTaggingOperationResult>;
    /**
     * Get the list of vehicles currently associated with ZendriveSDK.
     */
    getAssociatedVehicles(): Promise<[ZendriveVehicleInfo]>;
    /**
     * Get the list of all paired bluetooth devices.
     * iOS Note:
     * The active bluetooth device is the one through which audio will play. Example: You may be connected to both car’s stereo and airpods but only one of them will be active, the one through which audio is playing or will play.
     * If the user does not have multiple connected audio devices like headphones, airpods etc, then the connected car’s stereo is the active device.
     */
    getBluetoothPairedDevices(): Promise<[ZendriveBluetoothDevice]>;
    /**
     * Nearby ibeacons with uuid.
     */
    getNearbyBeacons(uuid: string): Promise<[ZendriveScannedBeaconInfo]>;
    /**
     * Nearby ibeacons with uuid and range.
     */
    getNearbyBeaconsWithRange(uuid: string, major?: number, minor?: number): Promise<[ZendriveScannedBeaconInfo]>;
    /**
     * Register an event listener to receive trip events.
     * See [[ZendriveCallbackEvent]] for different types of events.
     */
    registerZendriveCallbackEventListener(handler: ZendriveCallbackEventHandler): void;
    /**
     * Pause auto tracking of drive for a specified duration.
     */
    pauseAutoDriveTracking(pausedTillTimestamp: number): Promise<ZendriveOperationResult>;
    /**
     * Resume auto drive tracking.
     */
    resumeAutoDriveTracking(): Promise<ZendriveOperationResult>;
    /**
     * Check status of auto trip tracking.
     */
    isAutoTripTrackingPaused(): Promise<Boolean>;
    /**
     * Unregister event listener, that was previously registered with [[Zendrive#registerZendriveCallbackEventListener]]
     * See [[ZendriveCallbackEvent]] for different types of events.
     */
    unregisterZendriveCallbackEventListener(handler: ZendriveCallbackEventHandler): void;
    /**
     * iOS only optional API.
     */
    logSDKHealth(sdkHealthReason: ZendriveSDKHealthReason): Promise<void>;
}
