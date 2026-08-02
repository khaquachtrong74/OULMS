import axiosClient from './axiosClient';

const courseApi = {
    getAll: (params) => axiosClient.get('/courses', { params }),
    getDetail: (courseId) => axiosClient.get(`/courses/${courseId}`),
    getMyCourses: () => axiosClient.get('/secure/courses/my-learning')
};

export default courseApi;