
package modelo;

public class Cubeta extends Herramienta{
    public Cubeta () {
        this.nombre = "cubeta";
    }
    public void aplicar (String[][] Lienzo, int x, int y, String NuevoColor){
        if (!posicion(Lienzo, x, y)) return;
        String ColorOriginal = Lienzo[x][y];
        if (ColorOriginal.equals(NuevoColor)) return;
        Rellenar(Lienzo, x, y, ColorOriginal, NuevoColor);

    }
    
    private void Rellenar(String[][] Lienzo, int x, int y, String ColorOriginal, String NuevoColor) {
        if(!posicion(Lienzo, x, y)) return;
        if(!Lienzo[x][y].equals(ColorOriginal)) return;
        Lienzo[x][y] = NuevoColor;
        
        Rellenar(Lienzo, x + 1, y, ColorOriginal, NuevoColor);
        Rellenar(Lienzo, x - 1, y, ColorOriginal, NuevoColor);
        Rellenar(Lienzo, x, y + 1, ColorOriginal, NuevoColor);
        Rellenar(Lienzo, x, y + 1, ColorOriginal, NuevoColor);

    }
}
