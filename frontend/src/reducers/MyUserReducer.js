import cookies from 'react-cookies'
import { ACTION_TYPE, COOKIE_KEYS } from '../configs/constants';

const MyUserReducer =(current, action) =>{ 
    switch(action.type) {
        case ACTION_TYPE.LOGIN:
            return action.payload;
        case ACTION_TYPE.LOGOUT:
            cookies.remove(COOKIE_KEYS.TOKEN);
            cookies.remove(COOKIE_KEYS.USER);
            return null;
        default:
            return current;
    }
}
export default MyUserReducer;