package org.ayed.gta.ui;

import org.ayed.gta.Vehiculo;

import javafx.scene.image.Image;


/**
 * Gestor centralizado de imágenes para vehículos.
 * Carga y cachea imágenes de diferentes tipos de vehículos.
 */
public class GestorImagenesVehiculos {
    
    //private static final Map<String, Image> cacheImagenes = new HashMap<>();
    private static final String RUTA_IMAGENES = "images/vehicles/";
    
    /**
     * Obtiene la imagen de un vehículo por su nombre.
     * Las imágenes se cachean en memoria para mejor performance.
     * 
     * @param nombreVehiculo Nombre del vehículo (ej: "auto_rojo", "moto_verde", "exotico_naranja")
     * @return Image del vehículo, o null si no existe
     */
    public static Image obtenerImagenVehiculo(Vehiculo vehiculo) {
        String ruta;
        
        try {

            // Construir ruta del recurso PNG
            if (vehiculo == null || vehiculo.getNombre().isEmpty()) {
                ruta = obtenerRutaPorTipo(vehiculo.getTipo().toString());
            }else{
                ruta = RUTA_IMAGENES + vehiculo.getNombre() + ".png";
            }

            System.out.println("▼ Cargando imagen: " + ruta);
            
            // Cargar imagen desde resources usando ClassLoader
            var recurso = GestorImagenesVehiculos.class.getClassLoader().getResourceAsStream(ruta);
            
            Image imagen = new Image(recurso);
            
            System.out.println("✓ Imagen '" + ruta + "' cargada (w=" + imagen.getWidth() + ", h=" + imagen.getHeight() + ")");
            
            return imagen;

        } catch (Exception e) {
            System.err.println("✗ Error cargando imagen del vehículo: " + e.getMessage());
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
    private static String obtenerRutaPorTipo(String tipoVehiculo) {
        if (tipoVehiculo == null) {
            return null;
        }
        
        switch (tipoVehiculo.toUpperCase()) {
            case "AUTO":
                return RUTA_IMAGENES + "auto-default.png";
            case "MOTO":
                return RUTA_IMAGENES + "moto-default.png";
            case "EXOTICO":
                return RUTA_IMAGENES + "exotico-default.png";
            default:
                return null;
        }
    }
    
}
