package org.ayed.gta.mapa;

import org.ayed.tda.matriz.*;
import org.ayed.tda.grafo.*;
import org.ayed.tda.vector.*;
import org.ayed.gta.GestorArchivos;

/*
 * Clase que crea y carga el mapa de la ciudad para jugar la misión.
 */
public class Mapa {
    
    private final Matriz<Celda> ciudad;
    private final Grafo<Celda> grafoCiudad;
    private int ancho;
    private int altura;

    private Coordenada jugador;
    private Coordenada destino;
    private Coordenada recompensaExtra;
    private boolean recompensaExtraRecogida;

    private static final String RUTA_MAPAS = "data/ciudades/";  //ver bien esto

    private static final String CALLE = "+"; 
    private static final String EDIFICIO = "#";
    private static final String PARQUE = "-";
    private static final String AGUA = "~";  
    private static final double TASA_TRAFICO = 0.1;
    private static final double TASA_DISTANCIA_MINIMA = 0.3;
    private static final int COSTO_CALLE = 1;
    private static final int COSTO_PARQUE = 99;
    private static final int COSTO_TRAFICO = 5;

    /**
     * Constructor de Mapa. 
     * Carga el mapa de la ciudad desde un archivo y lo inicializa completamente.
     * 
     * @param ciudadElegida   Nombre del archivo de la ciudad a cargar.
     * @throws ExcepcionMapa  si el archivo correspondiente no tiene formato valido o no existe.
     */
    public Mapa(String ciudadElegida) {
        
        Vector<String> lineas = GestorArchivos.leerArchivo(RUTA_MAPAS + ciudadElegida);
        if (lineas.tamanio() < 2) {
            throw new ExcepcionMapa("El archivo de mapa es inválido o está vacío.");
        }
        
        this.procesarDimensiones(lineas.dato(0));
        
        this.ciudad = new Matriz<Celda>(this.altura, this.ancho);
        procesarCiudad(lineas);
        inicializarEntradaSalida();
        this.recompensaExtra = coordenadaAleatoria();
        this.recompensaExtraRecogida = false;
        
        this.grafoCiudad = new Grafo<Celda>();
        generarGrafo();
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
        return (terreno.equals(CALLE) && (Math.random() < TASA_TRAFICO)); 
    }
    
