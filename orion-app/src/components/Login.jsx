import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const Login = () => {


    // se definen los estados donde React vigila cada cambio
    // esto sirve para mostrar en vivo mientras se escribe el campo
    // si hay algun error, se muestra el mensaje de error en la interfaz en vivo.
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);


    const navigate = useNavigate(); // Hook para redirigir a otra página después del login exitoso

    // Funcion para enviar el form
    const handleSubmit = async (e) => {
        e.preventDefault(); // Evita que la página se recargue al enviar el formulario
        setError(null); // Resetea el error antes de intentar iniciar sesión
    
        try{

            // ACA SE HACE LA LLAMADA A LA API PARA INICIARR SESION
            const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },

                // la request se hace con el username y password que el usuario ingreso en el formulario
                // es el body que se envia a la API (como en postman)
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                // Si la respuesta no es OK, se lanza un error
                throw new Error('Credenciales invalidas'); // manda mensaje de error en vivo.
            }


            // recibimos la respuesta de la API, que en este caso es un token JWT
            const data = await response.json(); // Se obtiene la respuesta de la API en formato JSON
    
            localStorage.setItem('token', data.token); // Guarda el token en el almacenamiento local del navegador
            localStorage.setItem('userId',data.userId)

            navigate('/home'); // Redirige a la página de inicio después del login exitoso
    
    } catch (err) {
            setError(err.message); // Si hay un error, se muestra el mensaje de error en la interfaz
        }

    };

    // se devuelve el formulario de login, con los campos de username y password, y un boton para enviar el formulario
    return (

        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <div className="card shadow">
                        <div className="card-header">
                            <h1>Orion</h1>
                        </div>
                        <div className="card-body">
                            <h2 className="text-center mb-4">Iniciar Sesión</h2>

                            {error && <div className="alert alert-danger">{error}</div>} {/* Muestra el mensaje de error si existe */}

                            <form onSubmit={handleSubmit}>


                                <div className="mb-3">
                                    <label className="form-label">Username</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        value={username}
                                        onChange={(e) => setUsername(e.target.value)}
                                        required
                                    />
                                    </div>

                                <div className="mb-3">
                                    <label className="form-label">Contraseña</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </div>

                                <Link to="/register" className="d-block mb-3">Regístrate aquí</Link>

                                <button type="submit" className="btn btn-primary w-100">Iniciar Sesión</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default Login;