import { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// pasamos el id del usuario con el que 
// vamos a chatear
const Chat = ({ receptorId }) => {

    const [mensajes, setMensajes] = useState([]);
    const [nuevoMensaje, setNuevoMensaje] = useState("");
    const [conectado, setConectado] = useState(false);


    // useREf guarda la conexion para que no se pierda al re-renderizar

    const stompCLiente = useRef(null);
    const miUsuarioId = localStorage.getItem('userId');


    useEffect(() => {

        const token = localStorage.getItem('token');
        if (!token || !receptorId) return;

        
        // Stomp CLIENTE (phone)
        const client = new Client({
            // se usar la Ip del gateway y la ruta que se abre
            webSocketFactory: () => new SockJS(`${import.meta.env.VITE_API_BASE_URL}/ws-chat`),

            // le asignamos un header al recien conectarnos
            // para autenticar que somos un usuario registrado
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },


            onConnect: () => {
                console.log("connectado al Chat en vivo.");
                setConectado(true);


                // se suscribe a la linea privada para escuchar mensajes
                // Spring boot sabe que usuario es practicamente por solo el token
                client.subscribe('/user/queue/mensajes', (mensajeRecibido) => {
                    const mensaje = JSON.parse(mensajeRecibido.body);

                    if (mensaje.senderId.toString() === receptorId.toString()){
                        setMensajes((prev) => [...prev, mensaje]);
                    }
                });
            },
            onStompError: (frame) => {
                console.error('error de stomp ', frame.headers['message']);
            }
        });

        // se enciende el telefono
        client.activate();
        stompCLiente.current = client;


        // al cerrar la ventana se cuelga la llamada
        return () => {
            if (stompCLiente.current){
                stompCLiente.current.deactivate();
            }
        };

    },[receptorId])


    // Funcion para enviar cuando presionas el boton

    const handleEnviar = () => {
        if (nuevoMensaje.trim() !== "" && stompCLiente.current?.connected){
            const mensajeAEnviar = {
                senderId: miUsuarioId,
                receiverId: receptorId,
                contenido: nuevoMensaje
            };

            stompCLiente.current.publish({
                destination: '/app/chat.enviar',
                body: JSON.stringify(mensajeAEnviar)
            });


            setMensajes((prev) => [...prev, mensajeAEnviar]);
            setNuevoMensaje("");
        }
    };


    

    return (
        <div className="card mt-4 shadow-sm" style={{ height: '400px', display: 'flex', flexDirection: 'column' }}>
            <div className="card-header bg-primary text-white font-weight-bold">
                Chat {conectado ? '🟢 (En línea)' : '🔴 (Conectando...)'}
            </div>
            
            {/* Caja de mensajes (Se hace scroll automáticamente si hay muchos) */}
            <div className="card-body" style={{ flex: 1, overflowY: 'auto', backgroundColor: '#f8f9fa' }}>
                {mensajes.length === 0 ? (
                    <p className="text-center text-muted mt-5">Aún no hay mensajes. ¡Di hola!</p>
                ) : (
                    mensajes.map((msg, index) => (
                        <div 
                            key={index} 
                            className={`mb-2 p-2 rounded ${msg.senderId.toString() === miUsuarioId ? 'bg-primary text-white ms-auto' : 'bg-light border'}`}
                            style={{ width: 'fit-content', maxWidth: '75%' }}
                        >
                            {msg.contenido}
                        </div>
                    ))
                )}
            </div>

            {/* Caja para escribir */}
            <div className="card-footer d-flex gap-2">
                <input 
                    type="text" 
                    className="form-control" 
                    placeholder="Escribe un mensaje..." 
                    value={nuevoMensaje}
                    onChange={(e) => setNuevoMensaje(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handleEnviar()}
                    disabled={!conectado}
                />
                <button 
                    className="btn btn-primary" 
                    onClick={handleEnviar}
                    disabled={!conectado}
                >
                    Enviar
                </button>
            </div>
        </div>
    );
};

export default Chat;