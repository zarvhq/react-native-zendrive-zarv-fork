//
//  RNZendriveEventEmitter.swift
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 24/07/19.
//

import Foundation

@objc(RCTZendriveEventEmitter)
open class RCTZendriveEventEmitter: RCTEventEmitter {

    override init() {
        super.init()
        EventEmitter.sharedInstance.registerEventEmitter(eventEmitter: self)
    }

    @objc open override func supportedEvents() -> [String] {
        return EventEmitter.sharedInstance.allEvents
    }

    public override static func requiresMainQueueSetup() -> Bool {
        // initialize event emitter as early as possible
        return true
    }

}
