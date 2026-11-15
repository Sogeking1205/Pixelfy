
package modelo;

public class Lapiz extends Herramienta{
    public Lapiz (){
        this.nombre="Lapiz";
    }

    @Override
    public void aplicar (String[][] Lienzo, int x, int y, String color){
        if (posicion(Lienzo, x, y)){
            Lienzo[x][y] = color;
        }
    }
}
