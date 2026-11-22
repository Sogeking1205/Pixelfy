/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author EMMA
 */
package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;
import modelo.Borrador;
import modelo.Casilla;
import modelo.Herramienta;
import modelo.Lapiz;
import modelo.Lienzo;

public class PanelLienzo extends JPanel {

    private Lienzo lienzo;
    private Herramienta herramientaActual;
    private String colorActual = "#000000"; // Negro por defecto
    private boolean mostrarCuadricula = true;
    private boolean validarModificado = false;
    

    public PanelLienzo(Lienzo lienzo) {
        this.lienzo = lienzo;
        this.herramientaActual = new Lapiz(); // Herramienta inicial por defecto
        setBackground(Color.WHITE);

        // Detectar clics individuales
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ejecutarHerramienta(e.getX(), e.getY());
            }
        });

        // Detectar arrastre del ratón (solo para Lápiz y Borrador)
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (herramientaActual instanceof Lapiz || herramientaActual instanceof Borrador) {
                    ejecutarHerramienta(e.getX(), e.getY());
                }
            }
        });
    }

    // Dibuja la matriz de Casillas en el panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (lienzo == null) return;

        int filas = lienzo.getFilas();
        int columnas = lienzo.getColumnas();
        Casilla[][] cuadricula = lienzo.getCuadricula();

        int tamanoCelda = Math.min(getWidth() / columnas, getHeight() / filas);
        if (tamanoCelda <= 0) return;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                String textoColor = cuadricula[i][j].getColor();
                
                // Traduce "Blanco", "blanco", null o "#RRGGBB" a un java.awt.Color válido
                g.setColor(convertirAColor(textoColor));
                g.fillRect(j * tamanoCelda, i * tamanoCelda, tamanoCelda, tamanoCelda);

                if (mostrarCuadricula) {
                    g.setColor(new Color(220, 220, 220));
                    g.drawRect(j * tamanoCelda, i * tamanoCelda, tamanoCelda, tamanoCelda);
                }
            }
        }
    }

    // Puentes entre Lienzo (Casilla[][]) y Herramienta (String[][])
    private void ejecutarHerramienta(int xMouse, int yMouse) {
        if (lienzo == null || herramientaActual == null) return;

        int filas = lienzo.getFilas();
        int columnas = lienzo.getColumnas();
        int tamanoCelda = Math.min(getWidth() / columnas, getHeight() / filas);
        if (tamanoCelda <= 0) return;

        int col = xMouse / tamanoCelda;
        int fila = yMouse / tamanoCelda;

        if (fila >= 0 && fila < filas && col >= 0 && col < columnas) {
            // 1. Extraer la matriz String[][] que esperan las herramientas
            String[][] matrizColores = new String[filas][columnas];
            Casilla[][] cuadricula = lienzo.getCuadricula();
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    matrizColores[i][j] = cuadricula[i][j].getColor();
                }
            }

            // 2. Aplicar polimórficamente la herramienta activa (Lapiz, Borrador o Cubeta)
            herramientaActual.aplicar(matrizColores, fila, col, colorActual);

            // 3. Devolver los colores modificados al objeto Lienzo
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    String colorResultado = matrizColores[i][j];
                    // Normalizamos "blanco" a "#FFFFFF" para que GestorArchivos lo guarde bien
                    if (colorResultado == null || colorResultado.equalsIgnoreCase("blanco")) {
                        colorResultado = "#FFFFFF";
                    }
                    lienzo.pintarCasilla(i, j, colorResultado);
                }
            }
            
            this.validarModificado = true;
            repaint();
        }
    }

    // Evita errores si el color dice "Blanco" en lugar de "#FFFFFF"
    private Color convertirAColor(String textoColor) {
        if (textoColor == null || textoColor.isEmpty() || textoColor.equalsIgnoreCase("blanco")) {
            return Color.WHITE;
        }
        try {
            return Color.decode(textoColor);
        } catch (NumberFormatException e) {
            return Color.WHITE;
        }
    }

    // --- Métodos para conectar con los botones de VentanaPrincipal ---

    public void setHerramienta(Herramienta herramienta) {
        this.herramientaActual = herramienta;
    }

    public void setColorActual(Color color) {
        this.colorActual = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    public void setLienzo(Lienzo lienzo) {
        this.lienzo = lienzo;
        repaint();
    }

    public boolean isValidarModificado() {
        return validarModificado;
    }

    public void setValidarModificado(boolean validarModificado) {
        this.validarModificado = validarModificado;
    }

    
    public Lienzo getLienzo() {
        return lienzo;
    }

    public void setMostrarCuadricula(boolean mostrar) {
        this.mostrarCuadricula = mostrar;
        repaint();
    }
}