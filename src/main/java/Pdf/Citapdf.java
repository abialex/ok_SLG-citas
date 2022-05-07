/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pdf;

import Entidades.Cita;
import Entidades.Doctor;

import Entidades.HoraAtencion;
import EntidadesSettings.SettingsDoctor;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import Pdf.style.style2;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.layout.border.SolidBorder;
import controller.App;
import java.awt.Label;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.VerticalAlignment;
import java.util.Collections;
import javax.swing.JOptionPane;

/**
 *
 * @author yalle
 */
public class Citapdf {

    public static String ImprimirCita(Doctor odoctor, LocalDate fecha) {
        LocalDate fechaInicio = fecha.minusDays(fecha.getDayOfWeek().getValue() - 1);
        LocalDate fechaFin = fechaInicio.plusDays(5);
        List<HoraAtencion> listHoraatencion = App.jpa.createQuery("select p from HoraAtencion p order by idhoraatencion ASC").setMaxResults(10).getResultList();
        List<Cita> listCita = App.jpa.createQuery("select p from Cita p  where "
                + "iddoctor=" + odoctor.getIddoctor() + " and "
                + " fechacita between" + "'" + fechaInicio.toString() + "' and '"
                + fechaFin.toString() + "'"
                + " order by minuto asc").getResultList();
        int volumen = 163;
        PdfWriter writer = null;
        String urlWrite = "Pdf\\cita_de_" + odoctor.getNombredoctor() + "_" + fechaInicio + "-" + fechaFin + ".pdf";
        try {
            writer = new PdfWriter(urlWrite);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new Label(), "agregue la carpeta Pdf");
        }
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(10, 10, 10, 10);

