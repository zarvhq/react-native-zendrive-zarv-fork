import { ZendriveInsurancePeriod } from './Insurance';
import {
  ZendriveSettingError,
  ZendriveSettingWarning,
  ZendriveErrorCode,
  ZendriveVehicleTaggingErrorCode,
} from './Errors';

/**
 * A geographic point location.
 */
export type LocationPoint = {
  /**
   * The latitude in degrees of this location point.
   */
  latitude: number;
  /**
   * The longitude in degrees of this location point.
   */
  longitude: number;
};

/**
 * Extrapolated start location
 */
export type ExtrapolationStartLocation = {
  /**
   * The latitude in degrees of this location point.
   */
  latitude: number;
  /**
   * The longitude in degrees of this location point.
   */
  longitude: number;
  /**
   * The timestamp in milliseconds that corresponds to the location.
   */
  timestampMillis: number;
};

/**
 * Information about the extrapolation details captured by the Zendrive SDK.
 */
export type ZendriveExtrapolationDetails = {
  /**
   * A unique identifier of this accident.
   */
  extrapolatedDistance: number;
  /**
   * Confidence measure of the detected accident.
   */
  estimatedStartLocation: ExtrapolationStartLocation;
};

/**
 * Information about an accident detected by the Zendrive SDK.
 */
export type AccidentInfo = {
  /**
   * A unique identifier of this accident.
   */
  accidentId: string;
  /**
   * Confidence measure of the detected accident.
   */
  confidence: ZendriveAccidentConfidence;
  /**
   * An identifier for the drive during which the accident occured.
   */
  driveId: string;
  /**
   * The location of the accident.
   */
  location: LocationPoint;
  /**
   * Session id is specified by the enclosing application when it wants to record a session.
   * See [[Zendrive.startSession]]
   */
  sessionId?: string;
  /**
   * The timestamp of the accident in milliseconds since epoch.
   */
  timestampMillis: number;
  /**
   * Tracking id is specified by the enclosing application when it wants to start a drive manually. See [[Zendrive.startDrive]]
   */
  trackingId?: string;
};

/**
 * Indicates the confidence of the reported accident. See [[AccidentInfo]] for details.
 */
export enum ZendriveAccidentConfidence {
  /**
   * Accident was detected with a high confidence.
   */
  HIGH = 'high',
  /**
   * Accident was detected with a low confidence.
   */
  LOW = 'low',
}

export enum ZendrivePauseAutoTrackingReason {
  BUSINESS_HOURS = 'business_hours',
  SDK_NOT_PAUSED = 'sdk_not_paused',
  USER = 'user',
}
/**
 * Completely analyzed information about a drive recorded by the Zendrive SDK.
The drive may have started manually by the application or due to Zendrive auto drive detection.

This is available only when [[ZendriveDriveType]] is not equal to [[ZendriveDriveType.INVALID]]. The info provided in this object supercedes the info provided in [[EstimatedDriveInfo]] via [[ZendriveOnDriveEndEvent]] for the same [[DriveInfo]] driveId. The application should ideally update the info stored for this drive with the info provided here.
 */
export type AnalyzedDriveInfo = DriveInfo;
/**
 * Information about a drive recorded by the Zendrive SDK.
The drive may have started manually by the application or due to Zendrive auto drive detection.

When [[ZendriveDriveType]] is not equal to [[ZendriveDriveType.INVALID]], the info provided in this object will be superceded by [[AnalyzedDriveInfo]] provided via [[ZendriveOnDriveEndEvent]] callback for the same [[DriveInfo]] driveId. The application should save the info provided here and update it when [[ZendriveOnDriveAnalyzedEvent]] is notified.
 */
export type EstimatedDriveInfo = DriveInfo;
/**
 * Information about the currently ongoing drive recorded by the Zendrive SDK.
 * The drive may have started manually by the application or due to Zendrive auto drive detection.
 */
