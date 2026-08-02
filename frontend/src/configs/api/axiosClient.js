import axios from "axios"


export default axios.create({
    baseURL: 'https://quietly-untrashed-darby.ngrok-free.dev/backend/api',
    withCredentials: true,
    headers: {
        'ngrok-skip-browser-warning': '69420'
    }
})
