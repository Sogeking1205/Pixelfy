
package modelo;


public abstract class Herramienta {
    protected String nombre;
    
    public abstract void aplicar(String[][] lienzo, int x, int y, String color);
    
    protected boolean posicion(String[][] lienzo, int x, int y) {
        return x >= 0 && x < lienzo.length && y >= 0 && y < lienzo[0].length;
    }
}
