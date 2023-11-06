//
//  RNZendriveFeedback.m
//  RNZendrive
//
//  Created by Madhusudhan Sambojhu on 02/08/19.
//

#import "React/RCTBridgeModule.h"
@interface RCT_EXTERN_MODULE(RCTZendriveFeedback, NSObject)
RCT_EXTERN_METHOD(
                  addDriveCategory: (NSString *)driveId
                  withCategory:(NSString *)category
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
RCT_EXTERN_METHOD(
                  addEventOccurrence: (NSString *)driveId
                  withEventTimestamp:(nonnull NSNumber *)eventTimestamp
                  withEventType: (NSString *)eventType
                  withOccurrence: (BOOL *) occurrence
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )
@end
