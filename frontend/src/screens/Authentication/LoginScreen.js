import { Activity, useContext, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import { ACTION_TYPE, COOKIE_KEYS } from "../../configs/constants";
import { useNavigate, useSearchParams } from "react-router-dom";
import { MyUserContext } from "../../configs/Contexts";
import cookies from "react-cookies";
import { Alert, Button, Form } from "react-bootstrap";
import { HttpStatusCode } from "axios";

const LoginPage = () => {
    const userInfo = [{
        field: "email",
        label: "Email đăng nhập",
        type: "text"
    }, {
        field: "password",
        label: "Mật khẩu",
        type: "password"
    }];

    const [user, setUser] = useState({})
    const [err, setErr] = useState("");
    const [loading, setLoading] = useState(false);
    const [, dispatch] = useContext(MyUserContext);
    const [q] = useSearchParams();

    const nav = useNavigate();



    const validate = () => {

        return true;
    }

    const handleLogin = async (e) => {
        e.preventDefault();

        if (validate()) {
            try {
                setLoading(true);

                let res = await Apis.post(endpoints['login'], { ...user });
                if (res.status === HttpStatusCode.Ok) {
                    setTimeout(async () => {
                        let userProfile = await Apis.get(endpoints['profile']);
                        cookies.save(COOKIE_KEYS.USER, userProfile.data);

                        dispatch({
                            "type": ACTION_TYPE.LOGIN,
                            "payload": userProfile.data
                        })
                        let next = q.get('next')
                        if (next)
                            nav(next);
                        else
                            nav('/');

                    }, 500)
                }
            } catch (ex) {
                console.error(ex);
                setErr(ex);
            } finally {
                setLoading(false);
            }
        }
    }

    return (
        // <div className="min-h-screen bg-slate-50 flex flex-col">
        //     <div className="flex-grow flex items-center justify-center p-6">
        //         <div className="w-full max-w-md bg-white p-10 rounded-3xl shadow-sm border border-slate-100">
        //             <div className="text-center mb-10">
        //                 <h1 className="text-4xl font-bold text-slate-900 mb-2">Chào mừng trở lại</h1>
        //                 <p className="text-slate-500">Đăng nhập vào hệ thống OULMS</p>
        //             </div>
        //             <form className="space-y-6" onSubmit={handleLogin}>
        //                 <div>
        //                     <label className="block text-sm font-semibold text-slate-700 mb-2">Tên đăng nhập</label>
        //                     <input type="text" placeholder="Nhập tên đăng nhập của bạn" 
        //                     className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl outline-none focus:border-blue-700 transition-colors" />
        //                 </div>
        //                 <div>
        //                     <label className="block text-sm font-semibold text-slate-700 mb-2">Mật khẩu</label>
        //                     <input type="password" placeholder="Nhập mật khẩu" 
        //                     className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl outline-none focus:border-blue-700 transition-colors" />
        //                 </div>
        //                 <button className="w-full bg-blue-700 hover:bg-blue-800 text-white font-bold py-4 rounded-xl shadow-md transition-all active:scale-[0.98]">
        //                     Đăng nhập →
        //                 </button>
        //             </form>
        //         </div>
        //     </div>
        // </div>
        <>
            <h1 className="text-center text-success mt-1">ĐĂNG NHẬP NGƯỜI DÙNG</h1>

            {err && <Alert variant="danger">{err}</Alert>}

            <Form onSubmit={handleLogin}>
                {userInfo.map(u => <Form.Group key={u.field} className="mb-3" controlId={u.field}>
                    <Form.Label>{u.label}</Form.Label>
                    <Form.Control type={u.type} placeholder={u.label} value={user[u.field]} onChange={e => setUser({ ...user, [u.field]: e.target.value })} required />
                </Form.Group>)}

                <Form.Group className="mb-3">
                    {loading === true ? <Activity /> : <Button variant="success" type="submit">Đăng nhập</Button>}
                </Form.Group>
            </Form>
        </>
    )

};

export default LoginPage;