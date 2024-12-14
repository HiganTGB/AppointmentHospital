import { ExaminationModel } from "../models/ExaminationModel";
import { apiServer } from "../utils/api";
import { getAuthToken } from "./HttpApiService";

export type PrescriptionRequest = {
    description?: string;
}

export type PrescriptionResponse = {
    examination?: number;
    description?: string;
}

export async function getExaminations() {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "examination", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as ExaminationModel[]
        }
        console.warn("getExaminations", await response.text());
        return null;
    } catch (error) {
        console.error("getExaminations", error);
        return null;
    }
}

export async function getExaminationById(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "examination/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as ExaminationModel
        }
        console.warn("getExaminationById", await response.text());
        return null;
    } catch (error) {
        console.error("getExaminationById", error);
        return null;
    }
}

export async function existExaminationByAppointment(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "examination/appointment/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        return response.ok;
    } catch (error) {
        console.error("existExaminationByAppointment", error);
        return false;
    }
}

export async function addExamination(appointmentId: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "examination/" + appointmentId, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "POST"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("addExamination", await response.text());
        return null;
    } catch (error) {
        console.error("addExamination", error);
        return null;
    }
}

export async function updateExamination(exam: ExaminationModel) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "examination/" + exam.id, {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(exam),
            method: "PUT"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("updateExamination", await response.text());
        return null;
    } catch (error) {
        console.error("updateExamination", error);
        return null;
    }
}

export async function deleteExamination(id: number) {
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
        console.warn("deleteExamination", await response.text());
        return null;
    } catch (error) {
        console.error("deleteExamination", error);
        return null;
    }
}

export async function getPrescription(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "examination/" + id + "/perscription", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as PrescriptionResponse
        }
        console.warn("getPrescription", await response.text());
        return null;
    } catch (error) {
        console.error("getPrescription", error);
        return null;
    }
}

export async function addPrescription(examId: number, prescription: PrescriptionRequest) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "examination/" + examId + "/perscription", {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(prescription),
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

export async function deletePerscription(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "examination/" + id + "/perscription", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "DELETE"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("deleteExamination", await response.text());
        return null;
    } catch (error) {
        console.error("deleteExamination", error);
        return null;
    }
}