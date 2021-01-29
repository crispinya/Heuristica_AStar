import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.lang.Comparable;

public class Estado implements Comparable<Estado> {
    
    Problem problema;

    String operaciones; //operacion mediante la cual ha sido creado

    ArrayList<MuestraObservacion> OT = new ArrayList<MuestraObservacion>(); //observaciones ya transmitidas a base
    int Hour; //hora actual en el problema

    int CargaSAT1; //Cargar de SAT1
    int[] BandaSAT1; //Banda de observacion de SAT1
    ArrayList<MuestraObservacion> OSAT1 = new ArrayList<MuestraObservacion>(); //observaciones realizadas por SAT1 aun no transmitidas a base 

    int CargaSAT2; //Cargar de SAT2
    int[] BandaSAT2; //Banda de observacion de SAT2
    ArrayList<MuestraObservacion> OSAT2 = new ArrayList<MuestraObservacion>(); //observaciones realizadas por SAT2 aun no transmitidas a base
    
    //valores a calcular sobre el estado
    int g; // Coste desde el estado S inicial 
    int h; // Coste heuristico desde este estado hasta algún estado final t
    int f; //suma de g y h 
    
    Estado padre; //Referencia al estado padre

    //constructor del estado inicial
    public Estado(Problem problema){
        this.problema = problema;
        this.operaciones = "Estado inicial";

        this.Hour = 0; //por ser estado inicial
        this.CargaSAT1 = problema.SAT1.bateria_maxima;
        this.BandaSAT1 = this.problema.SAT1.bandas_admitidas[0]; //(0,1)

        this.CargaSAT2 = problema.SAT2.bateria_maxima;
        this.BandaSAT2 = this.problema.SAT2.bandas_admitidas[0]; //(2,3)

        this.g = 0; //por ser estado inicial
        this.h = calcularCosteHeuristico();
        this.f = calcularCosteF(this.g, this.h);

        this.padre = null; //por ser estado inicial
    }

    //constructor estados sucesores
    public Estado(Estado EstadoPadre){
        
        //copiamos los valores del padre, luego los modificaremos segun las operaciones aplicadas
        this.problema = EstadoPadre.problema;
        //array OT
        for(int i = 0; i < EstadoPadre.OT.size(); i++){
            this.OT.add(EstadoPadre.OT.get(i));
        }
    
        this.CargaSAT1 = EstadoPadre.CargaSAT1;
        this.BandaSAT1 = EstadoPadre.BandaSAT1;
        //array OSAT1
        for(int i = 0; i < EstadoPadre.OSAT1.size(); i++){
            this.OSAT1.add(EstadoPadre.OSAT1.get(i));
        }

        this.CargaSAT2 = EstadoPadre.CargaSAT2;
        this.BandaSAT2 = EstadoPadre.BandaSAT2;
        //array OSAT2
        for(int i = 0; i < EstadoPadre.OSAT2.size(); i++){
            this.OSAT2.add(EstadoPadre.OSAT2.get(i));
        }

        // Actualizamos los valores
        this.padre = EstadoPadre;
        this.Hour = (EstadoPadre.Hour + 1) % 24; //hora es modulo 24

        //Calculamos los costes
        this.g = calcularCosteG();
        this.h = calcularCosteHeuristico();
        this.f = calcularCosteF(this.g, this.h);
    }

    //funciones calculo de costes:

    int calcularCosteG(){
        //estamos minimizando el coste en horas. Pasa una hora cada vez (+1)
        return padre.g + 1;
    }
    
