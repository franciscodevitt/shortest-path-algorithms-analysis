[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/cGHEVCeI)
# TPG - 2c2025 - Manejando por la ciudad - Grupo GRAFOSAURIOS

![Captura del juego](doc/captura-mision.png)

## Integrantes:

1. Devitt, Francisco - 105402 (Delegado)
2. Sciani, Carola - 111406
3. Woiciechowski, Matias - 104837
4. Gonzalez, Axel - 114001

## Entregables:

1. [Informe de complejidad algorítmica](Informe_Grafosaurios.pdf)
2. [Video de demostración del programa](https://drive.google.com/file/d/1-wpHSg8PORbuOqN_nmjHhJw3OPbRReaN/view?usp=drive_link)


## Proyecto

El proyecto está configurado usando [Maven](https://maven.apache.org/),
y puede ser compilado, empaquetado y probado fácilmente usando los siguientes comandos:

### Compilar

```
mvn compile -DskipTests
```

### Correr el juego

```
mvn -DskipTests exec:java -Dexec.mainClass="org.ayed.Main" -Dexec.cleanupDaemonThreads=false -Dexec.vmArgs="--add-modules=javafx.controls,javafx.fxml,javafx.media"
```

### Empaquetar

```
mvn package -DskipTests
```

### Pruebas

```
mvn test
```

## Aclaraciones para el corrector

Referencias:

- Sobreescritura de los métodos equals(Object) y hashCode() para el manejo de objetos en las claves de estructuras de datos basadas en hashing.

<https://www.geeksforgeeks.org/java/internal-working-of-hashmap-java/>

Si necesitan aclarar o justificar alguna decisión de implementación,
lo pueden hacer escribiendo en esta sección del README.
