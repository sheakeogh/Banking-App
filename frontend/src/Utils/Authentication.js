export const saveTokens = (accessToken, refreshToken) => {
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
}

export const getAccessToken = () => {
    return localStorage.getItem("accessToken");
}

export const removeTokens = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
}

export const isAuthenticated = () => {
    return !!getAccessToken();
}