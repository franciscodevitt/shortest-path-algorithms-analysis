package org.ayed.gta.mapa;

import org.ayed.tda.matriz.*;
import org.ayed.tda.grafo.*;
import org.ayed.tda.vector.*;
import org.ayed.tda.aestrella.*;
import org.ayed.tda.lista.Pila;
import java.io.*;
import java.util.Random;

/*
 * Clase que crea y carga el mapa de la ciudad para jugar la misión.
 */
public class Mapa {
    
    private final Matriz<Nodo> ciudad;
    private final Grafo<Nodo> grafoCiudad;
    private int ancho;
    private int altura;

    private Coordenada posicionJugador;
    private Coordenada posicionAnteriorJugador;
    private Coordenada destino;
    private Coordenada recompensaExtra;
    private boolean recompensaExtraRecogida;
    private AEstrella<Nodo> gps;

    private final boolean esModoAnalisis;

    private final Random SEMILLA = new Random(42);  // semilla que garantiza que el tráfico sea siempre igual
    private static final String RUTA_MAPAS = "data/ciudades/";  
    private static final String RUTA_MAPAS_ANALISIS = "data/analisisCiudades/";
    private static final String CALLE = "+"; 
    private static final String EDIFICIO = "#";
    private static final String PARQUE = "-";
    private static final String AGUA = "~";  
    private static final double TASA_TRAFICO = 0.1;
    private static final double TASA_DISTANCIA_MINIMA = 0.3;
    private static final int COSTO_CALLE = 1;
    private static final int COSTO_PARQUE = 15;
    private static final int COSTO_TRAFICO = 5;
   
    /**
     * Constructor de Mapa. 
     * Carga el mapa de la ciudad desde un archivo y lo inicializa completamente.
     * 
     * @param ciudadElegida   Nombre del archivo de la ciudad a cargar.
     * @param modoAnalisis    True si el mapa será usado para el análisis de complejidad.
     *                        False si será usado para el juego.
     * @throws ExcepcionMapa  si el archivo correspondiente no tiene formato valido o no existe.
     */
    public Mapa(String ciudadElegida, boolean modoAnalisis) {
        
        this.esModoAnalisis = modoAnalisis;

        String rutaBase = esModoAnalisis ? RUTA_MAPAS_ANALISIS : RUTA_MAPAS;
        Vector<String> lineas = leerArchivo(rutaBase + ciudadElegida);
        if (lineas.tamanio() < 2) {
            throw new ExcepcionMapa("El archivo de mapa es inválido o está vacío en: \" + rutaBase + ciudadElegida);");
        }
        
        this.procesarDimensiones(lineas.dato(0));
        this.ciudad = new Matriz<Nodo>(this.altura, this.ancho);
        this.grafoCiudad = new Grafo<Nodo>();
        procesarCiudad(lineas);
        generarGrafo();
        
        if (!esModoAnalisis) {
            inicializarEntradaSalida();
            inicializarRecompensaExtra();
            this.gps = new AEstrella<Nodo>(this.grafoCiudad, new Manhattan());
        } else {
            this.posicionJugador = null;
            this.destino = null;
            this.recompensaExtra = null;
        }

        this.recompensaExtraRecogida = false;
        this.posicionAnteriorJugador = null;
    }    

    // Constructor por defecto para el juego
    public Mapa(String ciudadElegida) {
        this(ciudadElegida, false);
    }

    /**
     * Lee el archivo y devuelve su contenido línea por línea en un Vector.
     * Maneja el cierre del archivo usando try-with-resources.
     * 
     * @param rutaCompleta La ruta del archivo a leer.
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
     * Parsea la primer linea del archivo para obtener la altura y el ancho de la ciudad.
     *
     * @param  dimensiones    String correspondiente a la primera línea del archivo.
     * @throws ExcepcionMapa  si el formato es incorrecto y la primer linea no guarda las dimensiones.
     *                        si las dimensiones no son válidas.
     */
    private void procesarDimensiones(String dimensiones) {
        try {
            String[] dim = dimensiones.split(",");
            if (dim.length != 2) {
                throw new Exception("Se esperan valores en el formato (Filas,Columnas)");
            }
            this.altura = Integer.parseInt(dim[0].trim());
            this.ancho = Integer.parseInt(dim[1].trim());
            if (this.altura <= 0 || this.ancho <= 0) {
                throw new Exception("Las dimensiones del mapa deben ser números enteros positivos.");
            }

        } catch (Exception e) {
            throw new ExcepcionMapa("Error en el formato de la cabecera del mapa. Detalle: " + e.getMessage());
        }
    }

