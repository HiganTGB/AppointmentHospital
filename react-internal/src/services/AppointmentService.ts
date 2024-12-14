import { AppointmentModel } from "../models/AppointmentModel";
import { AppointmentResponseModel } from "../models/AppointmentResponseModel";
import { apiServer } from "../utils/api";
import { getAuthToken } from "./HttpApiService";

export type AppointmentResponse = {
    id?: number;
    at?: string;
    number?: number;
    state?: number;
    profile?: number;
    doctor?: number;
}

export async function getAppointmentsWithBody() {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "appointment", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as AppointmentResponseModel[]
        }
        console.warn("getAppointmentsWithBody", await response.text());
        return null;
    } catch (error) {
        console.error("getAppointmentsWithBody", error);
        return null;
    }
}

export async function getAppointments() {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "appointment", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as AppointmentResponse[]
        }
        console.warn("getExaminations", await response.text());
        return null;
    } catch (error) {
        console.error("getExaminations", error);
        return null;
    }
}


export async function getAppointmentById(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "appointment/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as AppointmentModel
        }
        console.warn("getAppointmentById", await response.text());
        return null;
    } catch (error) {
        console.error("getAppointmentById", error);
        return null;
    }
}

export async function getAppointmentResponseById(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "appointment/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as AppointmentResponseModel
        }
        console.warn("getAppointmentResponseById", await response.text());
        return null;
    } catch (error) {
        console.error("getAppointmentResponseById", error);
        return null;
    }
}

export async function addAppointment(appointment: AppointmentModel) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "appointment", {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(appointment),
            method: "POST"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("addPrescription", await response.text());
        return null;
    } catch (error) {
        console.error("addPrescription", error);
        return null;
    }
}

export async function updateAppointment(appointmentId: number, profileId: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "appointment/" + appointmentId + "?profileId=" + profileId, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "PUT"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("updateAppointment", await response.text());
        return null;
    } catch (error) {
        console.error("updateAppointment", error);
        return null;
    }
}

export async function changeAppointmentStatus(appointmentId: number, status: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "appointment/" + appointmentId + "/status?statusId=" + status, {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify({ status }),
            method: "PUT"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("updateAppointment", await response.text());
        return null;
    } catch (error) {
        console.error("updateAppointment", error);
        return null;
    }
}

export async function deleteAppointment(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "examination/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "DELETE"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("deleteAppointment", await response.text());
        return null;
    } catch (error) {
        console.error("deleteAppointment", error);
        return null;
    }
}