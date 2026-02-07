package org.ayed.informe.programa;

import org.ayed.gta.mapa.*;
import org.ayed.informe.programa.algoritmos.*;
import org.ayed.tda.aestrella.Manhattan;
import org.ayed.tda.grafo.Grafo;
import java.io.*;
import java.util.Random;

/**
 * Clase encargada de realizar las pruebas de complejidad y rendimiento
 * comparando los algoritmos A*, Dijkstra y Bellman-Ford.
 * Genera un archivo CSV con métricas detalladas de tamaños, operaciones,
 * tiempos de ejecución, costos totales sobre diversos mapas.
 */
public class PruebaComplejidad {
    
    private static final String RUTA_MAPAS = "data/ciudades/";
    private static final String SALIDA_CSV = "src/main/java/org/ayed/informe/resultados/mediciones.csv";
    private static final String[] ALGORITMOS = {"A*", "Dijkstra", "Bellman-Ford"};
    private static final int REPETICIONES = 10;

    /**
     * Punto de entrada principal.  
     * Carga los archivos de mapas y coordina la ejecución de las pruebas.
     */
    public static void main(String[] args) {
        
        File carpetaMapas = new File(RUTA_MAPAS);
        File[] listaArchivos = carpetaMapas.listFiles();
        if (listaArchivos != null) {
            java.util.Arrays.sort(listaArchivos); 
        }

        try (PrintWriter escritorCsv = new PrintWriter(new FileWriter(SALIDA_CSV))) {
            
            escritorCsv.println("Mapa,Tamaño(V+E),Nodos(V),Aristas(E),Algoritmo,Expansiones,Relajaciones,OperacionesTotales,TiempoMs,Costo");

            for (File archivoTxt : listaArchivos) {
                System.out.println("Procesando mapa: " + archivoTxt.getName());
                
                Mapa mapa = new Mapa(archivoTxt.getName(), true);
                Grafo<Nodo> grafo = mapa.obtenerGrafo();
                
                int cantidadNodos = grafo.obtenerVertices().size();
                int cantidadAristas = calcularCantidadAristas(grafo);
                ejecutarPruebas(archivoTxt.getName(), grafo, cantidadNodos, cantidadAristas, escritorCsv);
            }
            System.out.println("Resultados generados con éxito en: " + SALIDA_CSV);
        } 
        catch (IOException e) {
            System.err.println("Error al manejar el archivo CSV: " + e.getMessage());
        }
    }

    /**
     * Calcula la cantidad total de aristas presentes en el grafo.
     * @param  grafo El grafo a analizar.
     * @return Número total de aristas.
     */
    private static int calcularCantidadAristas(Grafo<Nodo> grafo) {
        int acumulador = 0;
        for (Nodo v : grafo.obtenerVertices()) {
            acumulador += grafo.obtenerAdyacentes(v).size();
        }
        return acumulador;
    }

    /**
     * Ejecuta una serie de pruebas para cada algoritmo sobre un grafo específico.
     * Garantiza que todos los algoritmos se prueben sobre la misma cantidad de rutas válidas
     * mediante el uso de una semilla aleatoria reiniciable.
     * 
     * @param nombreMapa      Nombre del archivo del mapa procesado.
     * @param grafo           Grafo de la ciudad.
     * @param cantidadNodos   Número de nodos (V).
     * @param cantidadAristas Número de aristas (E).
     * @param csv             PrintWriter para registrar los resultados.
     */
    private static void ejecutarPruebas(String nombreMapa, Grafo<Nodo> grafo, int cantidadNodos, int cantidadAristas, PrintWriter csv) {

        Object[] nodos = grafo.obtenerVertices().toArray();
        for (String algoritmo : ALGORITMOS) {

            Metricas metricaFinal = new Metricas(algoritmo, cantidadNodos, cantidadAristas);
            Random semilla = new Random(28);
            int rutasValidas = 0;

            while (rutasValidas < REPETICIONES) {     
                Nodo origen = (Nodo) nodos[semilla.nextInt(nodos.length)];
                Nodo destino = (Nodo) nodos[semilla.nextInt(nodos.length)];
                Metricas resultadoRuta = medirRuta(algoritmo, grafo, origen, destino, cantidadNodos, cantidadAristas);

                if (resultadoRuta != null) {
                    metricaFinal.sumarRendimiento(resultadoRuta);
                    rutasValidas++;
                }
            }

            if (rutasValidas > 0) {
                csv.println(metricaFinal.filaCsv(nombreMapa));
            }
        }
    }

    /**
     * Realiza las 10 mediciones de tiempo para una ruta específica y devuelve una métrica
     * con el tiempo promedio.
     * 
     * @return Objeto Metricas con el promedio de tiempo seteado, o null si la ruta es inalcanzable.
     */
    private static Metricas medirRuta(String algoritmo, Grafo<Nodo> grafo, Nodo origen, Nodo destino, int cantidadNodos, int cantidadAristas) {
        long sumaTiempos = 0;
        Metricas metricaReferencia = null;
        boolean hayRuta = true;
        int intentosExitosos = 0;

        while (intentosExitosos < REPETICIONES && hayRuta) {
            Metricas metricaIndividual = new Metricas(algoritmo, cantidadNodos, cantidadAristas);
            ejecutarAlgoritmo(algoritmo, grafo, origen, destino, metricaIndividual);
            
            if (metricaIndividual.obtenerCostoTotal() == -1) {
                hayRuta = false;
            } else {
                sumaTiempos += metricaIndividual.obtenerTiempoNs();
                metricaReferencia = metricaIndividual; 
                intentosExitosos++;
            }
        }

        if (hayRuta && metricaReferencia != null) {
            metricaReferencia.setearTiempoNs(sumaTiempos / REPETICIONES);
            return metricaReferencia;
        }
        return null; 
    }
    
    /**
     * Instancia y ejecuta el algoritmo seleccionado.
     * @param algoritmo Nombre del algoritmo a ejecutar.
     * @param grafo     Grafo de la ciudad.
     * @param origen    Nodo de partida.
     * @param destino   Nodo de llegada.
     * @param metrica   Metrica para registrar las mediciones de la ejecución.
     */
    private static void ejecutarAlgoritmo(String algoritmo, Grafo<Nodo> grafo, Nodo origen, Nodo destino, Metricas metrica) {
        if (algoritmo.equals("A*")) {
            new AEstrellaInforme(grafo, new Manhattan()).ejecutar(origen, destino, metrica);
        } 
        else if (algoritmo.equals("Dijkstra")) {
            new AEstrellaInforme(grafo, (n1, n2) -> 0).ejecutar(origen, destino, metrica);
        } 
        else if (algoritmo.equals("Bellman-Ford")) {
            new BellmanFordInforme().ejecutar(grafo, origen, destino, metrica);
        }
    }
}