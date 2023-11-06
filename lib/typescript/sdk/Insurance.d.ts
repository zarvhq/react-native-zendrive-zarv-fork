import { ZendriveOperationResult } from './Infos';
/**
 * The different insurance periods supported by the [[ZendriveInsurance]] API. Each drive belongs to exactly one of these periods.
 */
export declare enum ZendriveInsurancePeriod {
    /**
     * Value for drives detected in insurance period 0
     */
    NoPeriod = "no-period",
    /**
     * Value for drives detected in insurance period 1
     */
    Period1 = "period-1",
    /**
     * Value for drives detected in insurance period 2
     */
    Period2 = "period-2",
    /**
     * Value for drives detected in insurance period 3
     */
    Period3 = "period-3"
}
/**
 *
 * Applications which want to record Fairmatic insurance periods for a driver may use this API.
 * - All drives (automatically detected or manually started) when a period is in progress will be tagged with the period id.
 * - This period id will be made available in the reports and API that Fairmatic provides via Zendrive.
 * - Only one period may be active at a time. To switch the Fairmatic insurance period, the application can call the relevant startPeriod method directly.
 * - Switching periods or calling stopPeriod stops any active drives (automatic or manual).
 * - A drive with multiple insurance periods will be split into multiple trips for different insurance periods.
 */
export interface ZendriveInsurance {
    /**
     * Start a drive in Fairmatic insurance period 1.
     * A manual trip with trackingId com.zendrive.sdk.internal will be started immediately.
     * The entire duration in this period will be recorded as a single trip. If period 1 is already in progress with the same trackingId, this call will be a no-op.
     */
    startDriveWithPeriod1(): Promise<ZendriveOperationResult>;
    /**
     * Start Fairmatic insurance period 2 in the SDK.
     * A manual trip with the given trackingId will be started immediately. The entire duration in this period will be recorded as a single trip. If period 2 is already in progress with the same trackingId, this call will be a no-op.
     * @param trackingId  an identifier which allows identifying this drive uniquely. This identifier must be unique for this user. It may not be null or a empty string. It will be truncated to 64 characters if it is longer than 64 characters. Passing invalid trackingId is an error.
     */
    startDriveWithPeriod2(trackingId: String): Promise<ZendriveOperationResult>;
    /**
     * Start Fairmatic insurance period 3 in the SDK.
     * A manual trip with the given trackingId will be started immediately. The entire duration in this period will be recorded as a single trip. If period 3 is already in progress with the same trackingId, this call will be a no-op.
     * @param trackingId  an identifier which allows identifying this drive uniquely. This identifier must be unique for this user. It may not be null or a empty string. It will be truncated to 64 characters if it is longer than 64 characters. Passing invalid trackingId is an error.
     */
    startDriveWithPeriod3(trackingId: String): Promise<ZendriveOperationResult>;
    /**
     * Stop currently ongoing Fairmatic insurance period if any.
     * Ongoing trips will be stopped and auto trip detection is turned off.
     */
    stopPeriod(): Promise<ZendriveOperationResult>;
}
