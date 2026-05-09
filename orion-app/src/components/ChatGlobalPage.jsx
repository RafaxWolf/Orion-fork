import NavBar from "./Navbar";
import {useState, useEffect, useRef} from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from "sockjs-client";


const ChatGlobalPage = () => {

    const [mensajes, setMensajes] = useState([]);
    const [nuevoMensaje, setNuevoMensaje] = useState("");
    const [conectado, setConectado] = useState(false);
    const stompClient = useRef(null);
    const miUsuarioId = localStorage.getItem('userId');


    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) return;

        const client = new Client({
            webSocketFactory: () => new SockJS(`${import.meta.env.VITE_API_BASE_URL}/ws-chat`),
            connectHeaders: { Authorization: `Bearer ${token}` },
            onConnect: () => {
                setConectado(true);

                
                client.subscribe('/topic/publico', (payload) => {
                    const mensaje = JSON.parse(payload.body);
                    setMensajes((prev) => [...prev, mensaje]);
                });
            }
        });

        client.activate();

        stompClient.current = client;

        return () => {
            if (stompClient.current) stompClient.current.deactivate();
        };


    }, []);


    const handleEnviar = () => {

        if (nuevoMensaje.trim() !== "" && stompClient.current?.connected){
            const mensajeAEnviar = {
                senderId: miUsuarioId,
                contenido: nuevoMensaje,
                receiverId: 0
            };


            stompClient.current.publish({
                destination: '/app/chat.global',
                body: JSON.stringify(mensajeAEnviar)
            });
            setNuevoMensaje("");
        }

    };


    return(

        <div>
            <NavBar/>

            <div className="card shadow-sm">
            
            <div className="card-header bg-dark text-white font-weight-bold text-center">
                CHAT GLOBAL ORION
            </div>

            <div className="card-body">

                {mensajes.map((msg, index) => (
                    <div 
                        key={index} 
                        className={`mb-2 p-2 rounded shadow-sm ${String(msg.senderId) === String(miUsuarioId) ? 'bg-success text-white ms-auto' : 'bg-white text-dark'}`}
                        style={{ width: 'fit-content', maxWidth: '80%' }}
                    >
                        {/* Aquí podrías añadir el nombre del usuario si lo tienes en el DTO */}
                        <small className="d-block font-weight-bold" style={{ fontSize: '0.7rem' }}>
                            ID: {msg.senderId}
                        </small>
                        {msg.contenido}
                    </div>
                ))}
            </div>

            <div className="card-footer d-flex gap-2">
                <input
                type="text" 
                    className="form-control" 
                    placeholder="Escribe a todo el mundo..." 
                    value={nuevoMensaje}
                    onChange={(e) => setNuevoMensaje(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handleEnviar()}
                />

                <button className="btn btn-dark" onClick={handleEnviar}>Enviar</button>
            </div>

        </div>
        </div>

        
    );
};

export default ChatGlobalPage;