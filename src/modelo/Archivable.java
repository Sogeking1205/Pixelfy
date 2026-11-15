
package modelo;

public interface Archivable {
    
    public void guardarArchivo(Lienzo lienzo);
    
    public void cargarArchivo(Lienzo lienzo);
    
    public boolean validarArchivo();
    
}
