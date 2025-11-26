package org.ayed.gta;

import java.io.*;
import org.ayed.tda.vector.Vector;

/*
 * Clase que maneja las operaciones elementales con archivos.
 */
public class GestorArchivos {
    
    /**
     * Lee un archivo y devuelve su contenido línea por línea en un Vector.
     * Maneja el cierre del archivo usando try-with-resources.
     * 
     * @param rutaCompleta La ruta del archivo a leer.
     * 
     * @return Un Vector de Strings con cada línea del archivo.
     */
    public static Vector<String> leerArchivo(String rutaCompleta) {

        Vector<String> lineas = new Vector<>(); 
        
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaCompleta))) {
            String linea;
            while ((linea = reader.readLine()) != null) {  
                lineas.agregar(linea); 
            }
        } catch (IOException e) {
            System.err.println("Error de lectura: No se pudo abrir el archivo " + rutaCompleta + ". Mensaje: " + e.getMessage());
        }
        
        return lineas;
    }

    /**
     * Escribe en un archivo las cadenas de texto guardadas en el vector.
     * Sobreescribe contenido previo.
     * Maneja el cierre del archivo usando try-with-resources.
     * 
     * @param rutaCompleta La ruta del archivo a escribir (ej: "data/garaje.csv").
     * @param lineas El Vector de Strings con el contenido a guardar.
     */
    public static void escribirArchivo(String rutaCompleta, Vector<String> lineas) {
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta))) {
            for (int i = 0; i < lineas.tamanio(); i++) {
                writer.println(lineas.dato(i));
            }
        } catch (IOException e) {
            System.err.println("Error de escritura: No se pudo guardar el archivo " + rutaCompleta + ". Mensaje: " + e.getMessage());
        }
    }
}
