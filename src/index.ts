import { NativeModules, Platform, Image } from 'react-native';
import {
  Zendrive as IZendrive,
  ZendriveDebug as IZendriveDebug,
  ZendrivePermissions as IZendrivePermissions,
} from './sdk/Zendrive';
import {
  ActiveDriveInfo,
  ZendriveEventType,
  ZendriveSettings,
  ZendriveConfiguration,
  ZendriveOperationResult,
  ZendriveDriveDetectionMode,
  ZendriveAccidentConfidence,
  ZendriveState,
  NotificationConfig,
  ZendriveVehicleInfo,
  ZendriveVehicleTaggingOperationResult,
  ZendriveBluetoothDevice,
  ZendriveScannedBeaconInfo,
  ZendrivePauseAutoTrackingReason,
  ZendriveSDKHealthReason,
} from './sdk/Infos';
import { ZendriveErrorCode } from './sdk/Errors';
import {
  ZendriveCallbackEventHandler,
  ZendriveCallbackEvent,
} from './sdk/Events';
import RNEventHandler from './RNEventHandler';
import { ZendriveInsurance as IZendriveInsurance } from './sdk/Insurance';
import {
  ZendriveFeedback as IZendriveFeedback,
  ZendriveFeedbackDriveCategory,
} from './sdk/Feedback';
import AndroidPermissionsHandler from './AndroidPermissionsHandler';
import IOSPermissionsHandler from './IOSPermissionsHandler';

export * from './sdk/Infos';
export * from './sdk/Errors';
export * from './sdk/Insurance';
export * from './sdk/Events';
export * from './sdk/Feedback';

const {
  Zendrive,
  ZendriveInsurance,
  ZendriveFeedback,
  ZendriveDebug,
} = NativeModules as any;
const ZendriveNativeModule: IZendrive = Zendrive;
const ZendriveInsuranceNativeModule: IZendriveInsurance = ZendriveInsurance;
const ZendriveFeedbackNativeModule: IZendriveFeedback = ZendriveFeedback;
const ZendriveDebugNativeModule: IZendriveDebug = ZendriveDebug;

/**
 * @inheritdoc
 */
class ZendriveDebugImpl implements IZendriveDebug {
  isZendriveSessionIdentifier(
    identifier: string
  ): Promise<ZendriveOperationResult> {
    return ZendriveDebugNativeModule.isZendriveSessionIdentifier(identifier);
  }
  handleEventsForBackgroundURLSession(
    identifier: string
  ): Promise<ZendriveOperationResult> {
    return ZendriveDebugNativeModule.handleEventsForBackgroundURLSession(
      identifier
    );
  }
  uploadAllZendriveData(
    zendriveConfiguration: ZendriveConfiguration,
    notificationConfiguration?: NotificationConfig
  ): Promise<ZendriveOperationResult> {
    if (Platform.OS === 'ios') {
      return ZendriveDebugNativeModule.uploadAllZendriveData(
        zendriveConfiguration
      );
    }
    return ZendriveDebugNativeModule.uploadAllZendriveData(
      zendriveConfiguration,
      notificationConfiguration
    );
  }
}

/**
 * @inheritdoc
 */
class ZendriveFeedbackImpl implements IZendriveFeedback {
  addDriveCategory(
    driveId: string,
    category: ZendriveFeedbackDriveCategory
  ): Promise<boolean> {
    return ZendriveFeedbackNativeModule.addDriveCategory(driveId, category);
  }
  addEventOccurrence(
    driveId: string,
    eventTimestamp: number,
    eventType: ZendriveEventType,
    occurrence: boolean
  ): Promise<boolean> {
    return ZendriveFeedbackNativeModule.addEventOccurrence(
      driveId,
      eventTimestamp,
      eventType,
      occurrence
    );
  }
}
/**
 * @inheritdoc
 */
class ZendriveInsuranceImpl implements IZendriveInsurance {
  startDriveWithPeriod1(): Promise<ZendriveOperationResult> {
    return ZendriveInsuranceNativeModule.startDriveWithPeriod1();
  }
  startDriveWithPeriod2(trackingId: String): Promise<ZendriveOperationResult> {
    if (trackingId) {
      return ZendriveInsuranceNativeModule.startDriveWithPeriod2(trackingId);
    } else {
      let err: ZendriveOperationResult = {
        isSuccess: false,
        errorCode: ZendriveErrorCode.INVALID_PARAMS,
        errorMessage: 'trackingId cannot be empty',
      };
      return Promise.resolve(err);
    }
  }
  startDriveWithPeriod3(trackingId: String): Promise<ZendriveOperationResult> {
    if (trackingId) {
      return ZendriveInsuranceNativeModule.startDriveWithPeriod3(trackingId);
    } else {
      let err: ZendriveOperationResult = {
        isSuccess: false,
        errorCode: ZendriveErrorCode.INVALID_PARAMS,
        errorMessage: 'trackingId cannot be empty',
      };
      return Promise.resolve(err);
    }
  }
  stopPeriod(): Promise<ZendriveOperationResult> {
    return ZendriveInsuranceNativeModule.stopPeriod();
  }
}

