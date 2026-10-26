import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Pila LIFO implementada con arreglo redimensionable.
 *
 * <p>Adaptacion de {@code edu.princeton.cs.algs4.ResizingArrayStack}. La
 * capacidad inicial es 2; se duplica cuando el arreglo se llena y se reduce a
 * la mitad cuando queda ocupado a un cuarto de su capacidad. Incluye
 * instrumentacion para medir redimensionamientos y capacidad maxima.</p>
 *
 * @param <T> tipo de los elementos almacenados
 */
public class PilaArreglo<T> implements Pila<T>, Iterable<T> {

    private static final int CAPACIDAD_INICIAL = 2;

    private T[] a;
    private int n;
    private int cantidadRedimensionamientos;
    private int capacidadMaximaObservada;

    /** Crea una pila vacia con capacidad inicial 2. */
    @SuppressWarnings("unchecked")
    public PilaArreglo() {
        a = (T[]) new Object[CAPACIDAD_INICIAL];
        n = 0;
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
     * Redimensiona el arreglo interno a la capacidad indicada y actualiza la
     * instrumentacion.
     *
     * @param capacidad nueva capacidad (mayor o igual al numero de elementos)
     */
    @SuppressWarnings("unchecked")
    private void resize(int capacidad) {
        assert capacidad >= n;
        T[] copia = (T[]) new Object[capacidad];
        for (int i = 0; i < n; i++) {
            copia[i] = a[i];
        }
        a = copia;
        cantidadRedimensionamientos++;
        if (capacidad > capacidadMaximaObservada) capacidadMaximaObservada = capacidad;
    }

    /** {@inheritDoc} Duplica la capacidad si el arreglo esta lleno. */
    @Override
    public void push(T item) {
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }

    /**
     * {@inheritDoc} Reduce la capacidad a la mitad si el arreglo queda ocupado
     * a un cuarto.
     *
     * @throws NoSuchElementException si la pila esta vacia
     */
    @Override
    public T pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        T item = a[n - 1];
        a[n - 1] = null; // evita retencion innecesaria
        n--;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
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
        return a[n - 1];
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
     * Retorna un iterador que recorre los elementos desde la cima hasta la base.
     *
     * @return iterador sobre los elementos en orden LIFO
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorInverso();
    }

    private class IteradorInverso implements Iterator<T> {
        private int i = n - 1;

        @Override
        public boolean hasNext() {
            return i >= 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[i--];
        }
    }
}
