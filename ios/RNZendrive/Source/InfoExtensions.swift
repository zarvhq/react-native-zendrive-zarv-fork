//
//  File.swift
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 23/07/19.
//

import Foundation
import ZendriveSDK

protocol RnObject {
    func toRNObject() -> RnJsonObject
}

extension ZendriveSettingsError: RnObject {
    private func getErrorType() -> String {
        switch self.errorType {
        case .activityPermissionNotAuthorized:
            return "activity-recognition-permission-denied"
        case .locationPermissionNotAuthorized:
            return "location-permission-denied"
        case .locationAccuracyAuthorizationReduced:
            return "location-accuracy-authorization-reduced"
        case .locationServiceOff:
            return "location-service-off"
        @unknown default:
            return ""
        }
    }

    func toRNObject() -> RnJsonObject {
        return [
            "type": getErrorType()
        ]
    }
}

extension ZendriveSettings: RnObject {

    private func rnErrors() -> [RnJsonObject] {
        return self.errors.map { (error) in
            error.toRNObject()
        }
    }

    func toRNObject() -> RnJsonObject {
        return [
            "errors": rnErrors(),
            "warnings": []
        ]
    }

    func toErrorsFoundRNObject() -> RnJsonObject {
        return [
            "errorsFound": self.errors.count > 0 ? true : false,
            "warningsFound": false
        ]
    }
}

extension ZendriveDriveStartInfo: RnObject {
    func toRNObject() -> RnJsonObject {
        let locationPoints = waypoints as? [ZendriveLocationPoint]
        return [
            "startLocation": locationPoints?.first?.toRNObject() ?? nil,
            "driveId": driveId,
            "insurancePeriod": Utils.insurancePeriod(of: insurancePeriod),
            "sessionId": sessionId ?? nil,
            "startTimeMillis": startTimestamp,
            "trackingId": trackingId
        ]
    }
}

extension ZendriveDriveResumeInfo: RnObject {
    func toRNObject() -> RnJsonObject {
        return [
            "driveGapEndTimestampMillis": driveGapEndTimestampMillis,
            "driveGapStartTimestampMillis": driveGapStartTimestampMillis,
            "driveId": driveId,
            "insurancePeriod": Utils.insurancePeriod(of: insurancePeriod),
            "sessionId": sessionId ?? nil,
            "startTimeMillis": startTimestamp,
            "trackingId": trackingId
        ]
    }
}

extension ZendriveExtrapolationDetails: RnObject {
    convenience init(withRnObject conf: RnJsonObject) {
        self.init()
        if let num = conf["extrapolatedDistance"] as? Double {
            extrapolatedDistance = num
        }
        if let obj = conf["estimatedStartLocation"] as? RnJsonObject {
            estimatedStartLocation = ZendriveLocationPoint(
                timestamp: obj["timestampMillis"] as? Int64 ?? 0,
                latitude: obj["latitude"] as? Double ?? 0.0,
                longitude: obj["longitude"] as? Double ?? 0.0
            )
        }
    }
    func toRNObject() -> RnJsonObject {
        return [
            "extrapolatedDistance": extrapolatedDistance,
            "estimatedStartLocation": estimatedStartLocation.toRNObject()
        ]
    }
}