export type ActiveDriveInfo = {
  /**
   * The current location of the vehicle.
   */
  currentLocation: LocationPoint;
  /**
   * The current speed of the vehicle in meters/second.
   */
  currentSpeed: number;
  /**
   * The distance traversed so far in this drive in meters.
   */
  distanceMeters: number;
  /**
   * An identifier for this drive that was recorded.
   */
  driveId: string;
  /**
   * The insurance period in which the drive was detected.
   */
  insurancePeriod: ZendriveInsurancePeriod;
  /**
   * Session id is optionally specified by the enclosing application when it wants to record a session.
   */
  sessionId: string;
  /**
   * The start timestamp of the drive in milliseconds since epoch.
   */
  startTimeMillis: number;
  /**
   * Tracking id is optionally specified by the enclosing application when it wants to start a drive manually.
   */
  trackingId: string;
};

/**
 * Information about a drive that was resumed in the Zendrive SDK.
 * This is called after the drive recording resumes after a gap. The gap may occur due to an application restart by the OS, application kill and restart by a user, an application crash etc.
 */
export type DriveResumeInfo = {
  /**
   * The end timestamp of the gap in drive recording in millisecs.
   */
  driveGapEndTimestampMillis: number;
  /**
   * The start timestamp of the gap in drive recording in millisecs.
   */
  driveGapStartTimestampMillis: number;
  /**
   * An identifier for this drive that was resumed.
   */
  driveId: string;
  /**
   * The insurance period in which the drive was detected.
   */
  insurancePeriod: ZendriveInsurancePeriod;
  /**
   * Session id is specified by the enclosing application when it wants to record a session.
   */
  sessionId: string;
  /**
   * The start timestamp of the drive in milliseconds since epoch.
   */
  startTimeMillis: number;
  /**
   * Tracking id is specified by the enclosing application when it wants to start a drive manually.
   */
  trackingId: string;
};

export type DriveStartInfo = {
  /**
   * The start timestamp of the drive in milliseconds since epoch.
   */
  startTimeMillis: number;
  /**
   * An identifier for this drive that just started.
   */
  driveId: string;
  /**
   * The insurance period in which the drive was detected.
   */
  insurancePeriod: ZendriveInsurancePeriod;
  /**
   * Session id is specified by the enclosing application when it wants to record a session.
   */
  sessionId: string;
  /**
   * Tracking id is specified by the enclosing application when it wants to start a drive manually.
   */
  trackingId: string;
  /**
   * The start location of the drive.
   */
  startLocation?: LocationPoint;
};

/**
 * Information about a drive recorded by the Zendrive SDK.
 * The drive may have started manually by the application or due to Zendrive auto drive detection.
 */
export type DriveInfo = {
  /**
   * The average speed of the drive in meters per second.
   */
  averageSpeed: number;
  /**
   * The total distance of the drive in meters.
   */
  distanceMeters: number;
  /**
   * The vehicle type of the drive, either car or motorcycle.
   */
  vehicleType?: ZendriveVehicleType;
  /**
   * An identifier for this drive that was recorded.
   */
  driveId: number;
  /**
   * The type of drive.
   */
  driveType: ZendriveDriveType;
  /**
   * The end timestamp of the drive in milliseconds since epoch.
   */
  endTimeMillis: number;
  /**
   * Ratings associated with various event types for this drive. See [[ZendriveEventType]]
   */
  eventRatings: ZendriveEventRatings;
  /**
   * A list of events detected by the sdk for this drive.
   */
  events: Array<ZendriveEvent>;
  /**
   * The insurance period in which the drive was detected.
   */
  insurancePeriod: ZendriveInsurancePeriod;
  /**
   * The maximum speed of the drive in meters per second.
   */
  maxSpeed: number;
  /**
   * Position of the phone or device for the majority of the drive.
   */
  phonePosition: PhonePosition;
  /**
   * The driving behaviour score for this drive.
   */
  score: DriveScore;
  /**
   * Session id is specified by the enclosing application when it wants to record a session.
   * See [[Zendrive.startSession]]
   */
  sessionId?: string;
  /**
   * The start timestamp of the drive in milliseconds since epoch.
   */
  startTimeMillis: number;
  /**
   * Tracking id is specified by the enclosing application when it wants to start a drive manually.
   * See [[Zendrive.startDrive]]
   */
  trackingId?: string;
  /**
   * Whether the user was a driver or a passenger.
   */
  userMode: ZendriveUserMode;
  /**
   * A list of warnings for this drive.
   */
  warnings: Array<DriveInfoWarning>;
  /**
   * A list of [[LocationPointWithTimestamp]] objects corresponding to this drive in increasing order of timestamp.
   */
  waypoints: Array<LocationPointWithTimestamp>;
  /**
   * Extrapolation details of the trip.
   */
  extrapolationDetails?: ZendriveExtrapolationDetails;
};

