package ttps.clasificados.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Menu")
public class Menu extends HttpServlet {

    // Soporta tanto peticiones que lleguen por POST (desde el formulario de Login)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        generarMenuDinamico(request, response);
    }

    // Soporta peticiones que puedan llegar por GET (por si se delega desde un GET)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        generarMenuDinamico(request, response);
    }

    private void generarMenuDinamico(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Recuperamos el "atributo" que guardó el Servlet Login en la petición.
        // Como los atributos viajan como objetos genéricos (Object), los convertimos a String de forma segura.
        Object perfilObj = request.getAttribute("perfil");

        // Configuramos la cabecera del tipo de respuesta para el navegador
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Empezamos a estructurar el documento HTML dinámico
        out.println("<!DOCTYPE html>");
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Clasificados - Menú</title>");
        out.println("</head>");
        out.println("<body>");

        // === INICIO DE CAMBIO PARA EL EJERCICIO 8 ===
        // Pedimos el despachador de tráfico apuntando a la ruta del Servlet "/Encabezado"
        RequestDispatcher dispatcherHeader = request.getRequestDispatcher("/Encabezado");
        if (dispatcherHeader != null) {
            // Incrustamos el contenido HTML generado por el Servlet Encabezado en este punto exacto
            dispatcherHeader.include(request, response);
        }
        // === FIN DE CAMBIO PARA EL EJERCICIO 8 ===

        // Validación segura de que el atributo existe
        if (perfilObj != null) {
            String perfil = perfilObj.toString();

            out.println("<h1>Portal de Clasificados</h1>");
            out.println("<p>Bienvenido. Tu perfil de acceso es: <strong>" + perfil + "</strong></p>");
            out.println("<hr>");
            out.println("<p>Operaciones disponibles para tu perfil:</p>");
            out.println("<ul>");

            // Tu estilo de comparación natural: obtenemos el valor y comparamos
            if (perfil.equals("administrador")) {
                out.println("    <li><a href='#'>Listar Usuarios Publicadores</a></li>");
                out.println("    <li><a href='#'>ABM Administradores</a></li>");
                out.println("    <li><a href='#'>Ver Estadísticas</a></li>");
            } else if (perfil.equals("publicador")) {
                out.println("    <li><a href='#'>Actualizar Datos de Contacto</a></li>");
                out.println("    <li><a href='#'>ABM de Publicaciones</a></li>");
                out.println("    <li><a href='#'>Contestar Consultas</a></li>");
            } else {
                out.println("    <li>Perfil no reconocido por el sistema.</li>");
            }

            out.println("</ul>");

        } else {
            // Si alguien intenta entrar a /Menu directamente escribiendo la URL sin pasar por el Login
            out.println("<h1 style='color:red;'>Acceso Denegado</h1>");
            out.println("<p>No se ha encontrado información de sesión válida para generar este menú.</p>");
        }

        out.println("<br>");
        out.println("<p><a href='login.html'>Volver al Login (Cerrar Sesión)</a></p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}