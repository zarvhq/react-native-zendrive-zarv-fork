//
//  EventEmitter.swift
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 24/07/19.
//

import Foundation
import ZendriveSDK
class EventEmitter: NSObject {
    public static var sharedInstance = EventEmitter()

    private static var eventEmitter: RCTZendriveEventEmitter!
    var isBridgeAvailable = false
    var postBridgeAvailableClosures: [() -> Void] = []

    func registerEventEmitter(eventEmitter: RCTZendriveEventEmitter) {
        EventEmitter.eventEmitter = eventEmitter
    }

    func send(name: String, body: RnJsonObject) {
        self.isBridgeAvailable = EventEmitter.eventEmitter.bridge.isValid && !EventEmitter.eventEmitter.bridge.isLoading
        if isBridgeAvailable {
            EventEmitter.eventEmitter.sendEvent(withName: name, body: body)
        } else {
            // remember the event
            postBridgeAvailableClosures.append {
                EventEmitter.eventEmitter.sendEvent(withName: name, body: body)
            }
            // add observer for notifications from javascript
            let notif = NotificationCenter.default
            notif.addObserver(
                self,
                selector: #selector(rnJavaScriptWillStartLoadingNotification),
                name: Notification.Name("RCTJavaScriptWillStartLoadingNotification"),
                object: nil
            )
            notif.addObserver(
                self,
                selector: #selector(rnJavaScriptDidLoadNotification),
                name: Notification.Name("RCTJavaScriptDidLoadNotification"),
                object: nil
            )
            notif.addObserver(
                self,
                selector: #selector(rnJavaScriptDidFailToLoadNotification),
                name: Notification.Name("RCTJavaScriptDidFailToLoadNotification"),
                object: nil
            )
            notif.addObserver(
                self,
                selector: #selector(rnBridgeWillReloadNotification),
                name: Notification.Name("RCTBridgeWillReloadNotification"),
                object: nil
            )
        }
    }

    lazy var allEvents: [String] = {
        var allEventNames: [String] = [
            "com.zendrive.onDriveStart",
            "com.zendrive.onDriveResume",
            "com.zendrive.onDriveEnd",
            "com.zendrive.onDriveAnalyzed",
            "com.zendrive.onPotentialAccident",
            "com.zendrive.onAccident",
            "com.zendrive.onSettingsChanged",
            "com.zendrive.onLocationApproved",
            "com.zendrive.onLocationDenied",
            "com.zendrive.onActivityApproved",
            "com.zendrive.onActivityDenied",
            "com.zendrive.onBootCompleted",
            "com.zendrive.onMyPackageReplaced"
        ]

        return allEventNames
    }()
}

extension EventEmitter: ZendriveDelegateProtocol {
    func settingsChanged(_ settings: ZendriveSettings) {
        send(name: "com.zendrive.onSettingsChanged", body: settings.toErrorsFoundRNObject())
    }
    func processLocationDenied() {
        send(name: "com.zendrive.onLocationDenied", body: [:])
    }
    func processLocationApproved() {
        send(name: "com.zendrive.onLocationApproved", body: [:])
    }
    func processStart(ofDrive startInfo: ZendriveDriveStartInfo) {
        send(name: "com.zendrive.onDriveStart", body: startInfo.toRNObject())
    }
    func processResume(ofDrive resumeInfo: ZendriveDriveResumeInfo) {
        send(name: "com.zendrive.onDriveResume", body: resumeInfo.toRNObject())
    }
    func processPotentialAccidentDetected(_ accidentInfo: ZendriveAccidentInfo) {
        send(name: "com.zendrive.onPotentialAccident", body: accidentInfo.toRNObject())
    }
    func processAccidentDetected(_ accidentInfo: ZendriveAccidentInfo) {
        send(name: "com.zendrive.onAccident", body: accidentInfo.toRNObject())
    }
    func processEnd(ofDrive estimatedDriveInfo: ZendriveEstimatedDriveInfo) {
        send(name: "com.zendrive.onDriveEnd", body: estimatedDriveInfo.toRNObject())
    }
    func processAnalysis(ofDrive analyzedDriveInfo: ZendriveAnalyzedDriveInfo) {
        send(name: "com.zendrive.onDriveAnalyzed", body: analyzedDriveInfo.toRNObject())
    }
    func processActivityApproved() {
        send(name: "com.zendrive.onActivityApproved", body: [:])
    }
    func processActivityDenied() {
        send(name: "com.zendrive.onActivityDenied", body: [:])
    }
}

extension EventEmitter {
    @objc func rnJavaScriptWillStartLoadingNotification(notification: Notification) {
        self.isBridgeAvailable = false
    }

    @objc func rnJavaScriptDidLoadNotification(notification: Notification) {
        self.isBridgeAvailable = true
        let copiedArr = self.postBridgeAvailableClosures
        copiedArr.forEach { (fn: @escaping () -> Void) in
            DispatchQueue.global(qos: .background).async {
                fn()
            }
        }
        self.postBridgeAvailableClosures.removeAll()
    }

    @objc func rnJavaScriptDidFailToLoadNotification(notification: Notification) {
        self.isBridgeAvailable = false
    }

    @objc func rnBridgeWillReloadNotification(notification: Notification) {
        self.isBridgeAvailable = false
    }
}
