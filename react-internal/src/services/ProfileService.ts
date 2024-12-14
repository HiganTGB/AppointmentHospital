import { ProfileModel } from "../models/ProfileModel";
import { apiServer } from "../utils/api";
import { getAuthToken } from "./HttpApiService";

export type ProfileResponse = {
	id: number;
	patient?: number;
	full_name?: string;
	date_of_birth?: string; // dateonly
	gender?: "M" | "F";
}

export async function getProfiles() {
	try {
		const token = getAuthToken();
		const response = await fetch(apiServer + "profile", {
			headers: {
				...(token ? { "Authorization": "Bearer " + token } : {})
			},
			method: "GET"
		});
		if (response.ok) {
			return await response.json() as ProfileResponse[]
		}
		console.warn("getProfiles", await response.text());
		return null;
	} catch (error) {
		console.error("getProfiles", error);
		return null;
	}
}

export async function getProfileById(id: number) {
	try {
		const token = getAuthToken();
		const response = await fetch(apiServer + "profile/" + id, {
			headers: {
				...(token ? { "Authorization": "Bearer " + token } : {})
			},
			method: "GET"
		});
		if (response.ok) {
			return await response.json() as ProfileModel
		}
		console.warn("getProfileById", await response.text());
		return null;
	} catch (error) {
		console.error("getProfileById", error);
		return null;
	}
}

export async function addProfile(profile: ProfileModel) {
	try {
		const token = getAuthToken();
		const response = await fetch(apiServer + "profile", {
			headers: {
				"Content-Type": "application/json",
				...(token ? { "Authorization": "Bearer " + token } : {})
			},
			body: JSON.stringify(profile),
			method: "POST"
		});
		if (response.ok) {
			return await response.text()
		}
		console.warn("addProfile", await response.text());
		return null;
	} catch (error) {
		console.error("addProfile", error);
		return null;
	}
}

export async function updateProfile(profile: ProfileModel) {
	try {
		const token = getAuthToken();
		const response = await fetch(apiServer + "profile/" + profile.id, {
			headers: {
				"Content-Type": "application/json",
				...(token ? { "Authorization": "Bearer " + token } : {})
			},
			body: JSON.stringify(profile),
			method: "PUT"
		});
		if (response.ok) {
			return await response.text()
		}
		console.warn("updateProfile", await response.text());
		return null;
	} catch (error) {
		console.error("updateProfile", error);
		return null;
	}
}

export async function deleteProfile(id: number) {
	try {
		const token = getAuthToken();
		const response = await fetch(apiServer + "profile/" + id, {
			headers: {
				...(token ? { "Authorization": "Bearer " + token } : {})
			},
			method: "DELETE"
		});
		if (response.ok) {
			return await response.text()
		}
		console.warn("deleteProfile", await response.text());
		return null;
	} catch (error) {
		console.error("deleteProfile", error);
		return null;
	}
}