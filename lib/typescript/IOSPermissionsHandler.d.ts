import { ZendrivePermissions as IZendrivePermissions, ZendriveRequiredPermission, PermissionRationale } from './sdk/Zendrive';
export default class IOSPermissionsHandler implements IZendrivePermissions {
    openSettings(): Promise<boolean>;
    check(kind: ZendriveRequiredPermission): Promise<boolean>;
    request(kind: ZendriveRequiredPermission, rationale?: PermissionRationale): Promise<boolean>;
    openAdvancedBatterySettings(): Promise<boolean>;
}
