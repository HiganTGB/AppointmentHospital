export type RoleModel = {
    id?: number;
    name?: string; // [Required(ErrorMessage = "Tên không được để trống")]
    description?: string;
    isDefault?: boolean;
    permissions: { [permission: string]: boolean; }
}