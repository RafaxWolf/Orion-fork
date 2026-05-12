import { useState, useEffect, use} from "react";
import { Link, useNavigate } from "react-router-dom";

const Post = ({ postId, autorId, contenido }) => {

    const [nombreUsuario, setNombreUsuario] = useState('Cargando...'); // Estado para almacenar el nombre de usuario del autor
    

    const [likesCount, setLikesCount] = useState(0); // Estado para almacenar el número de likes
    const [isLiked, setIsLiked] = useState(false);





    useEffect(() => {

        const fetchLikesCount = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;

            try {

                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/interacciones/post/${postId}/like/count`,{
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    setLikesCount(data); // Actualiza el estado con el número de likes
                } else {
                    console.error('Error al cargar el número de likes: ', response.status);
                }



                const statusResponse = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/interacciones/post/${postId}/like/status`,{
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (statusResponse.ok){
                    const statusData = await statusResponse.json();
                    setIsLiked(statusData);
                }

            } catch (error) {
                console.error('Error al cargar el número de likes: ', error);
        }

    };

    fetchLikesCount();


}, [postId]); // El efecto se ejecuta cada vez que el postId cambia


const handleLikePost = async () => {

    const token = localStorage.getItem('token');

    if (!token) return;

    try{

        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/interacciones/post/${postId}/like`,{
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });


        if (response.ok){

            const data = await response.json();
            setIsLiked(data);
            console.log(data);

            // para actualizar en vivo como sube o baja el contador de seguidores al dejar de seguir
                    setLikesCount((prev) => data ? prev + 1 : Math.max(0,prev - 1));
        }

    } catch (error){
        console.error('error encontrado: ',error)
    }

};



    useEffect(() => { // useEffect para cargar el nombre de usuario del autor cuando el componente se monta
        const fetchNombreUsuario = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;

            try {

                const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/usuarios/${autorId}`,{
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                })

                if (response.ok) {
                    const data = await response.json();

                    setNombreUsuario(data.username); // Actualiza el estado con el nombre de usuario del autor
            } else {
                setNombreUsuario('Usuario Eliminado'); // Si no se puede obtener el nombre de usuario, muestra un mensaje por defecto
            }
        } catch (error) {
            console.error("Error al cargar el nombre de usuario: ", error);
            setNombreUsuario('Desconocido'); // Si hay un error, muestra un mensaje de error
        }
    };



    if (autorId) {
        fetchNombreUsuario();
    }

}, [autorId]); // El efecto se ejecuta cada vez que el autorId cambia


    return(

        <div>
            <div className="card">
                <div className="card-header font-weight-bold">
                    <Link to={`/profiles/${autorId}`} className="text-decoration-none text-dark">
                    @{nombreUsuario} {/* Muestra el ID del autor o un mensaje si no está disponible */}
                    </Link>
                    </div>
                <div className="card-body">
                    <p className="card-text">
                        {contenido || 'Contenido no disponible.'} {/* Muestra el contenido del post o un mensaje si no está disponible */}
                    </p>
                    </div>
                <div className="card-footer">
                    <button
                    className={`btn ${isLiked ? 'btn-danger' : 'btn-primary'}`}
                    onClick={handleLikePost}
                    >
                        {isLiked ? '❤️ Quitar Like' : '🤍 Like'}
                    </button>
                    <span>  {likesCount} like</span>
                </div>
            </div>
        </div>

    );
}

export default Post;