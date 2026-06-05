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
    documentDetail: (documentId) => `/documents/${documentId}`,
    documentComments: (documentId) => `/documents/${documentId}/comments`,
    addcomment: (documentId) => `/secure/documents/${documentId}/comments`,
    topic: '/topics',
    subject: '/subjects',
    level: '/levels',
    loginGoogle: '/auth/google',
    tag: '/tags'

}

export default axios.create({
   baseURL: endpoints.baseURL,
    withCredentials: true

})
