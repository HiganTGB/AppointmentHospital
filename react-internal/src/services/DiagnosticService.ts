import { DiagnosticServiceModel } from "../models/DiagnosticModel";
import { apiServer } from "../utils/api";
import { getAuthToken } from "./HttpApiService";


export type DiagnosticServiceResponse = {
    id?: number;
    name?: string;
    price?: number;
}

export type ExaminationDiagnosticResponse = {
    name?: string;
    price?: number;
    doctor?: number;
    diagnostic_service?: number;
    examination?: number;
}

export async function getDiagnosticServices() {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService", {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as DiagnosticServiceResponse[]
        }
        console.warn("getDiagnosticServices", await response.text());
        return null;
    } catch (error) {
        console.error("getDiagnosticServices", error);
        return null;
    }
}

export async function getDiagnosticServiceById(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as DiagnosticServiceResponse
        }
        console.warn("getDiagnosticServiceById", await response.text());
        return null;
    } catch (error) {
        console.error("getDiagnosticServiceById", error);
        return null;
    }
}

export async function getDiagnosticServiceModelById(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as DiagnosticServiceModel
        }
        console.warn("getDiagnosticServiceModelById", await response.text());
        return null;
    } catch (error) {
        console.error("getDiagnosticServiceModelById", error);
        return null;
    }
}

export async function addDiagnosticService(diagnostic: DiagnosticServiceModel) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService", {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(diagnostic),
            method: "POST"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("addDiagnosticService", await response.text());
        return null;
    } catch (error) {
        console.error("addDiagnosticService", error);
        return null;
    }
}

export async function updateDiagnosticService(diagnostic: DiagnosticServiceModel) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService/" + diagnostic.id, {
            headers: {
                "Content-Type": "application/json",
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            body: JSON.stringify(diagnostic),
            method: "PUT"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("addDiagnosticService", await response.text());
        return null;
    } catch (error) {
        console.error("addDiagnosticService", error);
        return null;
    }
}

export async function deleteDiagnosticService(id: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService/" + id, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "DELETE"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("deleteDiagnosticService", await response.text());
        return null;
    } catch (error) {
        console.error("deleteDiagnosticService", error);
        return null;
    }
}

export async function getExaminationDiagnostic(id: number, examination: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService/examination/" + id + "?examination=" + examination, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "GET"
        });
        if (response.ok) {
            return await response.json() as ExaminationDiagnosticResponse
        }
        console.warn("getExaminationDiagnostic", await response.text());
        return null;
    } catch (error) {
        console.error("getExaminationDiagnostic", error);
        return null;
    }
}

export async function addExaminationDiagnosticService(diagnostic: number, examination: number, doctor: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService/" + diagnostic + "/examination?examination=" + examination + "&doctor=" + doctor, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "POST"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("addExaminationDiagnosticService", await response.text());
        return null;
    } catch (error) {
        console.error("addExaminationDiagnosticService", error);
        return null;
    }
}

export async function updateExaminationDiagnosticService(diagnostic: number, examination: number, doctor: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService/" + diagnostic + "/examination?examination=" + examination + "&doctor=" + doctor, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "PUT"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("updateExaminationDiagnosticService", await response.text());
        return null;
    } catch (error) {
        console.error("updateExaminationDiagnosticService", error);
        return null;
    }
}

export async function deleteExaminationDiagnosticService(diagnostic: number, examination: number) {
    try {
        const token = getAuthToken();
        const response = await fetch(apiServer + "diagnosticService/" + diagnostic + "/examination?examination=" + examination, {
            headers: {
                ...(token ? { "Authorization": "Bearer " + token } : {})
            },
            method: "DELETE"
        });
        if (response.ok) {
            return await response.text()
        }
        console.warn("deleteExaminationDiagnosticService", await response.text());
        return null;
    } catch (error) {
        console.error("deleteExaminationDiagnosticService", error);
        return null;
    }
}