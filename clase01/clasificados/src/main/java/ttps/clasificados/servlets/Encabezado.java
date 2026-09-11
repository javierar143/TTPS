package ttps.clasificados.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ttps.clasificados.model.SitioClasificado;

import java.io.IOException;
import java.io.PrintWriter;

// Registramos el Servlet para que Tomcat sepa que responde en la ruta "/Encabezado"
@WebServlet("/Encabezado")
public class Encabezado extends HttpServlet {

    // Soporta peticiones tanto si el Servlet principal llegó por GET como por POST
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        generarHTML(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        generarHTML(request, response);
    }

    private void generarHTML(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Acudimos al pizarrón global de la aplicación (ServletContext) para recuperar el objeto
        // de datos que tu Listener guardó al encender el servidor.
        SitioClasificado sitio = (SitioClasificado) getServletContext().getAttribute("sitio_config");

        PrintWriter out = response.getWriter();

        // 2. Si el objeto existe en memoria, dibujamos un encabezado web prolijo con estilos CSS embebidos
        if (sitio != null) {
            out.println("<header style='background-color: #3827F5; padding: 20px; border-bottom: 3px solid #1976d2; text-align: center; font-family: sans-serif; margin-bottom: 20px;'>");
            out.println("    <h2 style='margin: 0; color: #8099BD; font-size: 24px;'>" + sitio.getNombre() + "</h2>");
            out.println("    <p style='margin: 8px 0 0 0; font-size: 14px; color: #8099BD;'>");
            out.println("        Soporte: <strong><a href='mailto:" + sitio.getEmail() + "' style='color: #1565c0; text-decoration: none;'>" + sitio.getEmail() + "</a></strong> | ");
            out.println("        Teléfono de Contacto: <strong>" + sitio.getTelefono() + "</strong>");
            out.println("    </p>");
            out.println("</header>");
        } else {
            // Protección por si el Listener no llegó a cargar el objeto
            out.println("<header style='background-color: #ffebee; padding: 15px; border-bottom: 2px solid #c62828; text-align: center;'>");
            out.println("    <p style='color: #c62828; margin: 0; font-weight: bold;'>[Error] No se ha podido cargar el encabezado institucional.</p>");
            out.println("</header>");
        }
        // IMPORTANTE: No cerramos el flujo "out" con out.close() porque el Servlet principal
        // todavía necesita seguir escribiendo la página en la red.
    }
}