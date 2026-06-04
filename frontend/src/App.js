import {useReducer} from "react";
import MyUserReducer from './reducers/MyUserReducer';
import cookies from "react-cookies";
import { MyUserContext } from './configs/Contexts';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Footer from "./components/Footer";
import Header from "./components/Header";
import HomeScreen from "./screens/Home/HomeScreen";
import LoginPage from "./screens/Authentication/LoginScreen";
import './App.css';
import DocumentScreen from "./screens/DocumentManager/DocumentScreen";
import ProfileScreen from "./screens/Authentication/ProfileScreen";
import RegisterScreen from "./screens/Authentication/RegisterScreen";
import ForumScreen from "./screens/Community/ForumScreen";
import ForumCreate from "./screens/Community/ForumCreate";
import ExploreCourseScreen from "./screens/Explore/ExploreCourseScreen";
import CourseDetailedScreen from "./screens/Courses/CourseDetailedScreen";

function App() {
  const [user, dispatch] = useReducer(MyUserReducer, cookies.load('user') || null);
  return (
    <MyUserContext.Provider value={[user, dispatch]}>
        <BrowserRouter>
            <Header />
                <Routes>
                    <Route path="/" element={<HomeScreen />}></Route>    
                    <Route path="/login" element={<LoginPage />}></Route>    
                    <Route path="/register" element={<RegisterScreen />}></Route>
                    <Route path="/documents-manage" element={<DocumentScreen />}></Route>
                    <Route path="/explore" element={<ExploreCourseScreen />}></Route>    
                    <Route path="/profile" element={<ProfileScreen />}></Route>    
                    <Route path="/forum" element={<ForumScreen />}></Route>    
                    <Route path="/forum-create" element={<ForumCreate />}></Route>    
                    {/* <Route path="/courses" element={<CourseScreen />}></Route>     */}
                    <Route path="/courses/:courseId" element={<CourseDetailedScreen />} />
                </Routes>
            
            <Footer/>
        </BrowserRouter>
    </MyUserContext.Provider>
  );

}

export default App;
