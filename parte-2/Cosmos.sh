#!/bin/bash
# -*- ENCODING: UTF-8 -*-

#el comando javac nos permite compilar nuestro codigo fuente
javac CosmosMain.java

#ejecuta los codigos binarios creados por javac. $1 y $2 seran los parametros pasados en el comando
java CosmosMain $1 $2

# Valores para $1 : ./ejemplos/problema.prob, ./ejemplos/problema1.prob, etc
# Si cambia la ruta de los ficheros .prob, habria que cambiar la ruta. Por ejemplo: si se mete en el directorio raiz $1 seria problema.prob, si estuviera en la carpeta x seria ./x/problema.prob
# Valores para $2 : heuristica1, heuristica2 . Para el resto de valores se tomara el valor por defecto
# Ejemplo final valido: ./Cosmos.sh ./ejemplos/problema.prob heuristica2

exit

