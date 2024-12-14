import { apiServer } from "../utils/api";
import { setAuthToken } from "./HttpApiService";

type AuthRequest = {
	username: string;
	password: string;
}

export async function token(request: AuthRequest) {
	try {
		// Send POST request to API
		const body = new FormData();
		body.set("username", request.username);
		body.set("password", request.password);
		const response = await fetch(apiServer + "auth/token/doctor", {
			headers: {},
			body: body,
			method: "POST"
		});
		if (response.ok) {
			return (await response.json())["access_token"] as string;
		}
		console.warn("token", await response.text());
		return null;
	} catch (error) {
		console.warn("token", error);
		return null;
	}
}

export async function saveTokenToService(request: AuthRequest) {
	const r = await token(request);
	if (r) setAuthToken(r);
	return r;
}
