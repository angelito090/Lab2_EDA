import java.util.List;
import java.util.NoSuchElementException;

/**
 * Pruebas de correctitud de las pilas, filas y del rastreador offline.
 * Se ejecuta con {@code java -cp .;algs4.jar PruebasCorrectitud}.
 */
public class PruebasCorrectitud {

    private static int total = 0;
    private static int fallidas = 0;

    private static void check(String nombre, boolean ok) {
        total++;
        if (!ok) fallidas++;
        System.out.println((ok ? "[OK]   " : "[FAIL] ") + nombre);
    }

    private interface Accion {
        void ejecutar();
    }

    private static boolean lanzaNoSuchElement(Accion a) {
        try {
            a.ejecutar();
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    public static void main(String[] args) {
        pruebaEquivalenciaPilas();
        pruebaEquivalenciaFilas();
        pruebaEstructurasVacias();
        pruebaRedimensionamiento();
        pruebaEtiquetas();
        pruebaGeneradorWeb();
        pruebaBFS();

        System.out.println();
        System.out.println((total - fallidas) + "/" + total + " pruebas exitosas");
        if (fallidas > 0) System.exit(1);
    }

    private static void pruebaEquivalenciaPilas() {
        System.out.println("== Equivalencia PilaEnlazada / PilaArreglo ==");
        PilaEnlazada<Integer> pe = new PilaEnlazada<Integer>();
        PilaArreglo<Integer> pa = new PilaArreglo<Integer>();
        boolean iguales = true;
        for (int i = 0; i < 100; i++) {
            pe.push(i);
            pa.push(i);
            iguales &= pe.peek().equals(pa.peek()) && pe.size() == pa.size();
        }
        check("push/peek/size coinciden", iguales);

        iguales = true;
        for (int i = 99; i >= 0; i--) {
            int x = pe.pop();
            int y = pa.pop();
            iguales &= (x == i) && (y == i);
        }
        check("pop devuelve orden LIFO en ambas", iguales);
        check("ambas quedan vacias", pe.isEmpty() && pa.isEmpty());

        // intercalado de push y pop
        pe.push(1); pa.push(1);
        pe.push(2); pa.push(2);
        boolean a = pe.pop().equals(pa.pop());
        pe.push(3); pa.push(3);
        boolean b = pe.pop().equals(pa.pop()) && pe.pop().equals(pa.pop());
        check("operaciones intercaladas coinciden", a && b && pe.isEmpty() && pa.isEmpty());
    }

    private static void pruebaEquivalenciaFilas() {
        System.out.println("== Equivalencia FilaEnlazada / FilaArreglo ==");
        FilaEnlazada<Integer> fe = new FilaEnlazada<Integer>();
        FilaArreglo<Integer> fa = new FilaArreglo<Integer>();
        boolean iguales = true;
        for (int i = 0; i < 100; i++) {
            fe.enqueue(i);
            fa.enqueue(i);
            iguales &= fe.peek().equals(fa.peek()) && fe.size() == fa.size();
        }
        check("enqueue/peek/size coinciden", iguales);

        iguales = true;
        for (int i = 0; i < 100; i++) {
            int x = fe.dequeue();
            int y = fa.dequeue();
            iguales &= (x == i) && (y == i);
        }
        check("dequeue devuelve orden FIFO en ambas", iguales);
        check("ambas quedan vacias", fe.isEmpty() && fa.isEmpty());

        // fuerza el salto circular del arreglo
        iguales = true;
        for (int ronda = 0; ronda < 50; ronda++) {
            fe.enqueue(ronda); fa.enqueue(ronda);
            fe.enqueue(ronda + 1000); fa.enqueue(ronda + 1000);
            iguales &= fe.dequeue().equals(fa.dequeue());
        }
        while (!fe.isEmpty() && !fa.isEmpty()) {
            iguales &= fe.dequeue().equals(fa.dequeue());
        }
        check("orden FIFO con envoltura circular", iguales && fe.isEmpty() && fa.isEmpty());
    }

    private static void pruebaEstructurasVacias() {
        System.out.println("== Estructuras vacias y excepciones ==");
        final Pila<Integer> pe = new PilaEnlazada<Integer>();
        final Pila<Integer> pa = new PilaArreglo<Integer>();
        final Fila<Integer> fe = new FilaEnlazada<Integer>();
        final Fila<Integer> fa = new FilaArreglo<Integer>();

        check("pilas nuevas vacias, size 0",
                pe.isEmpty() && pa.isEmpty() && pe.size() == 0 && pa.size() == 0);
        check("filas nuevas vacias, size 0",
                fe.isEmpty() && fa.isEmpty() && fe.size() == 0 && fa.size() == 0);

        check("PilaEnlazada.pop vacia lanza excepcion", lanzaNoSuchElement(() -> pe.pop()));
        check("PilaEnlazada.peek vacia lanza excepcion", lanzaNoSuchElement(() -> pe.peek()));
        check("PilaArreglo.pop vacia lanza excepcion", lanzaNoSuchElement(() -> pa.pop()));
        check("PilaArreglo.peek vacia lanza excepcion", lanzaNoSuchElement(() -> pa.peek()));
        check("FilaEnlazada.dequeue vacia lanza excepcion", lanzaNoSuchElement(() -> fe.dequeue()));
        check("FilaEnlazada.peek vacia lanza excepcion", lanzaNoSuchElement(() -> fe.peek()));
        check("FilaArreglo.dequeue vacia lanza excepcion", lanzaNoSuchElement(() -> fa.dequeue()));
        check("FilaArreglo.peek vacia lanza excepcion", lanzaNoSuchElement(() -> fa.peek()));

        // vaciar tras usar tambien lanza excepcion
        pa.push(1);
        pa.pop();
        check("PilaArreglo vaciada de nuevo lanza excepcion", lanzaNoSuchElement(() -> pa.pop()));
        fa.enqueue(1);
        fa.dequeue();
        check("FilaArreglo vaciada de nuevo lanza excepcion", lanzaNoSuchElement(() -> fa.dequeue()));
    }

    private static void pruebaRedimensionamiento() {
        System.out.println("== Redimensionamiento de arreglos ==");
        PilaArreglo<Integer> p = new PilaArreglo<Integer>();
        check("pila: capacidad inicial 2", p.getCapacidadMaximaObservada() == 2
                && p.getCantidadRedimensionamientos() == 0);
        p.push(1); p.push(2);
        check("pila: sin resize al llenar exactamente", p.getCantidadRedimensionamientos() == 0);
        p.push(3);
        check("pila: duplica a 4", p.getCantidadRedimensionamientos() == 1
                && p.getCapacidadMaximaObservada() == 4);
        p.push(4); p.push(5);
        check("pila: duplica a 8", p.getCantidadRedimensionamientos() == 2
                && p.getCapacidadMaximaObservada() == 8);
        for (int i = 6; i <= 16; i++) p.push(i);
        check("pila: capacidad maxima 16 con 16 elementos", p.getCapacidadMaximaObservada() == 16
                && p.size() == 16 && p.getCantidadRedimensionamientos() == 3);
        int antes = p.getCantidadRedimensionamientos();
        while (p.size() > 4) p.pop(); // 4 == 16/4 -> reduce a 8
        check("pila: reduce al llegar a un cuarto", p.getCantidadRedimensionamientos() == antes + 1);
        check("pila: la capacidad maxima no baja", p.getCapacidadMaximaObservada() == 16);
        boolean orden = true;
        for (int i = 4; i >= 1; i--) orden &= (p.pop() == i);
        check("pila: conserva los datos tras redimensionar", orden && p.isEmpty());

        FilaArreglo<Integer> f = new FilaArreglo<Integer>();
        check("fila: capacidad inicial 2", f.getCapacidadMaximaObservada() == 2
                && f.getCantidadRedimensionamientos() == 0);
        f.enqueue(1); f.enqueue(2);
        check("fila: sin resize al llenar exactamente", f.getCantidadRedimensionamientos() == 0);
        f.enqueue(3);
        check("fila: duplica a 4", f.getCantidadRedimensionamientos() == 1
                && f.getCapacidadMaximaObservada() == 4);
        for (int i = 4; i <= 16; i++) f.enqueue(i);
        check("fila: capacidad maxima 16", f.getCapacidadMaximaObservada() == 16 && f.size() == 16);
        antes = f.getCantidadRedimensionamientos();
        for (int i = 1; i <= 12; i++) f.dequeue(); // quedan 4 == 16/4 -> reduce
        check("fila: reduce al llegar a un cuarto", f.getCantidadRedimensionamientos() == antes + 1);
        orden = true;
        for (int i = 13; i <= 16; i++) orden &= (f.dequeue() == i);
        check("fila: conserva los datos tras redimensionar", orden && f.isEmpty());

        // redimensionar con el arreglo circular desplazado
        FilaArreglo<Integer> g = new FilaArreglo<Integer>();
        g.enqueue(1); g.enqueue(2);
        g.dequeue();
        g.enqueue(3);
        g.enqueue(4); // fuerza resize con primero != 0
        orden = g.dequeue() == 2 && g.dequeue() == 3 && g.dequeue() == 4;
        check("fila: resize con indices envueltos conserva orden", orden && g.isEmpty());
    }

    private static boolean valida(String[] tokens, boolean enlazada) {
        Pila<String> pila = enlazada ? new PilaEnlazada<String>() : new PilaArreglo<String>();
        return new WebCrawlerOffline().validarEtiquetas(tokens, pila);
    }

    private static void probarEtiquetas(String nombre, String[] tokens, boolean esperado) {
        check(nombre + " (enlazada)", valida(tokens, true) == esperado);
        check(nombre + " (arreglo)", valida(tokens, false) == esperado);
    }

    private static void pruebaEtiquetas() {
        System.out.println("== Validacion de etiquetas ==");
        probarEtiquetas("documento vacio es valido", new String[] {}, true);
        probarEtiquetas("par simple",
                new String[] {"html", "/html"}, true);
        probarEtiquetas("anidado correcto",
                new String[] {"html", "body", "p", "/p", "/body", "/html"}, true);
        probarEtiquetas("hermanas correctas",
                new String[] {"ul", "li", "/li", "li", "/li", "/ul"}, true);
        probarEtiquetas("tokens t0..t2 sin delimitadores",
                new String[] {"t0", "t1", "t2", "/t2", "/t1", "/t0"}, true);
        probarEtiquetas("documento anidado generado (n=50)",
                GeneradorWeb.generarDocumentoAnidado(50), true);
        probarEtiquetas("cruzadas invalidas",
                new String[] {"a", "b", "/a", "/b"}, false);
        probarEtiquetas("falta cierre",
                new String[] {"a", "b", "/b"}, false);
        probarEtiquetas("cierre sin apertura",
                new String[] {"/a"}, false);
        probarEtiquetas("cierre de sobra",
                new String[] {"a", "/a", "/a"}, false);
        probarEtiquetas("solo aperturas",
                new String[] {"a", "b"}, false);
        probarEtiquetas("token mal formado",
                new String[] {"a", "texto", "/a"}, false);

        String[] roto = GeneradorWeb.generarDocumentoAnidado(10);
        roto[12] = "</tX>";
        probarEtiquetas("documento anidado alterado", roto, false);
    }

    private static boolean mismasListas(GeneradorWeb a, GeneradorWeb b) {
        if (a.cantidadPaginas() != b.cantidadPaginas()) return false;
        for (int i = 0; i < a.cantidadPaginas(); i++) {
            if (!a.obtenerEnlaces(i).equals(b.obtenerEnlaces(i))) return false;
        }
        return true;
    }

    private static void pruebaGeneradorWeb() {
        System.out.println("== GeneradorWeb ==");

        int[] tamanos = {1, 2, 10, 500};
        for (int n : tamanos) {
            check("misma semilla, n=" + n + ": listas iguales",
                    mismasListas(new GeneradorWeb(n, 77L), new GeneradorWeb(n, 77L)));
        }
        check("semillas 99 y 100 (n=500): listas distintas",
                !mismasListas(new GeneradorWeb(500, 99L), new GeneradorWeb(500, 100L)));

        int[] varios = {1, 2, 3, 10, 100, 500, 1000};
        for (int n : varios) {
            GeneradorWeb g = new GeneradorWeb(n, 5L);
            int total = 0;
            boolean enRango = true;
            for (int i = 0; i < n; i++) {
                List<Integer> destinos = g.obtenerEnlaces(i);
                total += destinos.size();
                for (int k = 0; k < destinos.size(); k++) {
                    int d = destinos.get(k);
                    enRango &= (d >= 0 && d < n);
                }
            }
            check("n=" + n + ": total de referencias = 4n-1", total == 4 * n - 1);
            check("n=" + n + ": todos los destinos en [0, n)", enRango);
        }

        GeneradorWeb g = new GeneradorWeb(50, 3L);
        boolean mismaInstancia = true;
        for (int i = 0; i < 50; i++) {
            mismaInstancia &= (g.obtenerEnlaces(i) == g.obtenerEnlaces(i));
        }
        check("obtenerEnlaces devuelve la misma instancia de lista", mismaInstancia);

        boolean cero = false;
        boolean negativo = false;
        try {
            new GeneradorWeb(0, 1L);
        } catch (IllegalArgumentException e) {
            cero = true;
        }
        try {
            new GeneradorWeb(-5, 1L);
        } catch (IllegalArgumentException e) {
            negativo = true;
        }
        check("GeneradorWeb(0, seed) lanza IllegalArgumentException", cero);
        check("GeneradorWeb(n < 0, seed) lanza IllegalArgumentException", negativo);

        // sitios pequenos con repetidos, autoenlaces y ciclos de dos paginas
        WebCrawlerOffline w = new WebCrawlerOffline();
        boolean hayRepetidos = false;
        boolean hayAutoenlace = false;
        boolean hayCiclo2 = false;
        boolean todosCompletos = true;
        int[] chicos = {3, 5};
        for (int n : chicos) {
            for (long seed = 1; seed <= 20; seed++) {
                GeneradorWeb s = new GeneradorWeb(n, seed);
                for (int i = 0; i < n; i++) {
                    List<Integer> destinos = s.obtenerEnlaces(i);
                    for (int k = 0; k < destinos.size(); k++) {
                        int j = destinos.get(k);
                        if (destinos.indexOf(j) != k) hayRepetidos = true;
                        if (j == i) hayAutoenlace = true;
                        if (j != i && s.obtenerEnlaces(j).contains(i)) hayCiclo2 = true;
                    }
                }
                todosCompletos &= w.recorrerSitio(s, 0, new FilaEnlazada<Integer>()) == n;
                todosCompletos &= w.recorrerSitio(s, 0, new FilaArreglo<Integer>()) == n;
            }
        }
        check("sitios chicos: existe lista con destinos repetidos", hayRepetidos);
        check("sitios chicos: existe un autoenlace", hayAutoenlace);
        check("sitios chicos: existe un ciclo de dos paginas (i->j y j->i)", hayCiclo2);
        check("sitios chicos (n=3,5; semillas 1..20): BFS visita exactamente n con ambas filas",
                todosCompletos);
    }

    private static void pruebaBFS() {
        System.out.println("== Recorrido BFS ==");
        WebCrawlerOffline w = new WebCrawlerOffline();

        GeneradorWeb uno = new GeneradorWeb(1, 1L);
        check("sitio de 1 pagina visita 1",
                w.recorrerSitio(uno, 0, new FilaEnlazada<Integer>()) == 1);

        long[] semillas = {1L, 7L, 42L};
        int[] tamanos = {2, 10, 100, 1000};
        for (int n : tamanos) {
            for (long s : semillas) {
                GeneradorWeb g = new GeneradorWeb(n, s);
                Fila<Integer> fe = new FilaEnlazada<Integer>();
                Fila<Integer> fa = new FilaArreglo<Integer>();
                int ve = w.recorrerSitio(g, 0, fe);
                int va = w.recorrerSitio(g, 0, fa);
                check("n=" + n + " semilla=" + s + ": alcanza todas las paginas",
                        ve == n && va == n);
                check("n=" + n + " semilla=" + s + ": frontera queda vacia",
                        fe.isEmpty() && fa.isEmpty());
            }
        }

        // desde otra pagina inicial: resultado igual con ambas filas
        GeneradorWeb g = new GeneradorWeb(200, 5L);
        int d1 = w.recorrerSitio(g, 150, new FilaEnlazada<Integer>());
        int d2 = w.recorrerSitio(g, 150, new FilaArreglo<Integer>());
        check("pagina inicial distinta: ambas filas coinciden", d1 == d2 && d1 >= 1 && d1 <= 200);

        // el tamano maximo de la frontera nunca supera n (sin duplicados)
        final int n = 500;
        final GeneradorWeb gg = new GeneradorWeb(n, 3L);
        FilaArreglo<Integer> f = new FilaArreglo<Integer>();
        w.recorrerSitio(gg, 0, f);
        check("sin duplicados: capacidad maxima de frontera acotada por 2n",
                f.getCapacidadMaximaObservada() <= 2 * n);
    }
}
