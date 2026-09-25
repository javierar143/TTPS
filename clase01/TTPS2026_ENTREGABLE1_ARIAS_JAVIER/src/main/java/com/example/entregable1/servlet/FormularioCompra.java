package com.example.entregable1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/formulario")
public class FormularioCompra extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> shows =
                (Map<String, String>) getServletContext()
                        .getAttribute("shows");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"es\">");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>Compra de entradas</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Compra de entradas</h1>");

        out.println("<form action=\"ImprimeEntrada\" method=\"post\">");

        out.println("<label for=\"nombre\">Nombre:</label>");
        out.println("<input type=\"text\" id=\"nombre\" name=\"nombre\" required>");
        out.println("<br><br>");

        out.println("<label for=\"apellido\">Apellido:</label>");
        out.println("<input type=\"text\" id=\"apellido\" name=\"apellido\" required>");
        out.println("<br><br>");

        out.println("<label for=\"email\">E-mail:</label>");
        out.println("<input type=\"email\" id=\"email\" name=\"email\" required>");
        out.println("<br><br>");

        out.println("<label for=\"dni\">DNI:</label>");
        out.println("<input type=\"text\" id=\"dni\" name=\"dni\" required>");
        out.println("<br><br>");

        out.println("<label for=\"show\">Show:</label>");
        out.println("<select id=\"show\" name=\"show\" required>");

        out.println("<option value=\"\">Seleccione un show</option>");

        for (Map.Entry<String, String> show : shows.entrySet()) {
            out.println(
                    "<option value=\"" + show.getKey() + "\">"
                            + show.getKey() + " - " + show.getValue()
                            + "</option>"
            );
        }

        out.println("</select>");
        out.println("<br><br>");

        out.println("<label for=\"cantidadEntradas\">Cantidad de entradas:</label>");
        out.println(
                "<input type=\"number\" "
                        + "id=\"cantidadEntradas\" "
                        + "name=\"cantidadEntradas\" "
                        + "min=\"1\" required>"
        );
        out.println("<br><br>");

        out.println("<button type=\"submit\">Comprar entradas</button>");

        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }
}
