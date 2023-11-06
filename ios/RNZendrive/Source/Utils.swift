//
//  Utils.swift
//  Pods
//
//  Created by Madhusudhan Sambojhu on 23/07/19.
//

import Foundation
import ZendriveSDK

typealias RnJsonObject = [String: Any?]

class Utils {
    // swiftlint:disable cyclomatic_complexity
    static func feedbackCategory( of feedbackType: String) -> ZendriveDriveCategory {
        switch feedbackType {
        case "bicycle": return .bicycle
        case "bus":  return .bus
        case "car":  return .car
        case "car-driver":  return .carDriver
        case "car-passenger":  return .carPassenger
        case "foot":  return .foot
        case "invalid":  return .invalid
        case "motorcycle":  return .motorcycle
        case "not-car":  return .notCar
        case "other":  return .other
        case "train":  return .train
        case "transit":  return .transit
        default:
            return .other
        }
    }
    // swiftlint:enable cyclomatic_complexity

    static func eventType( of type: String) -> ZendriveEventType {
        switch type {
        case "collision":
            return .accident
        case "aggressive-acceleration":
            return .aggressiveAcceleration
        case "hard-brake":
            return .hardBrake
        case "hard-turn":
            return .hardTurn
        case "speeding":
            return .overSpeeding
        case "phone-handling":
            return .phoneHandling
        case "phone-screen-interaction":
            return .phoneScreenInteraction
        default:
            return .aggressiveAcceleration
        }
    }

    static func accidentConfidence(
        of confidence: String
    ) -> ZendriveAccidentConfidence {
        switch confidence {
        case "high":
            return .high
        case "low":
            return .low
        default:
            return .high
        }
    }

    static func logSdkHealthCompletionHandler(error: Error?) -> [String: Any] {
        if let err = error as NSError? {
            return [
                "isSuccess": false,
                "errorCode": self.getErrorCode(err: err),
                "errorMessage": error?.localizedDescription ?? "none"
            ]
        } else {
            return [
                "isSuccess": true
            ]
        }
    }

    static func convertZendriveOperationResult(
        withCompleted completed: Bool,
        error: Error?) -> [String: Any] {
        if let err = error as NSError? {
            return [
                "isSuccess": false,
                "errorCode": self.getErrorCode(err: err),
                "errorMessage": error?.localizedDescription ?? "none"
            ]
        } else {
            return [
                "isSuccess": true
            ]
        }
    }

    static func convertVehicleTaggingOperationResult(
        withCompleted completed: Bool,
        error: Error?) -> [String: Any] {

        if let err = error as NSError? {
            return [
                "isSuccess": false,
                "errorCode": self.getVehicleTaggingErrorCode(err: err),
                "errorMessage": error?.localizedDescription ?? "none"
            ]
        } else {
            return [
                "isSuccess": true
            ]
        }
    }

    static func getVehicleTaggingErrorCode(err: NSError) -> String {
        let errorCode = ZendriveVehicleTaggingError(rawValue: Int32(err.code))
        switch errorCode {
        case .associatedVehicleConflict:
            return "associated-vehicle-conflict"
        case .associatedVehiclesLimitExceeded:
            return "associated-vehicle-limit-exceeded"
        case .invalidVehicleId:
            return "invalid-vehicle-id"
        case .invalidVehicleInfo:
            return "invalid-zendrive-vehicle-info"
        case .notSetup:
            return "sdk-not-setup"
        case .vehicleNotAssociated:
            return "vehicle-not-associated"
        default:
            return "sdk-not-setup"
        }
    }