export type TagInfo = {
  key: string;
  value: string;
};

/**
 * A location and the timestamp corresponding to the location.
 */
export type LocationPointWithTimestamp = {
  /**
   * A location point.
   */
  location: LocationPoint;
  /**
   * The timestamp in milliseconds that corresponds to the location.
   */
  timestampMillis: number;
};

/**
 * Enum representing warnings detected for the current drive.
 */
export enum DriveInfoWarning {
  /**
   * The trip duration is unexpectedly large and signifies a possible integration issue.
   */
  UnexpectedTripDuration = 'unexpected-trip-duration',
}

/**
 * Type of drive recorded by the Zendrive SDK.
 */
export enum ZendriveDriveType {
  /**
   * This trip was taken in a car.
   */
  DRIVE = 'drive',
  /**
   * Sometimes, the SDK detects that a drive is invalid after it has been started.
   */
  INVALID = 'invalid',
  /**
   * This drive was not taken on a car.
   */
  NON_DRIVING = 'non-driving',
}

/**
 * Type of vehicle type recorded by the Zendrive SDK.
 */
export enum ZendriveVehicleType {
  /**
   * This trip was taken in a car.
   */
  CAR = 'car',
  /**
   * This trip was taken on a motorcycle
   */
  MOTORCYCLE = 'motorcycle',
}

/**
 * Represents ratings associated with the various event types for a trip. Higher rating for an event represents safe driving behaviour with respect to that event. For cases where rating is absent, [[ZendriveStarRating.NOT_AVAILABLE]] is reported.
 */
export type ZendriveEventRatings = {
  aggressiveAccelerationRating: ZendriveStarRating;
  hardBrakeRating: ZendriveStarRating;
  hardTurnRating: ZendriveStarRating;
  phoneHandlingRating: ZendriveStarRating;
  speedingRating: ZendriveStarRating;
};

/**
 * Type of events that may be reported by the Zendrive SDK.
 */
export enum ZendriveEventType {
  /**
   * The driver accelerated aggressively.
   */
  AGGRESSIVE_ACCELERATION = 'aggressive-acceleration',
  /**
   * The driver was involved in a collision.
   */
  COLLISION = 'collision',
  /**
   * The driver applied a hard brake.
   */
  HARD_BRAKE = 'hard-brake',
  /**
   * The driver performed a hard turn.
   */
  HARD_TURN = 'hard-turn',
  /**
   * The driver was handling the phone while driving.
   */
  PHONE_HANDLING = 'phone-handling',
  /**
   * The driver was touching the phone screen while driving.
   */
  PHONE_SCREEN_INTERACTION = 'phone-screen-interaction',
  /**
   * The driver was speeding beyond 75mph (33.52 m/s).
   */
  SPEEDING = 'speeding',
  /**
   * The driver did not stop at a stop sign.
   */
  STOP_SIGN_VIOLATION = 'stop-sign-violation',
}

/**
 * Zendrive follows the star rating system, under which five star defined as the best rating with one being the worst.
 */