    /**
     * Define si el terreno va a tener tráfico o no
     * 
     * @param terreno String con el tipo de terreno.
     * @return        True si es una CALLE que va a tener trafico.
     *                False caso contrario.   
     */
    private boolean generarTrafico(String terreno) {
        if (!terreno.equals(CALLE)) return false;

        double probabilidad = this.esModoAnalisis ? SEMILLA.nextDouble() : Math.random();
    
        return probabilidad < TASA_TRAFICO;
    }
    
    /**
     * Metodo que recibe y parsea todas las lineas del archivo del mapa creando y cargando 
     * cada nodo de la matriz.
     * 
     * @param  lineas        Vector donde cada elemento es un string representando una fila del mapa. 
     * @throws ExcepcionMapa si el formato del mapa es incorrecto o le faltan datos.
     *                       si hay errores con los nodos o la matriz.
     */
    private void procesarCiudad (Vector<String> lineas) {

        try{
            if (lineas.tamanio() - 1 != altura) {
                throw new Exception("El número de filas no coincide con la dimensión declarada.");
            }
            for (int i = 1; i < lineas.tamanio(); i++) {
                int f = i - 1;
                String[] nodos = lineas.dato(i).trim().split("\\s+");
                
                if (nodos.length != ancho) {
                    throw new Exception("El número de columnas de la fila " + f + " no coincide con la dimensión declarada.");
                }
                for (int c = 0; c < ancho; c++) {
                    String terreno = nodos[c].trim();   
                    if (terreno.isEmpty()) {
                        throw new Exception("Nodo vacía en (" + f + "," + c + ").");
                    } else if (!esTerrenoValido(terreno)) {
                        throw new Exception("Tipo de terreno invalido en (" + f + "," + c + ").");
                    }
                    
                    boolean tieneTrafico = generarTrafico(terreno);
                    Nodo nodo = new Nodo(new Coordenada(f, c), terreno, tieneTrafico);
                    this.ciudad.agregarEntrada(f, c, nodo); 
                }
            } 
        }
        catch (Exception e) { 
            throw new ExcepcionMapa("Error al cargar el mapa de la ciudad: " + e.getMessage());
        }
    }

    /**
     * Metodo que elige una coordenada al azar que sea transitable y de tipo distinto a PARQUE. 
     * 
     * @throws ExcepcionMapa si no logra encontrar ninguna coordenada que cumpla estas condiciones. 
     * @return               Cordenada transitable distinta a PARQUE.
     */
    private Coordenada coordenadaAleatoria() {
        
        boolean encontrado = false;
        int intentos = 0;
        Coordenada candidata = null;

        while (!encontrado && intentos < ancho*altura*100) {
            
            int f = (int)(Math.random() * altura);
            int c = (int)(Math.random() * ancho);
            Nodo nodo = ciudad.obtenerEntrada(f, c);

            if (esTransitable(nodo) && !nodo.obtenerTerreno().equals(PARQUE)) {
               encontrado = true;
               candidata = new Coordenada(f, c);
            }  
            intentos++;
        }
        if (candidata == null) {
            throw new ExcepcionMapa("Fallo al elegir coordenada transitable aleatoria");
        } 
        return candidata;
    }

    /**
     * Calcula si ambas coordenadas cumplen con una distancia minima 
     * de un TASA_DISTANCIA_MINIMA de las dimensiones ancho y altura de la ciudad.
     * 
     * @param entrada   Coordenada candidata para el jugador .
     * @param salida    Coordenada candidata para el destino.
     * @return          True si cumplen distancia minima.
     *                  False en el caso contrario.
     */
    private boolean cumplenDistanciaMinima (Coordenada entrada, Coordenada salida) {
        
        double distanciaX = Math.abs(entrada.obtenerX() - salida.obtenerX());
        double distanciaY = Math.abs(entrada.obtenerY() - salida.obtenerY()); 
        
        return (distanciaX > (double)this.ancho*TASA_DISTANCIA_MINIMA) || 
               (distanciaY > (double)this.altura*TASA_DISTANCIA_MINIMA);
    }

    /**
     * Inicializa las coordenadas de jugador y destino aleatoriamente.
     * Intenta que esten a una distancia minima una de la otra.
     * Si no lo logra, las inicializa completamente al azar.
     * 
     * @throws ExcepcionMapa si no logra encontrar un par de coordenadas validas.
     */
    private void inicializarEntradaSalida() {

        Coordenada entrada = null;
        Coordenada salida = null;
        int intentos = 0;
        do {
            entrada = coordenadaAleatoria();
            salida = coordenadaAleatoria();
            intentos++;
        } while (!cumplenDistanciaMinima (entrada, salida) && intentos < ancho*altura*100);

        this.posicionJugador = entrada;
        this.destino = salida;
    }

