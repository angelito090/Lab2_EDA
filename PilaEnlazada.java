import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Pila LIFO implementada con lista simplemente enlazada.
 *
 * <p>Adaptacion de {@code edu.princeton.cs.algs4.Stack}. Todas las operaciones
 * toman tiempo constante en el peor caso.</p>
 *
 * @param <T> tipo de los elementos almacenados
 */
public class PilaEnlazada<T> implements Pila<T>, Iterable<T> {

    /** Nodo de la lista enlazada. */
    private static class Nodo<T> {
        private T item;
        private Nodo<T> siguiente;
    }

    private Nodo<T> primero;
    private int n;

    /** Crea una pila vacia. */
    public PilaEnlazada() {
        primero = null;
        n = 0;
    }

    /** {@inheritDoc} */
    @Override
    public void push(T item) {
        Nodo<T> viejoPrimero = primero;
        primero = new Nodo<T>();
        primero.item = item;
        primero.siguiente = viejoPrimero;
        n++;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException si la pila esta vacia
     */
    @Override
    public T pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        T item = primero.item;
        primero = primero.siguiente;
        n--;
        return item;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException si la pila esta vacia
     */
    @Override
    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
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
     * Retorna un iterador que recorre los elementos desde la cima hasta la base.
     *
     * @return iterador sobre los elementos en orden LIFO
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
