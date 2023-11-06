import {
  DeviceEventEmitter,
  NativeEventEmitter,
  NativeModules,
  Platform,
} from 'react-native';
import {
  ZendriveCallbackEventKind,
  ZendriveCallbackEventHandler,
  ZendriveCallbackEvent,
} from './sdk/Events';
type nativeEventName =
  | 'com.zendrive.onDriveStart'
  | 'com.zendrive.onDriveResume'
  | 'com.zendrive.onDriveEnd'
  | 'com.zendrive.onDriveAnalyzed'
  | 'com.zendrive.onAccident'
  | 'com.zendrive.onPotentialAccident'
  | 'com.zendrive.onSettingsChanged'
  | 'com.zendrive.onLocationApproved'
  | 'com.zendrive.onLocationDenied'
  | 'com.zendrive.onActivityApproved'
  | 'com.zendrive.onActivityDenied'
  | 'com.zendrive.onBootCompleted'
  | 'com.zendrive.onMyPackageReplaced';

const callbackEventMappings: Record<
  nativeEventName,
  ZendriveCallbackEventKind
> = {
  'com.zendrive.onDriveStart': ZendriveCallbackEventKind.ON_DRIVE_START,
  'com.zendrive.onDriveResume': ZendriveCallbackEventKind.ON_DRIVE_RESUME,
  'com.zendrive.onDriveEnd': ZendriveCallbackEventKind.ON_DRIVE_END,
  'com.zendrive.onDriveAnalyzed': ZendriveCallbackEventKind.ON_DRIVE_ANALYZED,
  'com.zendrive.onAccident': ZendriveCallbackEventKind.ON_ACCIDENT,
  'com.zendrive.onPotentialAccident':
    ZendriveCallbackEventKind.ON_POTENTIAL_ACCIDENT,
  'com.zendrive.onSettingsChanged':
    ZendriveCallbackEventKind.ON_SETTINGS_CHANGED,
  'com.zendrive.onLocationApproved':
    ZendriveCallbackEventKind.ON_LOCATION_APPROVED,
  'com.zendrive.onLocationDenied': ZendriveCallbackEventKind.ON_LOCATION_DENIED,
  'com.zendrive.onActivityApproved':
    ZendriveCallbackEventKind.ON_ACTIVITY_APPROVED,
  'com.zendrive.onActivityDenied': ZendriveCallbackEventKind.ON_ACTIVITY_DENIED,
  'com.zendrive.onBootCompleted': ZendriveCallbackEventKind.ON_BOOT_COMPLETED,
  'com.zendrive.onMyPackageReplaced':
    ZendriveCallbackEventKind.ON_MY_PACKAGE_REPLACED,
};
type nativeListener = (event: any) => void;
export default class RNEventHandler {
  emitter =
    Platform.OS === 'ios'
      ? new NativeEventEmitter(NativeModules.ZendriveEventEmitter)
      : DeviceEventEmitter;
  listeners: Map<nativeEventName, nativeListener> = new Map();

  register(callbackHandler: ZendriveCallbackEventHandler) {
    for (const eventName in callbackEventMappings) {
      if (callbackEventMappings.hasOwnProperty(eventName)) {
        const sdkEvent = callbackEventMappings[eventName as nativeEventName];
        const listener = (event: any) => {
          callbackHandler({
            kind: sdkEvent,
            event,
          } as ZendriveCallbackEvent);
        };
        this.listeners.set(eventName as nativeEventName, listener);
        this.emitter.addListener(eventName, listener);
      }
    }
  }

  unregister() {
    for (const eventName in callbackEventMappings) {
      if (callbackEventMappings.hasOwnProperty(eventName)) {
        const listener = this.listeners.get(eventName as nativeEventName);
        if (listener) {
          this.emitter.removeAllListeners(eventName);
        }
        this.listeners.delete(eventName as nativeEventName);
      }
    }
  }
}
