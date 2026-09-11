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
import java.util.Locale;

@WebFilter("/*")
public class FiltroLenguajeCliente implements Filter {

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
            Locale localeDeterminado = determinarLocaleCliente(httpRequest);
            httpRequest.setAttribute("localeCliente", localeDeterminado);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    private Locale determinarLocaleCliente(HttpServletRequest request) {
        Locale resultadoLocale = request.getLocale();
        String paramLenguaje = request.getParameter("lang");

        if (paramLenguaje != null && !paramLenguaje.trim().isEmpty()) {
            resultadoLocale = Locale.forLanguageTag(paramLenguaje);
        } else if (resultadoLocale == null) {
            resultadoLocale = new Locale("es");
        }

        return resultadoLocale;
    }
}