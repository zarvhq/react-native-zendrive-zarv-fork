//
//  RNZendriveDebug.m
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 02/08/19.
//

#import "React/RCTBridgeModule.h"
@interface RCT_EXTERN_MODULE(RCTZendriveDebug, NSObject)
RCT_EXTERN_METHOD(
                  uploadAllZendriveData: (NSDictionary *)configuration
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  isZendriveSessionIdentifier: (NSString *)identifier
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  handleEventsForBackgroundURLSession: (NSString *)identifier
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
@end
