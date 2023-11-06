//
//  RNZendrivePermissions.swift
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 06/08/19.
//

import Foundation
import CoreLocation
import CoreMotion

@objc(RCTZendrivePermissions)
class RCTZendrivePermissions: NSObject {
    let locationManager = CLLocationManager()
    let motionManager = CMMotionManager()
    var currentLocationResolver: RCTPromiseResolveBlock?
    var currentMotionResolver: RCTPromiseResolveBlock?

    @objc
    func constantsToExport() -> [AnyHashable: Any]! {
        return [:]
    }

    @objc
    func checkLocation(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        if CLLocationManager.locationServicesEnabled() {
            switch CLLocationManager.authorizationStatus() {
            case .authorizedAlways:
                resolve(true)
            default:
                resolve(false)
            }
        } else {
            resolve(false)
        }
    }

    @objc
    func requestLocation(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        print("Requesting for location")
        if CLLocationManager.locationServicesEnabled() {
            if self.currentLocationResolver != nil {
                // there is existing request in progress
                resolve(false)
                return
            }

            if CLLocationManager.authorizationStatus() == .authorizedAlways {
                resolve(true)
                return
            }

            switch CLLocationManager.authorizationStatus() {
            case .notDetermined:
                locationManager.delegate = self
                self.currentLocationResolver = resolve
                locationManager.requestWhenInUseAuthorization()
            case .authorizedAlways:
                resolve(true)
            default:
                resolve(false)
            }
        } else {
            resolve(false)
        }
    }

    @objc
    func checkMotion(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        if #available(iOS 11.0, *) {
            resolve(CMMotionActivityManager.authorizationStatus() == .authorized)
        } else {
            // Fallback on earlier versions
            resolve(true)
        }
    }

    @objc
    func requestMotion(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {
        if #available(iOS 11.0, *) {
            resolve(CMMotionActivityManager.authorizationStatus() == .authorized)
        } else {
            resolve(false)
        }
    }

    @objc
    func openSettings(
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: RCTPromiseRejectBlock
    ) {

        if let settingsUrl = URL(string: UIApplication.openSettingsURLString) {
            DispatchQueue.main.async {
                if UIApplication.shared.canOpenURL(settingsUrl) {
                    if #available(iOS 10.0, *) {
                        UIApplication.shared.open(settingsUrl)
                        resolve(true)
                    } else {
                        resolve(false)
                    }
                } else {
                    resolve(false)
                }
            }
            return
        }
        resolve(false)
    }

    @objc
    static func requiresMainQueueSetup() -> Bool {
        return false
    }
}

extension RCTZendrivePermissions: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        switch status {
        case .authorizedAlways:
            if let resolver = currentLocationResolver {
                resolver(true)
            }
        default:
            if let resolver = currentLocationResolver {
                resolver(false)
            }
            currentLocationResolver = nil
        }
        currentLocationResolver = nil
        locationManager.delegate = nil
    }
}
