# Laberinto_Aleatorio.
Implementar un generador de laberintos aleatorios y un resolvedor que encuentre la salida con menor distancia desde una celda inicial.

# INTEGRANTES 
-Mabel Iveth Diaz  

-Juan Diego Borja Valdez

-Cristian Camilo Dorado

-Viktor Manuelle Velasco  

-Gustavo Saúl Marquez 

## Características
- Laberinto Aleatorio 

## Tecnologías utilizadas
Lenguaje de Programacion:
- Java

## Requisitos
- Codigo limpio
- POO

## Instalación
1. Clonar el repositorio.
2. Abrir el proyecto en NetBeans o IntelliJ.
3. Configurar la base de datos.
4. Ejecutar la aplicación.

## Uso
1. Iniciar sesión.
2. Seleccionar una opción del menú.
3. Realizar las operaciones deseadas.

## Estructura del proyecto
🧩 Laberinto Aleatorio
Generador y resolvedor de laberintos aleatorios implementado en Java.
El programa construye un laberinto en una cuadrícula, luego aplica BFS (Búsqueda por Amplitud) para encontrar el camino más corto entre la entrada y la salida.

📁 Estructura del proyecto
LaberintoAleatorio/
│
├── src/
│   └── laberinto/
│       ├── Main.java          # Punto de entrada del programa
│       ├── Laberinto.java     # Genera y representa el laberinto
│       ├── Celda.java         # Modelo de cada celda del laberinto
│       └── BFS.java           # Algoritmo de búsqueda del camino
│
├── README.md
└── .gitignore

⚙️ Requisitos

Java 11 o superior


▶️ Cómo compilar y ejecutar
bash# Compilar
javac -d bin src/laberinto/*.java

# Ejecutar
java -cp bin laberinto.Main

🗂️ Descripción de clases
Main.java — Punto de entrada
Es la clase que inicia el programa. Sus responsabilidades son:

Definir el tamaño del laberinto (filas y columnas)
Crear una instancia de Laberinto
Llamar al generador aleatorio
Invocar al solucionador BFS
Mostrar el laberinto en consola, con y sin la solución marcada

javapublic class Main {
    public static void main(String[] args) {
        Laberinto lab = new Laberinto(10, 10);
        lab.generar();
        BFS bfs = new BFS(lab);
        bfs.resolver();
        lab.imprimir();
    }
}

Celda.java — Modelo de cada casilla
Representa una unidad del laberinto. Cada celda conoce su posición y su estado.
Atributos principales:
AtributoTipoDescripciónfilaintPosición vertical en la cuadrículacolumnaintPosición horizontal en la cuadrículaesParedbooleantrue si es pared, false si es caminovisitadabooleanIndica si BFS ya pasó por ellaanteriorCeldaReferencia a la celda previa (para reconstruir el camino)
Métodos principales:

getVecinos(Laberinto lab) — Devuelve las celdas adyacentes (arriba, abajo, izquierda, derecha) que no son paredes

javapublic class Celda {
    int fila, columna;
    boolean esPared;
    boolean visitada;
    Celda anterior;

    public Celda(int fila, int columna, boolean esPared) {
        this.fila = fila;
        this.columna = columna;
        this.esPared = esPared;
        this.visitada = false;
        this.anterior = null;
    }
}

Laberinto.java — Generador del laberinto
Crea la cuadrícula y coloca paredes de forma aleatoria.
Atributos principales:
AtributoTipoDescripcióngridCelda[][]Matriz bidimensional con todas las celdasfilasintNúmero de filas del laberintocolumnasintNúmero de columnas del laberintoentradaCeldaCelda de inicio (generalmente esquina superior izquierda)salidaCeldaCelda de destino (generalmente esquina inferior derecha)
Métodos principales:

generar() — Llena la cuadrícula con celdas, asignando paredes aleatoriamente y garantizando que la entrada y salida siempre sean caminos libres
getCelda(int fila, int col) — Devuelve la celda en una posición dada
imprimir() — Dibuja el laberinto en consola usando caracteres (# para paredes, espacios para caminos, · para la solución)

javapublic class Laberinto {
    private Celda[][] grid;
    private int filas, columnas;
    public Celda entrada, salida;

    public void generar() {
        // Recorre la cuadrícula y asigna paredes con cierta probabilidad
        // Siempre deja libre la entrada y la salida
    }
}

BFS.java — Algoritmo de búsqueda
Implementa BFS (Breadth-First Search / Búsqueda en Anchura) para encontrar el camino más corto desde la entrada hasta la salida.
¿Por qué BFS?
BFS explora el laberinto nivel por nivel, garantizando que el primer camino que encuentra hacia la salida es el más corto en número de pasos.
Atributos principales:
AtributoTipoDescripciónlaberintoLaberintoReferencia al laberinto a resolvercolaQueue<Celda>Cola FIFO para el recorrido en anchura
Métodos principales:

resolver() — Ejecuta BFS desde la entrada y, al encontrar la salida, reconstruye el camino usando los punteros anterior de cada celda

javapublic class BFS {
    private Laberinto laberinto;
    private Queue<Celda> cola;

    public void resolver() {
        cola.add(laberinto.entrada);
        laberinto.entrada.visitada = true;

        while (!cola.isEmpty()) {
            Celda actual = cola.poll();

            if (actual == laberinto.salida) {
                reconstruirCamino(actual);
                return;
            }

            for (Celda vecino : actual.getVecinos(laberinto)) {
                if (!vecino.visitada && !vecino.esPared) {
                    vecino.visitada = true;
                    vecino.anterior = actual;
                    cola.add(vecino);
                }
            }
        }
    }
}

🔍 ¿Cómo funciona BFS paso a paso?

Inicio: Se pone la celda de entrada en una cola vacía y se marca como visitada.
Exploración: Se saca la celda al frente de la cola y se revisan sus vecinos (arriba, abajo, izquierda, derecha).
Condición: Si un vecino no es pared y no ha sido visitado, se marca como visitado, se guarda su celda anterior y se agrega a la cola.
Destino: Cuando se saca la celda de salida de la cola, se detiene la búsqueda.
Reconstrucción: Se recorre la cadena de punteros anterior desde la salida hasta la entrada para trazar el camino encontrado.

Cola inicial: [Entrada]

Paso 1: Saco Entrada → agrego sus vecinos libres
Paso 2: Saco Vecino1 → agrego sus vecinos libres
...
Paso N: Saco Salida → reconstruyo el camino

📌 Ejemplo de salida en consola
# # # # # # # # # #
E · · # · · · · · #
# # · # · # # # · #
# # · · · · # # · #
# # # # # # # # · #
# # # # # # # # · S
SímboloSignificado#Pared Camino libreEEntradaSSalida·Camino encontrado por BFS

🗒️ Notas de diseño

El laberinto se genera de forma aleatoria en cada ejecución; no siempre tiene solución garantizada. Si BFS no encuentra camino, se muestra un mensaje indicándolo.
El algoritmo BFS garantiza el camino más corto en número de celdas, no el único posible.
La clase Celda actúa como nodo del grafo implícito; las conexiones entre celdas vecinas son las aristas.


📌 Convención de commits
feat: agregar generación aleatoria del laberinto
feat: implementar algoritmo BFS
fix: corregir detección de bordes en la cuadrícula
docs: actualizar README con descripción de clases
refactor: separar lógica de impresión en Laberinto