extension ZendriveVehicleTaggingDetails: RnObject {
    convenience init(withRnObject conf: RnJsonObject) {
        self.init()
        if let str = conf["vehicleId"] as? String {
            vehicleId = str
        }
        if let beaconResult = conf["isTaggedByBeacon"] as? Bool {
            isTaggedByBeacon = beaconResult
        }
        if let btStereoResult = conf["isTaggedByBluetoothStereo"] as? Bool {
            isTaggedByBluetoothStereo = btStereoResult
        }
    }
    func toRNObject() -> RnJsonObject {
        return [
            "vehicleId": vehicleId,
            "isTaggedByBeacon": isTaggedByBeacon,
            "isTaggedByBluetoothStereo": isTaggedByBluetoothStereo
        ]
    }
}
extension ZendriveDriveInfo: RnObject {
    convenience init(withRnObject conf: RnJsonObject) {
        self.init()
        // swiftlint:disable force_cast
        if let num = conf["averageSpeed"] as? Double {
            averageSpeed = num
        }
        if let num = conf["distanceMeters"] as? Double {
            distance = num
        }
        if let str = conf["driveId"] as? String {
            driveId = str
        }
        if let str = conf["insurancePeriod"] as? String {
            insurancePeriod = Utils.insurancePeriod(of: str)
        }
        if let str = conf["sessionId"] as? String {
            sessionId = str
        }
        if let num = conf["startTimeMillis"] as? Int64 {
            startTimestamp = num
        }
        if let num = conf["endTimeMillis"] as? Int64 {
            endTimestamp = num
        }
        if let str = conf["trackingId"] as? String {
            trackingId = str
        }
        if let obj = conf["eventRatings"] as? RnJsonObject {
            eventRatings = ZendriveEventRatings.init(rnObj: obj)
        }
        if let str = conf["driveType"] as? String {
            driveType = Utils.driveType(of: str)
        }
        if let str = conf["vehicleType"] as? String {
            vehicleType = Utils.vehicleType(of: str)
        }
        if let str = conf["userMode"] as? String {
            userMode = Utils.userMode(of: str)
        }
        if let str = conf["phonePosition"] as? String {
            phonePosition = Utils.phonePosition(of: str)
        }
        if let num = conf["maxSpeed"] as? Double {
            maxSpeed = num
        }
        if let obj = conf["score"] as? RnJsonObject {
            score = ZendriveDriveScore(
                zendriveScore: (obj["zendriveScore"] as? Int32 ?? 0)
            )
        }
        if let obj = conf["extrapolationDetails"] as? RnJsonObject {
            extrapolationDetails = ZendriveExtrapolationDetails(withRnObject: obj)
        }

        if let obj = conf["vehicleTaggingDetails"] as? RnJsonObject {
            vehicleTaggingDetails = ZendriveVehicleTaggingDetails(withRnObject: obj)
        }
        // swiftlint:enable force_cast
    }

    func toRNObject() -> RnJsonObject {
        let eventsArr = events as? [ZendriveEvent]
        return [
            "averageSpeed": averageSpeed,
            "distanceMeters": distance,
            "driveId": driveId,
            "insurancePeriod": Utils.insurancePeriod(of: insurancePeriod),
            "sessionId": sessionId ?? nil,
            "startTimeMillis": startTimestamp,
            "endTimeMillis": endTimestamp,
            "trackingId": trackingId,
            "eventRatings": eventRatings.toRNObject(),
            "events": eventsArr != nil ? Utils.toArray(items: eventsArr!) : [],
            "driveType": Utils.driveType(of: driveType),
            "vehicleType": Utils.vehicleType(of: vehicleType),
            "maxSpeed": maxSpeed,
            "phonePosition": Utils.phonePosition(of: phonePosition),
            "score": score.toRNObject(),
            "warnings": tripWarnings.map { Utils.driveInfoWarning(of: $0.tripWarningType) },
            "extrapolationDetails": extrapolationDetails?.toRNObject(),
            "userMode": Utils.userMode(of: userMode),
            // swiftlint:disable force_cast
            "waypoints": Utils.toArray(
                items: waypoints.map {
                    ZendriveLocationPointWithTimeStamp(fromLocationPoint: $0 as! ZendriveLocationPoint) }
            )
            // swiftlint:enable force_cast
        ]
    }

}

extension ZendriveActiveDriveInfo: RnObject {
    func toRNObject() -> RnJsonObject {
        return [
            "currentLocation": nil,
            "currentSpeed": currentSpeed,
            "distanceMeters": distance,
            "driveId": driveId,
            "insurancePeriod": Utils.insurancePeriod(of: insurancePeriod),
            "sessionId": sessionId ?? nil,
            "startTimeMillis": startTimestamp,
            "trackingId": trackingId
        ]
    }
}

