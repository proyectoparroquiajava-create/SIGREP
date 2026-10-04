package parroquia;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
public class Login extends JFrame {
	
	private int intentos = 0;
	private static final String CORREO = "proyectoparroquia.java@gmail.com";
	private static final String CLAVE_APP = "jvxdgbbnlvbosrrb";
	private String contrasenaActual = "1234";
    
    public Login() {
    	 setLayout(null);

         JLabel lblUsuario = new JLabel("Usuario:");
         lblUsuario.setBounds(50, 50, 100, 25);
         add(lblUsuario);

         JTextField txtUsuario = new JTextField();
         txtUsuario.setBounds(150, 50, 180, 25);
         add(txtUsuario);

         JLabel lblContrasena = new JLabel("Contraseña:");
         lblContrasena.setBounds(50, 90, 100, 25);
         add(lblContrasena);

         JPasswordField txtContrasena = new JPasswordField();
         txtContrasena.setBounds(150, 90, 180, 25);
         add(txtContrasena);

         JButton btnIngresar = new JButton("Iniciar sesión");
         btnIngresar.setBounds(125, 140, 150, 30);
         add(btnIngresar);
         
         JButton btnOlvideContrasena = new JButton("¿Olvidaste tu contraseña?");
         btnOlvideContrasena.setBounds(100, 180, 200, 30);
         add(btnOlvideContrasena);
         
         btnOlvideContrasena.addActionListener(e -> {

        	    String correo = JOptionPane.showInputDialog(this,
        	            "Ingrese el correo asociado al sistema:");
        	    if (correo != null && correo.equalsIgnoreCase("proyectoparroquia.java@gmail.com")) {

        	        JOptionPane.showMessageDialog(this,
        	                "Correo verificado correctamente.");
        	        int codigo = (int)(Math.random() * 900000) + 100000;
        	        Properties propiedades = new Properties();
        	        propiedades.put("mail.smtp.auth", "true");
        	        propiedades.put("mail.smtp.starttls.enable", "true");
        	        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        	        propiedades.put("mail.smtp.port", "587");

        	        Session sesion = Session.getInstance(propiedades, new Authenticator() {
        	            @Override
        	            protected PasswordAuthentication getPasswordAuthentication() {
        	                return new PasswordAuthentication(CORREO, CLAVE_APP);
        	            }
        	        });

        	        try {
        	            Message mensaje = new MimeMessage(sesion);
        	            mensaje.setFrom(new InternetAddress(CORREO));
        	            mensaje.setRecipients(Message.RecipientType.TO,
        	                    InternetAddress.parse(CORREO));
        	            mensaje.setSubject("Código de recuperación - SIGREP");
        	            mensaje.setText("Su código de recuperación es: " + codigo);

        	            Transport.send(mensaje);

        	            JOptionPane.showMessageDialog(this,
        	                    "Se envió un código de recuperación al correo.");
        	            String codigoIngresado = JOptionPane.showInputDialog(this,
        	                    "Ingrese el código recibido en su correo:");

        	            if (codigoIngresado != null &&
        	                    codigoIngresado.equals(String.valueOf(codigo))) {

        	                JOptionPane.showMessageDialog(this,
        	                        "Código verificado correctamente.");
        	                JPasswordField txtNuevaContrasena = new JPasswordField();
        	                JPasswordField txtConfirmarContrasena = new JPasswordField();

        	                Object[] campos = {
        	                        "Nueva contraseña:", txtNuevaContrasena,
        	                        "Confirmar contraseña:", txtConfirmarContrasena
        	                };

        	                int opcion = JOptionPane.showConfirmDialog(this,
        	                        campos,
        	                        "Cambiar contraseña",
        	                        JOptionPane.OK_CANCEL_OPTION,
        	                        JOptionPane.PLAIN_MESSAGE);

        	                if (opcion == JOptionPane.OK_OPTION) {

        	                    String nuevaContrasena =
        	                            new String(txtNuevaContrasena.getPassword());

        	                    String confirmarContrasena =
        	                            new String(txtConfirmarContrasena.getPassword());

        	                    if (nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {

        	                        JOptionPane.showMessageDialog(this,
        	                                "No puede haber campos vacíos.",
        	                                "Datos incompletos",
        	                                JOptionPane.WARNING_MESSAGE);

        	                    } else if (!nuevaContrasena.equals(confirmarContrasena)) {

        	                        JOptionPane.showMessageDialog(this,
        	                                "Las contraseñas no coinciden.",
        	                                "Contraseñas diferentes",
        	                                JOptionPane.ERROR_MESSAGE);

        	                    } else {

        	                        contrasenaActual = nuevaContrasena;

        	                        JOptionPane.showMessageDialog(this,
        	                                "Contraseña actualizada correctamente.");

        	                        intentos = 0;
        	                        btnIngresar.setEnabled(true);
        	                        txtContrasena.setText("");
        	                    }
        	                }

        	            } else if (codigoIngresado != null) {

        	                JOptionPane.showMessageDialog(this,
        	                        "El código ingresado es incorrecto.",
        	                        "Código incorrecto",
        	                        JOptionPane.ERROR_MESSAGE);
        	            }

        	        } catch (Exception ex) {

        	            JOptionPane.showMessageDialog(this,
        	                    "No se pudo enviar el código al correo.",
        	                    "Error de envío",
        	                    JOptionPane.ERROR_MESSAGE);

        	            ex.printStackTrace();
        	        }
           

        	    } else if (correo != null) {

        	        JOptionPane.showMessageDialog(this,
        	                "El correo ingresado no está asociado al sistema.",
        	                "Correo incorrecto",
        	                JOptionPane.ERROR_MESSAGE);
        	    }

        	});
         
         btnIngresar.addActionListener(e -> {

        	 String usuario = txtUsuario.getText().trim();
        	    String contrasena = new String(txtContrasena.getPassword());

        	    if (usuario.equals("Mesadepartes") && contrasena.equals(contrasenaActual)) {

        	        JOptionPane.showMessageDialog(this,
        	                "Inicio de sesión correcto.");

        	        VentanaPrincipal principal = new VentanaPrincipal();
        	        principal.setVisible(true);
        	        dispose();

        	    } else {
        	        intentos++;

        	        if (intentos >= 3) {

        	            JOptionPane.showMessageDialog(this,
        	                    "Ha superado el número máximo de intentos.",
        	                    "Acceso bloqueado",
        	                    JOptionPane.ERROR_MESSAGE);

        	            btnIngresar.setEnabled(false);

        	        } else {

        	            JOptionPane.showMessageDialog(this,
        	                    "Usuario o contraseña incorrectos. Intento "
        	                    + intentos + " de 3.",
        	                    "Datos incorrectos",
        	                    JOptionPane.WARNING_MESSAGE);
        	        }
        	    }
        	});
         
        setTitle("SIGREP - Inicio de sesión");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
   
}