    private void inicializarRecompensaExtra() {
        Coordenada recompensa = null;
        int fil;
        int col;
        int intentos = 0;
        do {
            recompensa = coordenadaAleatoria();
            intentos++;
            fil = recompensa.obtenerY();
            col = recompensa.obtenerX();
        } while ((recompensa.equals(this.posicionJugador) || recompensa.equals(this.destino) || ciudad.obtenerEntrada(fil, col).tieneTrafico() ) && intentos < ancho*altura*100); //si es la posicion del jugador, del destino o si tiene trafico, busco otra.
        this.recompensaExtra = recompensa;
    }
    

    /**
     * Determina si terreno representa un tipo permitido para el nodo.
     * 
     * @param  terreno  String del tipo de terreno que se quiere procesar.
     * @return          True si es un terreno valido (CALLE, PARQUE o EDIFICIO).
     *                  False si no lo es.
     */
    private boolean esTerrenoValido (String terreno) {
        return terreno.equals(CALLE) || terreno.equals(EDIFICIO) || 
               terreno.equals(PARQUE) || terreno.equals(AGUA);
    }

    /**
     * Determina si el Nodo es transitable.
     * 
     * @param  nodo   Nodo que se quiere consultar.
     * @return         True si es transitable (CALLE o PARQUE).
     *                 False si no lo es (EDIFICIO, AGUA).
     */
    private boolean esTransitable(Nodo nodo) {
        return nodo.obtenerTerreno().equals(CALLE) || nodo.obtenerTerreno().equals(PARQUE);
    }

    /**
    * Calcula el costo de un nodo según su tipo de terreno.
    * Si el nodo tiene tráfico, el costo del terreno se multiplica por COSTO_TRAFICO.
    *
    * @param nodo   Nodo para el cual se desea calcular el costo.
    * @return        El costo calculado.
    */
    private int calcularCosto(Nodo nodo) {
        
        String terreno = nodo.obtenerTerreno();
        int costo = Integer.MAX_VALUE;
        
        if (esTransitable(nodo)){
        
            if (terreno.equals(CALLE)) {
                costo = COSTO_CALLE;
            } else if (terreno.equals(PARQUE)) {
                costo = COSTO_PARQUE;
            }
            if (nodo.tieneTrafico()){
                costo *= COSTO_TRAFICO;
            }  
        }
        return costo;
    }

    /**
    * Agrega aristas dirigidas entre el nodo actual y el nodo vecino.
    * Las aristas son dirigidas porque el costo depende del nodo al que se ENTRA:
    *   - actual → vecino: costo de entrar al vecino
    *   - vecino → actual: costo de entrar al actual
    * 
    * Las aristas se agregan manualmente en ambas direcciones para simular un grafo dirigido,
    * ya que la clase Grafo por defecto es no dirigida.
    *
    * @param actual      Nodo origen desde el cual se quiere conectar.
    * @param filaVecino  Fila del nodo vecino.
    * @param colVecino   Columna del nodo vecino.
    */
    private void conectarVecino(Nodo actual, int filaVecino, int colVecino) {

        boolean filaValida = filaVecino >= 0 && filaVecino < altura;
        boolean colValida = colVecino >= 0 && colVecino < ancho;

        if (filaValida && colValida) {

            Nodo vecino = ciudad.obtenerEntrada(filaVecino, colVecino);
            if (vecino != null && esTransitable(vecino)) {
                
                // Agregar aristas dirigidas manualmente
                // actual → vecino: costo de entrar al vecino
                int costoAlVecino = calcularCosto(vecino);
                grafoCiudad.obtenerAdyacentes(actual).put(vecino, costoAlVecino);
                
                // vecino → actual: costo de entrar al actual
                int costoAlActual = calcularCosto(actual);
                grafoCiudad.obtenerAdyacentes(vecino).put(actual, costoAlActual);
            }
        }   
    }

    /**
    * Carga el grafo de la ciudad a partir de la matriz ciudad.
    * Recorre la matriz de arriba hacia abajo y de izquierda a derecha. 
    * Agrega los nodos transitables como vértice y los conecta con el vecino izquierdo 
    * y el vecino superior (si existen y son transitables).
    * 
    * Nota: solo conecta con los vecinos izquierdo y superior porque agregarArista de clase Grafo 
    *       necesita que el vertice ya exista en el grafo ciudad. De todas formas al terminar de recorrer 
    *       la matriz, todos las aristas van a terminar correctamente conectadas.
    */
    private void generarGrafo() {

        for (int f = 0; f < altura; f++) {
            for (int c = 0; c < ancho; c++) {

                Nodo actual = ciudad.obtenerEntrada(f, c);
                if (actual != null && esTransitable(actual)) {
                    grafoCiudad.agregarVertice(actual);
                    conectarVecino(actual, f, c - 1);
                    conectarVecino(actual, f - 1, c); 
                }
            }
        }
    }
    
    // ==================== GETTERS PARA LA INTERFAZ GRÁFICA ====================
    
