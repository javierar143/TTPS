package com.example.entregable1.filtro;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@WebFilter("/*")
public class FiltroLogger implements Filter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(FiltroLogger.class);

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        long inicio = System.currentTimeMillis();

        chain.doFilter(request, response);

        long fin = System.currentTimeMillis();

        LOGGER.info(
                "Request: method={}, url={}, remoteAddr={}, "
                        + "userAgent={}, params={}",
                httpRequest.getMethod(),
                httpRequest.getRequestURL(),
                httpRequest.getRemoteAddr(),
                httpRequest.getHeader("User-Agent"),
                obtenerParametros(httpRequest)
        );

        LOGGER.info(
                "Response: method={}, url={}, status={}, "
                        + "contentType={}, tiempo={}ms",
                httpRequest.getMethod(),
                httpRequest.getRequestURL(),
                httpResponse.getStatus(),
                httpResponse.getContentType(),
                fin - inicio
        );
    }

    private Map<String, String> obtenerParametros(
            HttpServletRequest request) {

        Map<String, String> parametros =
                new LinkedHashMap<>();

        Enumeration<String> nombres =
                request.getParameterNames();

        while (nombres.hasMoreElements()) {

            String nombre = nombres.nextElement();

            parametros.put(
                    nombre,
                    request.getParameter(nombre)
            );
        }

        return parametros;
    }
}