    int calcularCosteHeuristico(){
        int hCalculado = 0; //heuristica por defecto es h = 0
        
        if(this.problema.heuristica.equals("heuristica1")){
            //relajamos las restricciones de bateria, horas de luz, y posicion en hora y banda de las muestras
            //se mantiene la restriccion de solo poder realizar una accion cada vez

            int noTransmit = problema.muestrasAObservar.size() - this.OT.size();
            int observadasNoTransmit = OSAT1.size() + OSAT2.size();
            int noObservadas = noTransmit - observadasNoTransmit;

            hCalculado = (2 * noObservadas + 1 * observadasNoTransmit) / 2;
            // 2* ya que cuesta 1 hora observarlas y otra hora transmitirlas
            // 1* porque solo hay que transmitirla, lo cual cuesta 1 hora
            // divimos /2 ya que hay dos satelites que pueden realizar observaciones / transmisiones a la vez
        }

        else if (this.problema.heuristica.equals("heuristica2")) {
            // Igual a la anterior, pero no relajamos la restriccion de que este en la misma banda

            int noTransmit = problema.muestrasAObservar.size() - this.OT.size();
            ArrayList<MuestraObservacion> muestrasPorObservar = new ArrayList<MuestraObservacion>();
            // insertamos los astros que faltan por observar
            for (int i = 0; i < problema.muestrasAObservar.size(); i++) {
                if (!this.OT.contains(problema.muestrasAObservar.get(i))) {
                    muestrasPorObservar.add(problema.muestrasAObservar.get(i));
                }
            }

            int observadasNoTransmit = OSAT1.size() + OSAT2.size();
            int noObservadas = noTransmit - observadasNoTransmit;

            // checkeamos si hay alguna observacion disponible en la banda de alguno de los
            // astros
            boolean bandaOk = false;

            for (int i = 0; i < muestrasPorObservar.size() && !bandaOk; i++) {
                if (muestrasPorObservar.get(i).banda == this.BandaSAT1[0]
                        || muestrasPorObservar.get(i).banda == this.BandaSAT1[1]) {
                    bandaOk = true;
                }
                if (muestrasPorObservar.get(i).banda == this.BandaSAT2[0]
                        || muestrasPorObservar.get(i).banda == this.BandaSAT2[1]) {
                    bandaOk = true;
                }
            }

            if (bandaOk) {
                hCalculado = (2 * noObservadas + 1 * observadasNoTransmit) / 2;
            } else {
                hCalculado = (2 * noObservadas + 1 * observadasNoTransmit) / 2 + 1; // por lo menos un satelite tendra que girar para realizar observacion
            }

        }

        return hCalculado;
    }

    int calcularCosteF(int g, int h) {
        return g + h;
    }

    // funcion crear sucesores
    public ArrayList<Estado> crearSucesores() {
        ArrayList<Estado> sucesores = new ArrayList<Estado>();
        Estado estadoHijo = null;

        // intentamos ejecutar todas las combinaciones posibles, incluyendo la observacion de distintas muestras
        for (int i = 0; i < this.problema.ACCIONES.length; i++) { // para SAT1
            for (int j = 0; j < this.problema.ACCIONES.length; j++) { // para SAT2

                // Hay que pasar las posibles muestras a observar, que se encuentran en problema.muestrasAObservar
                if (this.problema.ACCIONES[i].equals("Observa") && this.problema.ACCIONES[j].equals("Observa")) {
                    for (int k = 0; k < this.problema.muestrasAObservar.size(); k++) {
                        for (int l = 0; l < this.problema.muestrasAObservar.size(); l++) {
                            if (l != k) { //no pueden observar la misma muestra
                                estadoHijo = realizarAcciones(this.problema.ACCIONES[i],
                                        this.problema.muestrasAObservar.get(k), this.problema.ACCIONES[j],
                                        this.problema.muestrasAObservar.get(l));
                                if (estadoHijo != null) {
                                    sucesores.add(estadoHijo); //si el estado creado es valido, se aniade a la lista de sucesores
                                }
                            }
                        }
                    }

                } else if (this.problema.ACCIONES[i].equals("Observa")) {
                    for (int k = 0; k < this.problema.muestrasAObservar.size(); k++) {
                        estadoHijo = realizarAcciones(this.problema.ACCIONES[i], this.problema.muestrasAObservar.get(k),
                                this.problema.ACCIONES[j], null);
                        if (estadoHijo != null) {
                            sucesores.add(estadoHijo);
                        }
                    }

                } else if (this.problema.ACCIONES[j].equals("Observa")) {
                    for (int k = 0; k < this.problema.muestrasAObservar.size(); k++) {
                        estadoHijo = realizarAcciones(this.problema.ACCIONES[i], null, this.problema.ACCIONES[j],
                                this.problema.muestrasAObservar.get(k));
                        if(estadoHijo != null){
                            sucesores.add(estadoHijo);
                        }
                    }

                } else{ //en caso de que ninguno de los dos satelites observe, no se pasan muestras como parametro
                    estadoHijo = realizarAcciones(this.problema.ACCIONES[i], null, this.problema.ACCIONES[j], null);
                    if(estadoHijo != null){
                        sucesores.add(estadoHijo);
                    }
                }
            }
        }

        if(sucesores.isEmpty()){
            return null;
        }

        Collections.sort(sucesores); //se ordena el array de sucesores
        return sucesores;
    }

