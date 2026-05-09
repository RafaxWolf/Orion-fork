import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./components/Login";
import Home from "./components/Home";
import Profile from "./components/Profile";
import Register from "./components/Register";
import Test from "./components/Test";
import ChatGlobalPage from "./components/ChatGlobalPage";
const PrivateRoute = ({ children }) => {

  const token = localStorage.getItem("token");

  return token ? children : <Navigate to="/login" />; {/*Si el token existe, renderiza los hijos (la ruta protegida), de lo contrario redirige a /login*/}

};


function App() {
  return(

    <BrowserRouter>
        <Routes>

          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />



          <Route 
            path="/home"
            element={
              <PrivateRoute>
                <Home />
              </PrivateRoute>
            }
          />

            <Route
            path="/profiles/:id"
            element={
              <PrivateRoute>
                <Profile />
              </PrivateRoute>
            }>
            </Route>


            <Route
            path="/tests"
            element={
              <PrivateRoute>
                <Test />
              </PrivateRoute>
            }>
            </Route>

            <Route
            path="/chat-global"
            element={
              <PrivateRoute>
                <ChatGlobalPage/>
              </PrivateRoute>
            }
            >

            </Route>
            

          <Route path="*" element={<Navigate to="/login" />} /> {/*Redirige cualquier ruta no definida a /login*/}  
  

        </Routes>
    </BrowserRouter>

  );

}

export default App;