export function getSession(key: string) {
    return window.sessionStorage.getItem(key);
}

export function setSession(key: string, value?: string) {
    if (value) window.sessionStorage.setItem(key, value);
    else window.sessionStorage.removeItem(key);
}

export function getAuthToken() {
    return getSession("AuthToken");
}

export function setAuthToken(token?: string) {
    setSession("AuthToken", token);
}