import axios from "axios"


export const endpoints = {
    baseURL: 'http://localhost:8080/backend/api',
    login:'/login',
    register:'/users',
    logout:'/logout',
    profile:'/secure/profile',
    document: '/documents',
    secureDocuments: (uploadBy)=> `/secure/documents/${uploadBy}`, 
    secureDocument: '/secure/document',   
    topic: '/topics',
    subject: '/subjects',
    level: '/levels',
    loginGoogle: '/auth/google'

}

export default axios.create({
   baseURL: endpoints.baseURL,
    withCredentials: true

})
