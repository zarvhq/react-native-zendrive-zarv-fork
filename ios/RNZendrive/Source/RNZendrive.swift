//
//  RNZendrive.swift
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 16/07/19.
//

import Foundation
import ZendriveSDK.Test
@objc(RCTZendrive)
class RNZendrive: NSObject {

    @objc
    func constantsToExport() -> [AnyHashable: Any]! {
        return [:]
    }

    @objc
    func isSDKSetup(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        resolve(Zendrive.isSDKSetup())
    }

    @objc
    func setup(
        _ configuration: NSDictionary,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        // swiftlint:disable force_cast
        let conf = configuration as! RnJsonObject
        let configuration = ZendriveConfiguration(withRnObject: conf)
        Zendrive.setup(with: configuration,
                       delegate: EventEmitter.sharedInstance,
                       completionHandler: Utils.apiCallbackHandler(wtihResolver: resolve))
        // swiftlint:enable force_cast
    }

    @objc
    func resume(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        Zendrive.resume(withDelegate: EventEmitter.sharedInstance, completionHandler: Utils.apiCallbackHandler(wtihResolver: resolve))
    }

    @objc
    func getActiveDriveInfo(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        let activeDriveInfo = Zendrive.activeDriveInfo()
        if let info = activeDriveInfo {
            resolve(info.toRNObject())
        } else {
            resolve(nil)
        }
    }

    @objc
    func getBuildVersion(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        resolve(Zendrive.buildVersion())
    }

    @objc
    func getEventSupportForDevice(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        resolve(Utils.convertEventTypesDictionary(of: Zendrive.getEventSupportForDevice()))
    }

    @objc
    func getZendriveSettings(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        let settings = Zendrive.getSettings()
        resolve(settings?.toRNObject())
    }

    @objc
    func isAccidentDetectionSupported(
        _ resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        resolve(Zendrive.isAccidentDetectionSupportedByDevice())
    }

    @objc
    func isValidInputParameter(
        _ input: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        resolve(Zendrive.isValidInputParameter(input))
    }

    @objc
    func setZendriveDriveDetectionMode(
        _ driveDetectionMode: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        switch driveDetectionMode {
        case "auto-on":
            Zendrive.setDriveDetectionMode(.autoON,
                                           completionHandler: Utils.apiCallbackHandler(wtihResolver: resolve))
        case "auto-off":
            Zendrive.setDriveDetectionMode(.autoOFF,
                                           completionHandler: Utils.apiCallbackHandler(wtihResolver: resolve))
        case "insurance":
            Zendrive.setDriveDetectionMode(.insurance,
                                           completionHandler: Utils.apiCallbackHandler(wtihResolver: resolve))
        default:
            resolve([
                "isSuccess": false,
                "errorCode": "zendrive-invalid-params",
                "errorMessage": "Detection mode should be one of (auto-on, auto-off, insurance)"
            ])
        }
    }

    @objc
    func startDrive(
        _ trackingId: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        var tiD: String?
        if trackingId != "" {
            tiD = trackingId
        }
        Zendrive.startManualDrive(tiD, completionHandler: Utils.apiCallbackHandler(wtihResolver: resolve))
    }

    @objc
    func startSession(
        _ sessionId: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        Zendrive.startSession(sessionId)
        resolve([
            "isSuccess": true
        ])
    }

    @objc
    func stopManualDrive(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        Zendrive.stopManualDrive(Utils.apiCallbackHandler(wtihResolver: resolve))
    }

    @objc
    func stopSession(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        Zendrive.stopSession()
        resolve([
            "isSuccess": true
        ])
    }

    @objc
    func teardown(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        Zendrive.teardown {
            resolve([
                "isSuccess": true
            ])
        }
    }

    @objc
    func triggerMockPotentialAccident(
        _ config: NSDictionary,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        // swiftlint:disable force_cast
        let mockConfig = ZendriveMockAccidentConfig()
        mockConfig.potentialAccidentConfidenceNumber = config["potentialCallbackConfidenceNumber"] as! Int32
        mockConfig.finalAccidentConfidenceNumber = config["finalCallbackConfidenceNumber"] as! Int32
        mockConfig.delayBetweenCallbacks = config["delayBetweenCallbacksSeconds"] as! Int32
        mockConfig.finalAccidentConfidence = Utils.accidentConfidence(of: config["finalCallbackConfidence"] as! String)
        mockConfig.potentialAccidentConfidence = Utils.accidentConfidence(of: config["potentialCallbackConfidence"] as! String)
        ZendriveTest.raiseMockAccident(using: mockConfig)
        resolve(["isSuccess": true])
        // swiftlint:enable force_cast
    }

    @objc
    func triggerMockAccident(
        _ confidence: String,
        resolver resolve: RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        let conf = Utils.accidentConfidence(of: confidence)
        ZendriveTest.raiseMockAccident(conf)
        resolve(["isSuccess": true])
    }

    @objc
    func uploadAllDebugDataAndLogs(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        Zendrive.uploadAllDebugDataAndLogs()
        resolve([
            "isSuccess": true
        ])
    }

    @objc
    func wipeOut(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        do {
            try Zendrive.wipeOut()
            resolve([
                "isSuccess": true
            ])
        } catch {
            resolve(Utils.convertZendriveOperationResult(withCompleted: false, error: error))
        }
    }

    @objc
    func getZendriveState(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        Zendrive.getState { (stateObj: ZendriveState?) in
            if let state = stateObj {
                resolve(state.toRNObject())
            } else {
                resolve(nil)
            }
        }
    }

