@echo off

DEL Estado.class
DEL CosmosMain.class
DEL Parseador.class
DEL Problem.class
DEL MuestraObservacion.class
DEL SATelit.class

javac CosmosMain.java

java CosmosMain problema.prob heuristica2

pause