import { DoctorModel } from "../models/DoctorModel";
import { apiServer } from "../utils/api";
import { getAuthToken } from "./HttpApiService";

export async function getDoctors() {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "doctor", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as DoctorModel[]
        }
        console.warn("getDoctors", await response.text());
        return null;
    } catch (error) {
        console.error("getDoctors", error);
        return null;
    }
}

export async function getDoctorById(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "doctor/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as DoctorModel
        }
        console.warn("getDoctorById", await response.text());
        return null;
    } catch (error) {
        console.error("getDoctorById", error);
        return null;
    }
}

export async function getCurrentDoctor() {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "doctor/current", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as DoctorModel
        }
        console.warn("getCurrentDoctor", await response.text());
        return null;
    } catch (error) {
        console.error("getCurrentDoctor", error);
        return null;
    }
}

export async function addDoctor(doctor: DoctorModel) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "doctor", {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(doctor),
            method: "POST"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("addDoctor", await response.text());
        return null;
    } catch (error) {
        console.error("addDoctor", error);
        return null;
    }
}

export async function updateDoctor(doctor: DoctorModel) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "doctor/" + doctor.id, {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(doctor),
            method: "PUT"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("updateDoctor", await response.text());
        return null;
    } catch (error) {
        console.error("updateDoctor", error);
        return null;
    }
}

export async function deleteDoctor(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "doctor/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "DELETE"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("deleteDoctor", await response.text());
        return null;
    } catch (error) {
        console.error("deleteDoctor", error);
        return null;
    }
}