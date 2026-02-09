# Informe de Complejidad Algorítmica

Este directorio contiene el informe de complejidad algorítmica para los Algoritmos de Caminos Mínimos

El objetivo es **comparar experimentalmente** distintos algoritmos de caminos
mínimos para decidir cuál es el más eficiente para implementar el GPS del programa principal.

---

## Objetivo del programa

El programa implementado en Java ejecuta una **rutina experimental automática**
que:

- Genera grafos de prueba bajo condiciones controladas.
- Ejecuta distintos algoritmos de caminos mínimos **sobre el mismo grafo**.
- Registra métricas de rendimiento.
- Exporta los resultados a un archivo CSV para su posterior análisis.

Los algoritmos evaluados incluyen:
- **A\*** con Heuristica Manhattan.
- **Dijkstra**.
- **Bellman-Ford**.

---

## Condiciones experimentales

Para garantizar comparaciones justas y reproducibles:

- Todos los algoritmos se ejecutan sobre **la misma instancia de grafo**.
- Los grafos se generan usando un **generador pseudoaleatorio con semilla fija**.
- No se regeneran grafos entre algoritmos ni entre repeticiones.
- Cada algoritmo se ejecuta múltiples veces y se promedian los resultados.

Esto asegura que las diferencias observadas se deban al algoritmo y no al azar.

---

## Resultados

Los resultados se guardan en un archivo CSV ubicado en el directorio `resultados/`.

Este archivo contiene, para cada experimento:
- tamaño del grafo
- algoritmo utilizado
- tiempo promedio de ejecución
- cantidad promedio de operaciones

El CSV se utiliza luego para generar tablas y gráficos en el informe escrito.