export enum ZendriveStarRating {
  FIVE = '5',
  FOUR = '4',
  THREE = '3',
  TWO = '2',
  ONE = '1',
  /**
   * Reported when rating is absent.
   */
  NOT_AVAILABLE = 'not-available',
}
/**
 * Represents an interesting event detected by Zendrive during a drive.
 * Events like speeding, phone use can span a time duration and start a particular location and end at another location. Events like hard braking and rapid acceleration are instantaneous. The start and end timestamps and the start and end locations for instantaneous events are the same.
 */
export type ZendriveEvent = {
  /**
   * The end location of the event.
   */
  endLocation: LocationPoint;
  /**
   * The end timestamp of the event in millisecs since epoch.
   */
  endTimestampMillis: number;
  /**
   * The type of event.
   */
  eventType: ZendriveEventType;
  /**
   * Indicates the severity of this event.
   */
  severity: ZendriveEventSeverity;
  /**
   * Extra information for a Speeding event.
   */
  speedingInfo?: SpeedingInfo;
  /**
   * The start location of the event.
   */
  startLocation: LocationPoint;
  /**
   * The start timestamp of the event in millisecs since epoch.
   */
  startTimestampMillis: number;
  /**
   *Indicates the turn direction of this event.
   */
  turnDirection: ZendriveTurnDirection;
};

/**
 * Indicates the severity of a event reported by Zendrive. Apps may use this to appropriately surface and communicate events to users.
 */
export enum ZendriveEventSeverity {
  /**
   * Indicates that the event has high severity.
   */
  HIGH = 'high',
  /**
   * Indicates that the event has low severity.
   */
  LOW = 'low',
  /**
   * Indicates that the severity is not available.
   */
  NOT_AVAILABLE = 'not-available',
}

/**
 * Extra information for a Speeding event. See [[ZendriveEventType.SPEEDING]]
 */
export type SpeedingInfo = {
  avgSpeed: number;
  maxSpeed: number;
  speedLimit: number;
};

/**
 * Indicates the direction of the [[ZendriveEventType.HARD_TURN]] event reported by Zendrive. This will be null for other event types.
 */
export enum ZendriveTurnDirection {
  /**
   * Indicates that the turn direction of the event was Left.
   */
  LEFT = 'left',
  /**
   * Indicates that the turn direction of the event was right.
   */
  RIGHT = 'right',
  /**
   * Indicates turn direction not available
   */
  NOT_AVAILABLE = 'not-available',
}

/**
 * Position of the phone or device for the majority of the drive
 */
export enum PhonePosition {
  /**
   * The device was in a mounted position for most of the drive.
   */
  MOUNT = 'mount',
  /**
   * No definitive value available
   */
  UNKNOWN = 'unknown',
}

/**
 * Driving Behaviour scores for a drive.
 * The scores are expressed as a number between 0 to 100 and will be -1 if not available.
 * High scores indicate safe driving and low scores reflect hazardous or risky driving patterns. Preventive or corrective actions should be prescribed in extreme cases.
 * More information is available [here](http://docs.zendrive.com/en/latest/api/scores.html)
 */
export type DriveScore = {
  zendriveScore: number;
};

/**
 * For a Drive, whether the user was a driver or a passenger
 * See [[DriveInfo]]
 */
export enum ZendriveUserMode {
  /**
   * Indicates that the user was in the driver seat.
   */
  DRIVER = 'driver',
  /**
   * Indicates that the user was in the passenger seat.
   */
  PASSENGER = 'passenger',
  /**
   * Indicates unavailable
   */
  NOT_AVAILABLE = 'not-available',
}

/**
 * This class surfaces errors in device or application settings that affect SDK trip detection as well as warnings that may affect the normal functioning of the Zendrive SDK.
 */
