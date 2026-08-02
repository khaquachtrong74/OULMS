import axiosClient from './axiosClient';

const authApi = {
    login: (credentials) => axiosClient.post('/login', credentials),
    register: (userData) => axiosClient.post('/users', userData),
    logout: () => axiosClient.post('/logout'),
    getProfile: () => axiosClient.get('/secure/profile'),
    patchProfile:(form) => axiosClient.patch('/secure/profile',form),
    loginGoogle: (tokenData) => axiosClient.post('/auth/google', tokenData)
};

export default authApi;