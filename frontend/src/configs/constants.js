
export const ACTION_TYPE = Object.freeze({
    LOGIN: "LOGIN",
    LOGOUT: "LOGOUT"
})

export const COOKIE_KEYS = Object.freeze({ 
    TOKEN: 'token',
    USER: 'user',
})

export const GOOGLE_CLIENT_ID = process.env.REACT_APP_GOOGLE_CLIENT_ID;