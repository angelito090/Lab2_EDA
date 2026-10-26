/**
 * TDA Pila (LIFO): el ultimo elemento insertado es el primero en salir.
 *
 * <p>Las operaciones de extraccion o consulta sobre una pila vacia mantienen
 * el comportamiento de las clases de algs4 que sirven de base
 * (lanzan {@link java.util.NoSuchElementException}).</p>
 *
 * @param <T> tipo de los elementos almacenados
 */
public interface Pila<T> {

    /**
     * Inserta un elemento en la cima de la pila.
     *
     * @param item elemento a insertar
     */
    void push(T item);

    /**
     * Elimina y retorna el elemento en la cima de la pila.
     *
     * @return el elemento mas recientemente insertado
     * @throws java.util.NoSuchElementException si la pila esta vacia
     */
    T pop();

    /**
     * Retorna, sin eliminarlo, el elemento en la cima de la pila.
     *
     * @return el elemento mas recientemente insertado
     * @throws java.util.NoSuchElementException si la pila esta vacia
     */
    T peek();

    /**
     * Indica si la pila no contiene elementos.
     *
     * @return {@code true} si la pila esta vacia
     */
    boolean isEmpty();

    /**
     * Retorna la cantidad de elementos de la pila.
     *
     * @return numero de elementos almacenados
     */
    int size();
}
