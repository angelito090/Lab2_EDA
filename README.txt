Laboratorio 2 - Pilas y filas: rastreador y validador offline de paginas web
Estructuras de Datos y Algoritmos (UDP, 2026-2)

Requisitos: JDK 11 o superior (algs4.jar esta compilado para Java 11) y algs4.jar,
que NO se incluye en esta carpeta. Todo el codigo esta en el paquete por defecto.
En los comandos, reemplazar <ruta a algs4.jar> por la ruta real del jar
(en Windows se usa ";" como separador del classpath; en Linux/macOS, ":").

1) Compilar (desde esta carpeta):
   javac --release 8 -cp <ruta a algs4.jar> *.java

2) Pruebas de correctitud (deben pasar todas):
   Windows:      java -cp ".;<ruta a algs4.jar>" PruebasCorrectitud
   Linux/macOS:  java -cp ".:<ruta a algs4.jar>" PruebasCorrectitud

3) Experimento oficial (escribe pila.csv y fila.csv en el directorio actual,
   sobrescribiendo los incluidos):
   Windows:      java -cp ".;<ruta a algs4.jar>" Experimento
   Linux/macOS:  java -cp ".:<ruta a algs4.jar>" Experimento

   Prueba rapida (escribe pila_prueba.csv y fila_prueba.csv):
   java -cp ".;<ruta a algs4.jar>" Experimento 10 12

Contenido: .java (codigo fuente), pila.csv y fila.csv (microdatos del experimento
oficial), analisis.xlsx (analisis: pestanas Pila, Fila y Resumen).
Los tiempos estan en segundos (StopwatchCPU, resolucion de 1/64 s en el equipo
donde se midio).
