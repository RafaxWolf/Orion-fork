import { useState, useEffect } from "react";
import Post from "./Post";


const Feed = () => {

    const [posts, setPosts] = useState([]);
    const [pagina, setPagina] = useState(0); // empezamos de la pagina 0 del feed
    const [hasMore, setHasMore] = useState(true); // booleano si hay mas posts para descargar?
    const [cargando, setCargando] = useState(false); // para evitar pedir la misma pagina dos veces seguidas




    const cargarPosts = async (numeroPagina) => {
        if (cargando) return; // si ya esta cargando no hacer llamadas duplicadas
        setCargando(true);

        try {
            const token = localStorage.getItem('token');

            const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/posts/feed?page=${numeroPagina}&size=10`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if(response.ok){
                const data = await response.json();

                // pegamos los posts nuevos ebajo de los que ya teniamos

                // el content es una propiedad del objeto que arroja el json del postman
                setPosts(prev => {

                    const postFiltrados = data.content.filter(
                        (nuevoPost) => !prev.some((postExistente) => postExistente.id === nuevoPost.id)
                    );
                    return [...prev, ...postFiltrados]
                });

                // si "last" es true,entonces significa que ya no hay mas paginas
                setHasMore(!data.last); // last lo arroja el endpoint (en el postman se ve mas especificado)
            }

        } catch (error){
            console.error("Error cargando el feed: ",error)
        } finally {
            setCargando(false);
        }

    }


    // efecto cuando se monta el componente
    useEffect(() => {
        cargarPosts(0);
    }, []);


    const handleScroll = (e) => {
        const { scrollTop, clientHeight, scrollHeight} = e.target;


        // Si se llega casi al final del contenedor (margen de 50px)
        if (scrollHeight - scrollTop <= clientHeight + 50) {
            if (hasMore && !cargando){
                const nuevaPagina = pagina + 1;
                setPagina(nuevaPagina);
                cargarPosts(nuevaPagina);
            }
        }

    };


    return(
        <div className="container mt-4">

            <h5 className="text-muted mb-3">Últimas publicaciones</h5>

            <div className="feed-container pb-4"
                 onScroll={handleScroll}
                 style={{ height: '700px', overflowY: 'auto', paddingRight: '10px' }}
            >

                {posts.map((post) => (
                    <div className="mt-3" key={post.id}>

                        <Post
                            postId={post.id}
                            autorId={post.userId}
                            contenido={post.content}
                        />
                    </div>
                ))}

                {cargando && <div className="text-center mt-4 text-primary fw-bold">Cargando mas posts...</div>}
                {!hasMore && posts.length > 0 && <div className="text-center mt-4 text-muted"> Estas al dia, no hay más publicaciones.</div>}
                
            </div>
        </div>
    );


}

export default Feed;
