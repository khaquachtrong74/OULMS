import axiosClient from './axiosClient';

const documentApi = {
    getAll: (params) => axiosClient.get('/documents', { params }),
    getDetail: (documentId) => axiosClient.get(`/documents/${documentId}`),
    getComments: (documentId) => axiosClient.get(`/documents/${documentId}/comments`),
    create: (data) => axiosClient.post('/secure/document', data),
    getSecureAll: (params) => axiosClient.get('/secure/documents', { params }),
    getSecureDetail: (documentId) => axiosClient.get(`/secure/documents/${documentId}`),
    deleteSecureDoc: (documentId) => axiosClient.delete(`/secure/documents/${documentId}`),
    updateSecure: (documentId, data) => axiosClient.post(`/secure/documents/${documentId}`, data),
    addComment: (documentId, data) => axiosClient.post(`/secure/documents/${documentId}/comments`, data),
    deleteComment: (commentId) => axiosClient.delete(`/secure/comments/${commentId}`)
};

export default documentApi;