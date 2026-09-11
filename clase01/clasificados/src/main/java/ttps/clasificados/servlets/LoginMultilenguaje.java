package ttps.clasificados.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/LoginMultilenguaje")
public class LoginMultilenguaje extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarRequerimiento(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarRequerimiento(request, response);
    }

    private void procesarRequerimiento(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        configurarRespuesta(response);
        Locale localeCliente = obtenerLocaleDelRequest(request);
        ResourceBundle bundle = ResourceBundle.getBundle("textos", localeCliente);

        PrintWriter out = response.getWriter();
        generarHtmlLogin(out, bundle);
        out.close();
    }

    private void configurarRespuesta(HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
    }

    private Locale obtenerLocaleDelRequest(HttpServletRequest request) {
        Object atributoLocale = request.getAttribute("localeCliente");
        Locale localeResultado = new Locale("es"); // Valor por defecto exigido por la consigna

        if (atributoLocale instanceof Locale) {
            localeResultado = (Locale) atributoLocale;
        }

        return localeResultado;
    }

    private void generarHtmlLogin(PrintWriter out, ResourceBundle bundle) {
        String titulo = bundle.getString("titulo");
        String labelUsuario = bundle.getString("labelusuario");
        String labelPassword = bundle.getString("labelpassword");

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>" + titulo + "</title></head>");
        out.println("<body>");
        out.println("<h2>" + titulo + "</h2>");
        out.println("<form action='ProcesarLogin' method='POST'>");
        out.println("<label>" + labelUsuario + ": </label>");
        out.println("<input type='text' name='usuario'/><br><br>");
        out.println("<label>" + labelPassword + ": </label>");
        out.println("<input type='password' name='password'/><br><br>");
        out.println("<input type='submit' value='Enviar'/>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }
}