    // swiftlint:disable cyclomatic_complexity
    static func getErrorCode(err: NSError) -> String {
        let errorCode = ZendriveError(rawValue: Int32(err.code))!
        switch errorCode {
        case .invalidSDKKeyString:
            return "invalid-sdk-key"
        case .deviceUnsupported:
            return "unsupported-device"
        case .insurancePeriodSame:
            return "insurance-period-same"
        case .internalFailure:
            return "sdk-error"
        case .invalidParams:
            return "invalid-params"
        case .invalidTrackingId:
            return "invalid-tracking-id"
        case .ioError:
            return "io-error"
        case .networkUnreachable:
            return "network-not-available"
        case .notSetup:
            return "sdk-not-setup"
        case .notTornDown:
            return "sdk-not-torn-down"
        case .unsupportedOSVersion:
            return "unsupported-os-version"
        case .regionUnsupported:
            return "region-unsupported"
        case .invalidRegion:
            return "region-unsupported"
        case .unauthorizedRegionSwitch:
            return "region-switch-error"
        case .userDeprovisioned:
            return "user-deprovisioned"
        case .unsupportedVehicleType:
            return "unsupported-vehicle-type"
        case .invalidBeaconUUID:
            return "invalid-beacon-uuid"
        case .beaconNotFound:
            return "beacon-not-found"
        case .maxBeaconsLimitReached:
            return "max-beacon-limit-reached"
        case .expiredPausedTillTimestamp:
            return "expired-paused-till-timestamp"
        case .refreshBusinessHoursTimeout:
            return "refresh-business-hours-timeout"
        case .refreshBusinessHoursNetworkNotAvailable:
            return "refresh-business-hours-network-not-available"
        case .refreshBusinessHoursNotEnabled:
            return "refresh-business-hours-feature-not-enabled"
        @unknown default:
            return "sdk-error"
        }
    }
    // swiftlint:enable cyclomatic_complexity

    static func insurancePeriod(of insuracePeriod: ZendriveInsurancePeriod) -> String {
        switch insuracePeriod {
        case .noPeriod:
            return "no-period"
        default:
            return "period-\(insuracePeriod.rawValue)"
        }
    }

    static func insurancePeriod(of insuracePeriod: String) -> ZendriveInsurancePeriod {
        switch insuracePeriod {
        case "period-1":
            return .period1
        case "period-2":
            return .period2
        case "period-3":
            return .period3
        default:
            return .noPeriod
        }
    }

    static func eventType(of eventType: Int) -> String {
        let typeOfEvent = ZendriveEventType(rawValue: eventType)
        switch typeOfEvent {
        case .accident:
            return "collision"
        case .aggressiveAcceleration:
            return "aggressive-acceleration"
        case .hardBrake:
            return "hard-brake"
        case .hardTurn:
            return "hard-turn"
        case .overSpeeding:
            return "speeding"
        case .phoneHandling:
            return "phone-handling"
        case .phoneScreenInteraction:
            return "phone-screen-interaction"
        case .stopSignViolation:
            return "stop-sign-violation"
        default:
            return "none"
        }
    }

    static func driveType(of driveType: ZendriveDriveType) -> String {
        switch driveType {
        case .drive:
            return "drive"
        case .invalid:
            return "invalid"
        case .nonDriving:
            return "non-driving"
        @unknown default:
            return "none"
        }
    }

    static func driveType(of driveType: String) -> ZendriveDriveType {
        switch driveType {
        case "drive":
            return .drive
        case "invalid":
            return .invalid
        case "non-driving":
            return .nonDriving
        default:
            return .invalid
        }
    }

    static func vehicleType(of vehicleType: ZendriveVehicleType) -> String {
        switch vehicleType {
        case .car:
            return "car"
        case .motorcycle:
            return "motorcycle"
        default:
            return "none"
        }
    }

    static func vehicleType(of vehicleType: String) -> ZendriveVehicleType {
        switch vehicleType {
        case "car":
            return .car
        case "motorcycle":
            return .motorcycle
        default:
            return .unknown
        }
    }

    static func phonePosition(of phonePosition: ZendrivePhonePosition) -> String {
        switch phonePosition {
        case .mount:
            return "mount"
        default:
            return "unknown"
        }
    }

