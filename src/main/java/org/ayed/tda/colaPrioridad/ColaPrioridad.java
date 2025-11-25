package org.ayed.tda.colaPrioridad;

import org.ayed.tda.comparador.Comparador;
import org.ayed.tda.conjunto.ExcepcionConjunto;
import org.ayed.tda.vector.Vector;

public class ColaPrioridad<T> {
    // NOTA: Implementar el vector con factor-of-two
    // para tener complejidades logarítmicas.
    private Vector<T> datos;
    private Comparador<T> comparador;

    /**
     * Constructor de ColaPrioridad.
     *
     * @param comparador Comparador a utilizar.
     *                   No puede ser nulo.
     * @throws ExcepcionColaPrioridad si el comparador es nulo.
     */
    public ColaPrioridad(Comparador<T> comparador) {
        // Implementar.
        if (comparador == null) throw new ExcepcionColaPrioridad("El comparador no puede ser nulo");
        this.datos = new Vector<>();
        this.comparador = comparador;
    }

    /**
     * Constructor de copia de ColaPrioridad.
     *
     * @param colaPrioridad Cola a copiar.
     *                      No puede ser nula.
     * @throws ExcepcionColaPrioridad si la cola es nula.
     */
    public ColaPrioridad(ColaPrioridad<T> colaPrioridad) {
        // Implementar.
        if(colaPrioridad == null){
            throw new ExcepcionColaPrioridad("No se puede copiar una cola de prioridad nula");
        }
        this.comparador = colaPrioridad.comparador;
        this.datos = new Vector<>(colaPrioridad.datos);
    }

    /**
     * Reordena el Heap para mantener el invariante.
     * Desplaza datos hacia arriba, comparando el dato actual
     * con su padre, hasta cumplir con el invariante.
     * Inicia en el último dato del vector.
     */
    private void heapificarHaciaArriba() {
        // Implementar.
        Comparador<T> cmp = this.comparador;
        
        int indice = this.datos.tamanio()-1;
        int indicePadre = (indice-1)/2;

        T datoActual = this.datos.dato(indice);
        T datoPadre = this.datos.dato(indicePadre);
        
        while ( cmp.comparar(datoActual, datoPadre) > 0 ) { // Mientras el dato actual sea mas grande que su padre, aplico swap entre ambos
            swap(indice, indicePadre);

            //actualizo los indices
            indice = indicePadre; 
            indicePadre = (indice-1)/2;

            //actualizo los datos
            datoPadre = this.datos.dato(indicePadre);
        }

    }

    /**
     * Reordena el Heap para mantener el invariante.
     * Desplaza datos hacia abajo, comparando el dato actual
     * con el hijo con mayor prioridad, hasta cumplir con el
     * invariante. Inicia en el primer dato del vector.
     */
    private void heapificarHaciaAbajo() {
        // Implementar.
        Comparador<T> cmp = this.comparador;
        
        int indice = 0;
        int indiceHijoIzq = 1;
        int indiceHijoDer = 2;
        int indiceHijoMaximo;

        T datoActual = this.datos.dato(indice);
        T datoHijoIzq = this.datos.dato(indiceHijoIzq);
        T datoHijoDer = null;
        T hijoMaximo;


        if(this.datos.tamanio() > 2){
            datoHijoDer = this.datos.dato(indiceHijoDer);
            //Busco el hijo de mayor priorirdad
            if(cmp.comparar(datoHijoIzq, datoHijoDer) >=0){ //hijo izquierdo >= hijo derecho
                hijoMaximo = datoHijoIzq;
                indiceHijoMaximo = indiceHijoIzq;
            }else{
                hijoMaximo = datoHijoDer;
                indiceHijoMaximo = indiceHijoDer;
            }
        }else{
            hijoMaximo = datoHijoIzq;
            indiceHijoMaximo = indiceHijoIzq;
        }

        while (cmp.comparar(datoActual, hijoMaximo) < 0) { //dato < el hijo de mayor prioridad
            swap(indice, indiceHijoMaximo);
            
            //Actualizo los indices
            indice = indiceHijoMaximo;
            indiceHijoIzq = (indice*2)+1;
            indiceHijoDer = (indice*2)+2;

            //actualizo los datos de los hijos
            if(indiceHijoIzq < this.datos.tamanio()){
                datoHijoIzq = this.datos.dato(indiceHijoIzq);
            }else{
                datoHijoIzq = null;
                datoHijoDer = null;
            }

            if(indiceHijoDer < this.datos.tamanio()){
                datoHijoDer = this.datos.dato(indiceHijoDer);
            }else{
                datoHijoDer= null;
            }
            
            if(datoHijoIzq == null && datoHijoDer == null){ //si ambos hijos son null ya estoy en la ultima posicion, termine
                break;
            }

            //vuelvo a buscar el hijo de mayor prioridad
            if(datoHijoDer == null || cmp.comparar(datoHijoIzq, datoHijoDer) >=0){ //hijo izquierdo >= hijo derecho
                hijoMaximo = datoHijoIzq;
                indiceHijoMaximo = indiceHijoIzq;
            }else{
                hijoMaximo = datoHijoDer;
                indiceHijoMaximo = indiceHijoDer;
            }

        }

    }

    /**
     * Agrega el dato a la cola, manteniendo el invariante
     * del Heap.
     *
     * @param dato Dato a agregar.
     */
    public void agregar(T dato) {
        // Implementar.
        this.datos.agregar(dato);
        this.heapificarHaciaArriba();
    }

    /**
     * Elimina el siguiente dato de la cola (mayor prioridad),
     * manteniendo el invariante del Heap.
     *
     * @return el dato con mayor prioridad en la cola.
     * @throws ExcepcionColaPrioridad si la cola está vacía.
     */
    public T eliminar() {
        // Implementar.
        T dato = this.siguiente();
        swap(0, this.datos.tamanio()-1);
        this.datos.eliminar();
        
        if(this.datos.tamanio()>1){
            this.heapificarHaciaAbajo();
        }

        return dato;
    }

    /**
     * Obtiene el siguiente dato de la cola (mayor prioridad).
     *
     * @return el dato con mayor prioridad en la cola.
     * @throws ExcepcionColaPrioridad si la cola está vacía.
     */
    public T siguiente() {
        // Implementar.
        if (this.datos.vacio()) throw new ExcepcionColaPrioridad("La cola esta vacia");
        return this.datos.dato(0);
    }

    /**
     * Obtiene el tamaño de la cola.
     *
     * @return el tamaño de la cola.
     */
    public int tamanio() {
        // Implementar.
        return this.datos.tamanio();
    }

    /**
     * Evalúa si la cola está vacía.
     *
     * @return true si la cola está vacía.
     */
    public boolean vacio() {
        // Implementar.
        return this.datos.vacio();
    }

    /**
     * Hace un intercambio de posiciones entre dos elementos de un Vector
     * @param i Posicion del elemento iesimo a intercambiar 
     * @param j Posicion del elemento jesimo a intercambiar
     */
    private void swap(int i, int j){
        T iDato = this.datos.dato(i);
        T jDato = this.datos.dato(j);
        this.datos.modificarDato(iDato, j);
        this.datos.modificarDato(jDato, i);
    }

}
