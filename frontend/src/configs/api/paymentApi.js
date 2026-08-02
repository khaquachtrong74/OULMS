import axiosClient from './axiosClient';

const paymentApi = {
    createZaloPay: (paymentData) => axiosClient.post('/payment/zalopay/create', paymentData),
    checkSuccess: (transactionData) => axiosClient.post('/payment/success', transactionData)
};

export default paymentApi;