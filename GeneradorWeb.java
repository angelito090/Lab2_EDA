import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Generador de datos sinteticos y preprocesados para el rastreador offline.
 *
 * <p>Reemplaza, dentro del laboratorio, dos fuentes de informacion que en un
 * sistema real producirian herramientas externas: el tokenizador usado por
 * {@code validarEtiquetas} y el extractor de hipervinculos usado por
 * {@code recorrerSitio}.</p>
 *
 * <p>Un sitio con {@code n} paginas (identificadas de 0 a {@code n - 1}) tiene
 * exactamente {@code 4n - 1} referencias a enlaces: {@code n - 1} enlaces
 * padre-hijo que garantizan que toda pagina es alcanzable desde la pagina 0, y
 * {@code 3n} enlaces adicionales aleatorios (con autoenlaces y destinos
 * repetidos permitidos). Tras el constructor el objeto es de solo lectura.</p>
 */
public class GeneradorWeb {

    /** Cantidad de enlaces aleatorios adicionales generados por pagina. */
    private static final int ENLACES_ADICIONALES = 3;

    /** Listas de adyacencia precomputadas, indexadas por identificador de pagina. */
    private final List<List<Integer>> enlaces;

    /**
     * Construye un sitio sintetico y genera de inmediato todas sus listas de
     * enlaces, usando {@code StdRandom.setSeed(seed)} para que la generacion
     * sea reproducible.
     *
     * @param n    cantidad de paginas del sitio (debe ser positiva)
     * @param seed semilla del generador de numeros aleatorios
     * @throws IllegalArgumentException si {@code n <= 0}
     */
    public GeneradorWeb(int n, long seed) {
        if (n <= 0) throw new IllegalArgumentException("n debe ser positivo: " + n);
        StdRandom.setSeed(seed);

        enlaces = new ArrayList<List<Integer>>(n);
        for (int i = 0; i < n; i++) {
            enlaces.add(new ArrayList<Integer>());
        }

        // n - 1 enlaces padre -> i, con el padre uniforme en [0, i)
        for (int i = 1; i < n; i++) {
            int padre = StdRandom.uniform(i);
            enlaces.get(padre).add(i);
        }

        // 3 enlaces adicionales independientes y uniformes en [0, n) por pagina
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < ENLACES_ADICIONALES; k++) {
                enlaces.get(i).add(StdRandom.uniform(n));
            }
        }
    }

    /**
     * Retorna la cantidad de paginas del sitio.
     *
     * @return numero de paginas
     */
    public int cantidadPaginas() {
        return enlaces.size();
    }

    /**
     * Simula la respuesta de un extractor externo de hipervinculos. Retorna la
     * lista ya construida en el constructor, en tiempo O(1), sin generar
     * numeros aleatorios ni crear objetos. El llamador no debe modificar la
     * lista retornada.
     *
     * @param paginaId identificador de la pagina, entre 0 y
     *                 {@code cantidadPaginas() - 1}
     * @return lista de destinos de los enlaces salientes de la pagina
     * @throws IndexOutOfBoundsException si {@code paginaId} esta fuera de rango
     */
    public List<Integer> obtenerEnlaces(int paginaId) {
        return enlaces.get(paginaId);
    }

    /**
     * Genera un documento sintetico valido: {@code n} etiquetas de apertura
     * seguidas de sus {@code n} etiquetas de cierre en orden inverso.
     *
     * <p>Por ejemplo, para {@code n = 2} retorna
     * {@code ["<t0>", "<t1>", "</t1>", "</t0>"]}.</p>
     *
     * @param n cantidad de etiquetas de apertura (no negativa)
     * @return arreglo de {@code 2n} tokens
     * @throws IllegalArgumentException si {@code n < 0}
     */
    public static String[] generarDocumentoAnidado(int n) {
        if (n < 0) throw new IllegalArgumentException("n no puede ser negativo: " + n);
        String[] tokens = new String[2 * n];
        for (int i = 0; i < n; i++) {
            tokens[i] = "<t" + i + ">";
            tokens[2 * n - 1 - i] = "</t" + i + ">";
        }
        return tokens;
    }
}
