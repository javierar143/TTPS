package ttps.clasificados.filtros;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@WebFilter("/*")
public class FiltroLogDeAccesos implements Filter {

    private static final Logger LOGGER = Logger.getLogger(FiltroLogDeAccesos.class.getName());
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String registroAuditoria = construirLineaDeLog(httpRequest);
            LOGGER.info(registroAuditoria);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    private String construirLineaDeLog(HttpServletRequest request) {
        String ipCliente = request.getRemoteAddr();
        String fechaHoraActual = LocalDateTime.now().format(FORMATO_FECHA);
        String metodoHttp = request.getMethod();
        String uriSolicitada = request.getRequestURI();
        String protocolo = request.getProtocol();
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null) {
            userAgent = "Desconocido";
        }

        String lineaLog = String.format(
                "[AUDITORIA LOG] IP: %s | Fecha: %s | Peticion: %s %s %s | Agente: %s",
                ipCliente, fechaHoraActual, metodoHttp, uriSolicitada, protocolo, userAgent
        );

        return lineaLog;
    }
}


