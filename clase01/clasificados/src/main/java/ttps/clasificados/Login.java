package ttps.clasificados;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

    // 1. La Anotación @WebServlet: Es un metadato que le avisa a Tomcat que esta clase es un Servlet.
    // Al poner "/Login", le estamos diciendo que cuando la interfaz de usuario (login.html)
    // envíe el formulario a "Login", Tomcat debe pasarle la petición a esta clase [6, 7].
@WebServlet("/Login")
public class Login extends HttpServlet { // Heredamos de HttpServlet para tener funciones preparadas para la web [8]

        // Aquí definimos el estado del Servlet: una lista en memoria que simulará nuestra base de datos [1]
        private List<Usuario> usuarios;
        private int visitas;
        private String ultimoUsuario;

        // 2. El método de ciclo de vida init():
        // Recuerda que Tomcat crea una única instancia de este Servlet y ejecuta init() una sola vez al arrancar [9, 10].
        // Es el lugar perfecto para preparar nuestros datos iniciales.
        @Override
        public void init() throws ServletException {
            usuarios = new ArrayList<>();

            //usuarios ficticios para probar el sistema [1]
            usuarios.add(new Usuario("admin", "1234", "administrador"));
            usuarios.add(new Usuario("juan", "5678", "publicador"));
            this.visitas=0;
            this.ultimoUsuario= "ninguno";
        }

        // 3. El método doPost():
        // Se ejecuta automáticamente cada vez que un usuario hace un envío de tipo POST desde la interfaz de usuario [11, 12].
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            this.visitas++;

            // El objeto "request" representa el Requerimiento HTTP del cliente
            // Usamos getParameter() para leer lo que el usuario escribió en las casillas de texto.
            // El texto entre comillas debe coincidir exactamente con el atributo "name" de tus etiquetas <input> del HTML.
            String userParam = request.getParameter("usuario");
            String passParam = request.getParameter("clave");

            // Optional: Es un contenedor seguro de Java para evitar errores si la búsqueda no encuentra nada.
            Optional<Usuario> usuarioEncontrado = usuarios.stream()
                    .filter(u -> u.getNombre().equals(userParam) && u.getClave().equals(passParam))
                    .findFirst();

            // 5. Validación y Redirección [1, 15]:
            if (usuarioEncontrado.isPresent()) {
                // Si el usuario existe, extraemos el objeto real
                Usuario usr = usuarioEncontrado.get();

                // --- ACTUALIZACIÓN DE ESTADO PARA EL INCISO I ---
                // Como el inicio de sesión fue exitoso, guardamos su nombre como el último en ingresar
                this.ultimoUsuario = usr.getNombre();

                /* Esto es anterior al punto 6
                // Evaluamos su perfil para enviarlo a la interfaz de usuario correspondiente
                if (usr.getPerfil().equals("administrador")){
                    // Redirecciona al menú de administrador
                    response.sendRedirect("menu_admin.html");
                } else if (usr.getPerfil().equals("publicador")) {
                    // Redirecciona al menú de publicador
                    response.sendRedirect("menu_publicador.html");
                }
            } else {
                // Si los datos son incorrectos, lo mandamos a la pantalla de error [15]
                response.sendRedirect("error.html");
            }
                 */

            // 1. Guardamos el perfil en el request para que el Servlet Menu pueda leerlo
            // Un "Atributo" puede ser cualquier objeto Java, a diferencia de un "Parámetro" que solo es un String.
            request.setAttribute("perfil", usr.getPerfil());

           // 2. Le pedimos a Tomcat el despachador de tráfico configurado para la ruta de nuestro Servlet "/Menu"
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Menu");

           // 3. Hacemos el "forward" (pase de manos interno) de la petición y la respuesta
           if (dispatcher != null) {
                dispatcher.forward(request, response);
           }

            } else {
                // Si las credenciales fallan, seguimos enviándolo de forma externa a la página estática de error
                response.sendRedirect("error.html");
            }
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            // Cada vez que alguien hace una petición GET al servlet, se suma una visita
            this.visitas++;

            // Leemos el parámetro "formato" de la URL (ej: ?formato=json)
            String formatoParam = request.getParameter("formato");

            // Validamos si el usuario escribió específicamente "json"
            if (formatoParam != null && formatoParam.equals("json")) {

                // 1. Configuramos el Content-Type adecuado para JSON (Esto responde al inciso J)
                response.setContentType("application/json;charset=UTF-8");

                // 2. Armamos la estructura de texto JSON a mano con nuestras variables
                String jsonResponse = "{\n" +
                        "  \"ultimoUsuario\": \"" + this.ultimoUsuario + "\",\n" +
                        "  \"visitas\": " + this.visitas + "\n" +
                        "}";

                // 3. Enviamos el JSON de vuelta al navegador del cliente
                response.getWriter().print(jsonResponse);

            } else {
                // Si entra por GET pero no pide JSON, lo redirigimos amablemente a la interfaz de login
                response.sendRedirect("login.html");
            }
        }
}
