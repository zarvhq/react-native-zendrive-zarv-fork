import { ZendriveEventType } from './Infos';
export declare enum ZendriveFeedbackDriveCategory {
    /**
     * Indicates that the trip was taken on a bicycle
     */
    BICYCLE = "bicycle",
    /**
     * Indicates that the trip was taken in a bus
     */
    BUS = "bus",
    /**
     * Indicates that the trip was taken in a car
     */
    CAR = "car",
    /**
     * Indicates that the trip was taken in a car and the user was the driver
     */
    CAR_DRIVER = "car-driver",
    /**
     * Indicates that the trip was taken in a car and the user was a passenger
     */
    CAR_PASSENGER = "car-passenger",
    /**
     * Indicates that the trip was taken using some form of air travel
     */
    FLIGHT = "flight",
    /**
     * Indicates that the trip was taken on foot (either walking or running)
     */
    FOOT = "foot",
    /**
     * Indicates that there wasn't enough movement and this shouldn't have been detected as a trip
     */
    INVALID = "invalid",
    /**
     * Indicates that the trip was taken on a motorcycle
     */
    MOTORCYCLE = "motorcycle",
    /**
     * Indicates that the trip was not taken in a car.
     */
    NOT_CAR = "not-car",
    /**
     * Fallback when the above options do not cover the use case.
     */
    OTHER = "other",
    /**
     * Indicates that the trip was taken in a train or a subway
     */
    TRAIN = "train",
    /**
     * Indicates that the trip was taken using some form of public transit (bus/train/subway/tram etc)
     */
    TRANSIT = "transit"
}
/**
 * Provide feedback on a trip or events
 */
export interface ZendriveFeedback {
    /**
     * Help Zendrive improve by providing feedback for a drive detected by the SDK
     * If the SDK is not setup, this method is a no-op.
     * @param driveId as returned at the end of drive in [[DriveInfo#driveId]]
     * @param category the category that best indicates the type of Drive, see [[ZendriveFeedbackDriveCategory]]
     */
    addDriveCategory(driveId: string, category: ZendriveFeedbackDriveCategory): Promise<boolean>;
    /**
     * Help Zendrive improve by providing information about whether an event detected by the SDK occurred or not
     * If the SDK is not setup, this method is a no-op.
     * @param driveId as returned at the end of drive in [[DriveInfo#driveId]] which this event is part of
     * @param eventTimestamp as returned by [[ZendriveEvent#startTimestampMillis]]
     * @param eventType as returned by [[ZendriveEvent#eventType]]
     * @param occurrence whether the event occurred or not
     */
    addEventOccurrence(driveId: string, eventTimestamp: number, eventType: ZendriveEventType, occurrence: boolean): Promise<boolean>;
}
