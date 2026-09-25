package com.example.entregable1.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfiguracionListener implements ServletContextListener {

    private static final String PARAMETRO_SHOWS = "shows";
    private static final String PARAMETRO_CANTIDAD_ENTRADAS =
            "cantidadEntradas";
    private static final String PARAMETRO_CANTIDAD_MEET_AND_GREET =
            "cantidadMeetAndGreet";

    private static final String ATRIBUTO_SHOWS = "shows";
    private static final String ATRIBUTO_ENTRADAS_INICIALES =
            "entradasIniciales";
    private static final String ATRIBUTO_ENTRADAS_DISPONIBLES =
            "entradasDisponibles";
    private static final String ATRIBUTO_MEET_AND_GREET_INICIALES =
            "meetAndGreetIniciales";
    private static final String ATRIBUTO_MEET_AND_GREET_DISPONIBLES =
            "meetAndGreetDisponibles";

    @Override
    public void contextInitialized(ServletContextEvent event) {

        ServletContext context = event.getServletContext();

        Map<String, String> shows = cargarShows(context);

        int cantidadEntradas = obtenerCantidad(
                context,
                PARAMETRO_CANTIDAD_ENTRADAS
        );

        int cantidadMeetAndGreet = obtenerCantidad(
                context,
                PARAMETRO_CANTIDAD_MEET_AND_GREET
        );

        Map<String, Integer> entradas =
                inicializarCantidades(shows, cantidadEntradas);

        Map<String, Integer> meetAndGreet =
                inicializarCantidades(shows, cantidadMeetAndGreet);

        context.setAttribute(
                ATRIBUTO_SHOWS,
                shows
        );

        context.setAttribute(
                ATRIBUTO_ENTRADAS_INICIALES,
                new LinkedHashMap<>(entradas)
        );

        context.setAttribute(
                ATRIBUTO_ENTRADAS_DISPONIBLES,
                entradas
        );

        context.setAttribute(
                ATRIBUTO_MEET_AND_GREET_INICIALES,
                new LinkedHashMap<>(meetAndGreet)
        );

        context.setAttribute(
                ATRIBUTO_MEET_AND_GREET_DISPONIBLES,
                meetAndGreet
        );
    }

    private Map<String, String> cargarShows(
            ServletContext context) {

        Map<String, String> shows =
                new LinkedHashMap<>();

        String configuracion =
                context.getInitParameter(PARAMETRO_SHOWS);

        String[] listaShows =
                configuracion.split(";");

        for (String show : listaShows) {

            String[] datos =
                    show.split("\\|");

            String nombre = datos[0];
            String fecha = datos[1];

            shows.put(nombre, fecha);
        }

        return shows;
    }

    private int obtenerCantidad(
            ServletContext context,
            String parametro) {

        return Integer.parseInt(
                context.getInitParameter(parametro)
        );
    }

    private Map<String, Integer> inicializarCantidades(
            Map<String, String> shows,
            int cantidad) {

        Map<String, Integer> cantidades =
                new LinkedHashMap<>();

        for (String show : shows.keySet()) {
            cantidades.put(show, cantidad);
        }

        return cantidades;
    }
}