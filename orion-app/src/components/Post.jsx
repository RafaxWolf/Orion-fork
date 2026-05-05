import { useState, useEffect, use} from "react";

const Post = ({ autorId, contenido }) => {

    const [nombreUsuario, setNombreUsuario] = useState('Cargando...'); // Estado para almacenar el nombre de usuario del autor

    useEffect(() => { // useEffect para cargar el nombre de usuario del autor cuando el componente se monta
        const fetchNombreUsuario = async () => {
            const token = localStorage.getItem('token');
            if (!token) return;

            try {

                const response = await fetch(`http://localhost:8000/api/usuarios/${autorId}`,{
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
                    @{nombreUsuario} {/* Muestra el ID del autor o un mensaje si no está disponible */}
                    </div>
                <div className="card-body">
                    <p className="card-text">
                        {contenido || 'Contenido no disponible.'} {/* Muestra el contenido del post o un mensaje si no está disponible */}
                    </p>
                    </div>
                <div className="card-footer">
                    <button className="btn btn-primary">Like</button>
                </div>
            </div>
        </div>

    );
}

export default Post;