/**
 * @inheritdoc
 */
class ZendriveImpl implements IZendrive {
  private eventHandlers: Array<ZendriveCallbackEventHandler> = [];
  private nativeEventHandler: RNEventHandler = new RNEventHandler();
  private callbackHandler: ZendriveCallbackEventHandler = () => {};
  private _insurance: IZendriveInsurance = new ZendriveInsuranceImpl();
  private _feedback: IZendriveFeedback = new ZendriveFeedbackImpl();
  private _debug: IZendriveDebug = new ZendriveDebugImpl();
  private _permissions: IZendrivePermissions =
    Platform.OS === 'android'
      ? new AndroidPermissionsHandler()
      : new IOSPermissionsHandler();

  getActiveDriveInfo(): Promise<ActiveDriveInfo | undefined> {
    return ZendriveNativeModule.getActiveDriveInfo();
  }
  getBuildVersion(): Promise<string> {
    return ZendriveNativeModule.getBuildVersion();
  }
  getEventSupportForDevice(): Promise<Map<ZendriveEventType, boolean>> {
    return ZendriveNativeModule.getEventSupportForDevice();
  }
  getZendriveSettings(): Promise<ZendriveSettings | undefined> {
    return ZendriveNativeModule.getZendriveSettings();
  }
  isAccidentDetectionSupported(): Promise<boolean> {
    return ZendriveNativeModule.isAccidentDetectionSupported();
  }
  isSDKSetup(): Promise<boolean> {
    return ZendriveNativeModule.isSDKSetup();
  }
  isValidInputParameter(input: string): Promise<boolean> {
    if (input) {
      return ZendriveNativeModule.isValidInputParameter(input);
    } else {
      return Promise.resolve(false);
    }
  }
  resume(): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.resume();
  }
  setup(
    configuration: ZendriveConfiguration
  ): Promise<ZendriveOperationResult> {
    if (configuration.notificationSettings) {
      if (
        configuration.notificationSettings.inDriveSettings &&
        configuration.notificationSettings.inDriveSettings.smallIcon
      ) {
        configuration.notificationSettings.inDriveSettings.smallIcon = Image.resolveAssetSource(
          configuration.notificationSettings.inDriveSettings.smallIcon as any
        ).uri;
      }
      if (
        configuration.notificationSettings.mayBeInDriveSettings &&
        configuration.notificationSettings.mayBeInDriveSettings.smallIcon
      ) {
        configuration.notificationSettings.mayBeInDriveSettings.smallIcon = Image.resolveAssetSource(
          configuration.notificationSettings.mayBeInDriveSettings
            .smallIcon as any
        ).uri;
      }

      if (
        configuration.notificationSettings.waitingForDriveSettings &&
        configuration.notificationSettings.waitingForDriveSettings.smallIcon
      ) {
        configuration.notificationSettings.waitingForDriveSettings.smallIcon = Image.resolveAssetSource(
          configuration.notificationSettings.waitingForDriveSettings
            .smallIcon as any
        ).uri;
      }
    }
    if (
      configuration.managesLocationPermission === true &&
      Platform.OS === 'ios' &&
      __DEV__
    ) {
      console.warn('managesLocationPermission is deprecated.');
    }
    if (
      configuration.managesActivityPermission === true &&
      Platform.OS === 'ios' &&
      __DEV__
    ) {
      console.warn('managesActivityPermission is deprecated.');
    }
    return ZendriveNativeModule.setup(configuration);
  }
  setZendriveDriveDetectionMode(
    driveDetectionMode: ZendriveDriveDetectionMode
  ): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.setZendriveDriveDetectionMode(
      driveDetectionMode
    );
  }
  startDrive(
    trackingId?: string | undefined
  ): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.startDrive(trackingId || '');
  }
  startSession(sessionId: string): Promise<ZendriveOperationResult> {
    if (sessionId) {
      return ZendriveNativeModule.startSession(sessionId);
    } else {
      let err: ZendriveOperationResult = {
        isSuccess: false,
        errorCode: ZendriveErrorCode.INVALID_PARAMS,
        errorMessage: 'session id cannot be empty',
      };
      return Promise.resolve(err);
    }
  }

  refreshBusinessHours(): Promise<any> {
    return ZendriveNativeModule.refreshBusinessHours();
  }

  autoTrackingPausedReason(): Promise<
    ZendrivePauseAutoTrackingReason | undefined
  > {
    return ZendriveNativeModule.autoTrackingPausedReason();
  }
  stopManualDrive(): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.stopManualDrive();
  }
  stopSession(): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.stopSession();
  }
  teardown(): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.teardown();
  }
  triggerMockAccident(
    confidence: ZendriveAccidentConfidence
  ): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.triggerMockAccident(confidence);
  }
  triggerMockPotentialAccident(config: {
    delayBetweenCallbacksSeconds: number;
    finalCallbackConfidence: ZendriveAccidentConfidence;
    finalCallbackConfidenceNumber: number;
    potentialCallbackConfidence: ZendriveAccidentConfidence;
    potentialCallbackConfidenceNumber: number;
  }): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.triggerMockPotentialAccident(config);
  }
  uploadAllDebugDataAndLogs(): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.uploadAllDebugDataAndLogs();
  }
  wipeOut(): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.wipeOut();
  }
  getZendriveState(): Promise<ZendriveState | undefined> {
    return ZendriveNativeModule.getZendriveState();
  }
  insurance(): IZendriveInsurance {
    return this._insurance;
  }
  feedback(): IZendriveFeedback {
    return this._feedback;
  }
  debug(): IZendriveDebug {
    return this._debug;
  }
  permissions(): IZendrivePermissions {
    return this._permissions;
  }
  getZendriveDriverCustomAttributes(
    attributes: [string]
  ): Promise<{ [x: string]: string } | null> {
    if (Platform.OS === 'ios') {
      throw new Error(
        'not supported on ios platform, configuration already provides custom attributes'
      );
    }
    return ZendriveNativeModule.getZendriveDriverCustomAttributes(attributes);
  }
  associateVehicle(
    vehicleInfo: ZendriveVehicleInfo
  ): Promise<ZendriveVehicleTaggingOperationResult> {
    return ZendriveNativeModule.associateVehicle(vehicleInfo);
  }
  dissociateVehicle(
    vehicleId: string
  ): Promise<ZendriveVehicleTaggingOperationResult> {
    return ZendriveNativeModule.dissociateVehicle(vehicleId);
  }
  getAssociatedVehicles(): Promise<[ZendriveVehicleInfo]> {
    return ZendriveNativeModule.getAssociatedVehicles();
  }
  getBluetoothPairedDevices(): Promise<[ZendriveBluetoothDevice]> {
    return ZendriveNativeModule.getBluetoothPairedDevices();
  }

  getNearbyBeacons(uuid: string): Promise<[ZendriveScannedBeaconInfo]> {
    return ZendriveNativeModule.getNearbyBeacons(uuid);
  }

  getNearbyBeaconsWithRange(
    uuid: string,
    major: number,
    minor: number
  ): Promise<[ZendriveScannedBeaconInfo]> {
    return ZendriveNativeModule.getNearbyBeaconsWithRange(uuid, major, minor);
  }

  // isTaggedByBluetooth(driveInfo: DriveInfo): Promise<boolean> {
  //   return ZendriveNativeModule.isTaggedByBluetooth(driveInfo);
  // }

  pauseAutoDriveTracking(
    pausedTillTimestamp: number
  ): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.pauseAutoDriveTracking(pausedTillTimestamp);
  }
  resumeAutoDriveTracking(): Promise<ZendriveOperationResult> {
    return ZendriveNativeModule.resumeAutoDriveTracking();
  }
  isAutoTripTrackingPaused(): Promise<Boolean> {
    return ZendriveNativeModule.isAutoTripTrackingPaused();
  }
  logSDKHealth(sdkHealthReason: ZendriveSDKHealthReason): Promise<void> {
    return ZendriveNativeModule.logSDKHealth(sdkHealthReason);
  }
  registerZendriveCallbackEventListener(
    handler: ZendriveCallbackEventHandler
  ): void {
    if (this.eventHandlers.length === 0) {
      this.callbackHandler = (event: ZendriveCallbackEvent) => {
        this.eventHandlers.forEach(fn => fn(event));
      };
      this.nativeEventHandler.register(this.callbackHandler);
    }
    this.eventHandlers.push(handler);
  }
  unregisterZendriveCallbackEventListener(
    handler: ZendriveCallbackEventHandler
  ): void {
    this.eventHandlers = this.eventHandlers.filter(item => item !== handler);
    if (this.eventHandlers.length === 0) {
      this.nativeEventHandler.unregister();
      this.callbackHandler = () => {};
    }
  }
}

const ZendriveSDK = new ZendriveImpl();
export default ZendriveSDK;
