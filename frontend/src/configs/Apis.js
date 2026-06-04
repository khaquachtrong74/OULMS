import axios from "axios"


export const endpoints = {
    baseURL: 'https://quietly-untrashed-darby.ngrok-free.dev/backend/api',
    login: '/login',
    register: '/users',
    logout: '/logout',
    profile: '/secure/profile',
    document: '/documents',
    secureDocument: (documentId) => `/secure/documents/${documentId}`,
    secureDocuments: '/secure/documents',
    topic: '/topics',
    subject: '/subjects',
    level: '/levels',
    loginGoogle: '/auth/google',
    tag: '/tags',
    courses: '/courses',
    courseDetail: (courseId) => `/courses/${courseId}`,

    payZaloPay: '/payment/zalopay/create',
    paymentSuccess: '/payment/success'
}

export default axios.create({
    baseURL: endpoints.baseURL,
    withCredentials: true,
    headers: {
        'ngrok-skip-browser-warning': '69420'
    }
})
