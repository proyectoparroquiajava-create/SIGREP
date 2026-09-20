package parroquia;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VentanaPrincipal extends JFrame {

    private final RepositorioDatos repositorio = RepositorioDatos.getInstancia();
    private final Map<String, Persona> personasPorClave = new HashMap<>();

    private JPanel contentPane;

    // --- Seccion: registro de personas ---
    private JTextField txtNombre;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtDireccionCargo;
    private JComboBox<String> comboRol;

    // --- Seccion: tabla de personas registradas ---
    private JTable tablaPersonas;
    private DefaultTableModel modeloTabla;

    // --- Seccion: registro de inscripcion (todos los sacramentos) ---
    private JComboBox<String> comboTipoSacramento;
    private JComboBox<String> comboBeneficiario;
    private JComboBox<String> comboSacerdote;
    private JLabel lblCampoExtra1;
    private JLabel lblCampoExtra2;
    private JLabel lblCampoExtra3;
    private JComboBox<String> comboCampoExtra1;
    private JComboBox<String> comboCampoExtra2;
    private JComboBox<String> comboCampoExtra3;
    private JLabel lblCupos;
    private JLabel lblCuposDetalle;
    private JTextField txtCupos;
    private JTextField txtCosto;
    private JTextField txtDiasParaFecha;

    // --- Seccion: resultados / reportes ---
    private JTextArea areaResultado;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaPrincipal frame = new VentanaPrincipal();
                frame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public VentanaPrincipal() {
        setTitle("SIGREP(Sistema Integrado De Gestion de Registros Parroquiales)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 976, 650);

        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        construirSeccionRegistroPersona();
        construirSeccionTablaPersonas();
        construirSeccionInscripcion();
        construirSeccionResultados();
    }

    private void construirSeccionRegistroPersona() {
        JLabel lblTitulo1 = new JLabel("1. Registrar persona (feligres o sacerdote)");
        lblTitulo1.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTitulo1.setBounds(10, 10, 350, 20);
        contentPane.add(lblTitulo1);

        JLabel lblNombre = new JLabel("Nombre completo:");
        lblNombre.setBounds(10, 40, 120, 14);
        contentPane.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(140, 37, 200, 20);
        contentPane.add(txtNombre);

        JLabel lblDni = new JLabel("DNI (8 digitos):");
        lblDni.setBounds(10, 65, 120, 14);
        contentPane.add(lblDni);

        txtDni = new JTextField();
        txtDni.setBounds(140, 62, 200, 20);
        contentPane.add(txtDni);

        JLabel lblTelefono = new JLabel("Telefono:");
        lblTelefono.setBounds(10, 90, 120, 14);
        contentPane.add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(140, 87, 200, 20);
        contentPane.add(txtTelefono);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(10, 115, 120, 14);
        contentPane.add(lblRol);

        comboRol = new JComboBox<>(new String[] { "Feligres", "Sacerdote" });
        comboRol.setBounds(140, 112, 200, 22);
        contentPane.add(comboRol);

        JLabel lblDireccionCargo = new JLabel("Dirección:");
        lblDireccionCargo.setBounds(10, 140, 260, 14);
        contentPane.add(lblDireccionCargo);

        txtDireccionCargo = new JTextField();
        txtDireccionCargo.setBounds(10, 160, 328, 20);
        contentPane.add(txtDireccionCargo);

        JButton btnRegistrarPersona = new JButton("Registrar persona");
        btnRegistrarPersona.setBounds(10, 190, 142, 28);
        btnRegistrarPersona.addActionListener(this::alRegistrarPersona);
        contentPane.add(btnRegistrarPersona);
    }

    private void construirSeccionTablaPersonas() {
        JLabel lblTablaPersonas = new JLabel("Personas registradas");
        lblTablaPersonas.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTablaPersonas.setBounds(380, 10, 250, 20);
        contentPane.add(lblTablaPersonas);

        modeloTabla = new DefaultTableModel(new Object[] { "Rol", "Nombre", "DNI", "Telefono", "Dirección" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPersonas = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaPersonas);
        scrollTabla.setBounds(380, 39, 470, 115);
        contentPane.add(scrollTabla);

        JButton btnModificarPersona = new JButton("Modificar");
        btnModificarPersona.setBounds(380, 160, 120, 25);
        btnModificarPersona.addActionListener(this::alModificarPersona);
        contentPane.add(btnModificarPersona);

        JButton btnEliminarPersona = new JButton("Eliminar");
        btnEliminarPersona.setBounds(510, 160, 120, 25);
        btnEliminarPersona.addActionListener(this::alEliminarPersona);
        contentPane.add(btnEliminarPersona);
    }

    private void construirSeccionInscripcion() {
        JLabel lblTitulo2 = new JLabel("2. Registrar inscripcion a sacramento y emitir recibo");
        lblTitulo2.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTitulo2.setBounds(10, 230, 333, 20);
        contentPane.add(lblTitulo2);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(10, 260, 100, 14);
        contentPane.add(lblTipo);

        comboTipoSacramento = new JComboBox<>(new String[] { "Bautizo", "Matrimonio", "Retiro", "Primera Comunión", "Confirmación" });
        comboTipoSacramento.setBounds(120, 256, 200, 22);
        comboTipoSacramento.addActionListener(this::alCambiarTipoSacramento);
        contentPane.add(comboTipoSacramento);

        JLabel lblBeneficiario = new JLabel("Beneficiario:");
        lblBeneficiario.setBounds(10, 285, 100, 14);
        contentPane.add(lblBeneficiario);

        comboBeneficiario = new JComboBox<>();
        comboBeneficiario.setBounds(120, 281, 200, 22);
        contentPane.add(comboBeneficiario);

        JLabel lblCosto = new JLabel("Costo (S/):");
        lblCosto.setBounds(10, 310, 100, 14);
        contentPane.add(lblCosto);

        txtCosto = new JTextField("80.0");
        txtCosto.setBounds(120, 307, 100, 20);
        contentPane.add(txtCosto);

        JLabel lblDias = new JLabel("Dias desde hoy:");
        lblDias.setBounds(10, 335, 100, 14);
        contentPane.add(lblDias);

        txtDiasParaFecha = new JTextField("15");
        txtDiasParaFecha.setBounds(120, 332, 100, 20);
        txtDiasParaFecha.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarCuposDisponibles();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarCuposDisponibles();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarCuposDisponibles();
            }
        });

        contentPane.add(txtDiasParaFecha);

        lblCupos = new JLabel("Cupos totales:");
        lblCupos.setBounds(10, 360, 120, 14);
        contentPane.add(lblCupos);

        txtCupos = new JTextField("40");
        txtCupos.setBounds(120, 357, 100, 20);
        contentPane.add(txtCupos);

        lblCuposDetalle = new JLabel("");
        lblCuposDetalle.setBounds(250, 360, 200, 14);
        contentPane.add(lblCuposDetalle);

        lblCampoExtra1 = new JLabel("Padrino:");
        lblCampoExtra1.setBounds(380, 197, 100, 14);
        contentPane.add(lblCampoExtra1);

        comboCampoExtra1 = new JComboBox<>();
        comboCampoExtra1.setBounds(490, 193, 360, 22);
        contentPane.add(comboCampoExtra1);

        lblCampoExtra2 = new JLabel("Madrina:");
        lblCampoExtra2.setBounds(380, 221, 100, 14);
        contentPane.add(lblCampoExtra2);

        comboCampoExtra2 = new JComboBox<>();
        comboCampoExtra2.setBounds(490, 217, 360, 22);
        contentPane.add(comboCampoExtra2);

        lblCampoExtra3 = new JLabel("Testigo 2:");
        lblCampoExtra3.setBounds(380, 248, 100, 14);
        contentPane.add(lblCampoExtra3);

        comboCampoExtra3 = new JComboBox<>();
        comboCampoExtra3.setBounds(490, 244, 360, 22);
        contentPane.add(comboCampoExtra3);

        JLabel lblSacerdote = new JLabel("Sacerdote:");
        lblSacerdote.setBounds(380, 281, 100, 14);
        contentPane.add(lblSacerdote);

        comboSacerdote = new JComboBox<>();
        comboSacerdote.setBounds(490, 271, 360, 28);
        contentPane.add(comboSacerdote);

        JButton btnActualizarCombos = new JButton("Actualizar listas");
        btnActualizarCombos.setBounds(10, 390, 129, 28);
        btnActualizarCombos.addActionListener(this::alActualizarCombos);
        contentPane.add(btnActualizarCombos);

        JButton btnCrearInscripcion = new JButton("Crear inscripcion y emitir recibo");
        btnCrearInscripcion.setBounds(149, 390, 190, 28);
        btnCrearInscripcion.addActionListener(this::alCrearInscripcion);
        contentPane.add(btnCrearInscripcion);

        actualizarCamposSegunTipo();
    }

    private void construirSeccionResultados() {
        JLabel lblTitulo3 = new JLabel("3. Reportes (programacion funcional: filter, map, reduce)");
        lblTitulo3.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTitulo3.setBounds(10, 430, 450, 20);
        contentPane.add(lblTitulo3);

        JButton btnReporteTipo = new JButton("Reporte del tipo seleccionado");
        btnReporteTipo.setBounds(10, 458, 190, 28);
        btnReporteTipo.addActionListener(this::alGenerarReportePorTipo);
        contentPane.add(btnReporteTipo);

        JButton btnTotalRecaudado = new JButton("Calcular total recaudado");
        btnTotalRecaudado.setBounds(210, 458, 160, 28);
        btnTotalRecaudado.addActionListener(this::alCalcularTotal);
        contentPane.add(btnTotalRecaudado);

        JButton btnGuardarRespaldo = new JButton("Guardar respaldo");
        btnGuardarRespaldo.setBounds(380, 461, 142, 28);
        btnGuardarRespaldo.addActionListener(this::alGuardarRespaldo);
        contentPane.add(btnGuardarRespaldo);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);

        JScrollPane scrollResultado = new JScrollPane(areaResultado);
        scrollResultado.setBounds(10, 495, 860, 105);
        contentPane.add(scrollResultado);
        
        JLabel lblNewLabel = new JLabel("imagen");
        lblNewLabel.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/imagen/imagen 3.jpeg")));
        lblNewLabel.setBounds(654, 310, 196, 176);
        contentPane.add(lblNewLabel);
    }

    // ------------------------------------------------------------
    // Manejadores de eventos (event handlers)
    // ------------------------------------------------------------

    private void alRegistrarPersona(ActionEvent evento) {
        try {
            String nombre = txtNombre.getText().trim();
            String dni = txtDni.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String direccionCargo = txtDireccionCargo.getText().trim();
            String rol = (String) comboRol.getSelectedItem();

            Persona nuevaPersona = "Feligres".equals(rol)
                    ? new Feligres(nombre, dni, telefono, direccionCargo)
                    : new Sacerdote(nombre, dni, telefono, direccionCargo);

            repositorio.registrarPersona(nuevaPersona);
            personasPorClave.put(claveDe(nuevaPersona), nuevaPersona);

            modeloTabla.addRow(new Object[] { rol, nuevaPersona.getNombreCompleto(), dni, telefono, direccionCargo });
            actualizarCombosDePersonas();
            limpiarFormularioPersona();

            JOptionPane.showMessageDialog(this, "Persona registrada correctamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

        } catch (ExcepcionSistemaParroquial error) {
            JOptionPane.showMessageDialog(this, error.getMessage(),
                    "Error de validacion", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alModificarPersona(ActionEvent evento) {
        int fila = tablaPersonas.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una persona de la tabla.",
                    "Modificar persona",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rolActual = modeloTabla.getValueAt(fila, 0).toString();
        String nombreActual = modeloTabla.getValueAt(fila, 1).toString();
        String dniActual = modeloTabla.getValueAt(fila, 2).toString();
        String telefonoActual = modeloTabla.getValueAt(fila, 3).toString();
        String direccionActual = modeloTabla.getValueAt(fila, 4).toString();

        String nuevoNombre = JOptionPane.showInputDialog(this, "Nombre completo:", nombreActual);

        if (nuevoNombre == null) {
            return;
        }

        String nuevoDni = JOptionPane.showInputDialog(this, "DNI:", dniActual);

        if (nuevoDni == null) {
            return;
        }

        String nuevoTelefono = JOptionPane.showInputDialog(this, "Teléfono:", telefonoActual);

        if (nuevoTelefono == null) {
            return;
        }

        String nuevaDireccion = JOptionPane.showInputDialog(this, "Dirección:", direccionActual);

        if (nuevaDireccion == null) {
            return;
        }

        try {
            Persona personaActualizada;

            if ("Feligres".equals(rolActual)) {
                personaActualizada = new Feligres(nuevoNombre, nuevoDni, nuevoTelefono, nuevaDireccion);
            } else {
                personaActualizada = new Sacerdote(nuevoNombre, nuevoDni, nuevoTelefono, nuevaDireccion);
            }

            repositorio.actualizarPersona(dniActual, personaActualizada);

            String claveAnterior = null;

            for (Map.Entry<String, Persona> entrada : personasPorClave.entrySet()) {
                if (entrada.getValue().getDni().equals(dniActual)) {
                    claveAnterior = entrada.getKey();
                    break;
                }
            }

            if (claveAnterior != null) {
                personasPorClave.remove(claveAnterior);
            }

            personasPorClave.put(claveDe(personaActualizada), personaActualizada);

            modeloTabla.setValueAt(nuevoNombre, fila, 1);
            modeloTabla.setValueAt(nuevoDni, fila, 2);
            modeloTabla.setValueAt(nuevoTelefono, fila, 3);
            modeloTabla.setValueAt(nuevaDireccion, fila, 4);

            actualizarCombosDePersonas();

            JOptionPane.showMessageDialog(this,
                    "Persona modificada correctamente.",
                    "Modificación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (ExcepcionSistemaParroquial error) {
            JOptionPane.showMessageDialog(this,
                    error.getMessage(),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alEliminarPersona(ActionEvent evento) {
        int fila = tablaPersonas.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una persona de la tabla.",
                    "Eliminar persona",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = modeloTabla.getValueAt(fila, 1).toString();
        String dni = modeloTabla.getValueAt(fila, 2).toString();

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar a " + nombre + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            repositorio.eliminarPersona(dni);

            String claveEliminar = null;

            for (Map.Entry<String, Persona> entrada : personasPorClave.entrySet()) {
                if (entrada.getValue().getDni().equals(dni)) {
                    claveEliminar = entrada.getKey();
                    break;
                }
            }

            if (claveEliminar != null) {
                personasPorClave.remove(claveEliminar);
            }

            modeloTabla.removeRow(fila);
            actualizarCombosDePersonas();

            JOptionPane.showMessageDialog(this,
                    "Persona eliminada correctamente.",
                    "Eliminación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (ExcepcionSistemaParroquial error) {
            JOptionPane.showMessageDialog(this,
                    error.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alActualizarCombos(ActionEvent evento) {
        actualizarCombosDePersonas();
    }

    private void alCambiarTipoSacramento(ActionEvent evento) {
        actualizarCamposSegunTipo();
    }

    private void alCrearInscripcion(ActionEvent evento) {
        try {
            Persona beneficiario = personasPorClave.get((String) comboBeneficiario.getSelectedItem());

            if (beneficiario == null) {
                throw new DatosInvalidosException("Debe registrar y seleccionar un beneficiario.");
            }

            Persona personaSacerdote =
                    personasPorClave.get((String) comboSacerdote.getSelectedItem());

            if (!(personaSacerdote instanceof Sacerdote)) {
                throw new DatosInvalidosException(
                        "Debe seleccionar un sacerdote encargado.");
            }

            Sacerdote sacerdote = (Sacerdote) personaSacerdote;

            double costo = Double.parseDouble(txtCosto.getText().trim());
            int dias = Integer.parseInt(txtDiasParaFecha.getText().trim());
            LocalDate fecha = LocalDate.now().plusDays(dias);
            String tipoUi = (String) comboTipoSacramento.getSelectedItem();

            Sacramento sacramento = crearSacramentoSegunTipo(tipoUi, beneficiario, sacerdote, fecha, costo);
            Inscripcion inscripcion = new Inscripcion(sacramento);
            var recibo = inscripcion.emitirRecibo();
            repositorio.registrarInscripcion(inscripcion);
            actualizarCuposDisponibles();

            String texto = "Inscripcion creada.\n" + inscripcion.aLineaTexto()
                    + "\nRecibo emitido: " + recibo.numero() + " por S/ " + String.format("%.2f", recibo.monto());

            if (sacramento instanceof Retiro) {
                texto += "\nCupos disponibles en esa fecha: "
                        + repositorio.cuposDisponiblesRetiroEn(fecha)
                        + " de " + ((Retiro) sacramento).getCuposTotales();
            }

            areaResultado.setText(texto);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El costo, los dias y los cupos deben ser numeros validos.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);

        } catch (ClassCastException ex) {
            JOptionPane.showMessageDialog(this,
                    "Matrimonio y retiro requieren feligreses como beneficiario (y contrayente en matrimonio).",
                    "Error de validacion",
                    JOptionPane.ERROR_MESSAGE);

        } catch (ExcepcionSistemaParroquial error) {
            JOptionPane.showMessageDialog(this,
                    error.getMessage(),
                    "Error de validacion",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Sacramento crearSacramentoSegunTipo(String tipoUi, Persona beneficiario,
            Sacerdote sacerdote, LocalDate fecha, double costo) {

        if ("Bautizo".equals(tipoUi)) {
            Persona padrino =
                    personasPorClave.get((String) comboCampoExtra1.getSelectedItem());

            Persona madrina =
                    personasPorClave.get((String) comboCampoExtra2.getSelectedItem());

            if (padrino == null || madrina == null) {
                throw new DatosInvalidosException(
                        "Debe seleccionar padrino y madrina.");
            }

            if (padrino.equals(beneficiario)) {
                throw new DatosInvalidosException(
                        "El beneficiario no puede ser su propio padrino.");
            }

            if (madrina.equals(beneficiario)) {
                throw new DatosInvalidosException(
                        "El beneficiario no puede ser su propia madrina.");
            }

            if (padrino.equals(madrina)) {
                throw new DatosInvalidosException(
                        "El padrino y la madrina no pueden ser la misma persona.");
            }

            if (sacerdote.equals(padrino) || sacerdote.equals(madrina)) {
                throw new DatosInvalidosException(
                        "El sacerdote encargado no puede ser padrino ni madrina del mismo bautizo.");
            }

            return SacramentoFactory.crear(
                    "bautizo",
                    beneficiario,
                    sacerdote,
                    fecha,
                    costo,
                    padrino,
                    madrina);
        }

        if ("Matrimonio".equals(tipoUi)) {
            Persona contrayente =
                    personasPorClave.get((String) comboCampoExtra1.getSelectedItem());

            Persona testigo1 =
                    personasPorClave.get((String) comboCampoExtra2.getSelectedItem());

            Persona testigo2 =
                    personasPorClave.get((String) comboCampoExtra3.getSelectedItem());

            if (contrayente == null || testigo1 == null || testigo2 == null) {
                throw new DatosInvalidosException(
                        "Debe seleccionar contrayente y dos testigos.");
            }

            if (beneficiario.equals(contrayente)) {
                throw new DatosInvalidosException(
                        "Los contrayentes no pueden ser la misma persona.");
            }

            if (beneficiario.equals(testigo1)
                    || beneficiario.equals(testigo2)) {
                throw new DatosInvalidosException(
                        "Un contrayente no puede ser su propio testigo.");
            }

            if (contrayente.equals(testigo1)
                    || contrayente.equals(testigo2)) {
                throw new DatosInvalidosException(
                        "Un contrayente no puede ser su propio testigo.");
            }

            if (testigo1.equals(testigo2)) {
                throw new DatosInvalidosException(
                        "Una misma persona no puede ser ambos testigos.");
            }

            if (sacerdote.equals(beneficiario)
                    || sacerdote.equals(contrayente)
                    || sacerdote.equals(testigo1)
                    || sacerdote.equals(testigo2)) {
                throw new DatosInvalidosException(
                        "El sacerdote encargado no puede ocupar otro rol en el mismo matrimonio.");
            }

            if (!(beneficiario instanceof Feligres)
                    || !(contrayente instanceof Feligres)) {
                throw new DatosInvalidosException(
                        "El beneficiario y el contrayente deben ser feligreses.");
            }

            List<Persona> testigos = new ArrayList<>();
            testigos.add(testigo1);
            testigos.add(testigo2);

            return SacramentoFactory.crear(
                    "matrimonio",
                    beneficiario,
                    sacerdote,
                    fecha,
                    costo,
                    contrayente,
                    testigos);
        }

        if ("Primera Comunión".equals(tipoUi)) {
            Persona padrino =
                    personasPorClave.get((String) comboCampoExtra1.getSelectedItem());

            Persona madrina =
                    personasPorClave.get((String) comboCampoExtra2.getSelectedItem());

            if (!(beneficiario instanceof Feligres)) {
                throw new DatosInvalidosException(
                        "El beneficiario de Primera Comunión debe ser un feligrés.");
            }

            if (padrino == null && madrina == null) {
                throw new DatosInvalidosException(
                        "Debe seleccionar un padrino o una madrina.");
            }

            if (padrino != null && madrina != null) {
                throw new DatosInvalidosException(
                        "Debe seleccionar solo un padrino o una madrina, no ambos.");
            }

            Persona acompanante = padrino != null ? padrino : madrina;

            if (beneficiario.equals(acompanante)) {
                throw new DatosInvalidosException(
                        "El beneficiario no puede ser su propio padrino o madrina.");
            }

            if (sacerdote.equals(acompanante)) {
                throw new DatosInvalidosException(
                        "El sacerdote encargado no puede ser padrino o madrina.");
            }

            return SacramentoFactory.crear(
                    "primera comunión",
                    beneficiario,
                    sacerdote,
                    fecha,
                    costo,
                    acompanante);
        }

        if ("Confirmación".equals(tipoUi)) {
            Persona acompanante =
                    personasPorClave.get((String) comboCampoExtra1.getSelectedItem());

            if (acompanante == null) {
                throw new DatosInvalidosException(
                        "Debe seleccionar un padrino o una madrina.");
            }

            if (!(beneficiario instanceof Feligres)) {
                throw new DatosInvalidosException(
                        "El beneficiario de Confirmación debe ser un feligrés.");
            }

            if (beneficiario.equals(acompanante)) {
                throw new DatosInvalidosException(
                        "El beneficiario no puede ser su propio padrino o madrina.");
            }

            if (sacerdote.equals(acompanante)) {
                throw new DatosInvalidosException(
                        "El sacerdote encargado no puede ser padrino o madrina.");
            }

            return SacramentoFactory.crear(
                    "confirmación",
                    beneficiario,
                    sacerdote,
                    fecha,
                    costo,
                    acompanante);
        }

        int cuposTotales = cuposTotalesParaRetiro(fecha);

        if (!(beneficiario instanceof Feligres)) {
            throw new DatosInvalidosException(
                    "El beneficiario de un retiro debe ser un feligres.");
        }

        if (repositorio.capacidadRetiroEn(fecha) != null
                && repositorio.cuposDisponiblesRetiroEn(fecha) <= 0) {
            throw new DatosInvalidosException(
                    "No hay cupos disponibles para el retiro de esa fecha.");
        }

        return SacramentoFactory.crear(
                "retiro",
                beneficiario,
                sacerdote,
                fecha,
                costo,
                cuposTotales);
    }

    private int cuposTotalesParaRetiro(LocalDate fecha) {
        Integer capacidadExistente = repositorio.capacidadRetiroEn(fecha);

        if (capacidadExistente != null) {
            return capacidadExistente;
        }

        return Integer.parseInt(txtCupos.getText().trim());
    }

    private void alGenerarReportePorTipo(ActionEvent evento) {
        String tipoUi = (String) comboTipoSacramento.getSelectedItem();
        String tipoFiltro = tipoFiltroDe(tipoUi);

        Reportes.ReporteTipo reporte =
                Reportes.generarReportePorTipo(repositorio.getInscripciones(), tipoFiltro);

        StringBuilder texto = new StringBuilder();

        texto.append("Reporte de ").append(tipoUi.toLowerCase())
                .append(" - cantidad: ").append(reporte.cantidad()).append("\n");

        for (String linea : reporte.detalle()) {
            texto.append(" - ").append(linea).append("\n");
        }

        texto.append("Total recaudado en ").append(tipoUi.toLowerCase())
                .append(": S/ ").append(String.format("%.2f", reporte.totalRecaudado()));

        areaResultado.setText(texto.toString());
    }

    private void alCalcularTotal(ActionEvent evento) {
        double total = Reportes.calcularTotalRecaudado(repositorio.getInscripciones());

        areaResultado.setText(
                String.format("Total recaudado (todas las inscripciones): S/ %.2f", total));
    }

    private void alGuardarRespaldo(ActionEvent evento) {
        try {
            var ruta = repositorio.guardarRespaldo("respaldo_parroquia.txt");

            areaResultado.setText(
                    "Respaldo guardado en: " + ruta.toAbsolutePath());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el respaldo: " + ex.getMessage(),
                    "Error de escritura",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ------------------------------------------------------------
    // Utilidades internas de la ventana
    // ------------------------------------------------------------

    private String claveDe(Persona persona) {
        return persona.getNombreCompleto() + " (" + persona.getDni() + ")";
    }

    private void actualizarCombosDePersonas() {
        comboBeneficiario.removeAllItems();
        comboCampoExtra1.removeAllItems();
        comboCampoExtra2.removeAllItems();
        comboCampoExtra3.removeAllItems();
        comboSacerdote.removeAllItems();

        comboCampoExtra1.addItem("-- Ninguno --");
        comboCampoExtra2.addItem("-- Ninguno --");
        comboCampoExtra3.addItem("-- Ninguno --");

        for (String clave : personasPorClave.keySet()) {
            Persona persona = personasPorClave.get(clave);

            comboBeneficiario.addItem(clave);
            comboCampoExtra1.addItem(clave);
            comboCampoExtra2.addItem(clave);
            comboCampoExtra3.addItem(clave);

            if (persona instanceof Sacerdote) {
                comboSacerdote.addItem(clave);
            }
        }
    }

    private void actualizarCamposSegunTipo() {
        String tipo = (String) comboTipoSacramento.getSelectedItem();

        boolean esBautizo = "Bautizo".equals(tipo);
        boolean esMatrimonio = "Matrimonio".equals(tipo);
        boolean esRetiro = "Retiro".equals(tipo);
        boolean esPrimeraComunion = "Primera Comunión".equals(tipo);
        boolean esConfirmacion = "Confirmación".equals(tipo);

        boolean usaPadrino = esBautizo || esPrimeraComunion || esConfirmacion;

        lblCampoExtra1.setVisible(usaPadrino || esMatrimonio);
        comboCampoExtra1.setVisible(usaPadrino || esMatrimonio);

        lblCampoExtra2.setVisible(esBautizo || esMatrimonio || esPrimeraComunion);
        comboCampoExtra2.setVisible(esBautizo || esMatrimonio || esPrimeraComunion);

        lblCampoExtra3.setVisible(esMatrimonio);
        comboCampoExtra3.setVisible(esMatrimonio);

        lblCupos.setVisible(esRetiro);
        txtCupos.setVisible(esRetiro);
        lblCuposDetalle.setVisible(esRetiro);

        if (esBautizo) {
            lblCampoExtra1.setText("Padrino:");
            lblCampoExtra2.setText("Madrina:");
            txtCosto.setText("80.0");

        } else if (esMatrimonio) {
            lblCampoExtra1.setText("Contrayente:");
            lblCampoExtra2.setText("Testigo 1:");
            lblCampoExtra3.setText("Testigo 2:");
            txtCosto.setText("350.0");

        } else if (esPrimeraComunion) {
            lblCampoExtra1.setText("Padrino:");
            lblCampoExtra2.setText("Madrina:");
            txtCosto.setText("80.0");

        } else if (esConfirmacion) {
            lblCampoExtra1.setText("Padrino/Madrina:");
            txtCosto.setText("80.0");

        } else {
            txtCosto.setText("45.0");
            actualizarCuposDisponibles();
        }
    }

    private void actualizarCuposDisponibles() {
        if (comboTipoSacramento == null
                || !"Retiro".equals(comboTipoSacramento.getSelectedItem())) {
            return;
        }

        LocalDate fecha = fechaProgramadaDesdeFormulario();

        if (fecha == null) {
            return;
        }

        Integer capacidad = repositorio.capacidadRetiroEn(fecha);

        if (capacidad == null) {
            boolean veniaDeRetiroExistente = !txtCupos.isEditable();

            txtCupos.setEditable(true);
            lblCupos.setText("Cupos totales:");
            lblCuposDetalle.setText("Aun no hay inscritos");

            if (veniaDeRetiroExistente) {
                txtCupos.setText("40");
            }

            return;
        }

        int disponibles = repositorio.cuposDisponiblesRetiroEn(fecha);
        int ocupados = repositorio.cuposOcupadosRetiroEn(fecha);

        txtCupos.setText(String.valueOf(disponibles));
        txtCupos.setEditable(false);
        lblCupos.setText("Cupos disponibles:");
        lblCuposDetalle.setText(ocupados + " ocupado(s) de " + capacidad);
    }

    private LocalDate fechaProgramadaDesdeFormulario() {
        try {
            int dias = Integer.parseInt(txtDiasParaFecha.getText().trim());

            return LocalDate.now().plusDays(dias);

        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String tipoFiltroDe(String tipoUi) {
        if ("Matrimonio".equals(tipoUi)) {
            return "matrimonio";
        }

        if ("Retiro".equals(tipoUi)) {
            return "retiro";
        }

        if ("Primera Comunión".equals(tipoUi)) {
            return "primera comunión";
        }

        if ("Confirmación".equals(tipoUi)) {
            return "confirmación";
        }

        return "bautizo";
    }

    private void limpiarFormularioPersona() {
        txtNombre.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtDireccionCargo.setText("");
    }
}