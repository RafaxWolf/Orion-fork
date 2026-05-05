import { Link, useNavigate } from "react-router-dom";
const NavBar = () => {

    const navigate = useNavigate();

    const handleLogout = () => {

        localStorage.removeItem("token"); // Elimina el token del almacenamiento local
        localStorage.clear(); // Limpia todo el almacenamiento local (opcional, si quieres eliminar otros datos relacionados)
        navigate("/login"); // Redirige al usuario a la página de login

    }


    return(
    <nav className="navbar navbar-dark bg-dark mb-4">
        <div className="container">
            <Link className="navbar-brand" to="/home">
            Orion App
            </Link>


            <div className="d-flex">

                <button className="btn btn-outline-light" onClick={handleLogout}>
                    Logout
                </button>

            </div>
        </div>
    </nav>
    );
}

export default NavBar;