export type ZendriveSettings = {
  /**
   * List of errors that must be resolved for trip detection to work correctly.
   */
  errors: Array<ZendriveSettingError>;
  /**
   * List of warnings that may affect the normal functioning of the Zendrive SDK.
   */
  warnings: Array<ZendriveSettingWarning>;
};

/**
 * Configuration to show notification by zendrive.
 * This is android specific and will be ignored for ios.
 */
export type NotificationConfig = {
  /**
   * Title to be shown in notification, default is application name.
   */
  contentTitle?: string;
  /**
   * Content Text to be shown in notification, default nothing.
   */
  contentText?: string;
  /**
   * Notification id with which this should shown, default is a unique value assigned by sdk
   */
  notificationId?: number;
  /**
   * Small icon to be shown in android notification
   * value should be `require('your-path-to-js-asset')`
   */
  smallIcon?: number | string;
};

/**
 * Notification settings for android.
 */
export type NotificationSettings = {
  /**
   * Notification configuration that will be used by zendrive foreground notification when sdk waits for a drive.
   */
  waitingForDriveSettings?: NotificationConfig;
  /**
   * Notification configuration that will be used by zendrive foreground notification when sdk detects that a drive may be possible.
   */
  mayBeInDriveSettings?: NotificationConfig;
  /**
   * Notification configuration that will be used by zendrive foreground notification when sdk detects that a drive is in progress.
   */
  inDriveSettings?: NotificationConfig;
  /**
   * Channel key used for notifications.
   */
  channelKey: string;
};

/**
 * Configuration for Zendrive sdk setup.
 * See [[Zendrive.setup]]
 */
export type ZendriveConfiguration = {
  /**
   * Additional attributes of the current driver.
   */
  attributes?: ZendriveAttributes;
  /**
   * Returns the unique ID of the driver using this application.
   */
  driverId: string;
  /**
   * Zendrive sdk key used by this application.
   */
  sdkKey: string;
  /**
   * Whether SDK should manage location permissions.
   * iOS only. Defaults to true when not provided
   */
  managesLocationPermission?: boolean;
  /**
   * Whether SDK should manage activity/fitness permissions.
   * iOS only. Defaults to true when not provided
   */
  managesActivityPermission?: boolean;
  /**
   * Drive detection mode specified for the Zendrive SDK.
   */
  driveDetectionMode?: ZendriveDriveDetectionMode;
  /**
   * NotificationSettings that will be used to show foreground notification by the Zendrive SDK
   * Android Only.
   */
  notificationSettings?: NotificationSettings;
  /**
   * Region specified for the Zendrive SDK.
   * Defaults to [[ZendriveRegion.US]] if unspecified
   */
  region?: ZendriveRegion;

  /**
   * Set this property to true if the app supports receiving multiple callbacks.
   * If set to true, the application will receive `on-potential-accident` event.
   */
  implementsMultipleAccidentCallbacks?: boolean;
  /**
   * Android Only
   * Set this property to true if the app supports Bluetooth Drive Start.
   * If set to true, the application will receive `on-drive-start` event whenever succesful bluetooth connection is established with an associated vehicle.
   */
  enabledBluetoothTripStart?: boolean;
};

/**
 * Additional attributes of a Zendrive driver.
The application can specify both predefined and custom attributes for a driver. These attributes are associated with a SDK driverId at SDK initialization time. In addition to predefined special attributes, up to 4 custom key value attributes can be associated with a driver of the Zendrive SDK. These attributes are available on the Zendrive dashboard and via the API.

All attribute keys and values can be upto 64 characters in length. Longer keys and values will be truncated to the first 64 characters.
 */
export type ZendriveAttributes = {
  alias?: string;
  group?: string;
  serviceLevel?: ServiceLevelAttribute;
  vehicleType?: ZendriveVehicleType;
} & { [key: string]: string };

