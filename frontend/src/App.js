import {useReducer} from "react";
import MyUserReducer from './reducers/MyUserReducer';
import cookies from "react-cookies";
import { MyUserContext } from './configs/Contexts';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Footer from "./components/Footer";
import Header from "./components/Header";
import { Container } from "react-bootstrap";
import HomeScreen from "./screens/Home/HomeScreen";
import LoginPage from "./screens/Authentication/LoginScreen";
import './App.css';

function App() {
  const [user, dispatch] = useReducer(MyUserReducer, cookies.load('user') || null);
  return (
    <MyUserContext.Provider value={[user, dispatch]}>
        <BrowserRouter>
            <Header />
                <Routes>
                    <Route path="/" element={<HomeScreen />}></Route>    
                    <Route path="/login" element={<LoginPage />}></Route>    
                    
                </Routes>
            
            <Footer/>
        </BrowserRouter>
    </MyUserContext.Provider>
  );

}

export default App;
