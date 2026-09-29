package parroquia;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import javax.swing.*;

public class Calendario {

    private static final String[] MESES = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public static void mostrarCalendario(List<Inscripcion> inscripciones) {
        if (inscripciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay eventos registrados.");
            return;
        }

        JComboBox<Integer> comboAnios = new JComboBox<>();

        for (Inscripcion inscripcion : inscripciones) {
            int anio = inscripcion.getSacramento().getFechaProgramada().getYear();
            boolean existe = false;

            for (int i = 0; i < comboAnios.getItemCount(); i++) {
                if (comboAnios.getItemAt(i) == anio) {
                    existe = true;
                }
            }

            if (!existe) {
                comboAnios.addItem(anio);
            }
        }

        JPanel panel = new JPanel();
        panel.add(new JLabel("Seleccione un año:"));
        panel.add(comboAnios);

        int respuesta = JOptionPane.showConfirmDialog(null, panel,
                "Calendario de eventos",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (respuesta == JOptionPane.OK_OPTION) {
            int anio = (int) comboAnios.getSelectedItem();
            mostrarMeses(inscripciones, anio);
        }
    }

    private static void mostrarMeses(List<Inscripcion> inscripciones, int anio) {
        JPanel panelMeses = new JPanel(new GridLayout(0, 3, 10, 10));

        for (int mes = 1; mes <= 12; mes++) {
            boolean tieneEventos = false;

            for (Inscripcion inscripcion : inscripciones) {
                LocalDate fecha = inscripcion.getSacramento().getFechaProgramada();

                if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                    tieneEventos = true;
                }
            }

            if (tieneEventos) {
                final int mesSeleccionado = mes;
                JButton botonMes = new JButton(MESES[mes - 1]);

                botonMes.addActionListener(e ->
                    mostrarMes(inscripciones, anio, mesSeleccionado)
                );

                panelMeses.add(botonMes);
            }
        }

        JOptionPane.showMessageDialog(null, panelMeses,
                "Eventos del año " + anio,
                JOptionPane.PLAIN_MESSAGE);
    }

    private static void mostrarMes(List<Inscripcion> inscripciones, int anio, int mes) {
        JPanel calendario = new JPanel(new GridLayout(0, 7, 5, 5));

        String[] diasSemana = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};

        for (String nombre : diasSemana) {
            calendario.add(new JLabel(nombre, SwingConstants.CENTER));
        }

        YearMonth yearMonth = YearMonth.of(anio, mes);
        LocalDate primerDia = LocalDate.of(anio, mes, 1);
        int espacios = primerDia.getDayOfWeek().getValue() - 1;

        for (int i = 0; i < espacios; i++) {
            calendario.add(new JLabel(""));
        }

        for (int dia = 1; dia <= yearMonth.lengthOfMonth(); dia++) {
            final int diaSeleccionado = dia;
            JButton botonDia = new JButton(String.valueOf(dia));
            boolean tieneEvento = false;

            for (Inscripcion inscripcion : inscripciones) {
                LocalDate fecha = inscripcion.getSacramento().getFechaProgramada();

                if (fecha.getYear() == anio &&
                    fecha.getMonthValue() == mes &&
                    fecha.getDayOfMonth() == dia) {
                    tieneEvento = true;
                }
            }

            if (tieneEvento) {
                botonDia.setBackground(new Color(144, 238, 144));
                botonDia.setOpaque(true);

                botonDia.addActionListener(e ->
                    mostrarEventosDelDia(inscripciones, anio, mes, diaSeleccionado)
                );
            } else {
                botonDia.setEnabled(false);
            }

            calendario.add(botonDia);
        }

        JOptionPane.showMessageDialog(null, calendario,
                MESES[mes - 1] + " " + anio,
                JOptionPane.PLAIN_MESSAGE);
    }

    private static void mostrarEventosDelDia(List<Inscripcion> inscripciones,
                                              int anio, int mes, int dia) {
        String informacion = "";

        for (Inscripcion inscripcion : inscripciones) {
            Sacramento sacramento = inscripcion.getSacramento();
            LocalDate fecha = sacramento.getFechaProgramada();

            if (fecha.getYear() == anio &&
                fecha.getMonthValue() == mes &&
                fecha.getDayOfMonth() == dia) {

                informacion += "Sacramento: " + sacramento.tipo() + "\n";
                informacion += "Beneficiario: " +
                        sacramento.getBeneficiario().getNombreCompleto() + "\n";
                informacion += "Hora: " + sacramento.getHora() + "\n";
                informacion += "Sacerdote: " +
                        sacramento.getSacerdote().getNombreCompleto() + "\n";
                informacion += "-----------------------------\n";
            }
        }

        JOptionPane.showMessageDialog(null, informacion,
                "Eventos del " + dia + "/" + mes + "/" + anio,
                JOptionPane.INFORMATION_MESSAGE);
    }
}