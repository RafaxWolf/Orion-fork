import { Link, useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
const NavBar = () => {

    const navigate = useNavigate();

    const handleLogout = () => {

        localStorage.removeItem("token"); // Elimina el token del almacenamiento local
        localStorage.clear(); // Limpia todo el almacenamiento local (opcional, si quieres eliminar otros datos relacionados)
        navigate("/login"); // Redirige al usuario a la página de login

    }

    const handleProfile = () => {
        const token = localStorage.getItem("token");

        if (!token) {
            navigate("/login"); // Si no hay token, redirige al login 
            return;
        }

        const decodedToken = jwtDecode(token); // Decodifica el token para obtener la información del usuario
        console.log("Token decodificado: ", decodedToken);
        const userId = decodedToken.id; // Asumiendo que el ID del usuario está en el campo "user_id" del token
        console.log("ID del usuario: ", userId);

        navigate(`/profiles/${userId}`); // Redirige a la página de perfil del usuario
    }


    return(
    <nav className="navbar navbar-dark bg-dark mb-4">
        <div className="container">
            <Link className="navbar-brand" to="/home">
            Orion
            </Link>


            <div className="d-flex">

                <button className="btn btn-outline-light" onClick={handleLogout}>
                    Logout
                </button>

                <button className="btn btn-primary" onClick={handleProfile}>
                    Perfil
                </button>

            </div>
            
        </div>
    </nav>
    );
}

export default NavBar;
