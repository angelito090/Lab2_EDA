import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StopwatchCPU;

import java.util.Locale;

/**
 * Experimentos oficiales del laboratorio (enunciado, secciones 5.2 y 5.3).
 *
 * <p>Compara las implementaciones enlazada y con arreglo de Pila y Fila:</p>
 * <ul>
 *   <li><b>Experimento 1</b> ({@code pila.csv}): {@code validarEtiquetas} sobre
 *       un documento anidado de {@code n} etiquetas de apertura y sus
 *       {@code n} de cierre, con {@link PilaEnlazada} y {@link PilaArreglo}.</li>
 *   <li><b>Experimento 2</b> ({@code fila.csv}): {@code recorrerSitio} (BFS)
 *       sobre un {@link GeneradorWeb} de {@code n} paginas, con
 *       {@link FilaEnlazada} y {@link FilaArreglo}.</li>
 * </ul>
 *
 * <p>Protocolo comun: {@code n = 2^t} con {@code t} entre 10 y 18; 30
 * repeticiones medidas por tamano (r = 0..29) tras una pasada de
 * calentamiento no registrada; el tiempo se mide con
 * {@link StopwatchCPU} unicamente alrededor de la llamada a
 * {@code validarEtiquetas} o {@code recorrerSitio}. Todo lo demas
 * (construccion de estructuras, de sitios y de tokens, verificaciones,
 * escritura de CSV y salida por consola) queda fuera del cronometro. En
 * las repeticiones pares se mide primero la version enlazada y en las
 * impares primero la de arreglo.</p>
 *
 * <p>Uso: sin argumentos ejecuta el protocolo oficial y escribe
 * {@code pila.csv} y {@code fila.csv}. Con dos argumentos {@code tMin tMax}
 * ejecuta solo ese rango de exponentes y escribe {@code pila_prueba.csv} y
 * {@code fila_prueba.csv}.</p>
 */
public class Experimento {

    /** Exponente minimo del protocolo oficial. */
    private static final int T_MIN_OFICIAL = 10;

    /** Exponente maximo del protocolo oficial. */
    private static final int T_MAX_OFICIAL = 18;

    /** Cantidad de repeticiones medidas por tamano. */
    private static final int REPETICIONES = 30;

    /** Cabecera de {@code pila.csv}. */
    private static final String CABECERA_PILA =
            "n,repeticion,tEnlazada,tArreglo,redimensionamientos,capacidadMaxima";

    /** Cabecera de {@code fila.csv}. */
    private static final String CABECERA_FILA =
            "n,repeticion,seed,tEnlazada,tArreglo,redimensionamientos,capacidadMaxima";

    private Experimento() { }

    /**
     * Punto de entrada. Sin argumentos corre el protocolo oficial; con
     * {@code tMin tMax} corre una version reducida que escribe archivos
     * {@code *_prueba.csv}.
     *
     * @param args vacio, o dos enteros {@code tMin tMax}
     * @throws IllegalArgumentException si los argumentos no son validos
     * @throws IllegalStateException    si alguna verificacion del
     *                                  experimento falla
     */
    public static void main(String[] args) {
        int tMin = T_MIN_OFICIAL;
        int tMax = T_MAX_OFICIAL;
        String archivoPila = "pila.csv";
        String archivoFila = "fila.csv";

        if (args.length != 0) {
            if (args.length != 2) {
                throw new IllegalArgumentException("Uso: java Experimento [tMin tMax]");
            }
            tMin = Integer.parseInt(args[0]);
            tMax = Integer.parseInt(args[1]);
            if (tMin < 0 || tMax < tMin || tMax > 30) {
                throw new IllegalArgumentException(
                        "Rango de exponentes invalido: " + tMin + " " + tMax);
            }
            archivoPila = "pila_prueba.csv";
            archivoFila = "fila_prueba.csv";
        }

        experimentoPila(tMin, tMax, archivoPila);
        experimentoFila(tMin, tMax, archivoFila);
    }

    /**
     * Experimento 1 (seccion 5.2): compara {@link PilaEnlazada} y
     * {@link PilaArreglo} al validar un documento anidado de profundidad
     * {@code n}.
     *
     * <p>Por cada tamano genera una unica secuencia de tokens
     * (fuera del cronometro), ejecuta un calentamiento no registrado con
     * cada pila y luego realiza 30 repeticiones. En cada una crea las pilas
     * vacias fuera del bloque medido, mide por separado
     * {@code validarEtiquetas} con cada una (alternando el orden segun la
     * paridad de la repeticion), verifica que ambas retornen
     * {@code true} y lee la instrumentacion de la pila con arreglo.</p>
     *
     * @param tMin    exponente minimo ({@code n = 2^tMin})
     * @param tMax    exponente maximo ({@code n = 2^tMax})
     * @param archivo nombre del CSV de salida
     * @throws IllegalStateException si alguna validacion retorna
     *                               {@code false}
     */
    private static void experimentoPila(int tMin, int tMax, String archivo) {
        WebCrawlerOffline crawler = new WebCrawlerOffline();
        Out csv = new Out(archivo);
        csv.println(CABECERA_PILA);

        for (int t = tMin; t <= tMax; t++) {
            int n = 1 << t;
            String[] tokens = GeneradorWeb.generarDocumentoAnidado(n);

            // calentamiento no registrado
            crawler.validarEtiquetas(tokens, new PilaEnlazada<String>());
            crawler.validarEtiquetas(tokens, new PilaArreglo<String>());

            for (int r = 0; r < REPETICIONES; r++) {
                Pila<String> enlazada = new PilaEnlazada<String>();
                PilaArreglo<String> arreglo = new PilaArreglo<String>();

                double tEnlazada;
                double tArreglo;
                boolean okEnlazada;
                boolean okArreglo;

                if (r % 2 == 0) {
                    StopwatchCPU reloj = new StopwatchCPU();
                    boolean res = crawler.validarEtiquetas(tokens, enlazada);
                    tEnlazada = reloj.elapsedTime();
                    okEnlazada = res;

                    reloj = new StopwatchCPU();
                    res = crawler.validarEtiquetas(tokens, arreglo);
                    tArreglo = reloj.elapsedTime();
                    okArreglo = res;
                } else {
                    StopwatchCPU reloj = new StopwatchCPU();
                    boolean res = crawler.validarEtiquetas(tokens, arreglo);
                    tArreglo = reloj.elapsedTime();
                    okArreglo = res;

                    reloj = new StopwatchCPU();
                    res = crawler.validarEtiquetas(tokens, enlazada);
                    tEnlazada = reloj.elapsedTime();
                    okEnlazada = res;
                }

                if (!okEnlazada) {
                    throw new IllegalStateException("validarEtiquetas retorno false con PilaEnlazada"
                            + " (n=" + n + ", repeticion=" + r + ")");
                }
                if (!okArreglo) {
                    throw new IllegalStateException("validarEtiquetas retorno false con PilaArreglo"
                            + " (n=" + n + ", repeticion=" + r + ")");
                }

                csv.println(String.format(Locale.US, "%d,%d,%.9f,%.9f,%d,%d",
                        n, r, tEnlazada, tArreglo,
                        arreglo.getCantidadRedimensionamientos(),
                        arreglo.getCapacidadMaximaObservada()));
            }
            System.out.println("pila: n = " + n + " (t = " + t + ") terminado");
        }
        csv.close();
    }

