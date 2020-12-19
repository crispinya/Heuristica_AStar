@echo off

DEL Estado.class
DEL CosmosMain.class
DEL Parseador.class
DEL Problem.class
DEL AstroObservacion.class
DEL SATelit.class

javac CosmosMain.java

java CosmosMain problema.prob a >ejecucion.txt

pause