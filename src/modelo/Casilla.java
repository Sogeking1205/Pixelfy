package modelo;

/**
 * Representa un píxel individual dentro del lienzo de dibujo.
 * Guarda sus coordenadas para identificarse dentro de la matriz 
 * y su color actual en formato de texto.
 */
public class Casilla {
    
    private int fila;
    private int columna;
    private String color; 

    /**
     * Constructor de la Casilla.
     * @param fila Posición en el eje vertical (Y).
     * @param columna Posición en el eje horizontal (X).
     * @param color El color inicial de la casilla (ej. "Blanco" o código Hexadecimal).
     */
    public Casilla(int fila, int columna, String color) {
        this.fila = fila;
        this.columna = columna;
        this.color = color;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Casilla en [" + fila + "][" + columna + "] - Color: " + color;
    }
}