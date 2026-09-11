package ttps.clasificados.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet("/ImprimeCupon")
public class ImprimeCupon extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarTransaccionGrafica(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarTransaccionGrafica(request, response);
    }

    private void procesarTransaccionGrafica(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String textoUsuario = extraerTextoValido(request);
        BufferedImage imagenRemera = cargarImagenBase();

        if (imagenRemera != null) {
            String codigoRetiro = generarCodigoRetiro();
            dibujarElementosGraficos(imagenRemera, textoUsuario, codigoRetiro);
            transmitirImagenBinaria(response, imagenRemera);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: Imagen base no disponible.");
        }
    }

    private String extraerTextoValido(HttpServletRequest request) {
        String resultado = "TTPS Sponsor Oficial";
        String texto = request.getParameter("texto");

        if (texto != null && !texto.trim().isEmpty()) {
            resultado = texto;
        }

        return resultado;
    }

    private BufferedImage cargarImagenBase() {
        BufferedImage imagenResultado = null;

        try {
            InputStream streamArchivo = getServletContext().getResourceAsStream("/WEB-INF/remera.jpg");
            if (streamArchivo != null) {
                imagenResultado = ImageIO.read(streamArchivo);
            }
        } catch (IOException e) {
            System.err.println("Error al intentar leer remera.jpg: " + e.getMessage());
        }

        return imagenResultado;
    }

    private String generarCodigoRetiro() {
        int numeroEnRango = ThreadLocalRandom.current().nextInt(1000000, 99999999);
        String codigoGenerado = "#" + numeroEnRango;

        return codigoGenerado;
    }

    private void dibujarElementosGraficos(BufferedImage lienzo, String frase, String identificador) {
        Graphics2D pincel = lienzo.createGraphics();

        pincel.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        pincel.setFont(new Font("Arial", Font.BOLD, 20));
        pincel.setColor(Color.WHITE);

        FontMetrics metricasLetra = pincel.getFontMetrics();
        int coordenadaXFrase = 200 - (metricasLetra.stringWidth(frase) / 2);
        int coordenadaYFrase = 325;

        pincel.drawString(frase, coordenadaXFrase, coordenadaYFrase);

        pincel.setFont(new Font("Monospaced", Font.BOLD, 26));
        pincel.setColor(new Color(238, 255, 65));

        FontMetrics metricasCodigo = pincel.getFontMetrics();
        int coordenadaXCodigo = 580 - (metricasCodigo.stringWidth(identificador) / 2);
        int coordenadaYCodigo = 390;

        pincel.drawString(identificador, coordenadaXCodigo, coordenadaYCodigo);

        pincel.dispose();
    }

    private void transmitirImagenBinaria(HttpServletResponse response, BufferedImage lienzoFinal) throws IOException {
        response.setContentType("image/jpeg");
        OutputStream canalSalida = response.getOutputStream();
        ImageIO.write(lienzoFinal, "jpeg", canalSalida);
        canalSalida.flush();
        canalSalida.close();
    }
}