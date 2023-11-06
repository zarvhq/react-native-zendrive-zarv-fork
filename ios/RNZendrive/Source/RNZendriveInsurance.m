#import "React/RCTBridgeModule.h"
@interface RCT_EXTERN_MODULE(RCTZendriveInsurance, NSObject)
RCT_EXTERN_METHOD(
                  startDriveWithPeriod1: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  startDriveWithPeriod2: (NSString *)trackingId
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  startDriveWithPeriod3: (NSString *)trackingId
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  stopPeriod: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
@end

