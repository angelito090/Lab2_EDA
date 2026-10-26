import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Fila FIFO implementada con lista simplemente enlazada.
 *
 * <p>Adaptacion de {@code edu.princeton.cs.algs4.Queue}. Todas las operaciones
 * toman tiempo constante en el peor caso.</p>
 *
 * @param <T> tipo de los elementos almacenados
 */
public class FilaEnlazada<T> implements Fila<T>, Iterable<T> {

    /** Nodo de la lista enlazada. */
    private static class Nodo<T> {
        private T item;
        private Nodo<T> siguiente;
    }

    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int n;

    /** Crea una fila vacia. */
    public FilaEnlazada() {
        primero = null;
        ultimo = null;
        n = 0;
    }

    /** {@inheritDoc} */
    @Override
    public void enqueue(T item) {
        Nodo<T> viejoUltimo = ultimo;
        ultimo = new Nodo<T>();
        ultimo.item = item;
        ultimo.siguiente = null;
        if (isEmpty()) primero = ultimo;
        else viejoUltimo.siguiente = ultimo;
        n++;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException si la fila esta vacia
     */
    @Override
    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        T item = primero.item;
        primero = primero.siguiente;
        n--;
        if (isEmpty()) ultimo = null; // evita retencion innecesaria
        return item;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException si la fila esta vacia
     */
    @Override
    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return primero.item;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return primero == null;
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return n;
    }

    /**
     * Retorna un iterador que recorre los elementos del frente al final.
     *
     * @return iterador sobre los elementos en orden FIFO
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorLista(primero);
    }

    private class IteradorLista implements Iterator<T> {
        private Nodo<T> actual;

        IteradorLista(Nodo<T> primero) {
            actual = primero;
        }

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T item = actual.item;
            actual = actual.siguiente;
            return item;
        }
    }
}
