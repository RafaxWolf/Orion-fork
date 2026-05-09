import NavBar from "./Navbar";
import {useState, useEffect, useRef, use} from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from "sockjs-client";


const ChatGlobalPage = () => {

    const [mensajes, setMensajes] = useState([]);
    const [nuevoMensaje, setNuevoMensaje] = useState("");
    const [conectado, setConectado] = useState(false);
    const stompClient = useRef(null);
    const miUsuarioId = localStorage.getItem('userId');


    // scroll historial
    const [pagina, setPagina] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const chatContainerRef = useRef(null);

    const cargarHistorial = async (p) => {

        try{

            const token = localStorage.getItem('token');

            const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/api/chat/historial/global?page=${p}&size=20`,{
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok){
                console.error("no hubo respuesta de la request de historial");
                return;
            }

            const data = await response.json();

            if (data.content.length < 20) setHasMore(false);

            const nuevosMensajes = data.content.reverse();
            setMensajes(prev => [...nuevosMensajes, ...prev]);

            // si es la primera vez que carga (pagina 0), bajamos el scroll a fondo
            if (p === 0){
                setTimeout(() => {
                    if (chatContainerRef.current){
                        chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
                    }
                }, 100) // timeou para dejar que renderice los mensajes primero
            }
        } catch (error){
            console.error("error cargando historial: ",error)
        }
        
    };


    const handleScroll = (e) => {
        if (e.target.scrollTop === 0 && hasMore){
            const hAnterior = e.target.scrollHeight;
            setPagina(prev => {
                const nuevaPagina = prev + 1;
                cargarHistorial(nuevaPagina).then(() => {

                    setTimeout(() => {
                        e.target.scrollTop = e.target.scrollHeight - hAnterior;
                    }, 100);
                });
                return nuevaPagina;
            });
        }
    };


    useEffect(() => {
        cargarHistorial(0);
    }, []);

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

            <div className="card-body"
            ref={chatContainerRef}
            onScroll={handleScroll}
            style={{overflowY: 'auto', height:'400px'}}
            >

                {mensajes.map((msg, index) => (
                    <div 
                        key={index} 
                        className={`mb-2 p-2 rounded shadow-sm ${String(msg.senderId) === String(miUsuarioId) ? 'bg-primary text-white ms-auto' : 'bg-light border'}`}
                        style={{ width: 'fit-content', maxWidth: '75%' }}
                    >
                        {/* Etiqueta del nombre de usuario */}
                        <div style={{ fontSize: '0.7rem', fontWeight: 'bold', marginBottom: '2px', opacity: 0.8 }}>
                            {String(msg.senderId) === String(miUsuarioId) ? 'Tú' : `@${msg.nombreEmisor}`}
                        </div>

                        {/* Contenido del mensaje */}
                        <div>{msg.contenido}</div>
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