    //operador heuristico
    private Estado realizarAcciones(String operacionSAT1, MuestraObservacion astro1, String operacionSAT2, MuestraObservacion astro2){
        Estado estadoHijo = null;

        //Comprobamos que las precondiciones sean validas, en caso contrario devolvemos null
        if(!validPreconditions(operacionSAT1, astro1, 1) || !validPreconditions(operacionSAT2, astro2, 2)){
            return null;
        }

        //Si las precondiciones son validas, creamos hijo pasando como parametro este estado (estado padre):
        estadoHijo = new Estado(this);

        //Aplicamos las postcondiciones, efecto de las acciones
        implementEffects(estadoHijo, operacionSAT1, astro1, 1);
        implementEffects(estadoHijo, operacionSAT2, astro2, 2);

        //indicamos con que operaciones se obtuvo el estado hijo
        String sAstro1 = "", sAstro2 = "";
        if(astro1 != null) sAstro1 = " " +  astro1.id;
        if(astro2 != null) sAstro2 = " " + astro2.id;

        estadoHijo.operaciones = "SAT1: " + operacionSAT1 + sAstro1 + ", SAT2: " + operacionSAT2 + sAstro2;

        //devolvemos el hijo creado
        return estadoHijo;
    }

    //comprobacion de las precondiciones
    private boolean validPreconditions(String operacionSAT, MuestraObservacion astro, int numSAT){
        //diferenciamos entre SAT1 y SAT2
        if(numSAT != 1 && numSAT != 2){
            return false;
        }

        if(operacionSAT.equals(this.problema.ACCIONES[0])){
            return true; //IDLE, no tiene precondiciones

        } else if(operacionSAT.equals(this.problema.ACCIONES[1])){
            return ComprobadorPrecondiciones.puedeObservar(this, operacionSAT, astro, numSAT);

        } else if(operacionSAT.equals(this.problema.ACCIONES[2])){
            return ComprobadorPrecondiciones.puedeTransmitir(this, operacionSAT, numSAT);

        } else if(operacionSAT.equals(this.problema.ACCIONES[3])){
            return ComprobadorPrecondiciones.puedeGirar(this, operacionSAT, numSAT);

        } else if(operacionSAT.equals(this.problema.ACCIONES[4])){
            return ComprobadorPrecondiciones.puedeCargar(this, operacionSAT, numSAT);
        }

        //si el tipo de operacion no es valido, tambien se devuelve false
        return false;
    }

    private void implementEffects(Estado estadoHijo, String operacionSAT, MuestraObservacion muestra, int numSAT){
        //los efectos dependen de las operaciones realizadas

        if(operacionSAT.equals(this.problema.ACCIONES[0])){
            //IDLE, no tiene efectos mas allá de la hora

        } else if(operacionSAT.equals(this.problema.ACCIONES[1])){
            ImplementadorEfectos.efectosObservar(this, estadoHijo, muestra, numSAT);
            
        } else if(operacionSAT.equals(this.problema.ACCIONES[2])){
            //TRANSMITE, por defecto la observacion mas reciente
            ImplementadorEfectos.efectosTransmitir(this, estadoHijo, numSAT);

        } else if(operacionSAT.equals(this.problema.ACCIONES[3])){
            ImplementadorEfectos.efectosGirar(this, estadoHijo, numSAT);
                
        } else if(operacionSAT.equals(this.problema.ACCIONES[4])){
            ImplementadorEfectos.efectosCargar(this, estadoHijo, numSAT);
        }
    }

    @Override //sobreescribimos el metodo por defecto de sort, para ajustarlo a nuestras necesidades
    public int compareTo(Estado e2){ //Comparacion del coste f, dentro de la lista de sucesores
        if(this.f>e2.f) return 1;
        else if(this.f<e2.f) return -1;
        if(this.h>e2.h) return 1;
        if(this.h<e2.h) return -1; 
        return 0; //En caso de empate total, cogemos el mas nuevo
    }
}
