//
//  RNZendriveInsurance.swift
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 30/07/19.
//

import Foundation
import ZendriveSDK.Insurance
@objc(RCTZendriveInsurance)
class RNZendriveInsurance: NSObject {

    @objc
    func constantsToExport() -> [AnyHashable: Any]! {
        return [:]
    }

    @objc
    func startDriveWithPeriod1(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        ZendriveInsurance.startDrive(period1: Utils.apiCallbackHandler(wtihResolver: resolve))
    }

    @objc
    func startDriveWithPeriod2(
        _ trackingId: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        ZendriveInsurance.startDrive(withPeriod2: trackingId,
                                     completionHandler: Utils.apiCallbackHandler(wtihResolver: resolve))
    }

    @objc
    func startDriveWithPeriod3(
        _ trackingId: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        ZendriveInsurance.startDrive(withPeriod3: trackingId,
                                     completionHandler: Utils.apiCallbackHandler(wtihResolver: resolve))
    }

    @objc
    func stopPeriod(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        //        ZendriveInsurance.stopPeriod(Utils.apiCallbackHandler(wtihResolver: resolve))
        ZendriveInsurance.stopPeriod(Utils.insuranceApiCallbackHandler(wtihResolver: resolve))
    }

    @objc
    static func requiresMainQueueSetup() -> Bool {
        return false
    }
}
