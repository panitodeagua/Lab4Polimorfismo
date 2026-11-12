
## Ejecutar

Para compilar desde los archivos fuente, necesitas un JDK 11 o posterior:

```sh
javac -d bin src/*.java
java -cp bin Main
```

## Menú

| Opción | Acción |
| --- | --- |
| 1 | Registrar equipo con código, nombre y empresa |
| 2 | Crear encuentro entre equipos registrados: fútbol sala o baloncesto |
| 3 | Crear exhibición sin equipos ni puntos |
| 4 | Programar o reprogramar por ID, fecha, hora y sede |
| 5 | Registrar resultado por ID del encuentro |
| 6 | Mostrar tabla general de ambos deportes |
| 7 | Mostrar calendario común |
| 0 | Salir |

Las fechas usan `aaaa-mm-dd` y las horas son enteros de 0 a 23.
Los IDs aparecen al crear un evento y al seleccionar las opciones 4 o 5.
Los códigos y las sedes se comparan respetando mayúsculas, después de quitar
espacios externos. El orden de nombres y sedes usa `String.compareTo`, conforme
al diseño, sin reglas especiales de acentuación.

## Organización

| Archivo | Responsabilidad |
| --- | --- |
| `Equipo.java` | Datos y puntos; `Comparable<Equipo>` ordena por puntos descendentes y nombre ascendente |
| `Programable.java` | Contrato del calendario |
| `Puntuable.java` | Contrato de resultados |
| `Encuentro.java` | Clase abstracta con estados, programación y resultado único |
| `FutbolSala.java` | Reglas de puntuación 3/1/0 |
| `Baloncesto.java` | Rechazo de empate y puntuación 2/1 |
| `Exhibicion.java` | Evento que implementa únicamente `Programable` |
| `Programacion.java` | Fecha, hora y sede inmutables; criterio cronológico único |
| `Copa.java` | Registros, conflictos, tabla e iteración del calendario |
| `Main.java` | Consola y datos iniciales |
| `test/PruebasArenaGT.java` | Pruebas de reglas de negocio sin dependencias |


El calendario usa `for (Programable evento : copa)`. El iterador filtra eventos
sin programación y ordena una copia por `Programacion.compareTo`. Las listas
devueltas y el iterador no permiten modificar las colecciones internas.

## Datos de demostración

Se cargan cuatro equipos de dos empresas, cuatro encuentros con ambos deportes
y una exhibición programada. Dos encuentros ya tienen resultado:

- Aurora 2 - 2 Búhos, fútbol sala: 1 punto para cada uno.
- Cóndores 60 - 50 Delfines, baloncesto: 2 y 1 puntos respectivamente.

Tabla inicial esperada: Cóndores (2), Aurora (1), Búhos (1), Delfines (1).
El empate de puntos se resuelve por nombre. Los otros dos encuentros quedan
programados y disponibles para registrar resultados.

Los datos viven en memoria. Al salir se descartan los cambios; al iniciar se
vuelve a cargar la demostración. No se exige almacenamiento en archivos en
el documento de requisitos.


# Lab4Polimorfismo
