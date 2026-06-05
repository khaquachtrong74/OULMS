import axios from "axios"


export const endpoints = {
    baseURL: 'http://localhost:8080/backend/api',
    login: '/login',
    register: '/users',
    logout: '/logout',
    profile: '/secure/profile',
    document: '/documents',
    secureDocument: (documentId) => `/secure/documents/${documentId}`,
    createDocument: '/secure/document',
    secureDocuments: '/secure/documents',
    myCourses: '/secure/courses/my-learning',
    documentDetail: (documentId) => `/documents/${documentId}`,
    documentComments: (documentId) => `/documents/${documentId}/comments`,
    addcomment: (documentId) => `/secure/documents/${documentId}/comments`,
    deleteComment: (documentId) => `/secure/comments/${documentId}`,
    topic: '/topics',
    subject: '/subjects',
    level: '/levels',
    loginGoogle: '/auth/google',
    tag: '/tags',
    createForumPost:'/secure/forumPosts',
    forumPosts: '/forumPosts',
    deleteForum: (forumPostId) => `/secure/forumPosts/${forumPostId}`,
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
