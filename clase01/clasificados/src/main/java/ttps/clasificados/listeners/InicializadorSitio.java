package ttps.clasificados.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ttps.clasificados.model.SitioClasificado;

// La anotación @WebListener le avisa a Tomcat que registre esta clase como un oyente de eventos
@WebListener
public class InicializadorSitio implements ServletContextListener {

    // Se ejecuta de manera automática por Tomcat apenas se enciende o inicializa la aplicación web
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 1. Obtenemos el ServletContext, que representa el entorno global de nuestra aplicación web
        ServletContext context = sce.getServletContext();

        // 2. Leemos los parámetros que declaramos en el web.xml usando sus nombres exactos
        String nombre = context.getInitParameter("nombre_sitio");
        String email = context.getInitParameter("email_contacto");
        String telefono = context.getInitParameter("telefono_contacto");

        // 3. Instanciamos nuestra clase de datos SitioClasificado con los valores recuperados
        SitioClasificado sitio = new SitioClasificado(nombre, email, telefono);

        // 4. Guardamos el objeto completo "sitio" dentro del alcance de Aplicación (ServletContext).
        // A partir de este momento, cualquier Servlet puede hacer getAttribute("sitio_config") y usar sus métodos.
        context.setAttribute("sitio_config", sitio);

        System.out.println("[LISTENER] Aplicación inicializada. Sitio '" + nombre + "' guardado en memoria con éxito.");
    }

    // Se ejecuta automáticamente por Tomcat justo antes de que la aplicación web se apague o desinstale
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[LISTENER] Aplicación destruida y liberando recursos.");
    }
}