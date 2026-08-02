import axiosClient from './axiosClient';

const categoryApi = {
    getTopics: () => axiosClient.get('/topics'),
    getSubjects: () => axiosClient.get('/subjects'),
    getLevels: () => axiosClient.get('/levels'),
    getTags: () => axiosClient.get('/tags')
};

export default categoryApi;