package parroquia;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

import java.awt.Image;
import java.awt.Font;
import java.awt.Color;

public class EquipoDesarrollo extends JFrame {

    public EquipoDesarrollo() {

        setTitle("SIGREP - Equipo de desarrollo");
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // COLORES DEL DISEÑO
        Color crema = new Color(248, 245, 235);
        Color verde = new Color(34, 82, 61);
        Color dorado = new Color(190, 145, 55);
        Color blanco = new Color(255, 255, 255);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(crema);
        setContentPane(panel);

        // TÍTULO
        JLabel titulo = new JLabel(
                "EQUIPO DE DESARROLLO - SIGREP",
                SwingConstants.CENTER);

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(verde);
        titulo.setBounds(190, 25, 600, 40);
        panel.add(titulo);

        // SUBTÍTULO
        JLabel subtitulo = new JLabel(
                "Sistema Integrado de Gestión de Registros Parroquiales",
                SwingConstants.CENTER);

        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(dorado);
        subtitulo.setBounds(190, 65, 600, 25);
        panel.add(subtitulo);

        String[] fotos = {
            "/imagen/integrante 1.jpeg",
            "/imagen/integrante 2.jpeg",
            "/imagen/integrante 3.jpeg",
            "/imagen/integrante 4.jpeg",
            "/imagen/integrante 5.jpeg"
        };

        String[] nombres = {
            "<html><center>Kilyan Valentino<br>Montoya Alván</center></html>",
            "<html><center>Percy José<br>Ortega Sandoval</center></html>",
            "<html><center>Camila Yamilet<br>Moncada Llocclla</center></html>",
            "<html><center>Aaron Estéfano<br>Vasquez Rodriguez</center></html>",
            "<html><center>Reena Chadni<br>Arroyo Quispe</center></html>"
        };

        String[] codigos = {
            "N00525599",
            "N00525833",
            "N00553558",
            "N00545540",
            "N00532273"
        };

        // ROLES
        String[] roles = {
            "Desarrollador",
            "Desarrollador",
            "Desarrolladora",
            "Desarrollador",
            "Desarrolladora"
        };

        int x = 25;

        for (int i = 0; i < fotos.length; i++) {

            // TARJETA
            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(null);
            tarjeta.setBackground(blanco);
            tarjeta.setBounds(x, 115, 175, 350);

            tarjeta.setBorder(
                BorderFactory.createLineBorder(dorado, 2)
            );

            panel.add(tarjeta);

            // FOTO
            ImageIcon fotoOriginal = new ImageIcon(
                    getClass().getResource(fotos[i]));

            Image fotoEscalada = fotoOriginal.getImage()
                    .getScaledInstance(
                            145,
                            175,
                            Image.SCALE_SMOOTH);

            JLabel foto = new JLabel(
                    new ImageIcon(fotoEscalada));

            foto.setBounds(15, 15, 145, 175);
            tarjeta.add(foto);

            // NOMBRE
            JLabel nombre = new JLabel(
                    nombres[i],
                    SwingConstants.CENTER);

            nombre.setFont(
                    new Font("Segoe UI", Font.BOLD, 13));

            nombre.setForeground(verde);
            nombre.setBounds(5, 200, 165, 45);
            tarjeta.add(nombre);

            // CÓDIGO
            JLabel codigo = new JLabel(
                    "Código: " + codigos[i],
                    SwingConstants.CENTER);

            codigo.setFont(
                    new Font("Segoe UI", Font.PLAIN, 12));

            codigo.setBounds(5, 255, 165, 20);
            tarjeta.add(codigo);

            // ROL
            JLabel rol = new JLabel(
                    roles[i],
                    SwingConstants.CENTER);

            rol.setFont(
                    new Font("Segoe UI", Font.BOLD, 12));

            rol.setForeground(dorado);
            rol.setBounds(5, 285, 165, 25);
            tarjeta.add(rol);

            x += 190;
        }

        // TEXTO INFERIOR
        JLabel pie = new JLabel(
                "Proyecto académico SIGREP",
                SwingConstants.CENTER);

        pie.setFont(
                new Font("Segoe UI", Font.ITALIC, 12));

        pie.setForeground(verde);
        pie.setBounds(290, 510, 400, 25);
        panel.add(pie);
    }
}