@echo off

DEL state.class
DEL cosmos.class
DEL parser.class
DEL problema.class
DEL Astro.class
DEL satelite.class

javac cosmos.java

java cosmos problema.prob a

pause