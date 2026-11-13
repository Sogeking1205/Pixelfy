package modelo;

/**
 * Gestiona la matriz bidimensional de casillas (el área de dibujo).
 * Centraliza la lógica para pintar, limpiar y consultar los píxeles.
 */
public class Lienzo {
    
    private int filas;
    private int columnas;
    private Casilla[][] cuadricula;

    /**
     * Constructor del Lienzo.
     * @param filas Número de casillas de alto.
     * @param columnas Número de casillas de ancho.
     */
    public Lienzo(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.cuadricula = new Casilla[filas][columnas];
        inicializarCuadricula();
    }

    /**
     * Llena la matriz instanciando un objeto Casilla en cada coordenada
     * con el color "Blanco" por defecto para tener un fondo limpio.
     */
    private void inicializarCuadricula() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                cuadricula[i][j] = new Casilla(i, j, "Blanco");
            }
        }
    }

    /**
     * Devuelve la casilla ubicada en una coordenada específica.
     * Esencial para que la herramienta Cubeta pueda leer los colores de sus vecinos.
     */
    public Casilla obtenerCasilla(int fila, int columna) {
        if (esCoordenadaValida(fila, columna)) {
            return cuadricula[fila][columna];
        }
        return null; 
    }

    /**
     * Cambia el color de una casilla validando primero que la coordenada exista.
     */
    public void pintarCasilla(int fila, int columna, String color) {
        if (esCoordenadaValida(fila, columna)) {
            cuadricula[fila][columna].setColor(color);
        }
    }

    /**
     * Reinicia todas las casillas al color blanco original.
     */
    public void limpiarLienzo() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                cuadricula[i][j].setColor("Blanco");
            }
        }
    }

    /**
     * Verifica que las coordenadas no se salgan de los límites de la matriz
     * para evitar errores de ejecución si se intenta pintar fuera del borde.
     */
    private boolean esCoordenadaValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public Casilla[][] getCuadricula() {
        return cuadricula;
    }

    public void setCuadricula(Casilla[][] cuadricula) {
        this.cuadricula = cuadricula;
    }

    @Override
    public String toString() {
        return "Lienzo de dimensiones " + filas + "x" + columnas;
    }
}