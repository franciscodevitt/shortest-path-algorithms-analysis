package org.ayed.tda.diccionario;

import org.ayed.tda.comparador.Comparador;
import org.ayed.tda.lista.Lista;
import org.ayed.tda.tupla.Tupla;

/**
 * Diccionario asociativo (clave, valor) que mantiene
 * el orden de los datos en base a su clave y al
 * comparador utilizado. Está implementado con un
 * árbol binario de búsqueda no balanceado.
 *
 * @param <C> El tipo de dato de la clave.
 *            Este tipo debe ser comparable.
 * @param <V> El tipo de dato del valor.
 *            Este tipo no necesita ser comparable obligatoriamente,
 *            pero puede ser útil para el usuario si se decide
 *            implementar métodos para consultar si un valor está
 *            en el diccionario.
 */
public class DiccionarioOrdenado<C, V> {
    private Nodo<C, V> raiz;
    private Comparador<C> comparador;
    private int cantidadDatos;

    /**
     * Constructor.
     *
     * @param comparador Comparador a utilizar.
     *                   No puede ser nulo.
     * @throws ExcepcionDiccionario si el comparador es nulo.
     */
    public DiccionarioOrdenado(Comparador<C> comparador) {
        // Implementar.
        if (comparador == null){
            throw new ExcepcionDiccionario("el comparador es nulo");
        }
        this.comparador = comparador;
        this.raiz = null;
        this.cantidadDatos = 0;
    }

    /**
     * Constructor de copia de DiccionarioOrdenado.
     * <p>
     * TIP: Implementar un método que clone un subárbol.
     *
     * @param diccionarioOrdenado Diccionario a copiar.
     *                            No puede ser nulo.
     * @throws ExcepcionDiccionario si el diccionario es nulo.
     */
    public DiccionarioOrdenado(DiccionarioOrdenado<C, V> diccionarioOrdenado) {
        // Implementar.
        if (diccionarioOrdenado == null){
            throw new ExcepcionDiccionario("El diccionario a copiar es null");
        }
        this.raiz = clonarSubArbol(diccionarioOrdenado.raiz);
        this.cantidadDatos = diccionarioOrdenado.cantidadDatos;
        this.comparador = diccionarioOrdenado.comparador;
    }

    /**
     * Obtiene el sucesor inmediato del nodo.
     *
     * @param nodo Nodo inicial.
     * @return el sucesor inmediato.
     */
    private Nodo<C, V> obtenerSucesorInmediato(Nodo<C, V> nodo) {
        // Implementar.
        Nodo<C, V> sucesor;
        if (nodo == null) { //si el nodo es null devuelvo null
            sucesor = null;
        }else if (nodo.hijoDerecho != null) { // Si hay subárbol derecho -> sucesor = mínimo del derecho
            sucesor = nodo.hijoDerecho;
            while (sucesor.hijoIzquierdo != null) {
                sucesor = sucesor.hijoIzquierdo;
            }
        }else{
            //Si no hay subárbol derecho -> hay que ver los ancestros: subo por los padres
            Nodo<C, V> padre = nodo.padre;
            Nodo<C, V> actual = nodo;
    
            while (padre != null && actual == padre.hijoDerecho) { // subir mientras sea hijo derecho
                actual = padre;
                padre = padre.padre;
            }
    
            sucesor = padre;

        }
        return sucesor;

    }


    /**
     * Agrega un mapeo {clave, valor} al diccionario.
     * Si ya existía la clave en el diccionario, reemplaza
     * el valor anterior y lo devuelve.
     *
     * @param clave Clave a agregar.
     * @param valor Valor a agregar.
     * @return el valor anterior. Si no había un valor
     * anterior, devuelve null.
     */
    public V agregar(C clave, V valor) {
        // Implementar.
        V valorAnterior = null;
        
        if (this.cantidadDatos == 0){ //si el diccionario esta vacio lo agrego en la raiz
            this.raiz = new Nodo<C,V>(clave, valor);
        }else{ //sino busco al nodo
            
            Nodo<C,V> nodo = buscarNodo(clave, this.raiz);
            if (!nodo.clave.equals(clave)){ //si las claves no coinciden es porque tengo al padre, y el nodo hay que crearlo
                if(cmp(clave, nodo.clave) <= 0){ //si es menor o igual al padre lo agrego a su izquierda
                    nodo.hijoIzquierdo = new Nodo<C,V>(clave, valor);
                }else{ //si es mayor al padre lo agrego a su derecha
                    nodo.hijoDerecho = new Nodo<C,V>(clave, valor);
                }
            }else{ //si el nodo ya existe le modifico el valor
                valorAnterior = nodo.valor;
                nodo.valor = valor;
            }
        }

        return valorAnterior;
    }

