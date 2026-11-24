package modelo;

import java.util.LinkedList;
import java.util.Queue;

public class Cubeta extends Herramienta {

    public Cubeta() {
        this.nombre = "Cubeta";
    }

    @Override
    public void aplicar(String[][] lienzo, int x, int y, String nuevoColor) {
        if (!posicion(lienzo, x, y)) {
            return;
        }

        String colorOriginal = normalizarColor(lienzo[x][y]);
        String colorDestino = normalizarColor(nuevoColor);

        // Si ya tiene el mismo color, no hace nada
        if (colorOriginal.equalsIgnoreCase(colorDestino)) {
            return;
        }

        Queue<int[]> cola = new LinkedList<>();
        cola.add(new int[]{x, y});
        lienzo[x][y] = colorDestino;

        // Abajo, arriba, derecha, izquierda
        int[][] direcciones = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int filaActual = actual[0];
            int colActual = actual[1];

            for (int[] dir : direcciones) {
                int nuevaFila = filaActual + dir[0];
                int nuevaCol = colActual + dir[1];

                if (posicion(lienzo, nuevaFila, nuevaCol)
                        && normalizarColor(lienzo[nuevaFila][nuevaCol]).equalsIgnoreCase(colorOriginal)) {
                    lienzo[nuevaFila][nuevaCol] = colorDestino;
                    cola.add(new int[]{nuevaFila, nuevaCol});
                }
            }
        }
    }

    private String normalizarColor(String color) {
        if (color == null || color.trim().isEmpty() || color.equalsIgnoreCase("blanco")) {
            return "#FFFFFF";
        }
        return color.trim().toUpperCase();
    }
}