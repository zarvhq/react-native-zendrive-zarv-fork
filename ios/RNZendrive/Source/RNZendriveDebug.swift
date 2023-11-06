//
//  RNZendriveDebug.swift
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 02/08/19.
//

import Foundation
import ZendriveSDK
@objc(RCTZendriveDebug)
class RNZendriveDebug: NSObject {
    var currentResolver: RCTPromiseResolveBlock?

    @objc
    func constantsToExport() -> [AnyHashable: Any]! {
        return [:]
    }

    @objc
    func uploadAllZendriveData(
        _ configuration: NSDictionary,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        // swiftlint:disable force_cast
        let conf = configuration as! RnJsonObject
        let config = ZendriveConfiguration(withRnObject: conf)
        currentResolver = resolve
        ZendriveDebug.uploadAllZendriveData(with: config, delegate: self)
        // swiftlint:enable force_cast
    }

    @objc
    func isZendriveSessionIdentifier(
        _ identifier: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        let success = ZendriveDebug.isZendriveSessionIdentifier(identifier)
        resolve(["isSuccess": success])
    }

    @objc
    func handleEventsForBackgroundURLSession(
        _ identifier: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        ZendriveDebug.handleEvents(forBackgroundURLSession: identifier, completionHandler: {
            resolve(["isSuccess": true])
        })
    }

    @objc
    static func requiresMainQueueSetup() -> Bool {
        return false
    }
}

extension RNZendriveDebug: ZendriveDebugDelegateProtocol {
    func zendriveDebugUploadFinished(_ status: ZendriveDebugUploadStatus) {
        if let resolve = self.currentResolver {
            switch status {
            case .failedInternal:
                resolve(["isSuccess": false, "errorMessage": "failed internal"])
            case .failedMissingApplicationKey:
                resolve(["isSuccess": false, "errorMessage": "failed missing application key"])
            case .failedMissingDriverId:
                resolve(["isSuccess": false, "errorMessage": "failed missing driver key"])
            case .success:
                resolve(["isSuccess": true])
            case .failedUnauthorizedRegion:
                resolve(["isSuccess": false, "errorMessage": "failed unauthorized region"])
            }
        }
        currentResolver = nil
    }
}