extension ZendriveConfiguration: RnObject {
    func toRNObject() -> RnJsonObject {
        return [
            "driverId": driverId,
            "sdkKey": applicationKey,
            "managesPermissions": managesLocationPermission,
            "attributes": driverAttributes?.asDictionary() ?? [:],
            "driveDetectionMode": Utils.driveDetectionMode(of: driveDetectionMode),
            "region": Utils.region(of: region),
            "implementsMultipleAccidentCallbacks": implementsMultipleAccidentCallbacks
        ]
    }
}

extension ZendriveState: RnObject {
    func toRNObject() -> RnJsonObject {
        return [
            "isDriveInProgress": isDriveInProgress,
            "isForegroundService": false,
            "zendriveConfiguration": zendriveConfiguration.toRNObject()
        ]
    }
}

extension ZendriveLocationPoint: RnObject {
    func toRNObject() -> RnJsonObject {
        return [
            "latitude": latitude,
            "longitude": longitude,
            "timestampMillis": timestamp
        ]
    }
}

class ZendriveLocationPointWithTimeStamp: RnObject {
    var locationPoint: ZendriveLocationPoint!
    init(fromLocationPoint point: ZendriveLocationPoint) {
        locationPoint = point
    }
    func toRNObject() -> RnJsonObject {
        return [
            "location": [
                "latitude": locationPoint.latitude,
                "longitude": locationPoint.longitude
            ],
            "timestampMillis": locationPoint.timestamp
        ]
    }
}

extension ZendriveEventRatings: RnObject {
    convenience init(rnObj: RnJsonObject) {
        self.init()
        if let str = rnObj["aggressiveAccelerationRating"] as? String {
            aggressiveAccelerationRating = Utils.starRatingOf(of: str)
        }
        if let str = rnObj["hardBrakeRating"] as? String {
            hardBrakeRating = Utils.starRatingOf(of: str)
        }
        if let str = rnObj["hardTurnRating"] as? String {
            hardTurnRating = Utils.starRatingOf(of: str)
        }
        if let str = rnObj["phoneHandlingRating"] as? String {
            phoneHandlingRating = Utils.starRatingOf(of: str)
        }
        if let str = rnObj["speedingRating"] as? String {
            speedingRating = Utils.starRatingOf(of: str)
        }
    }

    func toRNObject() -> RnJsonObject {
        return [
            "aggressiveAccelerationRating": Utils.starRatingOf(of: aggressiveAccelerationRating),
            "hardBrakeRating": Utils.starRatingOf(of: hardBrakeRating),
            "hardTurnRating": Utils.starRatingOf(of: hardTurnRating),
            "phoneHandlingRating": Utils.starRatingOf(of: phoneHandlingRating),
            "speedingRating": Utils.starRatingOf(of: speedingRating)
        ]
    }
}

extension ZendriveEvent: RnObject {
    func toRNObject() -> RnJsonObject {
        return [
            "endLocation": stopLocation.toRNObject(),
            "endTimestampMillis": endTime,
            "eventType": Utils.eventType(of: eventType.rawValue),
            "severity": Utils.severity(of: eventSeverity),
            "speedingInfo": speedingData?.toRNObject(),
            "startLocation": startLocation.toRNObject(),
            "startTimestampMillis": startTime,
            "turnDirection": Utils.turnDirection(of: turnDirection)
        ]
    }
}

extension ZendriveSpeedingData: RnObject {
    func toRNObject() -> RnJsonObject {
        return [
            "avgSpeed": userSpeedMPS,
            "maxSpeed": maxUserSpeedMPS,
            "speedLimit": speedLimitMPS
        ]
    }
}

extension ZendriveDriveScore: RnObject {
    func toRNObject() -> RnJsonObject {
        return [
            "zendriveScore": zendriveScore
        ]
    }
}

