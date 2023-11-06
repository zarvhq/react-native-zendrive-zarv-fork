import {
  Linking,
  NativeModules,
  PermissionsAndroid,
  Platform,
} from 'react-native';
import {
  PermissionRationale,
  ZendrivePermissions as IZendrivePermissions,
  ZendriveRequiredPermission,
} from './sdk/Zendrive';

const ANDROID_FINE_LOCATION = 'android.permission.ACCESS_FINE_LOCATION';
const ANDROID_BACKGROUND_LOCATION =
  'android.permission.ACCESS_BACKGROUND_LOCATION';
const ANDROID_ACTIVITY_RECOGNITION = 'android.permission.ACTIVITY_RECOGNITION';
const ZendrivePermissions = NativeModules.ZendrivePermissions;
export default class AndroidPermissionsHandler implements IZendrivePermissions {
  openSettings(): Promise<boolean> {
    (Linking as any).openSettings();
    return Promise.resolve(true);
  }
  async check(kind: ZendriveRequiredPermission): Promise<boolean> {
    let basicPermissionGranted = false;
    let fineLocation = false;
    let backgroundLocation = true;

    switch (kind) {
      case 'location':
        fineLocation = await PermissionsAndroid.check(ANDROID_FINE_LOCATION);
        if (Platform.OS === 'android' && Platform.Version >= 29) {
          console.log('Checking for background location');
          backgroundLocation = await PermissionsAndroid.check(
            // @ts-ignore
            ANDROID_BACKGROUND_LOCATION
          );
        }
        return Promise.resolve(fineLocation && backgroundLocation);
      case 'motion':
        if (Platform.OS === 'android' && Platform.Version >= 29) {
          basicPermissionGranted = await PermissionsAndroid.check(
            // @ts-ignore
            ANDROID_ACTIVITY_RECOGNITION
          );
          return Promise.resolve(basicPermissionGranted);
        }
        return Promise.resolve(true);
      case 'overlay':
        return ZendrivePermissions.checkOverlay();
      default:
        throw new Error('not-yet-implemented');
    }
  }
  async request(
    kind: ZendriveRequiredPermission,
    rationale?: PermissionRationale
  ): Promise<boolean> {
    if (Platform.OS === 'ios') {
      throw new Error('Not yet implemented');
    }
    let basicPermissionGranted = 'denied';
    let hasAllGrants = true;
    let getFineLocation = '';
    let getBackgroundLocation = '';
    switch (kind) {
      case 'location':
        getFineLocation = await PermissionsAndroid.request(
          ANDROID_FINE_LOCATION
        );
        getBackgroundLocation = await PermissionsAndroid.request(
          // @ts-ignore
          ANDROID_BACKGROUND_LOCATION
        );
        if (
          getFineLocation === 'granted' &&
          getBackgroundLocation === 'granted'
        ) {
          hasAllGrants = true;
        } else {
          hasAllGrants = false;
        }
        return Promise.resolve(hasAllGrants);
      case 'motion':
        if (Platform.OS === 'android' && Platform.Version >= 29) {
          basicPermissionGranted = await PermissionsAndroid.request(
            // @ts-ignore
            ANDROID_ACTIVITY_RECOGNITION,
            rationale
          );
          return Promise.resolve(basicPermissionGranted === 'granted');
        }
        return Promise.resolve(true);
      case 'overlay':
        return ZendrivePermissions.requestOverlay();
      default:
        throw new Error('not-yet-implemented');
    }
  }

  async openAdvancedBatterySettings(): Promise<boolean> {
    return ZendrivePermissions.openAdvancedBatterySettings();
  }
}
