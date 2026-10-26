/**
 * TDA Fila (FIFO): el primer elemento insertado es el primero en salir.
 *
 * <p>Las operaciones de extraccion o consulta sobre una fila vacia mantienen
 * el comportamiento de las clases de algs4 que sirven de base
 * (lanzan {@link java.util.NoSuchElementException}).</p>
 *
 * @param <T> tipo de los elementos almacenados
 */
public interface Fila<T> {

    /**
     * Inserta un elemento al final de la fila.
     *
     * @param item elemento a insertar
     */
    void enqueue(T item);

    /**
     * Elimina y retorna el elemento al frente de la fila.
     *
     * @return el elemento insertado hace mas tiempo
     * @throws java.util.NoSuchElementException si la fila esta vacia
     */
    T dequeue();

    /**
     * Retorna, sin eliminarlo, el elemento al frente de la fila.
     *
     * @return el elemento insertado hace mas tiempo
     * @throws java.util.NoSuchElementException si la fila esta vacia
     */
    T peek();

    /**
     * Indica si la fila no contiene elementos.
     *
     * @return {@code true} si la fila esta vacia
     */
    boolean isEmpty();

    /**
     * Retorna la cantidad de elementos de la fila.
     *
     * @return numero de elementos almacenados
     */
    int size();
}