    /**
     * Metodo que recibe y parsea todas las lineas del archivo del mapa creando y cargando 
     * cada celda de la matriz.
     * 
     * @param lineas         Vector donde cada elemento es un string representando una fila del mapa. 
     * @throws ExcepcionMapa si el formato del mapa es incorrecto o le faltan datos.
     *                       si hay errores con las celdas o la matriz.
     */
    private void procesarCiudad (Vector<String> lineas) {

        try{
            if (lineas.tamanio() - 1 != altura) {
                throw new Exception("El número de filas no coincide con la dimensión declarada.");
            }
            for (int i = 1; i < lineas.tamanio(); i++) {
                int f = i - 1;
                String[] celdas = lineas.dato(i).trim().split("\\s+");
                
                if (celdas.length != ancho) {
                    throw new Exception("El número de columnas de la fila " + f + " no coincide con la dimensión declarada.");
                }
                for (int c = 0; c < ancho; c++) {
                    String terreno = celdas[c].trim();   
                    if (terreno.isEmpty()) {
                        throw new Exception("Celda vacía en (" + f + "," + c + ").");
                    } else if (!esTerrenoValido(terreno)) {
                        throw new Exception("Tipo de terreno invalido en (" + f + "," + c + ").");
                    }
                    
                    boolean tieneTrafico = generarTrafico(terreno);
                    Celda celda = new Celda(new Coordenada(f, c), terreno, tieneTrafico);
                    this.ciudad.agregarEntrada(f, c, celda); 
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
            Celda celda = ciudad.obtenerEntrada(f, c);

            if (esTransitable(celda) && !celda.obtenerTerreno().equals(PARQUE)) {
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

        this.jugador = entrada;
        this.destino = salida;
    }

    /**
     * Determina si terreno representa un tipo permitido para la celda.
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
     * Determina si la Celda es transitable.
     * 
     * @param  celda   Celda que se quiere consultar.
     * @return         True si es transitable (CALLE o PARQUE).
     *                 False si no lo es (EDIFICIO, AGUA).
     */
    private boolean esTransitable(Celda celda) {
        return celda.obtenerTerreno().equals(CALLE) || celda.obtenerTerreno().equals(PARQUE);
    }

    /**
    * Calcula el costo de una celda según su tipo de terreno.
    * Si la celda tiene tráfico, el costo del terreno se multiplica por COSTO_TRAFICO.
    *
    * @param celda   Celda para la cual se desea calcular el costo.
    * @return        El costo calculado.
    */
    private int calcularCosto(Celda celda) {
        
        String terreno = celda.obtenerTerreno();
        int costo = 0;
        if (terreno.equals(CALLE)) {
            costo = COSTO_CALLE;
        } else if (terreno.equals(PARQUE)) {
            costo = COSTO_PARQUE;
        }
        if (celda.tieneTrafico()){
            costo *= COSTO_TRAFICO;
        }  
        return costo;
    }

    /**
    * Agrega la arista entre la celda actual y la celda vecino cuyas fila y columna se pasaron como parametro.
    * La arista se agrega solamente si celda vecino tiene coordenadas validas, existe y es transitable.
    * El peso de la arista se calcula como el máximo entre los costos de ambas celdas.
    *
    * @param actual      Celda origen desde la cual se quiere conectar.
    * @param filaVecino  Fila de la celda vecino izquierda o superior.
    * @param colVecino   Columna de la celda vecino izquierda o superior.
    */
    private void conectarVecino(Celda actual, int filaVecino, int colVecino) {

        boolean filaValida = filaVecino >= 0 && filaVecino < altura;
        boolean colValida = colVecino >= 0 && colVecino < ancho;

        if (filaValida && colValida) {

            Celda vecino = ciudad.obtenerEntrada(filaVecino, colVecino);
            if (vecino != null && esTransitable(vecino)) {
                
                int costoActual = calcularCosto(actual);
                int costoVecino = calcularCosto(vecino);
                int costo = Math.max(costoActual, costoVecino);
                grafoCiudad.agregarArista(actual, vecino, costo);
            }
        }   
    }

    /**
    * Carga el grafo de la ciudad a partir de la matriz ciudad.
    * Recorre la matriz de arriba hacia abajo y de izquierda a derecha. 
    * Agrega las celdas transitables como vértice y los conecta con el vecino izquierdo 
    * y el vecino derecho (si existen y son transitables).
    * 
    * Nota: solo conecta con los vecinos izquierdo y derecho porque agregarArista de clase Grafo 
    *       necesita que el vertice ya exista en el grafo ciudad. De todas formas al terminar de recorrer 
    *       la matriz, todos las aristas van a terminar correctamente conectadas.
    */
    private void generarGrafo() {

        for (int f = 0; f < altura; f++) {
            for (int c = 0; c < ancho; c++) {

                Celda actual = ciudad.obtenerEntrada(f, c);
                if (actual != null && esTransitable(actual)) {
                    grafoCiudad.agregarVertice(actual);
                    conectarVecino(actual, f, c - 1);
                    conectarVecino(actual, f - 1, c); 
                }
            }
        }
    }

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
    public Grafo<Celda> obtenerGrafo() {
        Grafo<Celda> copia = new Grafo<Celda>(this.grafoCiudad);

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

        Celda nuevaCelda = ciudad.obtenerEntrada(nuevaCoordenada.obtenerY(), nuevaCoordenada.obtenerX());
        if (nuevaCelda == null || !esTransitable(nuevaCelda)) {
            throw new ExcepcionMapa("Coordenada invalida, no es transitable");
        }
    
        this.jugador = nuevaCoordenada;

        if (this.recompensaExtra != null && this.jugador.equals(this.recompensaExtra)) {
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
        return this.jugador.equals(destino);
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
}