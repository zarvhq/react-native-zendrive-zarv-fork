import { Zendrive as IZendrive, ZendriveDebug as IZendriveDebug, ZendrivePermissions as IZendrivePermissions } from './sdk/Zendrive';
import { ActiveDriveInfo, ZendriveEventType, ZendriveSettings, ZendriveConfiguration, ZendriveOperationResult, ZendriveDriveDetectionMode, ZendriveAccidentConfidence, ZendriveState, ZendriveVehicleInfo, ZendriveVehicleTaggingOperationResult, ZendriveBluetoothDevice, ZendriveScannedBeaconInfo, ZendrivePauseAutoTrackingReason, ZendriveSDKHealthReason } from './sdk/Infos';
import { ZendriveCallbackEventHandler } from './sdk/Events';
import { ZendriveInsurance as IZendriveInsurance } from './sdk/Insurance';
import { ZendriveFeedback as IZendriveFeedback } from './sdk/Feedback';
export * from './sdk/Infos';
export * from './sdk/Errors';
export * from './sdk/Insurance';
export * from './sdk/Events';
export * from './sdk/Feedback';
/**
 * @inheritdoc
 */
declare class ZendriveImpl implements IZendrive {
    private eventHandlers;
    private nativeEventHandler;
    private callbackHandler;
    private _insurance;
    private _feedback;
    private _debug;
    private _permissions;
    getActiveDriveInfo(): Promise<ActiveDriveInfo | undefined>;
    getBuildVersion(): Promise<string>;
    getEventSupportForDevice(): Promise<Map<ZendriveEventType, boolean>>;
    getZendriveSettings(): Promise<ZendriveSettings | undefined>;
    isAccidentDetectionSupported(): Promise<boolean>;
    isSDKSetup(): Promise<boolean>;
    isValidInputParameter(input: string): Promise<boolean>;
    resume(): Promise<ZendriveOperationResult>;
    setup(configuration: ZendriveConfiguration): Promise<ZendriveOperationResult>;
    setZendriveDriveDetectionMode(driveDetectionMode: ZendriveDriveDetectionMode): Promise<ZendriveOperationResult>;
    startDrive(trackingId?: string | undefined): Promise<ZendriveOperationResult>;
    startSession(sessionId: string): Promise<ZendriveOperationResult>;
    refreshBusinessHours(): Promise<any>;
    autoTrackingPausedReason(): Promise<ZendrivePauseAutoTrackingReason | undefined>;
    stopManualDrive(): Promise<ZendriveOperationResult>;
    stopSession(): Promise<ZendriveOperationResult>;
    teardown(): Promise<ZendriveOperationResult>;
    triggerMockAccident(confidence: ZendriveAccidentConfidence): Promise<ZendriveOperationResult>;
    triggerMockPotentialAccident(config: {
        delayBetweenCallbacksSeconds: number;
        finalCallbackConfidence: ZendriveAccidentConfidence;
        finalCallbackConfidenceNumber: number;
        potentialCallbackConfidence: ZendriveAccidentConfidence;
        potentialCallbackConfidenceNumber: number;
    }): Promise<ZendriveOperationResult>;
    uploadAllDebugDataAndLogs(): Promise<ZendriveOperationResult>;
    wipeOut(): Promise<ZendriveOperationResult>;
    getZendriveState(): Promise<ZendriveState | undefined>;
    insurance(): IZendriveInsurance;
    feedback(): IZendriveFeedback;
    debug(): IZendriveDebug;
    permissions(): IZendrivePermissions;
    getZendriveDriverCustomAttributes(attributes: [string]): Promise<{
        [x: string]: string;
    } | null>;
    associateVehicle(vehicleInfo: ZendriveVehicleInfo): Promise<ZendriveVehicleTaggingOperationResult>;
    dissociateVehicle(vehicleId: string): Promise<ZendriveVehicleTaggingOperationResult>;
    getAssociatedVehicles(): Promise<[ZendriveVehicleInfo]>;
    getBluetoothPairedDevices(): Promise<[ZendriveBluetoothDevice]>;
    getNearbyBeacons(uuid: string): Promise<[ZendriveScannedBeaconInfo]>;
    getNearbyBeaconsWithRange(uuid: string, major: number, minor: number): Promise<[ZendriveScannedBeaconInfo]>;
    pauseAutoDriveTracking(pausedTillTimestamp: number): Promise<ZendriveOperationResult>;
    resumeAutoDriveTracking(): Promise<ZendriveOperationResult>;
    isAutoTripTrackingPaused(): Promise<Boolean>;
    logSDKHealth(sdkHealthReason: ZendriveSDKHealthReason): Promise<void>;
    registerZendriveCallbackEventListener(handler: ZendriveCallbackEventHandler): void;
    unregisterZendriveCallbackEventListener(handler: ZendriveCallbackEventHandler): void;
}
declare const ZendriveSDK: ZendriveImpl;
export default ZendriveSDK;
