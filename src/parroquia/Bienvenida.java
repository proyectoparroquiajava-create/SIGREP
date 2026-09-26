package parroquia;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;

public class Bienvenida extends JFrame {

    public Bienvenida() {

        setTitle("SIGREP - Sistema Parroquial");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        ImageIcon imagenOriginal = new ImageIcon(
                getClass().getResource("/imagen/bienvenida.jpeg"));

        Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(
                1200, 700, Image.SCALE_SMOOTH);

        JLabel lblFondo = new JLabel(new ImageIcon(imagenEscalada));
        lblFondo.setBounds(0, 0, 1200, 700);
        JButton btnIniciar = new JButton();
        btnIniciar.setBounds(350, 400, 500, 100);
        btnIniciar.setOpaque(false);
        btnIniciar.setContentAreaFilled(false);
        btnIniciar.setBorderPainted(false);
        btnIniciar.setFocusPainted(false);

        btnIniciar.addActionListener(e -> {
            Login login = new Login();
            login.setVisible(true);
            dispose();
        });

        add(btnIniciar);
        add(lblFondo);

    }

    public static void main(String[] args) {
        Bienvenida ventana = new Bienvenida();
        ventana.setVisible(true);
    }
}

