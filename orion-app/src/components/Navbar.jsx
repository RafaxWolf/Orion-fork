import { Link, useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { use, useEffect, useState } from "react";
const NavBar = () => {

    const navigate = useNavigate();

    const token = localStorage.getItem('token');

    let initialUserId = null;

    if (token){
        try {
            initialUserId = jwtDecode(token).id;
        } catch (error){
            console.error("token invalido o expirado");
        }
    }


    const [userId, setUserId] = useState(initialUserId);
    const [usuario, setUsuario] = useState(null);
    
    
    



    useEffect(() => {



        const fetchDatosUsuario = async () => {
            if (!token || !userId) return;


            try {

                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/usuarios/profile/${userId}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });


                if (response.ok){
                    const data = await response.json();

                    
                    setUsuario(data);
                }

            } catch (error){
                console.error("hay problema con traer al perfil de usuario: ", error)
            }
        };

        fetchDatosUsuario();
        
    }, [userId, token]);

    const handleLogout = () => {
        localStorage.clear(); // Limpia todo el almacenamiento local (opcional, si quieres eliminar otros datos relacionados)
        navigate("/login"); // Redirige al usuario a la página de login

    };

    const handleProfile = () => {
        const token = localStorage.getItem("token");

        if (!token) {
            navigate("/login"); // Si no hay token, redirige al login 
            return;
        }

        navigate(`/profiles/${userId}`); // Redirige a la página de perfil del usuario
    };

    console.log("url avatar: ", usuario?.avatarUrl);
    
    const fotoPerfil = usuario?.avatarUrl
        ? `${import.meta.env.VITE_API_BASE_URL}${usuario.avatarUrl}`
        : `${import.meta.env.VITE_API_BASE_URL}/api/media/avatar/default_avatar.png`

    return(
    <nav className="navbar navbar-dark bg-dark mb-4">
        <div className="container">
            <Link className="navbar-brand" to="/home">
            Orion
            </Link>


            <div className="d-flex align-items-center gap-3">

                <button className="btn btn-outline-light" onClick={handleLogout}>
                    Logout
                </button>


                <Link to={`/profiles/${userId}`} className="d-flex align-items-center text-decoration-none">
                    <img
                    src={fotoPerfil}
                    className="rounded-circle border border-2 border-secondary"
                    style={{ 
                                width: '40px', 
                                height: '40px', 
                                objectFit: 'cover',
                                cursor: 'pointer' 
                            }}
                    />
                </Link>

         
            </div>
            
        </div>
    </nav>
    );
}

export default NavBar;
