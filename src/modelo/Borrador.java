
package modelo;

public class Borrador extends Herramienta{
    public Borrador() {
        this.nombre = "Borrador";
    }

    @Override
    public void aplicar(String[][] Lienzo, int x, int y, String color) {
        if (posicion(Lienzo, x, y)) {
            Lienzo[x][y] = "#FFFFFF";
        }
    }
}