    @objc
    func associateVehicle(
        _ vehicleInfo: NSDictionary,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        // swiftlint:disable force_cast
        let vechicleInfo = vehicleInfo as! RnJsonObject
        let vehicleInfo = ZendriveVehicleInfo(withRnObject: vechicleInfo)
        do {
            try ZendriveVehicleTagging.associateVehicle(vehicleInfo)
            resolve(["isSuccess": true])
        } catch {
            resolve(Utils.convertVehicleTaggingOperationResult(withCompleted: false, error: error))
        }

        // swiftlint:enable force_cast
    }

    @objc
    func dissociateVehicle(
        _ vehicleId: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        // swiftlint:disable force_cast
        do {
            try ZendriveVehicleTagging.dissociateVehicle(vehicleId)
            resolve(["isSuccess": true])
        } catch {
            resolve(Utils.convertVehicleTaggingOperationResult(withCompleted: false, error: error))
        }

        // swiftlint:enable force_cast
    }

    @objc
    func getAssociatedVehicles(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        if let associatedVehicles = ZendriveVehicleTagging.getAssociatedVehicles() {
            resolve(
                associatedVehicles.map { (associatedVehicle) -> RnJsonObject in
                    associatedVehicle.toRNObject()
                }
            )
        } else {
            resolve([])
        }
    }

    @objc
    func getBluetoothPairedDevices(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        if let activeDevice = ZendriveVehicleTagging.getActiveBluetoothDevice() {
            resolve([activeDevice.toRNObject()])
        } else {
            resolve([])
        }
    }

    @objc
    func getNearbyBeaconsWithRange(
        _ uuid: String,
        major: Int32,
        minor: Int32,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        ZendriveVehicleTagging.getNearbyBeacons(uuid, major: major, minor: minor) { (beaconInfo, err) in
            if err != nil {
                resolve(err?.localizedDescription ?? "Error")
            } else {
                resolve(
                    beaconInfo.map({ (zendriveBeaconInfo) -> RnJsonObject in
                        zendriveBeaconInfo.toRNObject()
                    })
                )
            }
        }
    }

    @objc
    func getNearbyBeacons(
        _ uuid: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        ZendriveVehicleTagging.getNearbyBeacons(uuid) { (beaconInfo, err) in
            if err != nil {
                resolve(err?.localizedDescription ?? "Error")
            } else {
                resolve(
                    beaconInfo.map({ (zendriveBeaconInfo) -> RnJsonObject in
                        zendriveBeaconInfo.toRNObject()
                    })
                )
            }
        }
    }

    @objc
    static func requiresMainQueueSetup() -> Bool {
        return false
    }

    @objc
    func pauseAutoDriveTracking(
        _ pausedTillTimestamp: Int64,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock) {

        let pausedTillTimestamp = pausedTillTimestamp
        do {
            try Zendrive.pauseAutoTracking(pausedTillTimestamp)
            resolve(["isSuccess": true])
        } catch {
            resolve(Utils.convertZendriveOperationResult(withCompleted: false, error: error))
        }
    }

    @objc
    func resumeAutoDriveTracking(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        do {
            try Zendrive.resumeAutoTracking()
            resolve([
                "isSuccess": true
            ])
        } catch {
            resolve(Utils.convertZendriveOperationResult(withCompleted: false, error: error))
        }
    }

    @objc
    func isAutoTripTrackingPaused(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        do {
            resolve(try Zendrive.isAutoTrackingPaused().boolValue)
        } catch {
            resolve(Utils.convertZendriveOperationResult(withCompleted: false, error: error))
        }
    }

    @objc
    func autoTrackingPausedReason(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        do {
            //          Using the same reason as defined for Android
            let pausedReason = try Zendrive.getPausedReason()
            switch pausedReason.pausedReason {
            case .user:
                resolve(["pausedReason": "USER"])
            case .businessHours:
                resolve(["pausedReason": "BUSINESS_HOURS"])
            case .notPaused:
                resolve(["pausedReason": "SDK_NOT_PAUSED"])
            @unknown default:
                resolve(["pausedReason": "unknown"])
            }
        } catch {
            resolve(Utils.convertZendriveOperationResult(withCompleted: false, error: error))
        }

    }

    @objc
    func refreshBusinessHours(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {

        Zendrive.refreshBusinessHours {(zendriveShiftDetail, error) in
            if zendriveShiftDetail != nil {
                print(zendriveShiftDetail?.shiftName ?? "No Shift Name")
                resolve(["message": "Fetched latest business hours successfully",
                         "name": zendriveShiftDetail?.shiftName ?? "Shift Name Not found",
                         "isSuccess": true])
            } else {
                resolve(Utils.convertZendriveOperationResult(withCompleted: false, error: error))
            }
        }
    }

    @objc
    func logSDKHealth(_ sdkHealthReason: String,
                      resolver resolve: @escaping RCTPromiseResolveBlock,
                      rejecter reject: RCTPromiseRejectBlock) {
        switch sdkHealthReason {
        case "unknown":
            Zendrive.logSDKHealth(.unknown) {(err) in resolve(err?.localizedDescription.description ?? "success")}
        case "silentPushNotification":
            Zendrive.logSDKHealth(.silentPushNotification) {(err) in resolve(err?.localizedDescription.description ?? "success")}
        case "backgroundProcessing":
            Zendrive.logSDKHealth(.backgroundProcessing) {(err) in resolve(err?.localizedDescription.description ?? "success")}
        default:
            resolve(["isSuccess": false,
                     "errorCode": "zendrive-invalid-params",
                     "errorMessage": "Sdk health reason should be one of (silentPushNotification, backgroundProcessing, unknown)"])
        }
    }
}
