import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import NavBar from "./Navbar";


const Profile = () => {

    // se consigue la id del url
    const { id } = useParams(); // Obtiene el ID del usuario desde la URL

    const [imagenLocal, setImagenLocal] = useState(null); // Estado para almacenar la imagen localmente 
    const [perfil, setPerfil] = useState(null); // Estado para almacenar los datos del perfil
    const [cargando, setCargando] = useState(true); // Estado para indicar si los datos están cargando
    const [error, setError] = useState(false); // Estado para almacenar errores


    useEffect(() => { // useEffect para cargar los datos del perfil cuando el componente se monta




        const fetchPerfil = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;


            try{

                const response = await fetch(`http://localhost:8000/api/usuarios/${id}`,{
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });


                if (response.ok){
                    const data = await response.json();
                    setPerfil(data); // Actualiza el estado con los datos del perfil
                } else {
                    setError(true); // Si la respuesta no es ok, se establece el estado de error
                }
            } catch (err) {
                console.error("Error al cargar el perfil: ", err);
                setError(true); // Si hay un error en la solicitud, se establece el estado de error
            } finally {
                setCargando(false); // Indica que la carga ha terminado, ya sea con éxito o con error
            }
        };

        fetchPerfil(); // Llama a la función para cargar los datos del perfil


        },    [id]); // El efecto se ejecuta cada vez que el ID cambia

        if (cargando) {
            return (
                <div>
                    <NavBar />
                    <div className="container mt-5 text-center">
                        <div className="spinner-border text-primary" role="status">
                            <span className="visually-hidden">Cargando...</span>
                        </div>
                    </div>
                </div>
            );
        }

        if (error || !perfil) {
            return (

                <div>
                    <NavBar />
                    <div className="container mt-5">
                        <div className="alert alert-secondary text-center shadow-sm" role="alert">
                            <h4 className="alert-heading mt-2">Error</h4>
                            <p>No se pudo cargar el perfil.</p>
                        </div>
                    </div>
                </div>

            );
        }


        return(

            <div>
                <NavBar />

                <div className="container mt-5">
                    <div className="card shadow-sm">

                        <div className="card-body text-center py-5">
                            <img src={`http://localhost:8000${perfil.avatarUrl}`}
                            className="rounded-circle mb-3 border border-3 border-primary shadow-sm"
                            style={{width: '100px', height: '100px', objectFit:'cover'}}
                            />
                            <h3 className="card-title fw-bold">@{perfil.username}</h3>
                            <p className="card-text text-muted">{perfil.bio || 'Sin biografía'}</p>
                            <p className="card-text text-muted">Ubicacion: {perfil.ubicacion || 'No especificada'}</p>
                        </div>
                            


                    </div>

                </div>

            </div>

        );
};


export default Profile;