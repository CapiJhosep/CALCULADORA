package calculadora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Calculadora extends JFrame {
    private static final long serialVersionUID = 1L;
    JTextField txtDisplay;
    String operador;
    double num1;
    boolean numNuevo;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Calculadora frame = new Calculadora();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Calculadora() {
        setTitle("Calculadora");
		ImageIcon icono = new ImageIcon(getClass().getResource("calculadora.jpg"));
		setIconImage(icono.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 400, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(false);

        txtDisplay = new JTextField();
        txtDisplay.setEditable(false);
        txtDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        txtDisplay.setFont(new Font("Segoe UI", Font.BOLD, 32));  
        txtDisplay.setBackground(new Color(30, 30, 30));  
        txtDisplay.setForeground(Color.WHITE);  
        txtDisplay.setBorder(new EmptyBorder(20, 20, 20, 20));  
        add(txtDisplay, BorderLayout.NORTH);

        JPanel pnlCalculadora = new JPanel();
        pnlCalculadora.setLayout(new GridLayout(6, 4, 10, 10));  
        pnlCalculadora.setBackground(new Color(40, 40, 40));  
        add(pnlCalculadora, BorderLayout.CENTER);

        String[] botones = {
            "%", "CE", "C", "←",
            "1/x", "x²", "√x", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
        };
        
        for (String texto : botones) {
            JButton boton = crearBoton(texto);
            pnlCalculadora.add(boton);
        }
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setForeground(Color.WHITE);
        boton.setOpaque(true);
        boton.setBorderPainted(false); 

        if (texto.matches("[0-9]")) {
            boton.setFont(new Font("Segoe UI", Font.BOLD, 20));
            boton.setBackground(new Color(70, 70, 70)); 
        } else if ("=".equals(texto)) {
            boton.setBackground(new Color(0, 102, 204));
        } else if ("C CE ← %".contains(texto)) {
            boton.setBackground(new Color(50, 50, 50));
        } else if ("1/x".equals(texto) || "x²".equals(texto) || "√x".equals(texto)) {
            boton.setFont(new Font("Segoe UI", Font.ITALIC, 18)); 
            boton.setBackground(new Color(60, 60, 60));
        } else {
            boton.setBackground(new Color(60, 60, 60));
        }

        boton.setBorder(new EmptyBorder(15, 15, 15, 15));

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                boton.setBackground(boton.getBackground().brighter()); 
            }

            public void mouseExited(MouseEvent evt) {
                if (texto.matches("[0-9	]")) {
                    boton.setBackground(new Color(70, 70, 70));
                } else if ("=".equals(texto)) {
                    boton.setBackground(new Color(0, 105, 205));
                } else if ("C CE ← %".contains(texto)) {
                    boton.setBackground(new Color(50, 50, 50));
                } else if ("1/x".equals(texto) || "x²".equals(texto) || "√x".equals(texto)) {
                    boton.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                    boton.setBackground(new Color(60, 60, 60));
                } else {
                	boton.setBackground(new Color(60, 60, 60));
                }
            }
        });

        boton.addActionListener(new BotonListener());
        return boton;
    }


    private class BotonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String comando = e.getActionCommand();

            if (comando.matches("[0-9]")) {
                usarNumero(comando);
            } else if (comando.equals(".")) {
                usarDecimal();
            } else if (comando.equals("C")) {
                resetearCalculadora();
            } else if (comando.equals("CE")) {
                limpiarEntrada();
            } else if (comando.equals("←")) {
                borrarDigito();
            } else if (comando.equals("=")) {
                calcularResultado();
            } else {
                usarOperador(comando);
            }
        }

        private void usarNumero(String comando) {
            if (numNuevo) {
                txtDisplay.setText(comando);
                numNuevo = false;
            } else {
                txtDisplay.setText(txtDisplay.getText() + comando);
            }
        }

        private void usarDecimal() {
            if (numNuevo) {
                txtDisplay.setText("0.");
                numNuevo = false;
            } else if (!txtDisplay.getText().contains(".")) {
                txtDisplay.setText(txtDisplay.getText() + ".");
            }
        }

        private void resetearCalculadora() {
            txtDisplay.setText("");
            num1 = 0;
            operador = "";
            numNuevo = true;
        }

        private void limpiarEntrada() {
            txtDisplay.setText("");
        }

        private void borrarDigito() {
            String textoActual = txtDisplay.getText();
            if (!textoActual.isEmpty()) {
                txtDisplay.setText(textoActual.substring(0, textoActual.length() - 1));
            }
        }

        private void usarOperador(String comando) {
            operador = comando;
            num1 = Double.parseDouble(txtDisplay.getText());
            numNuevo = true;
        }

        private void calcularResultado() {
            if (!txtDisplay.getText().isEmpty()) {
                resultado();
                operador = "";
                numNuevo = true;
            }
        }
    }

    private void resultado() {
        double num2 = Double.parseDouble(txtDisplay.getText());
        double result = 0;

        try {
            switch (operador) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "×":
                    result = num1 * num2;
                    break;
                case "÷":
                    if (num2 != 0) {
                        result = num1 / num2;
                    } else {
                        txtDisplay.setText("Error");
                        return;
                    }	
                    break;
                case "x²":
                    result = Math.pow(num1, 2);
                    break;
                case "√x":
                    result = Math.sqrt(num1);
                    break;
                case "1/x":
                    result = Math.pow(num1, -1);
                    break;
                case "%":
                    result = num1 / 100;
                    break;
                case "+/-":
                	result = num1 * -1;
                	break;
            }
        } catch (Exception e) {
            txtDisplay.setText("Error");
            return;
        }

        txtDisplay.setText(String.valueOf(result));
        num1 = result;
    }
}