    static func phonePosition(of phonePosition: String) -> ZendrivePhonePosition {
        switch phonePosition {
        case "mount":
            return .mount
        default:
            return .unknown
        }
    }

    static func convertEventTypesDictionary(of dict: [AnyHashable: Any]) -> [String: Any?] {
        var resp: [String: Any?] = [:]
        for (key, val) in dict {
            if let type = key as? Int {
                // swiftlint:disable force_cast
                let eType = eventType(of: type)
                resp[eType] = val as! Bool
                // swiftlint:enable force_cast
            }
        }
        return resp
    }

    static func apiCallbackHandler(wtihResolver resolve: @escaping RCTPromiseResolveBlock) -> ZendriveApiCallHandler {
        return { (completed: Bool, error: Error?) in
            resolve(Utils.convertZendriveOperationResult(withCompleted: completed, error: error))
        }
    }

    static func insuranceApiCallbackHandler(wtihResolver resolve: @escaping RCTPromiseResolveBlock) -> ZendriveInsuranceApiCallHandler {
        return {(completed: Bool, error: Error?) in
            resolve(Utils.convertZendriveOperationResult(withCompleted: completed, error: error))
        }
    }

    static func driveDetectionMode(of mode: ZendriveDriveDetectionMode) -> String {
        switch mode {
        case .autoOFF:
            return "auto-off"
        case .autoON:
            return "auto-on"
        case .insurance:
            return "insurance"
        @unknown default:
            return "auto-off"
        }
    }

    static func region(of mode: ZendriveRegion) -> String {
        switch mode {
        case .EU:
            return "EU"
        case .US:
            return "US"
        @unknown default:
            return "US"
        }
    }

    static func severity(of severity: ZendriveEventSeverity) -> String {
        switch severity {
        case .high:
            return "high"
        case .low:
            return "low"
        default:
            return "not-available"
        }
    }

    static func userMode(of userMode: ZendriveUserMode) -> String {
        switch userMode {
        case .driver:
            return "drive"
        case .passenger:
            return "passenger"
        default:
            return "not-available"
        }
    }

    static func userMode(of userMode: String) -> ZendriveUserMode {
        switch userMode {
        case "driver":
            return .driver
        case "passenger":
            return .passenger
        default:
            return .unavailable
        }
    }

    static func driveInfoWarning(of warning: ZendriveTripWarningType) -> String {
        switch warning {
        case .unexpectedTripDuration:
            return "unexpected-trip-duration"
        @unknown default:
            return "none"
        }
    }

    static func accidentConfidence(of confidence: ZendriveAccidentConfidence) -> String {
        switch confidence {
        case .high:
            return "high"
        case .low:
            return "low"
        case .invalid:
            return "invalid"
        @unknown default:
            return "none"
        }
    }

    static func turnDirection(of turnDirection: ZendriveTurnDirection) -> String {
        switch turnDirection {
        case .left:
            return "left"
        case .right:
            return "right"
        case .notAvailable:
            return "not-available"
        @unknown default:
            return "not-available"
        }
    }

    static func starRatingOf(of rating: String) -> ZendriveStarRating {
        switch rating {
        case "5":
            return .five
        case "4":
            return .four
        case "3":
            return .three
        case "2":
            return .two
        case "1":
            return .one
        default:
            return .NA
        }
    }

    static func starRatingOf(of rating: ZendriveStarRating) -> String {
        switch rating {
        case .five:
            return "5"
        case .four:
            return "4"
        case .three:
            return "3"
        case .two:
            return "2"
        case .one:
            return "1"
        case .NA:
            return "not-available"
        @unknown default:
            return "not-available"
        }
    }

    static func toArray<T>(items: [T]) -> [RnJsonObject] where T: RnObject {
        var vals: [RnJsonObject] = []
        items.forEach { (item: T) in
            vals.append(item.toRNObject())
        }
        return vals
    }
}
