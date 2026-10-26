import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Fila FIFO implementada con arreglo circular redimensionable.
 *
 * <p>Adaptacion de {@code edu.princeton.cs.algs4.ResizingArrayQueue}. La
 * capacidad inicial es 2; se duplica cuando el arreglo se llena y se reduce a
 * la mitad cuando queda ocupado a un cuarto de su capacidad. Incluye
 * instrumentacion para medir redimensionamientos y capacidad maxima.</p>
 *
 * @param <T> tipo de los elementos almacenados
 */
public class FilaArreglo<T> implements Fila<T>, Iterable<T> {

    private static final int CAPACIDAD_INICIAL = 2;

    private T[] q;
    private int n;
    private int primero;
    private int ultimo;
    private int cantidadRedimensionamientos;
    private int capacidadMaximaObservada;

    /** Crea una fila vacia con capacidad inicial 2. */
    @SuppressWarnings("unchecked")
    public FilaArreglo() {
        q = (T[]) new Object[CAPACIDAD_INICIAL];
        n = 0;
        primero = 0;
        ultimo = 0;
        cantidadRedimensionamientos = 0;
        capacidadMaximaObservada = CAPACIDAD_INICIAL;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return n;
    }

    /**
     * Redimensiona el arreglo interno a la capacidad indicada (reubicando los
     * elementos desde el indice 0) y actualiza la instrumentacion.
     *
     * @param capacidad nueva capacidad (mayor o igual al numero de elementos)
     */
    @SuppressWarnings("unchecked")
    private void resize(int capacidad) {
        assert capacidad >= n;
        T[] copia = (T[]) new Object[capacidad];
        for (int i = 0; i < n; i++) {
            copia[i] = q[(primero + i) % q.length];
        }
        q = copia;
        primero = 0;
        ultimo = n;
        cantidadRedimensionamientos++;
        if (capacidad > capacidadMaximaObservada) capacidadMaximaObservada = capacidad;
    }

    /** {@inheritDoc} Duplica la capacidad si el arreglo esta lleno. */
    @Override
    public void enqueue(T item) {
        if (n == q.length) resize(2 * q.length);
        q[ultimo++] = item;
        if (ultimo == q.length) ultimo = 0; // salto circular
        n++;
    }

    /**
     * {@inheritDoc} Reduce la capacidad a la mitad si el arreglo queda ocupado
     * a un cuarto.
     *
     * @throws NoSuchElementException si la fila esta vacia
     */
    @Override
    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        T item = q[primero];
        q[primero] = null; // evita retencion innecesaria
        n--;
        primero++;
        if (primero == q.length) primero = 0; // salto circular
        if (n > 0 && n == q.length / 4) resize(q.length / 2);
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
        return q[primero];
    }

    /**
     * Retorna cuantas veces se ha redimensionado el arreglo interno
     * (duplicaciones y reducciones).
     *
     * @return numero de redimensionamientos
     */
    public int getCantidadRedimensionamientos() {
        return cantidadRedimensionamientos;
    }

    /**
     * Retorna la mayor capacidad que ha tenido el arreglo interno.
     *
     * @return capacidad maxima observada
     */
    public int getCapacidadMaximaObservada() {
        return capacidadMaximaObservada;
    }

    /**
     * Retorna un iterador que recorre los elementos del frente al final.
     *
     * @return iterador sobre los elementos en orden FIFO
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorFila();
    }

    private class IteradorFila implements Iterator<T> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < n;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T item = q[(i + primero) % q.length];
            i++;
            return item;
        }
    }
}
