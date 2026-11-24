/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author EMMA
 */
package modelo;

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
import java.util.Stack;

public class PanelLienzo extends JPanel {

    private Lienzo lienzo;
    private Herramienta herramientaActual;
    private String colorActual = "#000000"; // Negro por defecto
    private boolean mostrarCuadricula = true;
    private boolean validarModificado = false;
    private static final int MAX_HISTORIAL = 50;
    private Stack<String[][]> historialDeshacer = new Stack<>();
    private Stack<String[][]> historialRehacer = new Stack<>();
    private String[][] estadoPrevioTrazo = null;
    private Runnable onHistorialCambiado;
    

    public PanelLienzo(Lienzo lienzo) {
        this.lienzo = lienzo;
        this.herramientaActual = new Lapiz(); // Herramienta inicial por defecto
        setBackground(Color.WHITE);

       // Detectar clics individuales
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                estadoPrevioTrazo = capturarEstadoActual();
                ejecutarHerramienta(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (estadoPrevioTrazo != null) {
                    String[][] estadoActual = capturarEstadoActual();
                    if (huboCambios(estadoPrevioTrazo, estadoActual)) {
                        registrarEnHistorial(estadoPrevioTrazo);
                    }
                    estadoPrevioTrazo = null;
                }
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
            String[][] matrizColores = capturarEstadoActual();

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
        limpiarHistorial();
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
    // --- Métodos para deshacer y rehacer ---

    private String[][] capturarEstadoActual() {
        if (lienzo == null) return null;
        int filas = lienzo.getFilas();
        int columnas = lienzo.getColumnas();
        String[][] copia = new String[filas][columnas];
        Casilla[][] cuadricula = lienzo.getCuadricula();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                String c = cuadricula[i][j].getColor();
                if (c == null || c.trim().isEmpty() || c.equalsIgnoreCase("blanco")) {
                    c = "#FFFFFF";
                }
                copia[i][j] = c.trim().toUpperCase();
            }
        }
        return copia;
    }

    private void restaurarEstado(String[][] estado) {
        if (lienzo == null || estado == null) return;
        for (int i = 0; i < estado.length; i++) {
            for (int j = 0; j < estado[i].length; j++) {
                lienzo.pintarCasilla(i, j, estado[i][j]);
            }
        }
        this.validarModificado = true;
        repaint();
        notificarCambioHistorial();
    }

    private boolean huboCambios(String[][] antes, String[][] despues) {
        if (antes == null || despues == null) return false;
        if (antes.length != despues.length || antes[0].length != despues[0].length) return true;
        for (int i = 0; i < antes.length; i++) {
            for (int j = 0; j < antes[i].length; j++) {
                if (!antes[i][j].equalsIgnoreCase(despues[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    private void registrarEnHistorial(String[][] estadoAnterior) {
        historialDeshacer.push(estadoAnterior);
        if (historialDeshacer.size() > MAX_HISTORIAL) {
            historialDeshacer.remove(0);
        }
        historialRehacer.clear();
        notificarCambioHistorial();
    }

    public void guardarEstadoParaDeshacer() {
        String[][] estadoActual = capturarEstadoActual();
        if (estadoActual != null) {
            registrarEnHistorial(estadoActual);
        }
    }

    public boolean deshacer() {
        if (historialDeshacer.isEmpty()) return false;
        historialRehacer.push(capturarEstadoActual());
        restaurarEstado(historialDeshacer.pop());
        return true;
    }

    public boolean rehacer() {
        if (historialRehacer.isEmpty()) return false;
        historialDeshacer.push(capturarEstadoActual());
        restaurarEstado(historialRehacer.pop());
        return true;
    }

    public void limpiarHistorial() {
        historialDeshacer.clear();
        historialRehacer.clear();
        estadoPrevioTrazo = null;
        notificarCambioHistorial();
    }

    public boolean puedeDeshacer() {
        return !historialDeshacer.isEmpty();
    }

    public boolean puedeRehacer() {
        return !historialRehacer.isEmpty();
    }

    public void setOnHistorialCambiado(Runnable callback) {
        this.onHistorialCambiado = callback;
        notificarCambioHistorial();
    }

    private void notificarCambioHistorial() {
        if (onHistorialCambiado != null) {
            onHistorialCambiado.run();
        }
    }
}