        style2 evento = new style2(document);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, evento);
        PdfFont bold = null, font = null;
        try {
            /*--------styles-------------*/
            font = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException ex) {
            Logger.getLogger(Citapdf.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*---------------------Color----------*/
        Color prueba = new DeviceRgb(0, 204, 204);
        Color colorAzul = new DeviceRgb(255, 178, 102);
        Color colorSubtitulo = Color.BLACK;
        Color colorNegro = new DeviceRgb(0, 204, 204);
        Color colorRojo = Color.RED;
        Color colorBlanco = Color.WHITE;

        /*----------------Style letras---------*/
        Style styleCell = new Style().setBorder(Border.NO_BORDER);
        Style styleTextRight = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Style styleTextLeft = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
        Style styleTextCenter = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(10f);
        Style styleTextLeft7 = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(7f);
        Style styleTextCenterRojo = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(10f).setFontColor(colorRojo).setFont(bold);
        Style styleTextCenterVertical = new Style().setVerticalAlignment(VerticalAlignment.MIDDLE);
        Style styleTextCenter8 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(8f).setFont(bold);
        /*----------------Border--------------*/
        Border subrayado = new SolidBorder(0.5f);
        Border subrayadoNo = Border.NO_BORDER;

        /*-----------------Palabras default-----------*/
        String palabra1 = "desc.";
        String palabra2 = "no presenta";

        /*----------------Palabras vacías-------------*/
        Paragraph palabraEnBlancoLimpio = new Paragraph(".").setFontColor(colorBlanco);
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(0.5f));
        /*---------FIN----Palabras vacías-------------*/

 /* Contenido del documento  página 1*/
        //table raya
        Table TableRayas = new Table(new float[]{volumen * 5});
        Cell cellraya = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
        TableRayas.addCell(cellraya);
        //Cabecera
        Table CabeceraParrafo1 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo1.addCell(getCell("Inicio:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo1.addCell(getCell(fechaInicio + "", styleTextCenter, styleCell, subrayado));

        Table CabeceraParrafo2 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo2.addCell(getCell("Fin:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo2.addCell(getCell(fechaFin + "", styleTextCenter, styleCell, subrayado));

        Image imgUp = null;
        try {
            imgUp = new Image(ImageDataFactory.create("images\\logoUp.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Citapdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        Cell cellimagUp = new Cell().add(imgUp.setAutoScale(true)).setBorder(Border.NO_BORDER);
        /* new SolidBorder(Color.BLACK,1*/

        Table TableHC = new Table(new float[]{volumen * 5});
        TableHC.addCell(new Cell().add(CabeceraParrafo1).addStyle(styleCell));
        TableHC.addCell(new Cell().add(CabeceraParrafo2).addStyle(styleCell));

        Table Cabecera = new Table(new float[]{volumen * 1.75f, volumen * 1.5f, volumen * 1.75f});
        Cabecera.addCell(getCell("Dr(a). " + odoctor.getDoctor().getNombredoctor(), styleTextCenter, styleCell, subrayadoNo).addStyle(styleTextCenterVertical));
        Cabecera.addCell(new Cell().add(cellimagUp.setPaddingTop(-5)).addStyle(styleCell));
        Cabecera.addCell(new Cell().add(TableHC).addStyle(styleCell));
        Cabecera.setMarginBottom(2.5f);

        //Fin Cabecera
        // Tabla Doctor
        Table tableSemana = new Table(new float[]{volumen * 0.2f, volumen * 0.8f, volumen * 0.8f, volumen * 0.8f, volumen * 0.8f, volumen * 0.8f, volumen * 0.8f});
        tableSemana.addCell(new Cell().add(new Paragraph("Hora").setFont(bold).addStyle(styleTextCenter)));
        tableSemana.addCell(new Cell().add(new Paragraph("LUNES \n" + fechaInicio.plusDays(0)).setFont(bold).addStyle(styleTextCenter)));
        tableSemana.addCell(new Cell().add(new Paragraph("MARTES\n" + fechaInicio.plusDays(1)).setFont(bold).addStyle(styleTextCenter)));
        tableSemana.addCell(new Cell().add(new Paragraph("MIÉRCOLES\n" + fechaInicio.plusDays(2)).setFont(bold).addStyle(styleTextCenter)));
        tableSemana.addCell(new Cell().add(new Paragraph("JUEVES\n" + fechaInicio.plusDays(3)).setFont(bold).addStyle(styleTextCenter)));
        tableSemana.addCell(new Cell().add(new Paragraph("VIERNES\n" + fechaInicio.plusDays(4)).setFont(bold).addStyle(styleTextCenter)));
        tableSemana.addCell(new Cell().add(new Paragraph("SÁBADO\n" + fechaInicio.plusDays(5)).setFont(bold).addStyle(styleTextCenter)));

        for (HoraAtencion ohora : listHoraatencion) {
            tableSemana.addCell(getCell(ohora.getHora() + " " + ohora.getAbreviatura(), styleTextCenter8, styleTextCenter8, subrayadoNo));
            for (int i = 0; i < 6; i++) {
                Table TableHoraDia = new Table(new float[]{volumen * 0.8f});
                boolean aux = true;
                LocalDate fechaCom = fechaInicio.plusDays(i);
                for (Cita cita : listCita) {
                    if (cita.getFechacita().equals(fechaCom) && cita.getHoraatencion() == ohora) {
                        aux = false;
                        if (cita.getNombrepaciente() != null) {
                            String datos = cita.getNombrepaciente();
                            if (datos.length() > 11) {
                                datos = datos.substring(0, 11);
                                datos = datos + "...";
                            }
                            TableHoraDia.addCell(getCell(cita.getHoraatencion().getHora() + ":" + cita.getMinuto() + " " + datos + " : " + cita.getRazon(), styleTextLeft7, styleCell, subrayadoNo));

                        } else {
                            TableHoraDia.addCell(getCell("OCUPADO", styleTextCenterRojo, styleCell, subrayadoNo));
                        }

                    }
                }
                if (aux) {
                    TableHoraDia.addCell(getCell("", styleTextLeft7, styleCell, subrayadoNo));
                    TableHoraDia.addCell(getCell("", styleTextLeft7, styleCell, subrayadoNo));
                    TableHoraDia.addCell(getCell("", styleTextLeft7, styleCell, subrayadoNo));

                }
                tableSemana.addCell(new Cell().add(TableHoraDia));
            }

        }

        /* Cuerpo del documentos*/
        document.add(Cabecera);
        document.add(tableSemana);

        document.close();
        /*----Fin Cuerpo del documentos-----*/
        return urlWrite;
    }

    public static String ImprimirCitaHoy(Doctor odoctor, LocalDate fecha, String tipo) {
        List<HoraAtencion> listHoraatencion = App.jpa.createQuery("select p from HoraAtencion p order by idhoraatencion ASC").setMaxResults(10).getResultList();
        List<Cita> listCita = App.jpa.createQuery("select p from Cita p  where "
                + "iddoctor=" + odoctor.getIddoctor() + " and "
                + " fechacita =" + "'" + fecha.toString() + "'"
                + " order by minuto asc").getResultList();
        int volumen = 115;
        PdfWriter writer = null;
        String urlWrite = "Pdf\\cita_de_" + odoctor.getNombredoctor() + "_" + fecha + "_" + tipo + ".pdf";
        try {
            writer = new PdfWriter(urlWrite);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new Label(), "agregue la carpeta Pdf");
        }
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(10, 10, 10, 10);

        style2 evento = new style2(document);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, evento);
        PdfFont bold = null, font = null;
        try {
            /*--------styles-------------*/
            font = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException ex) {
            Logger.getLogger(Citapdf.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*---------------------Color----------*/
        Color prueba = new DeviceRgb(0, 204, 204);
        Color colorAzul = new DeviceRgb(255, 178, 102);
        Color colorSubtitulo = Color.BLACK;
        Color colorNegro = new DeviceRgb(0, 204, 204);
        Color colorRojo = Color.RED;
        Color colorBlanco = Color.WHITE;

        /*----------------Style letras---------*/
        Style styleCell = new Style().setBorder(Border.NO_BORDER);
        Style styleTextRight = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Style styleTextLeft = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
        Style styleTextCenter = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(10f);
        Style styleTextLeft9 = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(9f);
        Style styleTextCenterRojo = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(13f).setFontColor(colorRojo).setFont(bold);
        Style styleTextCenterVertical = new Style().setVerticalAlignment(VerticalAlignment.MIDDLE);
        Style styleTextCenter8 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(8f).setFont(bold);
        Style styleTextCenter13 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(13f);
        /*----------------Border--------------*/
        Border subrayado = new SolidBorder(0.5f);
        Border subrayadoNo = Border.NO_BORDER;

        /*-----------------Palabras default-----------*/
        String palabra1 = "desc.";
        String palabra2 = "no presenta";

        /*----------------Palabras vacías-------------*/
        Paragraph palabraEnBlancoLimpio = new Paragraph(".").setFontColor(colorBlanco);
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(0.5f));
        /*---------FIN----Palabras vacías-------------*/

 /* Contenido del documento  página 1*/
        //table raya
        Table TableRayas = new Table(new float[]{volumen * 5});
        Cell cellraya = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
        TableRayas.addCell(cellraya);
        //Cabecera
        Table CabeceraParrafo1 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo1.addCell(getCell("Inicio:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo1.addCell(getCell(fecha + "", styleTextCenter, styleCell, subrayado));

        Table CabeceraParrafo2 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo2.addCell(getCell("Fin:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo2.addCell(getCell(fecha + "", styleTextCenter, styleCell, subrayado));

        Image imgUp = null;
        try {
            imgUp = new Image(ImageDataFactory.create("images\\logoUp.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Citapdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        Cell cellimagUp = new Cell().add(imgUp.setAutoScale(true)).setBorder(Border.NO_BORDER);
        /* new SolidBorder(Color.BLACK,1*/

        Table TableHC = new Table(new float[]{volumen * 5});
        TableHC.addCell(new Cell().add(CabeceraParrafo1).addStyle(styleCell));
        TableHC.addCell(new Cell().add(CabeceraParrafo2).addStyle(styleCell));

        Table Cabecera = new Table(new float[]{volumen * 1.75f, volumen * 1.5f, volumen * 1.75f});
        Cabecera.addCell(getCell("Dr(a). " + odoctor.getDoctor().getNombredoctor(), styleTextCenter, styleCell, subrayadoNo).addStyle(styleTextCenterVertical));
        Cabecera.addCell(new Cell().add(cellimagUp.setPaddingTop(-5)).addStyle(styleCell));
        Cabecera.addCell(new Cell().add(TableHC).addStyle(styleCell));
        Cabecera.setMarginBottom(2.5f);

        //Fin Cabecera
        // Tabla Doctor
        Table tableSemana = new Table(new float[]{volumen * 0.5f, volumen * 4.5f});
        tableSemana.addCell(new Cell().add(new Paragraph("Hora").setFont(bold).addStyle(styleTextCenter)));
        tableSemana.addCell(new Cell().add(new Paragraph(tipo + "\n" + fecha.plusDays(0)).setFont(bold).addStyle(styleTextCenter)));

        for (HoraAtencion ohora : listHoraatencion) {
            tableSemana.addCell(getCell(ohora.getHora() + " " + ohora.getAbreviatura(), styleTextCenter13, styleTextCenter8, subrayadoNo)).setMarginBottom(10f).setMarginTop(10f);
            for (int i = 0; i < 1; i++) {
                Table TableHoraDia = new Table(new float[]{volumen * 1.115f, volumen * 1.115f, volumen * 1.115f, volumen * 1.115f});
                boolean aux = true;
                LocalDate fechaCom = fecha.plusDays(i);
                for (Cita cita : listCita) {
                    if (cita.getFechacita().equals(fechaCom) && cita.getHoraatencion() == ohora) {
                        aux = false;
                        if (cita.getNombrepaciente() != null) {
                            String datos = cita.getNombrepaciente();
                            if (datos.length() > 15) {
                                datos = datos.substring(0, 15);
                                datos = datos + "...";
                            }
                            TableHoraDia.addCell(getCell(cita.getHoraatencion().getHora() + ":" + cita.getMinuto() + " " + datos + "\n" + cita.getRazon(), styleTextLeft9, styleCell, subrayadoNo));

                        } else {
                            TableHoraDia.addCell(new Cell(1, 4).add(getCell("OCUPADO", styleTextCenterRojo, styleCell, subrayadoNo)).addStyle(styleCell));
                        }

                    }
                }
                if (aux) {
                    TableHoraDia.addCell(getCell("", styleTextLeft9, styleCell, subrayadoNo));
                    TableHoraDia.addCell(getCell("", styleTextLeft9, styleCell, subrayadoNo));
                    TableHoraDia.addCell(getCell("", styleTextLeft9, styleCell, subrayadoNo));
                    TableHoraDia.addCell(getCell("", styleTextLeft9, styleCell, subrayadoNo));
                    TableHoraDia.addCell(getCell("", styleTextLeft9, styleCell, subrayadoNo));

                }
                tableSemana.addCell(new Cell().add(TableHoraDia));

            }
        }

        /* Cuerpo del documentos*/
        document.add(Cabecera);
        document.add(tableSemana);

        document.close();
        /*----Fin Cuerpo del documentos-----*/

        return urlWrite;
    }

    public static String ImprimirCitaDoctores(LocalDate fecha) {
        List<HoraAtencion> listHoraatencion = App.jpa.createQuery("select p from HoraAtencion p order by idhoraatencion ASC").setMaxResults(10).getResultList();
        List<Cita> listCita = App.jpa.createQuery("select p from Cita p  where "
                + " fechacita =" + "'" + fecha.toString() + "'"
                + " order by minuto asc").getResultList();
        List<SettingsDoctor> listsettings = App.jpa.createQuery("select p from SettingsDoctor p ").getResultList();
        int volumen = 115;
        PdfWriter writer = null;
        String urlWrite = "Pdf\\cita_de_doctores_" + fecha + ".pdf";
        try {
            writer = new PdfWriter(urlWrite);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new Label(), "agregue la carpeta Pdf");
        }
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(10, 10, 10, 10);

        style2 evento = new style2(document);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, evento);
        PdfFont bold = null, font = null;
        try {
            /*--------styles-------------*/
            font = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException ex) {
            Logger.getLogger(Citapdf.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*---------------------Color----------*/
        Color prueba = new DeviceRgb(0, 204, 204);
        Color colorAzul = new DeviceRgb(255, 178, 102);
        Color colorSubtitulo = Color.BLACK;
        Color colorNegro = new DeviceRgb(0, 204, 204);
        Color colorRojo = Color.RED;
        Color colorBlanco = Color.WHITE;

        /*----------------Style letras---------*/
        Style styleCell = new Style().setBorder(Border.NO_BORDER);
        Style styleTextRight = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Style styleTextLeft = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
        Style styleTextCenter = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(8f);
        Style styleTextLeft7 = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(7.5f);
        Style styleTextCenterRojo = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(13f).setFontColor(colorRojo).setFont(bold);
        Style styleTextCenterVertical = new Style().setVerticalAlignment(VerticalAlignment.MIDDLE);
        Style styleTextCenter8 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(8f).setFont(bold);
        Style styleTextCenter13 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(13f);
        /*----------------Border--------------*/
        Border subrayado = new SolidBorder(0.5f);
        Border subrayadoNo = Border.NO_BORDER;

        /*-----------------Palabras default-----------*/
        String palabra1 = "desc.";
        String palabra2 = "no presenta";

        /*----------------Palabras vacías-------------*/
        Paragraph palabraEnBlancoLimpio = new Paragraph(".").setFontColor(colorBlanco);
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(0.5f));
        /*---------FIN----Palabras vacías-------------*/

 /* Contenido del documento  página 1*/
        //table raya
        Table TableRayas = new Table(new float[]{volumen * 5});
        Cell cellraya = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
        TableRayas.addCell(cellraya);
        //Cabecera
        Table CabeceraParrafo1 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo1.addCell(getCell("Inicio:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo1.addCell(getCell(fecha + "", styleTextCenter, styleCell, subrayado));

        Table CabeceraParrafo2 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo2.addCell(getCell("Fin:", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo2.addCell(getCell(fecha + "", styleTextCenter, styleCell, subrayado));

        Image imgUp = null;
        try {
            imgUp = new Image(ImageDataFactory.create("images\\logoUp.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Citapdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        Cell cellimagUp = new Cell().add(imgUp.setAutoScale(true)).setBorder(Border.NO_BORDER);
        /* new SolidBorder(Color.BLACK,1*/

        Table TableHC = new Table(new float[]{volumen * 5});
        TableHC.addCell(new Cell().add(CabeceraParrafo1).addStyle(styleCell));
        TableHC.addCell(new Cell().add(CabeceraParrafo2).addStyle(styleCell));

        Table Cabecera = new Table(new float[]{volumen * 1.75f, volumen * 1.5f, volumen * 1.75f});
        Cabecera.addCell(getCell("DOCTORES", styleTextCenter, styleCell, subrayadoNo).addStyle(styleTextCenterVertical));
        Cabecera.addCell(new Cell().add(cellimagUp.setPaddingTop(-5)).addStyle(styleCell));
        Cabecera.addCell(new Cell().add(TableHC).addStyle(styleCell));
        Cabecera.setMarginBottom(2.5f);

        //Fin Cabecera
        // Tabla Doctor
        Table tableSemana = new Table(new float[]{volumen * 0.5f, volumen * 0.9f, volumen * 0.9f, volumen * 0.9f, volumen * 0.9f, volumen * 0.9f});
        tableSemana.addCell(new Cell().add(new Paragraph("Hora").setFont(bold).addStyle(styleTextCenter)));
        Collections.sort(listsettings);
        int tamanio = listsettings.size();
        for (int i = 0; i < 5 - tamanio; i++) {
            listsettings.add(new SettingsDoctor(new Doctor("NINGUNO"), "Z"));
        }
        for (SettingsDoctor settingsDoctor : listsettings) {
            tableSemana.addCell(new Cell().add(new Paragraph(settingsDoctor.getDoctor().getNombredoctor()).setFont(bold).addStyle(styleTextCenter)));
        }

        for (HoraAtencion ohora : listHoraatencion) {
            tableSemana.addCell(getCell(ohora.getHora() + " " + ohora.getAbreviatura(), styleTextCenter13, styleTextCenter8, subrayadoNo)).setMarginBottom(10f).setMarginTop(10f);
            for (SettingsDoctor settingsDoctor : listsettings) {
                Table TableHoraDia = new Table(new float[]{volumen * 0.89f});
                boolean aux = true;
                for (Cita cita : listCita) {
                    if (cita.getDoctor().equals(settingsDoctor.getDoctor()) && cita.getHoraatencion() == ohora) {
                        aux = false;
                        if (cita.getNombrepaciente() != null) {
                            String datos = cita.getNombrepaciente();
                            if (datos.length() > 15) {
                                datos = datos.substring(0, 15);
                                datos = datos + "...";
                            }
                            TableHoraDia.addCell(getCell(cita.getHoraatencion().getHora() + ":" + cita.getMinuto() + " " + datos + " : " + cita.getRazon(), styleTextLeft7, styleCell, subrayadoNo));

                        } else {
                            TableHoraDia.addCell(getCell("OCUPADO", styleTextCenterRojo, styleCell, subrayadoNo));
                        }

                    }
                }
                if (aux) {
                    TableHoraDia.addCell(getCell("", styleTextLeft7, styleCell, subrayadoNo));
                    TableHoraDia.addCell(getCell("", styleTextLeft7, styleCell, subrayadoNo));
                    TableHoraDia.addCell(getCell("", styleTextLeft7, styleCell, subrayadoNo));
                    TableHoraDia.addCell(getCell("", styleTextLeft7, styleCell, subrayadoNo));

                }
                tableSemana.addCell(new Cell().add(TableHoraDia));

            }
        }

        /* Cuerpo del documentos*/
        document.add(Cabecera);
        document.add(tableSemana);

        document.close();
        /*----Fin Cuerpo del documentos-----*/

        return urlWrite;
    }

    static Table getTable(String cadena, int volumen, Paragraph palabraEnBlanco, Style styleCell, Style styleTextLeft) {
        Table TablePrincipal = new Table(new float[]{volumen * 5});
        Table TableParrafo = new Table(new float[]{volumen * 5});
        Paragraph oParagrah;
        int numCharacteres = 108;
        int iteracion = cadena.length() / numCharacteres;
        for (int i = 0; i < iteracion; i++) {
            oParagrah = new Paragraph(cadena.substring(i * numCharacteres, (i + 1) * numCharacteres));
            Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableParrafo.addCell(cellParrafo1);
        }
        oParagrah = cadena.isEmpty() ? palabraEnBlanco : new Paragraph(cadena.substring(cadena.length() - cadena.length() % numCharacteres, cadena.length()));;
        Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableParrafo.addCell(cellParrafo1);
        for (int i = 0; i < 2 - iteracion; i++) {
            Cell cellraya = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
            TableParrafo.addCell(cellraya);
        }
        TablePrincipal.addCell(new Cell().add(TableParrafo).addStyle(styleCell));
        return TablePrincipal;
    }

    public static Table getTableField(String cadena, String titulo, float tamfield, float tamfield2, int volumen, Color colorNegro, Style styleCell, Style styleTextLeft, Paragraph palabraEnBlanco) {
        Table TableAtributo = new Table(new float[]{volumen * tamfield, volumen * tamfield2});
        Table TableParrafo = new Table(new float[]{volumen * 5});
        Paragraph oParagrah;
        int subnumCharacteres = 75;
        int numCharacteres = 100;
        int iteracion = cadena.length() / numCharacteres;
        int division = cadena.length() / subnumCharacteres;
        boolean aux = division == 0;
        Cell cellDi = new Cell().add(new Paragraph(titulo).setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableAtributo.addCell(cellDi);
        Paragraph oParagra = cadena.length() == 0 ? palabraEnBlanco : new Paragraph(aux ? cadena : cadena.substring(0 * numCharacteres, (0 + 1) * subnumCharacteres));
        Cell cellParraf = new Cell().add(oParagra.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableAtributo.addCell(cellParraf);
        TableParrafo.addCell(new Cell().add(TableAtributo).addStyle(styleCell));
        for (int i = 1; i < iteracion; i++) {
            oParagrah = new Paragraph(cadena.substring(i * numCharacteres, (i + 1) * numCharacteres));
            Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableParrafo.addCell(cellParrafo1);
        }
        oParagrah = aux ? palabraEnBlanco : new Paragraph(cadena.substring(cadena.length() - cadena.length() % numCharacteres, cadena.length()));;
        Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableParrafo.addCell(cellParrafo1);
        if (iteracion == 1 || iteracion == 0) {
            Cell cellraya2 = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
            TableParrafo.addCell(cellraya2);
        }
        return TableParrafo;
    }

    //si está vacío lo agrega en blanco
    static Cell getCell(String palabra, Style posicion, Style border, Border subrayado) {
        Color colorBlanco = Color.WHITE;
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(0.5f));
        Paragraph Parapalabra = palabra.isEmpty() ? palabraEnBlanco : new Paragraph(palabra);

        return new Cell().add(Parapalabra.setBorderBottom(subrayado)).addStyle(posicion).addStyle(border);
    }

}
