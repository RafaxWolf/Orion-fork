import { use, useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

import NavBar from "./Navbar";
import Post from "./Post";
import Chat from "./Chat";
import ProfileFeed from "./ProfileFeed";

const Profile = () => {

    // se consigue la id del url
    const { id } = useParams(); // Obtiene el ID del usuario desde la URL

    const token = localStorage.getItem('token');
    let miUsuarioId = null;

    if (token){

        try {
            miUsuarioId = jwtDecode(token).id;
        } catch (error){
            console.error("Error al leer token");
        }

    }

    // arroja true o false si coincide el token con el perfil de usuario que esta cargando
    const esMiPerfil = String(id) === String(miUsuarioId);


    const [imagenLocal, setImagenLocal] = useState(null); // Estado para almacenar la imagen localmente 
    const [perfil, setPerfil] = useState(null); // Estado para almacenar los datos del perfil
    const [cargando, setCargando] = useState(true); // Estado para indicar si los datos están cargando
    const [error, setError] = useState(false); // Estado para almacenar errores
    const [posts, setPosts] = useState([]); // Estado para almacenar los posts del usuario
    const [seguidores, setSeguidores] = useState(0); // Estado para almacenar el número de seguidores
    const [siguiendo, setSiguiendo] = useState(0);
    const [postCount, setPostCount] = useState(0);

    const [isFollowing, setIsFollowing] = useState(false);


    // para subir avatar

    const [subiendo, setSubiendo] = useState(false);
    const fileInputRef = useRef(null);

    const handleSubirAvatar = async (e) => {

        const file = e.target.files[0];
        if (!file) return;


        setSubiendo(true);

        const formData = new FormData();
        formData.append('file',file)

        try {
            // POSTEAR FORM DATA (media)
            const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/media/avatar/upload`,{
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                    // tiene que ser ContentType: 'multipart/form-data' pero ahora mismo el navegador lo pone automaticamente
                },
                body: formData // pasamos la imagen
            });

            if (response.ok){

                const dataMedia = await response.json();

                const nuevaUrl = dataMedia.urlAcceso;


                const responseUsuario = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/usuarios/${id}/avatar`,{
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({avatarUrl: nuevaUrl})
                });

                if (responseUsuario.ok){
                    // avtualizar la pantalla sin recargar pagina
                    setPerfil(prev => ({...prev, avatarUrl: nuevaUrl}));
                    console.log('foto cambiada con exito');
                } else {
                    console.error("error en el mediaService: ", response.status)
                }

            }

        } catch (error){
            console.log("error al subir el avatar.", error);
        } finally {
            setSubiendo(false);
        }

    };


    useEffect(() => { // useEffect para cargar los datos del perfil cuando el componente se monta


        // const fetchPosts = async () => {
        //     const token = localStorage.getItem('token');
        //     if (!token) return;


        //     try{

        //         const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/posts/usuario/${id}`,{
        //             method: 'GET',
        //             headers: {
        //                 'Authorization' : `Bearer ${token}`,
        //                 'Content-Type': 'application/json'
        //             }
        //         });


        //         if (response.ok){
        //             const data = await response.json();
        //             console.log("Posts del usuario: ", data);
        //             setPosts(data); // Actualiza el estado con los posts del usuario

        //         }

        //     } catch (err) {
        //         console.error("Error al cargar los posts del usuario: ", err);
        //     }

        // };

        // fetchPosts(); // Llama a la función para cargar los posts del usuario

        const fetchPerfil = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;


            try{

                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/usuarios/${id}`,{
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



        const fetchSeguidores = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;

            try {
                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/interacciones/usuarios/${id}/seguidores/count`,{
                    method: 'GET',
                    headers:{
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok){
                    const data = await response.json();
                    console.log("Número de seguidores: ", data);
                    setSeguidores(data); // Actualiza el estado con el número de seguidores
                } else {
                    console.error("Error al cargar el número de seguidores: ", response.status);
                }

            } catch (err) {
                console.error("Error al cargar el número de seguidores: ", err);
            }

        };


        const fetchPostCount = async () => {
            const token = localStorage.getItem('token');
            if(!token) return;

            try {

                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/posts/user/${id}/count`,{
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });


                if (response.ok){

                    const data = await response.json();

                    setPostCount(data);
                }

            } catch (error){
                console.error('no se pudo traer los numero de post', error)
            }

        };

        fetchPostCount();

        fetchSeguidores(); // Llama a la función para cargar el número de seguidores del usuario


        const fetchIsFollowing = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;

            try {

                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/interacciones/usuarios/${id}/siguiendo`,{
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok){
                    const data = await response.json();
                    if(data){
                        console.log('si sigues a este usuario')
                    } else {
                        console.log('no sigues a este usuario')
                    }
                    setIsFollowing(data);
                }
            } catch (error){
                console.error('error en el sistema: ', error);
            }

        };

        fetchIsFollowing();


        const fetchSiguiendoCount = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;


            try {

                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/interacciones/usuarios/${id}/seguidos/count`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok){
                    const data = await response.json();
                    console.log('siguiendo a ',data);
                    setSiguiendo(data);
                }


            } catch (error){
                console.error("error al obtener numero de seguidos del usuario: ", error)
            }

            
        };
        fetchSiguiendoCount();
        },    [id]); // El efecto se ejecuta cada vez que el ID cambia


        const handleFollowToggle = async () => {

            const token = localStorage.getItem('token');

            if (!token) return;

            try{

                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/interacciones/usuarios/${id}/follow`,{
                    method: 'POST',
                    headers:{
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok){
                    const isNowFollowing = await response.json();

                    setIsFollowing(isNowFollowing)

                    // para actualizar en vivo como sube o baja el contador de seguidores al dejar de seguir
                    setSeguidores((prev) => isNowFollowing ? prev + 1 : prev - 1);
                }


            } catch (error){
                console.error('error:', error)
            }
        };

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

                            <input
                                type="file"
                                accept="image/*"
                                ref={fileInputRef}
                                style={{display:'none'}}
                                onChange={handleSubirAvatar}
                            />

                            <img src={`${import.meta.env.VITE_API_BASE_URL}${perfil.avatarUrl}`}
                            className="rounded-circle mb-3 border border-3 border-primary shadow-sm"
                            style={{width: '100px', height: '100px', objectFit:'cover'}}
                            />
                            <h3 className="card-title fw-bold">@{perfil.username}</h3>
                            <p className="card-text text-muted">{perfil.biografia || 'Sin biografía'}</p>
                            <p className="card-text text-muted">Ubicacion: {perfil.ubicacion || 'No especificada'}</p>


                            {String(localStorage.getItem('userId')) === String(id) && (
                                <div className="mb-3">
                                    <button
                                        className="btn btn-outline-primary btn-sm"
                                        onClick={() => fileInputRef.current.click()} // al hacer click se activa el input oculto de arriba
                                        disabled={subiendo}
                                    >
                                        {subiendo ? 'Subiendo...' : 'Cambiar foto de perfil'}
                                    </button>
                                </div>
                            )}
                            

                            <div className="row">
                                <div className="col">
                                    <h6 className="text-muted mb-0">Posts</h6>
                                    <p className="fw-bold">{postCount}</p>
                                </div>
                                <div className="col">
                                    <h6 className="text-muted mb-0">Seguidores</h6>
                                    <p className="fw-bold">{seguidores || 0}</p>
                                </div>
                                <div className="col">
                                    <h6 className="text-muted mb-0">Siguiendo</h6>
                                    <p className="fw-bold">{siguiendo || 0}</p>
                                </div>

                                <div>
                                    <button 
                                className={`btn ${isFollowing ? 'btn-outline-danger' : 'btn-primary'} mb-4 px-4`}
                                onClick={handleFollowToggle}
                                style={{ borderRadius: '20px' }}
                            >
                                {isFollowing ? 'Dejar de seguir' : 'Seguir'}
                            </button>
                                </div>
                                
                            </div>


                            
                            
                        </div>


                            


                    </div>


                    <ProfileFeed
                        userId={id}
                        username={perfil.username}
                    />


                    {localStorage.getItem('userId') !== id && (
                        <Chat receptorId={id} />
                    )}
                </div>

            </div>

        );
};


export default Profile;