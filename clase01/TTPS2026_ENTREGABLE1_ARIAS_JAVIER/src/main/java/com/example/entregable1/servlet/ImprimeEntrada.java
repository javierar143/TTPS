package com.example.entregable1.servlet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

@WebServlet("/ImprimeEntrada")
public class ImprimeEntrada extends HttpServlet {

    private static final String ENTRADAS_DISPONIBLES =
            "entradasDisponibles";

    private static final String MEET_AND_GREET_DISPONIBLES =
            "meetAndGreetDisponibles";

    private static final String LOGO =
            "/imagenes/logoHipodromo.jpeg";

    private final Random random = new Random();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String dni = request.getParameter("dni");
        String show = request.getParameter("show");

        int cantidadEntradas =
                Integer.parseInt(
                        request.getParameter("cantidadEntradas")
                );

        Map<String, Integer> entradas =
                obtenerDisponibilidades(
                        ENTRADAS_DISPONIBLES
                );

        Map<String, Integer> meetAndGreet =
                obtenerDisponibilidades(
                        MEET_AND_GREET_DISPONIBLES
                );

        int entradasDisponibles =
                entradas.get(show);

        if (cantidadEntradas > entradasDisponibles) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "No hay suficientes entradas disponibles."
            );

        } else {

            entradas.put(
                    show,
                    entradasDisponibles - cantidadEntradas
            );

            boolean ganoMeetAndGreet =
                    determinarMeetAndGreet(
                            meetAndGreet,
                            show
                    );

            if (ganoMeetAndGreet) {

                int disponibles =
                        meetAndGreet.get(show);

                meetAndGreet.put(
                        show,
                        disponibles - 1
                );
            }

            generarPdf(
                    response,
                    nombre,
                    apellido,
                    dni,
                    show,
                    cantidadEntradas,
                    ganoMeetAndGreet
            );
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> obtenerDisponibilidades(
            String atributo) {

        return (Map<String, Integer>)
                getServletContext().getAttribute(atributo);
    }

    private boolean determinarMeetAndGreet(
            Map<String, Integer> meetAndGreet,
            String show) {

        boolean gano = false;

        int disponibles = meetAndGreet.get(show);

        if (disponibles > 0) {
            gano = random.nextBoolean();
        }

        return gano;
    }

    private void generarPdf(
            HttpServletResponse response,
            String nombre,
            String apellido,
            String dni,
            String show,
            int cantidadEntradas,
            boolean ganoMeetAndGreet)
            throws IOException {

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "inline; filename=\"entrada.pdf\""
        );

        try {

            Document document = new Document();

            PdfWriter.getInstance(
                    document,
                    response.getOutputStream()
            );

            document.open();

            agregarLogo(document);

            agregarDatos(
                    document,
                    nombre,
                    apellido,
                    dni,
                    show,
                    cantidadEntradas
            );

            agregarQr(
                    document,
                    nombre,
                    apellido,
                    dni,
                    show,
                    cantidadEntradas
            );

            if (ganoMeetAndGreet) {

                document.add(
                        new Paragraph(
                                "¡Felicitaciones! Ganaste un "
                                        + "meet & greet con los artistas "
                                        + "del show."
                        )
                );
            }

            document.close();

        } catch (Exception exception) {

            throw new IOException(
                    "No se pudo generar el PDF.",
                    exception
            );
        }
    }

    private void agregarLogo(Document document)
            throws Exception {

        String ruta =
                getServletContext().getRealPath(LOGO);

        Image logo =
                Image.getInstance(ruta);

        logo.scaleToFit(200, 100);

        document.add(logo);
    }

    private void agregarDatos(
            Document document,
            String nombre,
            String apellido,
            String dni,
            String show,
            int cantidadEntradas)
            throws Exception {

        document.add(
                new Paragraph(
                        "Comprador: "
                                + nombre
                                + " "
                                + apellido
                )
        );

        document.add(
                new Paragraph(
                        "DNI: " + dni
                )
        );

        document.add(
                new Paragraph(
                        "Show: " + show
                )
        );

        document.add(
                new Paragraph(
                        "Cantidad de entradas: "
                                + cantidadEntradas
                )
        );
    }

    private void agregarQr(
            Document document,
            String nombre,
            String apellido,
            String dni,
            String show,
            int cantidadEntradas)
            throws Exception {

        String contenidoQr =
                "Nombre: " + nombre
                        + "\nApellido: " + apellido
                        + "\nDNI: " + dni
                        + "\nShow: " + show
                        + "\nCantidad de entradas: "
                        + cantidadEntradas;

        BitMatrix matriz =
                new MultiFormatWriter().encode(
                        contenidoQr,
                        BarcodeFormat.QR_CODE,
                        250,
                        250
                );

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        MatrixToImageWriter.writeToStream(
                matriz,
                "PNG",
                output
        );

        Image qr =
                Image.getInstance(
                        output.toByteArray()
                );

        document.add(qr);
    }
}