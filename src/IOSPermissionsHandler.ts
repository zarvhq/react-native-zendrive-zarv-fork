import {
  ZendrivePermissions as IZendrivePermissions,
  ZendriveRequiredPermission,
  PermissionRationale,
} from './sdk/Zendrive';
import { NativeModules } from 'react-native';
const ZendrivePermissions = NativeModules.ZendrivePermissions;

export default class IOSPermissionsHandler implements IZendrivePermissions {
  openSettings(): Promise<boolean> {
    return ZendrivePermissions.openSettings();
  }
  async check(kind: ZendriveRequiredPermission): Promise<boolean> {
    switch (kind) {
      case 'location':
        return ZendrivePermissions.checkLocation();
      case 'motion':
        return ZendrivePermissions.checkMotion();
      default:
        throw new Error(`Not available for iOS - kind: ${kind}`);
    }
  }
  async request(
    kind: ZendriveRequiredPermission,
    rationale?: PermissionRationale
  ): Promise<boolean> {
    switch (kind) {
      case 'location':
        return ZendrivePermissions.requestLocation();
      case 'motion':
        return ZendrivePermissions.requestMotion();
      default:
        throw new Error(
          `Not available for iOS - kind: ${kind} - rationale: ${rationale}`
        );
    }
  }

  async openAdvancedBatterySettings(): Promise<boolean> {
    throw new Error(`Not available for iOS`);
  }
}
