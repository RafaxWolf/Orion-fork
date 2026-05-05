import { useEffect, useState } from "react";
import NavBar from "./Navbar";
import Post from "./Post";
const Home = () => {


    const [contentPost, setContentPost] = useState(''); // Estado para almacenar el contenido de un post
    const [error, setError] = useState(null); // Estado para almacenar errores
    const [success, setSuccess] = useState(false); // Estado para almacenar mensajes de éxito


    const [publicaciones, SetPublicaciones] = useState([]);


    const fetchPosts = async () => {
        const token = localStorage.getItem('token');
        if (!token) return;

        try{
            // get a la api de posts
            const response = await fetch('http://localhost:8000/api/posts',{
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok){

                // se convierte la respuesta de la api
                // en un objeto json con todas las publicaciones
                // recuperadas
                const data = await response.json();

                console.log("Publicaciones recuperadas: ", data);

                SetPublicaciones(data.reverse());
                
            }
        } catch (error){
            console.error("Error al cargar el feed: ", error)
        }
    };

    // useEffect ejecuta la funcion fetchPosts() solo
    // una vez al cargar la pagina
    useEffect(() => {
        fetchPosts();
    },[]); // arreglo [] vacio para decirle que ejecute solo al montar el componente

    


    const handlePost = async (e) => {
        e.preventDefault(); // Evita que la página se recargue al enviar el formulario
        setError(null); // Resetea el error antes de intentar crear un post
        setSuccess(false); // Resetea el éxito antes de intentar crear un post

        const token = localStorage.getItem('token'); // Obtiene el token del almacenamiento local

        if (!token){
            setError('No estas autenticado. Por favor inicia sesión de nuevo.'); // Si no hay token, muestra un mensaje de error
            return;
        }

        try {
        
            const response = await fetch('http://localhost:8000/api/posts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // Agrega el token en el encabezado de autorización
                },
                body: JSON.stringify({ content: contentPost, mediaUrl: null}) // Envía el contenido del post en el cuerpo de la solicitud
            });

            console.log(response.json());
        
            if (!response.ok) {
                throw new Error('Error al crear el post'); // Si la respuesta no es OK, se lanza un error
            }


            // si todo sale bien entonces

            setContentPost(''); // Limpia el campo de contenido del post
            setSuccess(true); // Muestra un mensaje de éxito

            // en el futuro se recargara el listado de posts para mostrar el nuevo post creado
            fetchPosts();
        } catch (err) {
            setError(err.message); // Si hay un error, se muestra el mensaje de error en la interfaz
        }
    };



    return (

        <div>
            <NavBar />

            <div className="container mt-5">
                <div className="row justify-content-center">
                    <div className="col-md-8">

                        {/* Formulario para crear un nuevo post */}
                        <div className="card shadow-sm mb-4">
                            <div className="card-body">
                                <form onSubmit={handlePost}>

                                    <div className="mb-3">
                                        <textarea
                                        className="form-control"
                                        rows="3"
                                        placeholder="¿Qué estás pensando?"
                                        value={contentPost}
                                        onChange={(e) => setContentPost(e.target.value)} // Actualiza el estado del contenido del post al escribir
                                        required
                                        >
                                        </textarea>
                                    </div>

                                    <div className="d-flex justify-content-end">
                                        <button type="submit" className="btn btn-primary">Publicar</button>
                                    </div>

                                </form>

                                {error && <div className="alert alert-danger mt-3">{error}</div>} {/* Muestra el mensaje de error si existe */}
                                {success && <div className="alert alert-success mt-3">Post creado exitosamente.</div>} {/* Muestra el mensaje de éxito si existe */}
                            </div>
                        </div>


                        <h5 className="text-muted mb-3">Ultimas publicaciones</h5>

                        {publicaciones.length === 0 ?(
                            <p className="text-center text-muted mt-5">
                                No hay publicaciones.
                            </p>
                        ) : (
                            publicaciones.map((post) => (
                                <Post
                                key={post.id}

                                autorId={post.userId}
                                contenido={post.content}
                                
                                />
                            ))
                        )}




                        <div>
                            <Post/>
                        </div>

                    </div>
                </div>


            </div>
        </div>

    );
}

export default Home;