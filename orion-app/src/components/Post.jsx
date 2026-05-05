
const Post = ({ autorId, contenido }) => {
    return(

        <div>
            <div className="card">
                <div className="card-header font-weight-bold">
                    @{autorId || 'Usuario Desconocido'} {/* Muestra el ID del autor o un mensaje si no está disponible */}
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