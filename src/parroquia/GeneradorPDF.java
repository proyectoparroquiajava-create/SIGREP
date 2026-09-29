package parroquia;

import java.awt.Color;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class GeneradorPDF {

    // =========================================================
    // COLORES INSTITUCIONALES
    // =========================================================

    private static final Color VERDE =
            new Color(24, 76, 58);

    private static final Color DORADO =
            new Color(190, 145, 45);

    private static final Color CREMA =
            new Color(250, 248, 242);

    private static final Color GRIS =
            new Color(90, 90, 90);


    // =========================================================
    // REPORTE GENERAL PARA LA PARROQUIA
    // =========================================================

    public static String generarReporte(
            String tipo,
            List<Inscripcion> inscripciones) {

        String nombreArchivo =
                "Reporte_" + tipo.replace(" ", "_") + ".pdf";

        try {

            Document documento = new Document(
                    PageSize.A4, 48, 48, 45, 45);

            PdfWriter writer = PdfWriter.getInstance(
                    documento,
                    new FileOutputStream(nombreArchivo));

            documento.open();

            agregarFondoYMarco(writer);

            // FUENTES
            Font parroquia = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    13,
                    DORADO);

            Font titulo = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    20,
                    VERDE);

            Font subtitulo = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    10,
                    VERDE);

            Font texto = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    9,
                    GRIS);

            Font numeroGrande = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    16,
                    VERDE);


            // =================================================
            // ENCABEZADO
            // =================================================

            agregarEncabezado(documento);

            Paragraph nombreParroquia = new Paragraph(
                    "PARROQUIA SAGRADA FAMILIA",
                    parroquia);

            nombreParroquia.setAlignment(
                    Element.ALIGN_CENTER);

            nombreParroquia.setSpacingBefore(4);

            documento.add(nombreParroquia);


            Paragraph sistema = new Paragraph(
                    "SISTEMA INTEGRADO DE GESTIÓN DE REGISTROS PARROQUIALES",
                    texto);

            sistema.setAlignment(Element.ALIGN_CENTER);

            documento.add(sistema);


            Paragraph tituloReporte = new Paragraph(
                    "REPORTE DE " + tipo.toUpperCase(),
                    titulo);

            tituloReporte.setAlignment(
                    Element.ALIGN_CENTER);

            tituloReporte.setSpacingBefore(22);
            tituloReporte.setSpacingAfter(4);

            documento.add(tituloReporte);


            Paragraph fecha = new Paragraph(
                    "Documento administrativo de SIGREP",
                    texto);

            fecha.setAlignment(Element.ALIGN_CENTER);
            fecha.setSpacingAfter(22);

            documento.add(fecha);


            // =================================================
            // CALCULAR RESUMEN
            // =================================================

            int cantidad = 0;
            double total = 0;

            for (Inscripcion inscripcion : inscripciones) {

                Sacramento sacramento =
                        inscripcion.getSacramento();

                if (sacramento.tipo()
                        .equalsIgnoreCase(tipo)) {

                    cantidad++;
                    total += sacramento.getCosto();
                }
            }


            // =================================================
            // RESUMEN
            // =================================================

            agregarTituloSeccion(
                    documento,
                    "RESUMEN DEL REPORTE");

            PdfPTable resumen = new PdfPTable(2);

            resumen.setWidthPercentage(100);
            resumen.setWidths(new float[]{50, 50});
            resumen.setSpacingBefore(8);
            resumen.setSpacingAfter(24);


            agregarCajaResumen(
                    resumen,
                    "INSCRIPCIONES REGISTRADAS",
                    String.valueOf(cantidad),
                    subtitulo,
                    numeroGrande);


            agregarCajaResumen(
                    resumen,
                    "TOTAL RECAUDADO",
                    "S/ " + String.format("%.2f", total),
                    subtitulo,
                    numeroGrande);

            documento.add(resumen);


            // =================================================
            // DETALLE
            // =================================================

            agregarTituloSeccion(
                    documento,
                    "DETALLE DE INSCRIPCIONES");


            PdfPTable tabla = new PdfPTable(7);

            tabla.setWidthPercentage(100);

            tabla.setWidths(
                    new float[]{6, 22, 12, 14, 10, 23, 13});

            tabla.setSpacingBefore(10);


            agregarEncabezadoTabla(tabla, "N°");
            agregarEncabezadoTabla(tabla, "BENEFICIARIO");
            agregarEncabezadoTabla(tabla, "DNI");
            agregarEncabezadoTabla(tabla, "FECHA");
            agregarEncabezadoTabla(tabla, "HORA");
            agregarEncabezadoTabla(tabla, "SACERDOTE");
            agregarEncabezadoTabla(tabla, "IMPORTE");


            int numero = 1;

            for (Inscripcion inscripcion : inscripciones) {

                Sacramento sacramento =
                        inscripcion.getSacramento();

                if (sacramento.tipo()
                        .equalsIgnoreCase(tipo)) {

                    Persona persona =
                            sacramento.getBeneficiario();

                    String dniOculto =
                            persona.getDni()
                                    .substring(0, 3)
                                    + "*****";


                    agregarCeldaSuave(
                            tabla,
                            String.valueOf(numero));

                    agregarCeldaSuave(
                            tabla,
                            persona.getNombreCompleto());

                    agregarCeldaSuave(
                            tabla,
                            dniOculto);

                    agregarCeldaSuave(
                            tabla,
                            sacramento
                                    .getFechaProgramada()
                                    .format(
                                            DateTimeFormatter.ofPattern(
                                                    "dd/MM/yyyy")));

                    agregarCeldaSuave(
                            tabla,
                            sacramento
                                    .getHora()
                                    .format(
                                            DateTimeFormatter.ofPattern(
                                                    "HH:mm")));

                    agregarCeldaSuave(
                            tabla,
                            sacramento
                                    .getSacerdote()
                                    .getNombreCompleto());

                    agregarCeldaSuave(
                            tabla,
                            "S/ "
                                    + String.format(
                                            "%.2f",
                                            sacramento.getCosto()));

                    numero++;
                }
            }

            documento.add(tabla);


            // =================================================
            // PIE
            // =================================================

            Paragraph pie = new Paragraph(
                    "Documento administrativo generado automáticamente por SIGREP\n"
                    + "Parroquia Sagrada Familia",
                    texto);

            pie.setAlignment(Element.ALIGN_CENTER);
            pie.setSpacingBefore(35);

            documento.add(pie);

            documento.close();

            System.out.println(
                    "Reporte PDF creado correctamente.");

            return nombreArchivo;

        } catch (Exception e) {

            System.out.println(
                    "Error al generar reporte: "
                    + e.getMessage());

            return null;
        }
    }


    // =========================================================
    // COMPROBANTE INDIVIDUAL DEL FELIGRÉS
    // =========================================================

    public static String generarComprobante(
            Inscripcion inscripcion) {

        if (inscripcion == null) {
            return null;
        }

        Sacramento sacramento =
                inscripcion.getSacramento();

        Persona persona =
                sacramento.getBeneficiario();

        Recibo recibo =
                inscripcion.getRecibo();


        String nombreArchivo =
                "Comprobante_"
                + persona.getDni()
                + ".pdf";


        try {

            Document documento = new Document(
                    PageSize.A4, 55, 55, 45, 45);

            PdfWriter writer = PdfWriter.getInstance(
                    documento,
                    new FileOutputStream(nombreArchivo));

            documento.open();

            agregarFondoYMarco(writer);


            // =================================================
            // FUENTES
            // =================================================

            Font parroquia = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    12,
                    DORADO);

            Font titulo = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    19,
                    VERDE);

            Font reciboFuente = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    9,
                    GRIS);

            Font etiqueta = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    9,
                    VERDE);

            Font texto = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    9,
                    Color.BLACK);

            Font textoGris = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    8,
                    GRIS);

            Font totalFuente = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    17,
                    VERDE);

            Font correctoFuente = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    10,
                    DORADO);


            // =================================================
            // ENCABEZADO
            // =================================================

            agregarEncabezado(documento);


            Paragraph nombreParroquia = new Paragraph(
                    "PARROQUIA SAGRADA FAMILIA",
                    parroquia);

            nombreParroquia.setAlignment(
                    Element.ALIGN_CENTER);

            nombreParroquia.setSpacingBefore(3);

            documento.add(nombreParroquia);


            Paragraph tituloComprobante = new Paragraph(
                    "COMPROBANTE DE INSCRIPCIÓN",
                    titulo);

            tituloComprobante.setAlignment(
                    Element.ALIGN_CENTER);

            tituloComprobante.setSpacingBefore(10);

            documento.add(tituloComprobante);


            String numeroRecibo = "Pendiente";

            if (recibo != null) {
                numeroRecibo = recibo.numero();
            }


            Paragraph numeroReciboTexto = new Paragraph(
                    "N.º " + numeroRecibo,
                    reciboFuente);

            numeroReciboTexto.setAlignment(
                    Element.ALIGN_CENTER);

            numeroReciboTexto.setSpacingBefore(3);
            numeroReciboTexto.setSpacingAfter(22);

            documento.add(numeroReciboTexto);


            // =================================================
            // DATOS DEL FELIGRÉS
            // =================================================

            agregarTituloSeccion(
                    documento,
                    "DATOS DEL FELIGRÉS");


            PdfPTable datos =
                    new PdfPTable(2);

            datos.setWidthPercentage(100);

            datos.setWidths(
                    new float[]{30, 70});

            datos.setSpacingBefore(8);
            datos.setSpacingAfter(22);


            String dniOculto =
                    persona.getDni()
                            .substring(0, 3)
                            + "*****";


            String telefonoOculto = "-";

            if (persona.getTelefono() != null
                    && !persona.getTelefono().isEmpty()) {

                telefonoOculto =
                        persona.getTelefono()
                                .substring(0, 1)
                                + "********";
            }


            String direccion = "-";

            if (persona instanceof Feligres) {

                Feligres feligres =
                        (Feligres) persona;

                direccion =
                        feligres.getDireccion();
            }


            String fechaEmision = "-";

            if (recibo != null) {

                fechaEmision =
                        recibo.fechaEmision()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "dd/MM/yyyy"));
            }


            agregarDatoElegante(
                    datos,
                    "Nombre",
                    persona.getNombreCompleto(),
                    etiqueta,
                    texto);

            agregarDatoElegante(
                    datos,
                    "DNI",
                    dniOculto,
                    etiqueta,
                    texto);

            agregarDatoElegante(
                    datos,
                    "Teléfono",
                    telefonoOculto,
                    etiqueta,
                    texto);

            agregarDatoElegante(
                    datos,
                    "Dirección",
                    direccion,
                    etiqueta,
                    texto);

            agregarDatoElegante(
                    datos,
                    "Fecha de emisión",
                    fechaEmision,
                    etiqueta,
                    texto);

            documento.add(datos);


            // =================================================
            // DETALLE DE INSCRIPCIÓN
            // =================================================

            agregarTituloSeccion(
                    documento,
                    "DETALLE DE INSCRIPCIÓN");


            PdfPTable detalle =
                    new PdfPTable(4);

            detalle.setWidthPercentage(100);

            detalle.setWidths(
                    new float[]{32, 24, 18, 26});

            detalle.setSpacingBefore(10);


            agregarEncabezadoTabla(
                    detalle,
                    "SACRAMENTO");

            agregarEncabezadoTabla(
                    detalle,
                    "FECHA");

            agregarEncabezadoTabla(
                    detalle,
                    "HORA");

            agregarEncabezadoTabla(
                    detalle,
                    "IMPORTE");


            agregarCeldaSuave(
                    detalle,
                    sacramento.tipo());

            agregarCeldaSuave(
                    detalle,
                    sacramento
                            .getFechaProgramada()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "dd/MM/yyyy")));

            agregarCeldaSuave(
                    detalle,
                    sacramento
                            .getHora()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "HH:mm")));

            agregarCeldaSuave(
                    detalle,
                    "S/ "
                            + String.format(
                                    "%.2f",
                                    sacramento.getCosto()));

            documento.add(detalle);


            // =================================================
            // SACERDOTE Y TOTAL
            // =================================================

            PdfPTable sacerdoteTotal =
                    new PdfPTable(2);

            sacerdoteTotal.setWidthPercentage(100);

            sacerdoteTotal.setWidths(
                    new float[]{65, 35});

            sacerdoteTotal.setSpacingBefore(14);
            sacerdoteTotal.setSpacingAfter(22);


            PdfPCell sacerdoteCelda =
                    new PdfPCell();

            sacerdoteCelda.setBorder(Rectangle.NO_BORDER);
            sacerdoteCelda.setPadding(4);

            sacerdoteCelda.addElement(
                    new Paragraph(
                            "SACERDOTE ENCARGADO",
                            etiqueta));

            sacerdoteCelda.addElement(
                    new Paragraph(
                            sacramento
                                    .getSacerdote()
                                    .getNombreCompleto(),
                            texto));

            sacerdoteTotal.addCell(sacerdoteCelda);


            PdfPCell totalCelda =
                    new PdfPCell();

            totalCelda.setBorder(Rectangle.NO_BORDER);
            totalCelda.setPadding(4);
            totalCelda.setHorizontalAlignment(
                    Element.ALIGN_RIGHT);

            Paragraph palabraTotal =
                    new Paragraph(
                            "TOTAL",
                            etiqueta);

            palabraTotal.setAlignment(
                    Element.ALIGN_RIGHT);

            totalCelda.addElement(palabraTotal);


            Paragraph monto =
                    new Paragraph(
                            "S/ "
                            + String.format(
                                    "%.2f",
                                    sacramento.getCosto()),
                            totalFuente);

            monto.setAlignment(
                    Element.ALIGN_RIGHT);

            totalCelda.addElement(monto);

            sacerdoteTotal.addCell(totalCelda);

            documento.add(sacerdoteTotal);


            // =================================================
            // INFORMACIÓN ADICIONAL
            // =================================================

            agregarTituloSeccion(
                    documento,
                    "INFORMACIÓN ADICIONAL");


            PdfPTable adicionales =
                    new PdfPTable(2);

            adicionales.setWidthPercentage(100);

            adicionales.setWidths(
                    new float[]{30, 70});

            adicionales.setSpacingBefore(8);


            // BAUTIZO
            if (sacramento instanceof Bautizo) {

                Bautizo bautizo =
                        (Bautizo) sacramento;

                agregarDatoElegante(
                        adicionales,
                        "Padrino",
                        nombrePersona(
                                bautizo.getPadrino()),
                        etiqueta,
                        texto);

                agregarDatoElegante(
                        adicionales,
                        "Madrina",
                        nombrePersona(
                                bautizo.getMadrina()),
                        etiqueta,
                        texto);
            }


            // CONFIRMACIÓN
            if (sacramento instanceof Confirmación) {

                Confirmación confirmacion =
                        (Confirmación) sacramento;

                agregarDatoElegante(
                        adicionales,
                        "Padrino",
                        nombrePersona(
                                confirmacion.getPadrino()),
                        etiqueta,
                        texto);
            }


            // PRIMERA COMUNIÓN
            if (sacramento instanceof PrimeraComunión) {

                PrimeraComunión comunion =
                        (PrimeraComunión) sacramento;

                agregarDatoElegante(
                        adicionales,
                        "Padrino",
                        nombrePersona(
                                comunion.getPadrino()),
                        etiqueta,
                        texto);
            }


            // MATRIMONIO
            if (sacramento instanceof Matrimonio) {

                Matrimonio matrimonio =
                        (Matrimonio) sacramento;

                agregarDatoElegante(
                        adicionales,
                        "Contrayente",
                        matrimonio
                                .getContrayente()
                                .getNombreCompleto(),
                        etiqueta,
                        texto);

                int numeroTestigo = 1;

                for (Persona testigo :
                        matrimonio.getTestigos()) {

                    agregarDatoElegante(
                            adicionales,
                            "Testigo " + numeroTestigo,
                            testigo.getNombreCompleto(),
                            etiqueta,
                            texto);

                    numeroTestigo++;
                }
            }


            // RETIRO
            if (sacramento instanceof Retiro) {

                Retiro retiro =
                        (Retiro) sacramento;

                agregarDatoElegante(
                        adicionales,
                        "Capacidad",
                        retiro.getCuposTotales()
                                + " cupos",
                        etiqueta,
                        texto);
            }


            documento.add(adicionales);


            // =================================================
            // CONFIRMACIÓN FINAL
            // =================================================

            Paragraph correcto = new Paragraph(
                    "✓  INSCRIPCIÓN REGISTRADA CORRECTAMENTE",
                    correctoFuente);

            correcto.setAlignment(
                    Element.ALIGN_CENTER);

            correcto.setSpacingBefore(30);

            documento.add(correcto);


            Paragraph sistema = new Paragraph(
                    "Registro procesado mediante SIGREP",
                    textoGris);

            sistema.setAlignment(
                    Element.ALIGN_CENTER);

            sistema.setSpacingBefore(3);

            documento.add(sistema);


            // =================================================
            // PIE
            // =================================================

            Paragraph pie = new Paragraph(
                    "Documento generado automáticamente por SIGREP\n"
                    + "Parroquia Sagrada Familia",
                    textoGris);

            pie.setAlignment(Element.ALIGN_CENTER);
            pie.setSpacingBefore(25);

            documento.add(pie);


            documento.close();

            System.out.println(
                    "Comprobante PDF creado correctamente.");

            return nombreArchivo;

        } catch (Exception e) {

            System.out.println(
                    "Error al generar comprobante: "
                    + e.getMessage());

            return null;
        }
    }


    // =========================================================
    // FONDO MARFIL + MARCO DORADO
    // =========================================================

    private static void agregarFondoYMarco(
            PdfWriter writer) {

        PdfContentByte fondo =
                writer.getDirectContentUnder();

        fondo.setColorFill(CREMA);

        fondo.rectangle(
                0,
                0,
                PageSize.A4.getWidth(),
                PageSize.A4.getHeight());

        fondo.fill();


        PdfContentByte marco =
                writer.getDirectContent();

        marco.setColorStroke(DORADO);
        marco.setLineWidth(0.8f);

        marco.rectangle(
                25,
                25,
                PageSize.A4.getWidth() - 50,
                PageSize.A4.getHeight() - 50);

        marco.stroke();
    }


    // =========================================================
    // ENCABEZADO CON LOGO
    // =========================================================

    private static void agregarEncabezado(
            Document documento) {

        try {

            PdfPTable encabezado =
                    new PdfPTable(3);

            encabezado.setWidthPercentage(100);

            encabezado.setWidths(
                    new float[]{25, 50, 25});


            // LOGO
            Image logo = Image.getInstance(
                    GeneradorPDF.class.getResource(
                            "/imagen/IMAGEN.png"));

            logo.scaleToFit(115, 55);


            PdfPCell celdaLogo =
                    new PdfPCell();

            celdaLogo.setBorder(
                    Rectangle.NO_BORDER);

            celdaLogo.setVerticalAlignment(
                    Element.ALIGN_MIDDLE);

            celdaLogo.addElement(logo);

            encabezado.addCell(celdaLogo);


            // ESPACIO CENTRAL
            PdfPCell centro =
                    new PdfPCell();

            centro.setBorder(
                    Rectangle.NO_BORDER);

            encabezado.addCell(centro);


            // SIGREP
            Font sigrepFuente =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            12,
                            VERDE);

            PdfPCell sigrep =
                    new PdfPCell(
                            new Phrase(
                                    "SIGREP",
                                    sigrepFuente));

            sigrep.setBorder(
                    Rectangle.NO_BORDER);

            sigrep.setHorizontalAlignment(
                    Element.ALIGN_RIGHT);

            sigrep.setVerticalAlignment(
                    Element.ALIGN_MIDDLE);

            encabezado.addCell(sigrep);

            documento.add(encabezado);

        } catch (Exception e) {

            System.out.println(
                    "No se pudo cargar el logo.");
        }
    }


    // =========================================================
    // TÍTULO DE CADA SECCIÓN
    // =========================================================

    private static void agregarTituloSeccion(
            Document documento,
            String titulo) throws Exception {

        Font fuente = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                10,
                VERDE);


        PdfPTable tabla =
                new PdfPTable(1);

        tabla.setWidthPercentage(100);


        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                titulo,
                                fuente));

        celda.setBorder(
                Rectangle.BOTTOM);

        celda.setBorderColor(DORADO);
        celda.setBorderWidth(0.7f);

        celda.setPaddingBottom(5);

        tabla.addCell(celda);

        documento.add(tabla);
    }


    // =========================================================
    // DATOS SIN CUADRÍCULAS PESADAS
    // =========================================================

    private static void agregarDatoElegante(
            PdfPTable tabla,
            String nombre,
            String valor,
            Font etiqueta,
            Font texto) {

        PdfPCell izquierda =
                new PdfPCell(
                        new Phrase(
                                nombre,
                                etiqueta));

        izquierda.setBorder(
                Rectangle.NO_BORDER);

        izquierda.setPaddingTop(5);
        izquierda.setPaddingBottom(5);

        tabla.addCell(izquierda);


        PdfPCell derecha =
                new PdfPCell(
                        new Phrase(
                                valor,
                                texto));

        derecha.setBorder(
                Rectangle.NO_BORDER);

        derecha.setPaddingTop(5);
        derecha.setPaddingBottom(5);

        tabla.addCell(derecha);
    }


    // =========================================================
    // ENCABEZADO DE TABLA
    // =========================================================

    private static void agregarEncabezadoTabla(
            PdfPTable tabla,
            String nombre) {

        Font fuente = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                8,
                Color.WHITE);


        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                nombre,
                                fuente));

        celda.setBackgroundColor(VERDE);

        celda.setBorderColor(VERDE);

        celda.setPaddingTop(7);
        celda.setPaddingBottom(7);

        celda.setHorizontalAlignment(
                Element.ALIGN_CENTER);

        celda.setVerticalAlignment(
                Element.ALIGN_MIDDLE);

        tabla.addCell(celda);
    }


    // =========================================================
    // CELDA DEL DETALLE
    // =========================================================

    private static void agregarCeldaSuave(
            PdfPTable tabla,
            String valor) {

        Font fuente = FontFactory.getFont(
                FontFactory.HELVETICA,
                8,
                Color.BLACK);


        PdfPCell celda =
                new PdfPCell(
                        new Phrase(
                                valor,
                                fuente));

        celda.setBackgroundColor(
                Color.WHITE);

        celda.setBorderColor(
                new Color(220, 213, 195));

        celda.setPaddingTop(7);
        celda.setPaddingBottom(7);
        celda.setPaddingLeft(5);
        celda.setPaddingRight(5);

        celda.setVerticalAlignment(
                Element.ALIGN_MIDDLE);

        tabla.addCell(celda);
    }


    // =========================================================
    // CAJAS DEL RESUMEN DEL REPORTE
    // =========================================================

    private static void agregarCajaResumen(
            PdfPTable tabla,
            String nombre,
            String valor,
            Font etiqueta,
            Font numero) {

        PdfPCell celda =
                new PdfPCell();

        celda.setBackgroundColor(
                Color.WHITE);

        celda.setBorderColor(
                DORADO);

        celda.setBorderWidth(0.7f);

        celda.setPadding(12);


        Paragraph titulo =
                new Paragraph(
                        nombre,
                        etiqueta);

        titulo.setAlignment(
                Element.ALIGN_CENTER);


        Paragraph dato =
                new Paragraph(
                        valor,
                        numero);

        dato.setAlignment(
                Element.ALIGN_CENTER);

        dato.setSpacingBefore(5);


        celda.addElement(titulo);
        celda.addElement(dato);

        tabla.addCell(celda);
    }


    // =========================================================
    // NOMBRE DE PERSONA
    // =========================================================

    private static String nombrePersona(
            Persona persona) {

        if (persona == null) {
            return "No registrado";
        }

        return persona.getNombreCompleto();
    }
}