    /**
     * Elimina un mapeo {clave, valor} del diccionario,
     * si existe. Si no existe, el diccionario queda en
     * el mismo estado.
     * <p>
     * NOTA: Para eliminar nodos interiores, se utiliza
     * el sucesor inmediato.
     *
     * @param clave Clave a eliminar.
     * @return el valor eliminado. Si no se eliminó
     * un valor, devuelve null.
     */
    public V eliminar(C clave) {
        // Implementar.
        Nodo<C,V> nodo = null;
        Nodo<C,V> padre = buscarNodo(clave, this.raiz);
        V dato = null;
        if (padre == null){
            this.raiz = null;
        }
        if (padre.hijoIzquierdo != null && padre.hijoIzquierdo.clave.equals(clave)){
            nodo = padre.hijoIzquierdo;
            dato = nodo.valor;
        }else if(padre.hijoDerecho != null && padre.hijoDerecho.clave.equals(clave)){
            nodo = padre.hijoDerecho;
            dato = nodo.valor;
        }

        if (nodo != null && cantHijos(nodo) == 0){
            eliminarSinHijos(nodo, padre);
        }else if(nodo != null && cantHijos(nodo) == 1){
            eliminarConUnHijo(nodo, padre);
        }else{
            Nodo<C,V> sucesor = obtenerSucesorInmediato(nodo);
            this.swap(nodo, sucesor);
            if (sucesor.hijoDerecho != null){
                sucesor.padre.hijoIzquierdo = sucesor.hijoDerecho;
            }

        }

        return (V) new Object();
    }

    /**
     * Obtiene un mapeo {clave, valor} del diccionario,
     * si existe.
     *
     * @param clave Clave a buscar.
     * @return el valor buscado. Si no existe, devuelve
     * null.
     */
    public V obtenerValor(C clave) {
        // Implementar.
        return (V) new Object();
    }

    /**
     * Devuelve el recorrido inorder del árbol.
     *
     * @return el recorrido.
     */
    public Lista<Tupla<C, V>> inorder() {
        // Implementar.
        return new Lista<>();
    }

    /**
     * Devuelve el recorrido preorder del árbol.
     *
     * @return el recorrido.
     */
    public Lista<Tupla<C, V>> preorder() {
        // Implementar.
        return new Lista<>();
    }

    /**
     * Devuelve el recorrido postorder del árbol.
     *
     * @return el recorrido.
     */
    public Lista<Tupla<C, V>> postorder() {
        // Implementar.
        return new Lista<>();
    }

    /**
     * Devuelve el recorrido en ancho del árbol.
     *
     * @return el recorrido.
     */
    public Lista<Tupla<C, V>> ancho() {
        // Implementar.
        return new Lista<>();
    }

    /**
     * Obtiene el tamaño del diccionario.
     *
     * @return el tamaño del diccionario.
     */
    public int tamanio() {
        // Implementar.
        return this.cantidadDatos;
    }

    /**
     * Evalúa si el diccionario está vacío.
     *
     * @return true si el diccionario está vacío.
     */
    public boolean vacio() {
        // Implementar.
        return (this.cantidadDatos==0);
    }


    private Nodo<C,V> clonarSubArbol(Nodo<C,V> nodo){
        if (nodo == null){
            return null;
        }
        // Creo nodo sin padre ni hijos
    Nodo<C,V> nuevo = new Nodo<>(nodo.clave, nodo.valor);

    // Clona el hijo izquierdo
    nuevo.hijoIzquierdo = clonarSubArbol(nodo.hijoIzquierdo);
    if (nuevo.hijoIzquierdo != null) {
        nuevo.hijoIzquierdo.padre = nuevo;
    }

    // Clona el hijo derecho
    nuevo.hijoDerecho = clonarSubArbol(nodo.hijoDerecho);
    if (nuevo.hijoDerecho != null) {
        nuevo.hijoDerecho.padre = nuevo;
    }

    return nuevo;
    }

    private int cmp(C clave1, C clave2){
        return this.comparador.comparar(clave1, clave2);
    }

    /**
     * Busca el padre de un nodo segun su clave. si el nodo no existe devuelve cual seria su padre si este nodo existiese.
     * @param clave la clave del nodo que estoy buscando
     * @param raiz Raiz del subarbol en el que busco la clave
     * @return el padre del nodo que estoy buscando, si el nodo no existe me devuelve el padre de donde tendria que estar.
     */
    private Nodo<C,V> buscarNodo(C clave, Nodo<C,V> raiz){
        Nodo<C,V> nodo = null;
        if (raiz != null){
            if (raiz.clave.equals(clave)){ //si la clave coincide encontre el nodo
                nodo = raiz;
            } else if (cmp(clave, raiz.clave) < 0){ // si la clave es menor busco a la izquierda
                if (raiz.hijoIzquierdo == null){ // si no hay izquierda el nodo no exite y encontre al padre
                    nodo = raiz;
                }else{
                    nodo = buscarNodo(clave, raiz.hijoIzquierdo);
                }
            }else if (cmp(clave, raiz.clave) > 0){ //si la clave es mayor busco a la derecha
                if (raiz.hijoDerecho == null){ // si no hay derecha el nodo no existe y encontre al padre
                    nodo = raiz;
                }else{
                    nodo = buscarNodo(clave, raiz.hijoDerecho);
                }
            }
        }
        return nodo;
    }

    private void swap(Nodo<C,V> nodo1, Nodo<C,V> nodo2){
        if (nodo1 == null || nodo2 ==null){
            throw new ExcepcionDiccionario("No se puede hacer swap con un nodo nulo");
        }
        C claveAux = nodo1.clave;
        V valorAux = nodo1.valor;
        
        nodo1.clave = nodo2.clave;
        nodo1.valor = nodo2.valor;

        nodo2.clave = claveAux;
        nodo2.valor = valorAux;

    }

    private int cantHijos(Nodo<C,V> nodo){
        int cantidad = 0;
        if (nodo.hijoDerecho != null && nodo.hijoIzquierdo != null){
            cantidad = 2;
        }else if (nodo.hijoDerecho != null || nodo.hijoIzquierdo != null){
            cantidad = 1;
        }
        return cantidad;
    }

}
