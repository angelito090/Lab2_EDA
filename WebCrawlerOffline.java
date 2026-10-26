import java.util.List;

/**
 * Rastreador web offline: valida el balance de etiquetas de un documento y
 * recorre un sitio sintetico en anchura (BFS).
 *
 * <p>Las estructuras de datos se reciben por parametro, de modo que el mismo
 * codigo funciona con las implementaciones enlazada y de arreglo.</p>
 */
public class WebCrawlerOffline {

    /**
     * Verifica si las etiquetas de un documento estan balanceadas.
     *
     * <p>Los tokens son el nombre de la etiqueta sin {@code <} ni {@code >}:
     * {@code "html"} para una apertura y {@code "/html"} para un cierre. Por
     * tolerancia, si un token trae los delimitadores (como los de
     * {@link GeneradorWeb#generarDocumentoAnidado}) se quitan antes de
     * procesarlo. Las aperturas se apilan y cada cierre debe coincidir con la
     * etiqueta en la cima.</p>
     *
     * @param tokens arreglo de tokens de etiquetas
     * @param pila   pila vacia usada como estructura auxiliar
     * @return {@code true} si toda apertura tiene su cierre en el orden
     *         correcto y la pila queda vacia
     */
    public boolean validarEtiquetas(String[] tokens, Pila<String> pila) {
        for (String token : tokens) {
            if (token.startsWith("<") && token.endsWith(">")) {
                token = token.substring(1, token.length() - 1);
            }
            if (token.startsWith("/")) {
                String nombre = token.substring(1);
                if (pila.isEmpty() || !pila.pop().equals(nombre)) return false;
            } else {
                pila.push(token);
            }
        }
        return pila.isEmpty();
    }

    /**
     * Recorre el sitio en anchura (BFS) desde la pagina inicial.
     *
     * <p>Cada pagina se marca como descubierta en el instante de enfilarla,
     * por lo que nunca se enfila dos veces, aun si hay ciclos o enlaces
     * repetidos.</p>
     *
     * @param sitio         sitio a recorrer
     * @param paginaInicial pagina donde comienza el recorrido
     * @param frontera      fila vacia usada como frontera del BFS
     * @return cantidad de paginas distintas visitadas
     */
    public int recorrerSitio(GeneradorWeb sitio, int paginaInicial, Fila<Integer> frontera) {
        boolean[] descubierta = new boolean[sitio.cantidadPaginas()];
        int visitadas = 0;

        descubierta[paginaInicial] = true;
        frontera.enqueue(paginaInicial);

        while (!frontera.isEmpty()) {
            int pagina = frontera.dequeue();
            visitadas++;
            List<Integer> destinos = sitio.obtenerEnlaces(pagina);
            for (int i = 0; i < destinos.size(); i++) {
                int destino = destinos.get(i);
                if (!descubierta[destino]) {
                    descubierta[destino] = true; // se marca al enfilar
                    frontera.enqueue(destino);
                }
            }
        }
        return visitadas;
    }
}
