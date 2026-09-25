package com.example.entregable1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/VisualizarEstadistica")
public class VisualizarEstadistica extends HttpServlet {

    private static final String SHOWS = "shows";

    private static final String ENTRADAS_INICIALES =
            "entradasIniciales";

    private static final String ENTRADAS_DISPONIBLES =
            "entradasDisponibles";

    private static final String MEET_AND_GREET_INICIALES =
            "meetAndGreetIniciales";

    private static final String MEET_AND_GREET_DISPONIBLES =
            "meetAndGreetDisponibles";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        Map<String, String> shows =
                obtenerShows();

        Map<String, Integer> entradasIniciales =
                obtenerCantidades(ENTRADAS_INICIALES);

        Map<String, Integer> entradasDisponibles =
                obtenerCantidades(ENTRADAS_DISPONIBLES);

        Map<String, Integer> meetAndGreetIniciales =
                obtenerCantidades(MEET_AND_GREET_INICIALES);

        Map<String, Integer> meetAndGreetDisponibles =
                obtenerCantidades(MEET_AND_GREET_DISPONIBLES);

        mostrarEstadisticas(
                response,
                shows,
                entradasIniciales,
                entradasDisponibles,
                meetAndGreetIniciales,
                meetAndGreetDisponibles
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> obtenerShows() {

        return (Map<String, String>)
                getServletContext().getAttribute(SHOWS);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> obtenerCantidades(
            String atributo) {

        return (Map<String, Integer>)
                getServletContext().getAttribute(atributo);
    }

    private void mostrarEstadisticas(
            HttpServletResponse response,
            Map<String, String> shows,
            Map<String, Integer> entradasIniciales,
            Map<String, Integer> entradasDisponibles,
            Map<String, Integer> meetAndGreetIniciales,
            Map<String, Integer> meetAndGreetDisponibles)
            throws IOException {

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='es'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Estadísticas</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Estadísticas</h1>");

        out.println("<table border='1'>");

        out.println("<tr>");
        out.println("<th>Show</th>");
        out.println("<th>Fecha</th>");
        out.println("<th>Entradas vendidas</th>");
        out.println("<th>Entradas restantes</th>");
        out.println("<th>Meet & Greet ganados</th>");
        out.println("<th>Meet & Greet restantes</th>");
        out.println("</tr>");

        for (String show : shows.keySet()) {

            int entradasVendidas =
                    entradasIniciales.get(show)
                            - entradasDisponibles.get(show);

            int meetAndGreetGanados =
                    meetAndGreetIniciales.get(show)
                            - meetAndGreetDisponibles.get(show);

            out.println("<tr>");

            out.println("<td>" + show + "</td>");

            out.println(
                    "<td>"
                            + shows.get(show)
                            + "</td>"
            );

            out.println(
                    "<td>"
                            + entradasVendidas
                            + "</td>"
            );

            out.println(
                    "<td>"
                            + entradasDisponibles.get(show)
                            + "</td>"
            );

            out.println(
                    "<td>"
                            + meetAndGreetGanados
                            + "</td>"
            );

            out.println(
                    "<td>"
                            + meetAndGreetDisponibles.get(show)
                            + "</td>"
            );

            out.println("</tr>");
        }

        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }
}