    /**
     * Experimento 2 (seccion 5.3): compara {@link FilaEnlazada} y
     * {@link FilaArreglo} como frontera del BFS de {@code recorrerSitio}.
     *
     * <p>Por cada tamano ejecuta un calentamiento no registrado sobre un
     * sitio con semilla {@code n + 30} (fuera del rango de las repeticiones).
     * En cada repeticion {@code r} construye, fuera del bloque medido,
     * un unico {@code GeneradorWeb(n, n + r)} y una fila vacia de
     * cada tipo; mide por separado {@code recorrerSitio(sitio, 0, frontera)}
     * con cada fila sobre el mismo sitio (alternando el orden segun la
     * paridad de {@code r}), verifica que ambas retornen exactamente
     * {@code n} y lee la instrumentacion de la fila con arreglo.</p>
     *
     * @param tMin    exponente minimo ({@code n = 2^tMin})
     * @param tMax    exponente maximo ({@code n = 2^tMax})
     * @param archivo nombre del CSV de salida
     * @throws IllegalStateException si algun recorrido no retorna {@code n}
     */
    private static void experimentoFila(int tMin, int tMax, String archivo) {
        WebCrawlerOffline crawler = new WebCrawlerOffline();
        Out csv = new Out(archivo);
        csv.println(CABECERA_FILA);

        for (int t = tMin; t <= tMax; t++) {
            int n = 1 << t;

            // calentamiento no registrado
            GeneradorWeb sitioCalentamiento = new GeneradorWeb(n, n + REPETICIONES);
            crawler.recorrerSitio(sitioCalentamiento, 0, new FilaEnlazada<Integer>());
            crawler.recorrerSitio(sitioCalentamiento, 0, new FilaArreglo<Integer>());

            for (int r = 0; r < REPETICIONES; r++) {
                long seed = (long) n + r;
                GeneradorWeb sitio = new GeneradorWeb(n, seed);
                Fila<Integer> enlazada = new FilaEnlazada<Integer>();
                FilaArreglo<Integer> arreglo = new FilaArreglo<Integer>();

                double tEnlazada;
                double tArreglo;
                int procesadasEnlazada;
                int procesadasArreglo;

                if (r % 2 == 0) {
                    StopwatchCPU reloj = new StopwatchCPU();
                    int res = crawler.recorrerSitio(sitio, 0, enlazada);
                    tEnlazada = reloj.elapsedTime();
                    procesadasEnlazada = res;

                    reloj = new StopwatchCPU();
                    res = crawler.recorrerSitio(sitio, 0, arreglo);
                    tArreglo = reloj.elapsedTime();
                    procesadasArreglo = res;
                } else {
                    StopwatchCPU reloj = new StopwatchCPU();
                    int res = crawler.recorrerSitio(sitio, 0, arreglo);
                    tArreglo = reloj.elapsedTime();
                    procesadasArreglo = res;

                    reloj = new StopwatchCPU();
                    res = crawler.recorrerSitio(sitio, 0, enlazada);
                    tEnlazada = reloj.elapsedTime();
                    procesadasEnlazada = res;
                }

                if (procesadasEnlazada != n) {
                    throw new IllegalStateException("recorrerSitio con FilaEnlazada retorno "
                            + procesadasEnlazada + " en vez de " + n
                            + " (seed=" + seed + ", repeticion=" + r + ")");
                }
                if (procesadasArreglo != n) {
                    throw new IllegalStateException("recorrerSitio con FilaArreglo retorno "
                            + procesadasArreglo + " en vez de " + n
                            + " (seed=" + seed + ", repeticion=" + r + ")");
                }

                csv.println(String.format(Locale.US, "%d,%d,%d,%.9f,%.9f,%d,%d",
                        n, r, seed, tEnlazada, tArreglo,
                        arreglo.getCantidadRedimensionamientos(),
                        arreglo.getCapacidadMaximaObservada()));
            }
            System.out.println("fila: n = " + n + " (t = " + t + ") terminado");
        }
        csv.close();
    }
}
