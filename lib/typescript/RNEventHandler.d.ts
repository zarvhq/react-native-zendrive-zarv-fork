import { NativeEventEmitter } from 'react-native';
import { ZendriveCallbackEventHandler } from './sdk/Events';
type nativeEventName = 'com.zendrive.onDriveStart' | 'com.zendrive.onDriveResume' | 'com.zendrive.onDriveEnd' | 'com.zendrive.onDriveAnalyzed' | 'com.zendrive.onAccident' | 'com.zendrive.onPotentialAccident' | 'com.zendrive.onSettingsChanged' | 'com.zendrive.onLocationApproved' | 'com.zendrive.onLocationDenied' | 'com.zendrive.onActivityApproved' | 'com.zendrive.onActivityDenied' | 'com.zendrive.onBootCompleted' | 'com.zendrive.onMyPackageReplaced';
type nativeListener = (event: any) => void;
export default class RNEventHandler {
    emitter: NativeEventEmitter;
    listeners: Map<nativeEventName, nativeListener>;
    register(callbackHandler: ZendriveCallbackEventHandler): void;
    unregister(): void;
}
export {};
