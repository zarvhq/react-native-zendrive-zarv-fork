//
//  RNZendriveFeedback.swift
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 02/08/19.
//

import Foundation
import ZendriveSDK
@objc(RCTZendriveFeedback)
class RNZendriveFeedback: NSObject {

    @objc
    func constantsToExport() -> [AnyHashable: Any]! {
        return [:]
    }

    @objc
    func addDriveCategory(
        _ driveId: String,
        withCategory category: String,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        ZendriveFeedback.addDriveCategory(withDriveId: driveId, driveCategory: Utils.feedbackCategory(of: category))
        resolve(true)
    }

    // swiftlint:disable function_parameter_count
    @objc
    func addEventOccurrence(
        _ driveId: String,
        withEventTimestamp eventTimestamp: NSNumber,
        withEventType eventType: String,
        withOccurrence occurrence: Bool,
        resolver resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        ZendriveFeedback.addEventOccurrence(withDriveId: driveId, eventTimestamp: Int64(truncating: eventTimestamp),
                                            eventType: Utils.eventType(of: eventType), occurrence: occurrence)
        resolve(true)
    }
    // swiftlint:enable function_parameter_count

    @objc
    static func requiresMainQueueSetup() -> Bool {
        return false
    }
}
