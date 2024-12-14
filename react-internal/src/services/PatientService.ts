import { PatientModel } from "../models/PatientModel";
import { apiServer } from "../utils/api";
import { getAuthToken } from "./HttpApiService";

export type PatientResponse = {
    id: number;
    full_name: string;
    username: string;
    email: string;
    phone: string;
    role: number;
}

export async function getPatients() {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "patient", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as PatientResponse[]
        }
        console.warn("getPatients", await response.text());
        return null;
    } catch (error) {
        console.error("getPatients", error);
        return null;
    }
}

export async function getPatientById(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "patient/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as PatientModel
        }
        console.warn("getPatientById", await response.text());
        return null;
    } catch (error) {
        console.error("getPatientById", error)
        return null;
    }
}

export async function addPatient(patient: PatientModel) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "patient", {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(patient),
            method: "POST"
        });
        if (response.ok) {
            return await response.json() as PatientModel
        }
        console.warn("addPatient", await response.text());
        return null;
    } catch (error) {
        console.error("addPatient", error)
        return null;
    }
}

export async function updatePatient(patient: PatientModel) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "patient/" + patient.id, {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(patient),
            method: "PUT"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("updatePatient", await response.text());
        return null;
    } catch (error) {
        console.error("updatePatient", error)
        return null;
    }
}

export async function deletePatient(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "patient/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "DELETE"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("deletePatient", await response.text());
        return null;
    } catch (error) {
        console.error("deletePatient", error)
        return null;
    }
}