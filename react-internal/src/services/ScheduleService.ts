import { apiServer } from "../utils/api";
import { getAuthToken } from "./HttpApiService";

export type SchedulerAllocation = {
    id?: number;
    at?: string;
}

export type SchedulerPart = {
    id?: number;
    start?: string;
    end?: string;
}

export async function getAll() {
    try
    {
        const token = getAuthToken();
        const response = await fetch(apiServer + "scheduler", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        
        if (response.ok) {
            return await response.json() as SchedulerPart[]
        }
        return null;
    }
    catch (error)
    {
        console.error("getAll", error);
        return null;
    }
}
        
export async function getScheduleById(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "scheduler/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        })
        if (response.ok) {
            return await response.json() as SchedulerPart;
        }
        console.warn("getScheduleById", await response.text());
        return null;
    } catch (error) {
        console.error("getScheduleById", error);
        return null;
    }
}