extension ZendriveAccidentInfo: RnObject {
    func toRNObject() -> RnJsonObject {
        return [
            "accidentId": accidentId,
            "confidence": Utils.accidentConfidence(of: confidence),
            "driveId": driveId,
            "location": accidentLocation.toRNObject(),
            "sessionId": sessionId,
            "timestampMillis": timestamp,
            "trackingId": trackingId
        ]
    }
}

extension ZendriveVehicleInfo: RnObject {
    convenience init(withRnObject conf: RnJsonObject) {
        self.init()
        if let str =  conf["vehicleId"] as? String {
            vehicleId = str
        }
        if let str =  conf["bluetoothAddress"] as? String {
            bluetoothId = str
        }
    }

    func toRNObject() -> RnJsonObject {
        return [
            "vehicleId": vehicleId,
            "bluetoothAddress": bluetoothId
        ]
    }
}

extension ZendriveConfiguration {
    convenience init(withRnObject conf: RnJsonObject) {
        self.init()
        if let str =  conf["driverId"] as? String {
            driverId = str
        }
        if let str =  conf["sdkKey"] as? String {
            applicationKey = str
        }
        if let mode = conf["driveDetectionMode"] as? String {
            switch mode {
            case "auto-on":
                driveDetectionMode = .autoON
            case "auto-off":
                driveDetectionMode = .autoOFF
            case "insurance":
                driveDetectionMode = .insurance
            default:
                break
            }
        }
        if let attrs = conf["attributes"] as? RnJsonObject {
            driverAttributes = ZendriveDriverAttributes(withRnObject: attrs)
        }
        if let manages =  conf["managesActivityPermission"] as? Bool {
            managesActivityPermission = manages
        }
        if let manages =  conf["managesLocationPermission"] as? Bool {
            managesLocationPermission = manages
        }
        if let regionStr = conf["region"] as? String {
            switch regionStr {
            case "US":
                region = .US
            case "EU":
                region = .EU
            default:
                break
            }
        }
        if let implsMultipleCallbacks = conf["implementsMultipleAccidentCallbacks"] as? Bool {
            implementsMultipleAccidentCallbacks = implsMultipleCallbacks
        }
    }
}

extension ZendriveDriverAttributes {
    convenience init(withRnObject attrs: RnJsonObject) {
        self.init()
        if let str =  attrs["alias"] as? String {
            setAlias(str)
        }

        if let str =  attrs["groupId"] as? String {
            setGroup(str)
        }

        if let mode = attrs["serviceLevel"] as? String {
            switch mode {
            case "default":
                setServiceLevel(.levelDefault)
            case "level-1":
                setServiceLevel(.level1)
            default:
                break
            }
        }

        if let mode = attrs["vehicleType"] as? String {
            switch mode {
            case "car":
                setVehicleType(.car)
            case "motorcycle":
                setVehicleType(.motorcycle)
            default:
                break
            }
        }

        for(key, value) in attrs {
            if key != "alias" && key != "groupId" && key != "serviceLevel" && key != "vehicleType" {
                if let str =  value as? String {
                    setCustomAttribute(str, forKey: key)
                }
            }
        }
    }
}

extension ZendriveBluetoothDevice: RnObject {
    func toRNObject() -> RnJsonObject {
        [
            "name": self.name,
            "address": self.identifier
        ]
    }
}

extension ZendriveScannedBeaconInfo: RnObject {
    convenience init(withRnObject conf: RnJsonObject) {
        self.init()
        // swiftlint:disable force_cast
        if let str = conf["uuid"] as? String {
            uuid = str
        }
        if let num = conf["major"] as? Int32 {
            major = NSNumber(value: num)
        }
        if let num = conf["minor"] as? Int32 {
            minor = NSNumber(value: num)
        }
        if let num = conf["rssi"] as? Int {
            rssi = num
        }
        if let num = conf["accuracy"] as? Double {
            accuracy = num
        }
        // swiftlint:enable force_cast
    }

    func toRNObject() -> RnJsonObject {
        [
            "uuid": self.uuid,
            "major": self.major,
            "minor": self.minor,
            "rssi": self.rssi,
            "accuracy": self.accuracy
        ]
    }
}
