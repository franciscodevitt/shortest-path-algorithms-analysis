# Shortest Path Algorithms Analysis — "Conduciendo por la ciudad"

Una aplicación en Java que combina una **simulación de ciudad inspirada en GTA** con un **análisis comparativo profundo de algoritmos de camino mínimo**. El proyecto fue construido desde cero: cada estructura de datos es una implementación propia (sin usar Java Collections Framework), la simulación modela vehículos, garajes y concesionarios, y el módulo analítico benchmarkea A\*, Dijkstra y Bellman-Ford sobre grafos de distintos tamaños.

![Captura del juego](doc/captura-mision.png)

---

## Tabla de contenidos

- [Descripción](#descripción)
- [Funcionalidades principales](#funcionalidades-principales)
- [Tecnologías utilizadas](#tecnologías-utilizadas)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Uso](#uso)
- [Ejemplos y resultados](#ejemplos-y-resultados)
- [Posibles mejoras futuras](#posibles-mejoras-futuras)

---

## Descripción

Este proyecto fue desarrollado como un trabajo práctico universitario para una materia de Estructuras de Datos y Algoritmos. Tiene dos objetivos entrelazados:

1. **Simulación de juego** — Una aplicación de consola + JavaFX que simula una ciudad simplificada al estilo GTA. El jugador administra un garaje de vehículos, compra en un concesionario y completa misiones que requieren navegar desde un origen hasta un destino a través de una grilla urbana. La navegación está impulsada por un motor de GPS basado en **A\***.

2. **Análisis de algoritmos** — Un módulo automatizado de benchmarking que ejecuta A\*, Dijkstra y Bellman-Ford sobre nueve mapas urbanos de tamaño creciente, recolectando tiempos promedio de ejecución, expansiones de nodos, relajaciones de aristas y operaciones de Priority Queue por consulta. Los resultados se exportan a CSV para su análisis posterior.

Una restricción clave de diseño fue que **todos los tipos abstractos de datos (TDAs) debían implementarse desde cero**: arreglos dinámicos, listas enlazadas, pilas, colas, Hash Tables, sets, Priority Queues, grafos genéricos y más, sin depender de las colecciones de `java.util`.

---

## Funcionalidades principales

### Estructuras de datos propias (TDAs)

Cada estructura fue implementada genéricamente desde cero:

| Estructura | Descripción |
|-----------|-------------|
| `Vector<T>` | Arreglo dinámico con redimensionamiento por factor de dos |
| `Lista<T>` | Lista doblemente enlazada con iterador bidireccional |
| `Pila<T>` | Stack LIFO usando nodos enlazados |
| `Cola<T>` | Queue FIFO usando nodos enlazados |
| `Diccionario<C,V>` | Hash Table con open chaining y rehash según factor de carga |
| `Conjunto<T>` | Hash Set respaldado por `Diccionario` |
| `Matriz<T>` | Arreglo genérico bidimensional de tamaño fijo |
| `ColaPrioridad<T>` | Max-heap (binary heap) sobre `Vector` |
| `Grafo<T>` | Grafo pesado no dirigido usando listas de adyacencia |
| `Tupla<C,V>` | Par genérico clave-valor |
| `Iterador<T>` | Interfaz de iterador bidireccional |

### Algoritmos de camino mínimo

Se implementaron e instrumentaron tres algoritmos para benchmarking:

| Algoritmo | Complejidad | Característica principal |
|-----------|------------|--------------------------|
| **A\*** | O((V + E) log E) | La heurística Manhattan guía la búsqueda, minimizando los nodos explorados |
| **Dijkstra** | O((V + E) log E) | A\* con heurística nula; explora toda la frontera |
| **Bellman-Ford** | O(V × E) | Basado en relajaciones; el más lento, pero soporta pesos negativos |

Los tres fueron instrumentados para contar expansiones de nodos, relajaciones de aristas y operaciones de Priority Queue por consulta. Cada experimento promedió 1 000 pares origen-destino aleatorios × 10 repeticiones, usando una semilla fija para garantizar reproducibilidad.

### Simulación de ciudad estilo GTA

- **Jerarquía de vehículos** — `Auto`, `Moto` y `Exotico` extienden una clase abstracta `Vehiculo` con lógica polimórfica de costo de mantenimiento y consumo de combustible.
- **Garage (`Garaje`)** — Almacenamiento de vehículos con capacidad limitada y una zona de espera FIFO; soporta carga de combustible, seguimiento de kilometraje y gestión diaria.
- **Dealership (`Concesionario`)** — Catálogo de vehículos con búsqueda, compra y persistencia en CSV.
- **Mapas de ciudad** — Grillas en archivos de texto con cuatro tipos de terreno: calle (`+`), edificio (`#`), parque (`-`) y agua (`~`). El tráfico se aplica probabilísticamente sobre las calles.
- **Navegación GPS** — El pathfinding con A\* encuentra la ruta más barata (las calles cuestan 1, los parques 15 y el tráfico suma +5), y luego guía al jugador paso a paso.
- **UI basada en menús** — Una interfaz en capas de consola + JavaFX que cubre nueva/cargar partida, gestión de garaje, exploración del concesionario y despacho de misiones.

### Benchmarking y análisis

- Carga nueve mapas urbanos que van desde ~31 vértices / 60 aristas hasta ~844 vértices / 1748 aristas.
- Ejecuta una suite de experimentos reproducible y exporta los resultados a `resultados/mediciones.csv`.
- Cubre tanto mapas regulares como mapas deformados intencionalmente (grafos dispersos/asimétricos) para hacer stress-test de cada algoritmo.

---

## Tecnologías utilizadas

| Tecnología | Rol |
|------------|-----|
| **Java 11** | Lenguaje principal |
| **JavaFX 20.0.1** | Interfaz gráfica y multimedia |
| **Maven** | Build, gestión de dependencias y testing |
| **JUnit 5** | Pruebas unitarias para todos los TDAs propios |
| **PlantUML** | Documentación de diagramas de clases UML |

> No se utilizaron librerías externas de algoritmos o estructuras de datos. Todos los TDAs fueron construidos desde primeros principios.

---

## Estructura del proyecto

```
shortest-path-algorithms-analysis/
│
├── data/
│   ├── ciudades/          # Mapas urbanos para el juego (mapa1–mapa8.txt)
│   ├── analisisCiudades/  # Mapas urbanos para benchmarking (mapa1–mapa9.txt + variantes deformadas)
│   ├── default/           # CSVs de inventario inicial de vehículos
│   └── vehiculos/         # Catálogo de vehículos exóticos
│
├── src/
│   ├── main/java/org/ayed/
│   │   ├── Main.java                  # Punto de entrada de la aplicación
│   │   ├── gta/                       # Simulación del juego
│   │   │   ├── Vehiculo.java          # Vehículo abstracto (Auto, Moto, Exotico)
│   │   │   ├── Garaje.java            # Garage con zona de espera FIFO
│   │   │   ├── Concesionario.java     # Catálogo del dealership
│   │   │   ├── mapa/                  # Carga de mapas, generación de grafo, GPS
│   │   │   │   ├── Mapa.java
│   │   │   │   ├── Nodo.java
│   │   │   │   └── Coordenada.java
│   │   │   ├── Menus/                 # Jerarquía de menús de consola
│   │   │   └── ui/                    # Vistas y controladores JavaFX
│   │   ├── tda/                       # Estructuras de datos propias
│   │   │   ├── vector/
│   │   │   ├── lista/
│   │   │   ├── colaPrioridad/
│   │   │   ├── diccionario/
│   │   │   ├── conjunto/
│   │   │   ├── grafo/
│   │   │   ├── matriz/
│   │   │   ├── iterador/
│   │   │   ├── tupla/
│   │   │   ├── comparador/
│   │   │   └── aestrella/             # A* con interfaz de heurística
│   │   └── informe/                   # Módulo de benchmarking
│   │       ├── programa/
│   │       │   └── algoritmos/        # A*, Dijkstra, Bellman-Ford (instrumentados)
│   │       └── resultados/            # Salida CSV
│   └── test/java/org/ayed/tda/        # Pruebas JUnit 5 para TDAs
│
├── doc/                   # Capturas de pantalla y fuentes PlantUML
├── UMLs/                  # Código de diagramas UML
└── pom.xml
```

---

## Instalación

**Prerrequisitos:**
- Java 11 o superior (se recomienda Java 17+)
- Maven 3.6+
- Un JDK compatible con JavaFX, o el SDK de JavaFX instalado por separado

**Clonar el repositorio:**

```bash
git clone https://github.com/<your-username>/shortest-path-algorithms-analysis.git
cd shortest-path-algorithms-analysis
```

**Instalar dependencias:**

```bash
mvn install -DskipTests
```

---

## Configuración

### Inventario inicial de vehículos

El juego carga su inventario inicial de vehículos desde dos archivos CSV:

- `data/default/default_garaje.csv` — vehículos precargados en el garage
- `data/default/default_concesionario.csv` — vehículos disponibles en el dealership

Podés editar estos archivos directamente para personalizar el inventario inicial. Cada fila sigue el formato:

```
NOMBRE,PRECIO,TIPO,RUEDAS,CAPACIDAD_GASOLINA,GASOLINA_ACTUAL,KILOMETRAJE,VELOCIDAD_MAXIMA
Fiat 600,500,AUTO,4,90,0,0,150
```

### Mapas de ciudad

Los mapas son archivos de texto plano en formato de grilla. Cada celda es una de las siguientes:

| Símbolo | Terreno | Costo |
|--------|---------|-------|
| `+` | Calle | 1 (+ 5 si hay tráfico) |
| `-` | Parque | 15 |
| `#` | Edificio | Intransitable |
| `~` | Agua | Intransitable |

Se pueden agregar nuevos mapas en `data/ciudades/` y seleccionarlos desde el menú de misiones.

---

## Uso

### Ejecutar el juego

```bash
mvn -DskipTests exec:java \
	-Dexec.mainClass="org.ayed.Main" \
	-Dexec.cleanupDaemonThreads=false \
	-Dexec.vmArgs="--add-modules=javafx.controls,javafx.fxml,javafx.media"
```

La aplicación comienza con un menú principal basado en consola:

```
MenuInicio
├── Nueva Partida       → crear una nueva sesión de juego
├── Cargar Partida      → reanudar desde un estado guardado
├── Créditos
└── Salir
```

Una vez dentro, podés:
- **Administrar tu garage** — agregar, vender y cargar combustible a vehículos
- **Explorar el dealership** — buscar y comprar vehículos
- **Iniciar una misión** — elegir un mapa; el GPS (A\*) calcula la ruta más corta y te guía paso a paso por la grilla urbana
- **Guardar y salir** — el estado se persiste en archivos CSV

### Ejecutar la suite de benchmarking

El módulo de análisis puede ejecutarse como una main class independiente:

```bash
mvn -DskipTests exec:java \
	-Dexec.mainClass="org.ayed.informe.programa.PruebaComplejidad"
```

Esto ejecuta A\*, Dijkstra y Bellman-Ford sobre los nueve mapas de benchmark y escribe los resultados en:

```
src/main/java/org/ayed/informe/resultados/mediciones.csv
```

### Solo compilar

```bash
mvn compile -DskipTests
```

### Ejecutar pruebas

```bash
mvn test
```

### Empaquetar como JAR

```bash
mvn package -DskipTests
```

---

## Ejemplos y Resultados

### Navegación GPS

Cuando comienza una misión, el jugador selecciona un mapa. La clase `Mapa` parsea la grilla en un grafo ponderado, aplica tráfico probabilístico (10 % de las calles, con semilla reproducible), y luego A\* calcula el camino de menor costo. El juego después renderiza el recorrido y simula el viaje paso a paso.

### Resultados del Benchmark de Algoritmos

A continuación se muestran promedios representativos sobre 1 000 consultas aleatorias por mapa (10 repeticiones cada una):

| Mapa | Vértices | Aristas | Tiempo A\* | Tiempo Dijkstra | Tiempo Bellman-Ford |
|-----|----------|---------|------------|-----------------|---------------------|
| mapa1 | 31 | 66 | 0.010 ms | 0.020 ms | 0.060 ms |
| mapa5 | 340 | 816 | 0.070 ms | 0.220 ms | 2.200 ms |
| mapa9 | 1 645 | 4 288 | 0.260 ms | 1.190 ms | 30.660 ms |

**Observaciones clave:**

- **A\*** es consistentemente el más rápido. Su heurística Manhattan poda aproximadamente el 80 % de los nodos que Dijkstra expandiría, lo que lo convierte en la opción más clara para un GPS en tiempo real.
- **Dijkstra** escala bien en la práctica (O((V + E) log E)), pero carece de la guía direccional que aporta una heurística.
- **Bellman-Ford** presenta un crecimiento O(V × E) que se vuelve prohibitivo a medida que aumenta el tamaño del mapa: es 118× más lento que A\* en el grafo más grande.

Los resultados completos están disponibles en [`src/main/java/org/ayed/informe/resultados/mediciones.csv`](src/main/java/org/ayed/informe/resultados/mediciones.csv) y el análisis escrito detallado está incluido en [`Informe_Grafosaurios.pdf`](Informe_Grafosaurios.pdf).

---

## Posibles Mejoras Futuras

- **A\* bidireccional** — ejecutar búsquedas simultáneas desde el origen y el destino para reducir a la mitad la frontera explorada.
- **Priority Queue con decrease-key** — reemplazar el enfoque actual de lazy deletion por un Fibonacci heap para alcanzar O((V + E) + V log V) en Dijkstra.
- **Tráfico dinámico** — actualizar pesos de aristas en tiempo real e implementar re-planificación incremental (D\* Lite) en lugar de volver a ejecutar A\* desde cero.
- **Generador de mapas más grandes** — generar grillas urbanas proceduralmente a escala arbitraria para producir curvas de benchmarking estadísticamente más robustas.
- **Vista gráfica de la ciudad** — renderizar la grilla de la ciudad y el camino calculado visualmente dentro de la ventana JavaFX en lugar de la consola.
- **Cifrado de archivos de guardado** — la persistencia actual basada en CSV almacena el estado del juego en texto plano; un cifrado liviano evitaría la edición trivial de partidas guardadas.
- **Validación de soporte para pesos negativos** — Bellman-Ford es el único algoritmo que maneja pesos negativos; agregar detección automática de ciclos negativos haría que la base de código esté lista para producción.
- **Heurísticas adicionales** — comparar las heurísticas Euclidean, Chebyshev y Octile contra Manhattan para medir su efecto sobre el rendimiento de A\* en grafos no alineados a una grilla.
