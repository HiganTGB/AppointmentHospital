import { apiServer } from "../utils/api";
import { getAuthToken } from "./HttpApiService";

export type RoleRequest = {
    name?: string;
    description?: string;
}

export type RoleResponse = {
    id?: number;
    name?: string;
    description?: string;
}

export async function getRoles() {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "role", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as RoleResponse[]
        }
        console.warn("getRoles", await response.text());
        return null;
    } catch (error) {
        console.error("getRoles", error);
        return null;
    }
}

export async function getRoleById(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "role/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as RoleResponse;
        }
        console.warn("getRoleById", await response.text())
        return null;
    } catch (error) {
        console.error("getRoleById", error);
        return null;
    }
}

export async function createRole(request: RoleRequest) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "role", {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(request),
            method: "POST"
        });
        if (response.ok) {
            return await response.text();
        }
        console.warn("createRole", await response.text());
        return null;
    } catch (error) {
        console.error("createRole", error);
        return null;
    }
}
export async function checkDefaultRole(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "role/default/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        return response.ok;
    } catch (error) {
        console.error("checkDefaultRole", error);
        return false;
    }
}

export async function updateDefaultRole(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "role/default/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "PUT"
        });
        return response.ok;
    } catch (error) {
        console.error("updateDefaultRole", error);
        return false;
    }
}

export async function updateRole(request: RoleRequest, id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "role/" + id, {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(request),
            method: "PUT"
        });
        if (response.ok) {
            return await response.text();
        }
        console.warn("updateRole", await response.text());
        return null;
    } catch (error) {
        console.error("updateRole", error);
        return false;
    }
}

export async function deleteRole(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "role/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "DELETE"
        });
        if (response.ok) {
            return await response.text();
        }
        console.warn("deleteRole", await response.text());
        return null;
    } catch (error) {
        console.error("deleteRole", error);
        return false;
    }
}

export async function getPermissions(id?: number) {
    try {
        const token = getAuthToken();
        const requestUrl = apiServer + (
            id !== undefined ? "role/" + id + "/permissions" : "role/permissions"
        )
        const response = await fetch(requestUrl, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as string[];
        }
        console.warn("getPermissions", await response.text());
        return [];
    } catch (error) {
        console.error("getPermissions", error);
        return [];
    }
}

export async function changePermission(id: number, perm: string, granted: boolean) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "role/" + id + "/permission/" + perm + "?granted=" + granted, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "PUT"
        });
        return response.ok;
    } catch (error) {
        console.error("", error);
        return null;
    }
}