/**
 * Enumeration for different service levels supported by Zendrive for a driver. By default, drivers will be assigned to the default service level - LEVEL_DEFAULT.
 * This is useful for applications which need special modes in the Zendrive SDK for different drivers - e.g default mode for free users and a advanced mode for paid users.
 * By default, multiple service levels are not enabled for an application. To be able to use different modes for your application, you should contact support@zendrive.com with your requirements and get that enabled for your application. Otherwise, if this is not enabled for your application, all drivers get mapped to LEVEL_DEFAULT irrespective of the service level specified.
 */
export enum ServiceLevelAttribute {
  /**
   * Special service level 1 that is enabled for a particular application.
   */
  LEVEL_1 = 'level-1',
  /**
   * Default service level.
   */
  LEVEL_DEFAULT = 'default',
}

/**
 * Dictates the functioning of Zendrive's drive detection.
 */
export enum ZendriveDriveDetectionMode {
  /**
   * Zendrive SDK will not automatically detect and track drives in background in this mode once the SDK is setup.
   */
  AUTO_OFF = 'auto-off',
  /**
   * Zendrive SDK will automatically detect and track drives in background in this mode once the SDK is setup.
   */
  AUTO_ON = 'auto-on',
  /**
   * In this mode drive detection is controlled by period APIs present in ZendriveInsurance.
   */
  INSURANCE = 'insurance',
}

/**
 * The region (US/EU) will be taken as an optional input from the application in ZendriveConfiguration. US will be the default. Existing applications would continue to work without any modification required.
 */
export enum ZendriveRegion {
  /**
   * US region
   */
  US = 'US',
  /**
   * EU region
   */
  EU = 'EU',
}

/**
 * A result of an operation performed on the Zendrive SDK. This indicates whether the operation was a success or a failure. In the case of failure, a error code and error message are provided.
 */
export type ZendriveOperationResult =
  | ZendriveOperationResultSuccess
  | ZendriveOperationResultError;

export type ZendriveOperationResultSuccess = {
  isSuccess: true;
};

export type ZendriveOperationResultError = {
  isSuccess: false;
  errorCode: ZendriveErrorCode;
  errorMessage: string;
};

/**
 * Represents the current state of the Zendrive SDK.
 */
export type ZendriveState = {
  /**
   * Is the SDK currently tracking a drive in progress?
   */
  isDriveInProgress: boolean;
  /**
   * Is the SDK currently running as a foreground service?
   */
  isForegroundService: boolean;
  /**
   * The current configuration of the SDK.
   */
  zendriveConfiguration: ZendriveConfiguration;
};

/**
 * Represents Vehicle info data
 */
export type ZendriveVehicleInfo = {
  vehicleId: string;
  bluetoothAddress: string;
};

export type ZendriveVehicleTaggingOperationResult = {
  isSuccess: boolean;
  errorMessage: string;
  errorCode: ZendriveVehicleTaggingErrorCode;
};

/**
 * Represents bluetooth info data
 */
export type ZendriveBluetoothDevice = {
  name: string;
  address: string;
};

/**
 * Represents ibeacon info data
 */
export type ZendriveBeaconDevice = {
  uuid: string;
  major: number;
  minor: number;
};

export type ZendriveVehicleBeacon = {
  vehicleId: string;
  beacon: ZendriveBeaconDevice;
};

export type ZendriveScannedBeaconInfo = {
  uuid: string;
  major: number;
  minor: number;
  rssi: number;
  accuracy: number;
};

/**
 *  Dictates the reason for is logging SDK Health
 *  The SDK health will be recorded when there is any update in permission from last recorded SDK health.
 */
export enum ZendriveSDKHealthReason {
  /**
   * Indicates the reason  is unknown.
   * This is the default value.
   */
  UNKNOWN = 'unknown',

  /**
   * Indicates the SDK health will recorded due to silent push notification
   */
  SILENT_PUSH_NOTIFICATION = 'silentPushNotification',

  /**
   * Indicates the SDK health will recorded due to background processing
   */
  BACKGROUND_PROCESSING = 'backgroundProcessing',
}

/**  */
