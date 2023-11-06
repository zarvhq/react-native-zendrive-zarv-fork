import { PermissionRationale, ZendrivePermissions as IZendrivePermissions, ZendriveRequiredPermission } from './sdk/Zendrive';
export default class AndroidPermissionsHandler implements IZendrivePermissions {
    openSettings(): Promise<boolean>;
    check(kind: ZendriveRequiredPermission): Promise<boolean>;
    request(kind: ZendriveRequiredPermission, rationale?: PermissionRationale): Promise<boolean>;
    openAdvancedBatterySettings(): Promise<boolean>;
}
