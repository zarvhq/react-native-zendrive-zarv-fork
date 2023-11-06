import {
  DriveStartInfo,
  DriveResumeInfo,
  EstimatedDriveInfo,
  AnalyzedDriveInfo,
  AccidentInfo,
} from './Infos';

export enum ZendriveCallbackEventKind {
  ON_DRIVE_START = 'on-drive-start',
  ON_DRIVE_RESUME = 'on-drive-resume',
  ON_DRIVE_END = 'on-drive-end',
  ON_DRIVE_ANALYZED = 'on-drive-analyzed',
  ON_ACCIDENT = 'on-accident',
  ON_POTENTIAL_ACCIDENT = 'on-potential-accident',
  /**
   * Android only
   * This event is sent when android sdk detects changes in settings that might limit the normal working of the sdk.
   */
  ON_SETTINGS_CHANGED = 'on-settings-changed',
  /**
   * Android only
   * This event is sent when android completes a boot. This callbacks should be used for setting up Zendrive after boot.
   */
  ON_BOOT_COMPLETED = 'on-boot-completed',
  /**
   * Android only
   * This event is sent when your application package is replaced on the device. This callbacks should be used for setting up Zendrive after app upgrade.
   */
  ON_MY_PACKAGE_REPLACED = 'on-my-package-replaced',
  /**
   * ios only
   */
  ON_LOCATION_APPROVED = 'on-location-approved',
  /**
   * ios only
   */
  ON_LOCATION_DENIED = 'on-location-denied',
  /**
   * ios only
   */
  ON_ACTIVITY_APPROVED = 'on-activity-approved',
  /**
   * ios only
   */
  ON_ACTIVITY_DENIED = 'on-activity-denied',
}

export type ZendriveOnDriveStartEvent = {
  kind: ZendriveCallbackEventKind.ON_DRIVE_START;
  event: DriveStartInfo;
};

export type ZendriveOnDriveResumeEvent = {
  kind: ZendriveCallbackEventKind.ON_DRIVE_RESUME;
  event: DriveResumeInfo;
};

export type ZendriveOnDriveEndEvent = {
  kind: ZendriveCallbackEventKind.ON_DRIVE_END;
  event: EstimatedDriveInfo;
};

export type ZendriveOnDriveAnalyzedEvent = {
  kind: ZendriveCallbackEventKind.ON_DRIVE_ANALYZED;
  event: AnalyzedDriveInfo;
};

export type ZendriveOnAccidentEvent = {
  kind: ZendriveCallbackEventKind.ON_ACCIDENT;
  event: AccidentInfo;
};

export type ZendriveOnPotentialAccidentEvent = {
  kind: ZendriveCallbackEventKind.ON_POTENTIAL_ACCIDENT;
  event: AccidentInfo;
};

export type ZendriveOnSettingsChangedEvent = {
  kind: ZendriveCallbackEventKind.ON_SETTINGS_CHANGED;
  event: {
    errorsFound: boolean;
    warningsFound: boolean;
  };
};

/**
 * Event received when location is approved
 * Available for iOS only. See [[ZendriveConfiguration.managesLocationPermission]] & [[ZendriveConfiguration.managesActivityPermission]]
 */
export type ZendriveOnActivityApprovedEvent = {
  kind: ZendriveCallbackEventKind.ON_ACTIVITY_APPROVED;
  event: {};
};

/**
 * Event received when location is denied
 * Available for iOS only. See [[ZendriveConfiguration.managesLocationPermission]] & [[ZendriveConfiguration.managesActivityPermission]]
 */
export type ZendriveOnActivityDeniedEvent = {
  kind: ZendriveCallbackEventKind.ON_ACTIVITY_DENIED;
  event: {};
};

/**
 * Event received when location is approved
 * Available for iOS only. See [[ZendriveConfiguration.managesLocationPermission]] & [[ZendriveConfiguration.managesActivityPermission]]
 */
export type ZendriveOnLocationApprovedEvent = {
  kind: ZendriveCallbackEventKind.ON_LOCATION_APPROVED;
  event: {};
};

/**
 * Event received when location is denied
 * Available for iOS only. See [[ZendriveConfiguration.managesLocationPermission]] & [[ZendriveConfiguration.managesActivityPermission]]
 */
export type ZendriveOnLocationDeniedEvent = {
  kind: ZendriveCallbackEventKind.ON_LOCATION_DENIED;
  event: {};
};

/**
 * See [[ZendriveCallbackEventKind#ON_BOOT_COMPLETED]]
 */
export type ZendriveOnBootCompleted = {
  kind: ZendriveCallbackEventKind.ON_BOOT_COMPLETED;
  event: {};
};

/**
 * See [[ZendriveCallbackEventKind#ON_BOOT_COMPLETED]]
 */
export type ZendriveOnMyPackageReplaced = {
  kind: ZendriveCallbackEventKind.ON_MY_PACKAGE_REPLACED;
  event: {};
};

export type ZendriveCallbackEvent =
  | ZendriveOnDriveStartEvent
  | ZendriveOnDriveResumeEvent
  | ZendriveOnDriveEndEvent
  | ZendriveOnDriveAnalyzedEvent
  | ZendriveOnAccidentEvent
  | ZendriveOnPotentialAccidentEvent
  | ZendriveOnSettingsChangedEvent
  | ZendriveOnLocationApprovedEvent
  | ZendriveOnLocationDeniedEvent
  | ZendriveOnActivityApprovedEvent
  | ZendriveOnActivityDeniedEvent
  | ZendriveOnBootCompleted
  | ZendriveOnMyPackageReplaced;

export type ZendriveCallbackEventHandler = (
  event: ZendriveCallbackEvent
) => void;
