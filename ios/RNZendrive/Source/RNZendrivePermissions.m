//
//  RNZendrivePermissions.m
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 06/08/19.
//

#import "React/RCTBridgeModule.h"
@interface RCT_EXTERN_MODULE(RCTZendrivePermissions, NSObject)
RCT_EXTERN_METHOD(
                  checkLocation: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  requestLocation: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  checkMotion: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  requestMotion: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  openSettings: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
@end
