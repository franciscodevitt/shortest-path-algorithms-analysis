package org.ayed.gta.ui;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestor centralizado de imágenes para vehículos.
 * Carga y cachea imágenes de diferentes tipos de vehículos.
 */
public class GestorImagenesVehiculos {
    
    private static final Map<String, Image> cacheImagenes = new HashMap<>();
    private static final String RUTA_IMAGENES = "images/vehicles/";
    
    /**
     * Obtiene la imagen de un vehículo por su nombre.
     * Las imágenes se cachean en memoria para mejor performance.
     * 
     * @param nombreVehiculo Nombre del vehículo (ej: "auto_rojo", "moto_verde", "exotico_naranja")
     * @return Image del vehículo, o null si no existe
     */
    public static Image obtenerImagenVehiculo(String nombreVehiculo) {
        if (nombreVehiculo == null || nombreVehiculo.isEmpty()) {
            return null;
        }
        
        // Verificar si está en caché
        if (cacheImagenes.containsKey(nombreVehiculo)) {
            System.out.println("✓ Imagen '" + nombreVehiculo + "' cargada desde caché");
            return cacheImagenes.get(nombreVehiculo);
        }
        
        try {
            // Construir ruta del recurso PNG
            String ruta = RUTA_IMAGENES + nombreVehiculo + ".png";
            System.out.println("▼ Cargando imagen: " + ruta);
            
            // Cargar imagen desde resources usando ClassLoader
            var recurso = GestorImagenesVehiculos.class.getClassLoader().getResourceAsStream(ruta);
            
            if (recurso == null) {
                System.err.println("✗ No se encontró: " + ruta);
                return null;
            }
            
            Image imagen = new Image(recurso);
            
            if (imagen.getWidth() <= 0 || imagen.getHeight() <= 0) {
                System.err.println("✗ Imagen vacía: " + ruta);
                return null;
            }
            
            System.out.println("✓ Imagen '" + nombreVehiculo + "' cargada (w=" + imagen.getWidth() + ", h=" + imagen.getHeight() + ")");
            
            // Cachear la imagen
            cacheImagenes.put(nombreVehiculo, imagen);
            return imagen;
        } catch (Exception e) {
            System.err.println("✗ Error cargando imagen '" + nombreVehiculo + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Obtiene la imagen por defecto basada en el tipo de vehículo.
     * 
     * @param tipoVehiculo Tipo del vehículo (AUTO, MOTO, EXOTICO)
     * @return Image del tipo de vehículo por defecto
     */
    public static Image obtenerImagenPorTipo(String tipoVehiculo) {
        if (tipoVehiculo == null) {
            return null;
        }
        
        switch (tipoVehiculo.toUpperCase()) {
            case "AUTO":
                return obtenerImagenVehiculo("auto_rojo");
            case "MOTO":
                return obtenerImagenVehiculo("moto_verde");
            case "EXOTICO":
                return obtenerImagenVehiculo("exotico_naranja");
            default:
                return null;
        }
    }
    
    /**
     * Limpia el caché de imágenes.
     * Útil para liberar memoria si es necesario.
     */
    public static void limpiarCache() {
        cacheImagenes.clear();
    }
    
    /**
     * Obtiene el número de imágenes cacheadas.
     */
    public static int obtenerTamanoCache() {
        return cacheImagenes.size();
    }
}