    /**
     * Metodo que devuelve una copia del grafo ciudad.
     *
     *  NOTA: Gps y Controlador necesitan conocer el grafo de la ciudad para funcionar. No podia ser un getter
     *       porque devolveria una referencia al original y podria llegar a ser modificado a travez de los metodos de 
     *       la clase Grafo, y no queremos eso. 
     *       Sin embargo la copia puede ser muy costosa. Consultar.
     *
     *  @return la copia del grafo. 
     */
    public Grafo<Nodo> obtenerGrafo() {
        Grafo<Nodo> copia = new Grafo<Nodo>(this.grafoCiudad);

        return copia;
    }

    /**
     * Actualiza la Coordenada del jugador en el mapa.
     * Si la nueva Coordenada coincide con recompensaExtra, la coordenada recompensa desaparece.
     * 
     * @param nuevaCoordenada  Coordenada nueva del jugador.
     * @throws ExcepcionMapa   si la nueva coordenada no es valida. 
     */
    public void actualizarJugador(Coordenada nuevaCoordenada) {

        Nodo nuevoNodo = ciudad.obtenerEntrada(nuevaCoordenada.obtenerY(), nuevaCoordenada.obtenerX());
        if (nuevoNodo == null || !esTransitable(nuevoNodo)) {
            throw new ExcepcionMapa("Coordenada invalida, no es transitable");
        }
        
        this.posicionAnteriorJugador = this.posicionJugador;
        this.posicionJugador = nuevaCoordenada;

        if (this.recompensaExtra != null && this.posicionJugador.equals(this.recompensaExtra)) {
            this.recompensaExtra = null;
            this.recompensaExtraRecogida = true;
        }
    }

    /**
     * Define si jugador guarda la misma coordenada que destino.
     * 
     * @return   True si Coordenada jugador es igual a Coordenada destino.
     *           False en el caso contrario.
     */
    public boolean llegoDestino() {
        return this.posicionJugador.equals(destino);
    }

    /**
     * Indica si el jugador consiguio la recompensaExtra.
     * 
     * @return   True si fue conseguida.
     *           False en el caso contrario.
     */
    public boolean seConsiguioRecompensaExtra() {
        return recompensaExtraRecogida;
    }


    /**
     * Obtiene la posición actual del jugador.
     */
    public Coordenada obtenerPosicionJugador() {
        return posicionJugador;
    }

     /**
     * Obtiene la posición anterior del jugador.
     */
    public Coordenada obtenerPosicionAnteriorJugador() {
        return posicionAnteriorJugador;
    }

    /**
     * Obtiene la coordenada destino.
     */
    public Coordenada obtenerDestino() {
        return destino;
    }

    /**
     * Obtiene el ancho del mapa.
     */
    public int obtenerAncho() {
        return ancho;
    }

    /**
     * Obtiene la altura del mapa.
     */
    public int obtenerAltura() {
        return altura;
    }

    /**
     * Obtiene el tipo de terreno en una posición específica.
     */
    public String obtenerTipoTerreno(int fila, int columna) {
        Nodo nodo = ciudad.obtenerEntrada(fila, columna);
        if (nodo != null) {
            return nodo.obtenerTerreno();
        }
        return "#"; // Retorna edificio si el nodo es nulo
    }

    public Coordenada obtenerCoordenadaRecompensaExtra() {
        return recompensaExtra;
    }

    /**
     * Verifica si una coordenada es caminable.
     */
    public boolean esCaminable(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= altura) {
            return false;
        }
        Nodo nodo = ciudad.obtenerEntrada(y, x);
        return nodo != null && esTransitable(nodo);
    }


    public int costoDeCelda(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= altura) {
            return Integer.MAX_VALUE;
        }
        Nodo nodo = ciudad.obtenerEntrada(y, x);
        if (nodo != null) {
            return calcularCosto(nodo);
        }
        return Integer.MAX_VALUE;
    }

    public boolean tieneTrafico(int x, int y) {
        if (x < 0 || x >= ancho || y < 0 || y >= altura) {
            return false;
        }
        Nodo nodo = ciudad.obtenerEntrada(y, x);
        return nodo != null && nodo.tieneTrafico();
    }

    /**
     * Mueve el jugador a una nueva posición.
     */
    public void moverJugador(int x, int y) {
        actualizarJugador(new Coordenada(y, x));
    }
    

    /**
     * Obtiene la ruta óptima entre dos coordenadas usando A*.
     */
    public Pila<Nodo> obtenerRutaOptima(Coordenada origen, Coordenada destino) {
        Nodo nodoOrigen = this.ciudad.obtenerEntrada(origen.obtenerY(), origen.obtenerX());
        Nodo nodoDestinoNodo = this.ciudad.obtenerEntrada(destino.obtenerY(), destino.obtenerX());
        return gps.buscarCaminoMinimo(nodoOrigen, nodoDestinoNodo);
    }
}