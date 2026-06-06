# 🌌 Orion — Red Social Distribuida

Plataforma social fullstack construida sobre una arquitectura de microservicios. Orion permite publicar posts, interactuar con likes y follows, comentar, chatear en tiempo real y crear eventos, todo orquestado con Docker Compose y enrutado a través de un API Gateway centralizado.

---

## Arquitectura general

Cada dominio tiene su propia base de datos MySQL y responsabilidad bien delimitada. El frontend se comunica exclusivamente con el API Gateway. La autenticación se resuelve con JWT: el `Usuario_Service` emite el token y cada microservicio lo valida de forma independiente.

```
[React + Vite]
      │
      ▼
[API Gateway]  ← Spring Cloud Gateway
      │
      ├──► [Usuario_Service    ]  →  usuario_db
      ├──► [Post_Service       ]  →  feed_db
      ├──► [Comentario_Service ]  →  comentarios_db
      ├──► [Media_Service      ]  →  media_db
      ├──► [Interaccion_Service]  →  interaction_db
      ├──► [Chat_Service       ]  →  chat_db   (WebSocket/STOMP)
      └──► [Eventos_Service    ]  →  eventos_db
```

---

## Tecnologías utilizadas

| Capa | Tecnología | Versión |
|---|---|---|
| Frontend | React | 19.x |
| Bundler | Vite | 8.x |
| Routing frontend | React Router DOM | 7.x |
| WebSocket cliente | STOMP.js + SockJS | 7.x / 1.6.x |
| Backend framework | Spring Boot | 4.0.x |
| API Gateway | Spring Cloud Gateway (WebFlux) | — |
| Lenguaje backend | Java | 21+ |
| Persistencia | MySQL | 8.0 |
| Reducción de boilerplate | Lombok | — |
| Autenticación | JWT  | — |
| HATEOAS | Spring HATEOAS | — |
| Documentación API | Swagger / SpringDoc | — |
| Contenedores | Docker + Docker Compose | — |

---

## Estructura del proyecto

```
Orion-main/
├── docker-compose.yml          # Orquestación de todos los servicios
├── init/init.sql               # Creación inicial de bases de datos
├── orion-app/                  # Frontend React + Vite
├── ApiGateway/                 # Spring Cloud Gateway
├── Usuario_Service/            # Autenticación y gestión de usuarios
├── Post_service/               # Publicaciones del feed
├── Comentario_Service/         # Comentarios por post
├── MediaService/               # Subida y almacenamiento de archivos
├── Interaccion_Service/        # Likes y follows
├── chatService/                # Chat en tiempo real (WebSocket/STOMP)
├── eventos_service/            # Creación y asistencia a eventos
└── media_volumen/              # Volumen local para archivos

---

## Microservicios

** Usuario_Service** — Núcleo de autenticación. Emite tokens JWT, gestiona perfiles, avatares, roles y permisos. Usa Liquibase para migraciones.

** Post_Service** — Feed principal. El `userId` se extrae del JWT, no del body del request.

** Comentario_Service** — Comentarios por post. Expone documentación Swagger.

** MediaService** — Subida y almacenamiento de imágenes/avatares en un volumen Docker persistente.

** Interaccion_Service** — Likes (toggle) y follows entre usuarios, resueltos a partir del token JWT.

** Chat_Service** — Chat privado en tiempo real con WebSocket/STOMP y SockJS como fallback. Autentica la conexión vía `JwtChannelInterceptor`.

** Eventos_Service** — Creación y asistencia a eventos. Implementa Spring HATEOAS y se comunica con `Usuario_Service` vía WebClient.

** API Gateway** — Punto de entrada único con CORS global configurado. Enruta por prefijo de path hacia cada microservicio.

---



## Cómo levantar el proyecto

**Prerrequisitos:** Docker y Docker Compose instalados.

```bash
git clone <url-del-repositorio>
cd Orion-main
docker compose --profile complete up --build
```

> En Windows también está disponible el script `iniciar.bat`.

---

## Base de datos

`init/init.sql` crea automáticamente todas las bases de datos al iniciar MySQL:

```
usuario_db       → usuarios, roles, permisos, perfiles
feed_db          → posts
comentarios_db   → comentarios
media_db         → archivos multimedia
interaction_db   → likes, follows
chat_db          → mensajes de chat
eventos_db       → eventos y asistentes
```
