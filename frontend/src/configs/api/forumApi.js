import axiosClient from './axiosClient';

const forumApi = {
    getAll: (params) => axiosClient.get('/forumPosts', { params }),
    create: (data) => axiosClient.post('/secure/forumPosts', data),
    delete: (forumPostId) => axiosClient.delete(`/secure/forumPosts/${forumPostId}`)
};

export default forumApi;