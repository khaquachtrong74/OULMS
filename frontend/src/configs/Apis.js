import axios from "axios"


export const endpoints = {
    baseURL: 'http://localhost:8080/backend/api',
    login:'/login',
    profile:'/secure/profile',

}

export default axios.create({
   baseURL: endpoints.baseURL,
    withCredentials: true

})
