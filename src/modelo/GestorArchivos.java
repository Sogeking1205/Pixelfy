/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author asnu1
 */
public class GestorArchivos implements Archivable{
    private String rutaArchivo;
    
    //Constructor
    public GestorArchivos(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    
    //Métodos get y set

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void guardarArchivo(Lienzo lienzo) {
        //Uso de try-catch para evitar error al guardar
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getRutaArchivo()))){
            int filas = lienzo.getFilas();
            int columnas = lienzo.getColumnas();
            Casilla[][] cuadricula = lienzo.getCuadricula();
            
            //Primero se guarda el tamaño de filas y columnas para que se puedan leer primero al abrir el archivo de texto
            writer.write(filas + " " + columnas);
            writer.newLine();
            
            //En esta nueva linea se guardaran los colores por cada casilla
            for(int i=0; i<filas; i++){
                for(int j=0; j<columnas; j++){
                    Casilla casilla = cuadricula[i][j];
                    String hex = casilla.getColor();
                    if(hex == null || hex.isEmpty()){
                        hex = "#FFFFFF";
                    }
                    
                    writer.write(hex + " "); //Aca escribiria el color con un espacio que separa cada casilla
                }
                
                writer.newLine();
            }
            
            System.out.println("Archivo guardado en: " + getRutaArchivo());
            
        }catch(IOException e){
            System.out.println("Error al guardar el archivo");
        }
    }

    @Override
    public void cargarArchivo(Lienzo lienzo) {
        if(!validarArchivo()){
            System.out.println("Error al cargar el archivo, no existe o esta corrupto");
            return;
        }
        //uso de Try-catch
        try(BufferedReader reader = new BufferedReader(new FileReader(getRutaArchivo()))){
            
            String[] dimensiones = reader.readLine().split(" ");
            int filas = Integer.parseInt(dimensiones[0]);
            int columnas = Integer.parseInt(dimensiones[1]);
            
            lienzo.setFilas(filas);
            lienzo.setColumnas(columnas);
            lienzo.inicializarCuadricula();
            
            //Aca se lee el archivo y se reconstruye
            for (int i = 0; i < filas; i++) {
                String linea = reader.readLine();
                String[] hexColores = linea.split(" ");
                
                for (int j = 0; j < columnas; j++) {
                    // Modificamos color de cada Casilla con la guardada en el texto
                    lienzo.pintarCasilla(i, j, hexColores[j]);
                }
            }
            System.out.println("Archivo cargado con éxito.");
            
        }catch(IOException | NumberFormatException e){
            System.out.println("Error durante la lectura del archivo.");
        }
    }

    @Override
    public boolean validarArchivo() {
        File archivo = new File(getRutaArchivo());
        
        //Necesitamos validar que el archivo sea el correcto
        if (!archivo.exists() || !archivo.isFile()){
            return false;
        }
    
        //Uso de try-catch en la primera linea del archivo con las dimensiones de la matriz
        try(BufferedReader reader = new BufferedReader(new FileReader(archivo))){
            String primeraLinea = reader.readLine();
            if(primeraLinea == null){
                return false;
            }
            //Aca dividiremos la primera linea para tener las filas y columnas guardadas.
            String[] dimensiones = primeraLinea.split(" ");
            if(dimensiones.length != 2){
                return false;
            }
            //Lee la division y verifica si son numeros 
            Integer.parseInt(dimensiones[0]);
            Integer.parseInt(dimensiones[1]);
            
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    //toString
    @Override
    public String toString() {
        return "GestorArchivos [rutaArchivo = " + getRutaArchivo() + " ]";
    }
    
}
