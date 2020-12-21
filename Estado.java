import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.lang.Comparable;

public class Estado implements Comparable<Estado> {
    
    Problem problema;

    String operaciones; //operacion mediante la cual ha sido creado

    ArrayList<AstroObservacion> OT = new ArrayList<AstroObservacion>(); //observaciones ya transmitidas a base
    int Hour; //current time

    int CargaSAT1; //Charge (Carga) of the batery of CSAT1
    int[] BandaSAT1; //Observation Band of the SAT1
    ArrayList<AstroObservacion> OSAT1 = new ArrayList<AstroObservacion>();

    int CargaSAT2; //Charge (Carga) of the batery of CSAT2
    int[] BandaSAT2; //Observation Band of the SAT2
    ArrayList<AstroObservacion> OSAT2 = new ArrayList<AstroObservacion>();
    
    //valores a calcular sobre el estado
    int g; // Coste desde el estado S inicial 
    int h; // Coste heurístico desde este estado hasta algún estado final t
    int f; //suma de g y h 
    
    Estado padre; //Referencia al estado padre

    //to create the initial state
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
    int calcularCosteG(){ //FIXME: Si queremos maximizar lo otro, cambiar esto
        //estamos minimizando el coste en horas. Pasa una hora cada vez (+1)
        return padre.g + 1;
    }
    
    int calcularCosteHeuristico(){//TODO: implementar
        int hCalculado = 0; //heuristica por defecto es h = 0
        
        if(this.problema.heuristica.equals("heuristica1")){
            int noTransmit = problema.astrosAObservar.size() - this.OT.size();
            int observadasNoTransmit = OSAT1.size() + OSAT2.size();
            int noObservadas = noTransmit - observadasNoTransmit;
            
            hCalculado = (2*noObservadas + 1*observadasNoTransmit)/2; 
            //hCalculado = noObservadas + observadasNoTransmit; 
            //2* ya que cuesta 1 hora observarlas y otra hora transmitirlas
            //1* porque solo hay que transmitirla, lo cual cuesta 1 hora
            //divimos /2 ya que hay dos satelites que pueden realizar observaciones / transmisiones a la vez

        } 

        else if(this.problema.heuristica.equals("heuristica2")){
            //no relajamos la restriccion de que este en la misma banda

            int noTransmit = problema.astrosAObservar.size() - this.OT.size();
            ArrayList<AstroObservacion> astrosPorObservar = new ArrayList<AstroObservacion>();
            //insertamos los astros que faltan por observar
            for(int i = 0; i < problema.astrosAObservar.size(); i++){
                if(!this.OT.contains(problema.astrosAObservar.get(i))){
                    astrosPorObservar.add(problema.astrosAObservar.get(i));
                }
            }

            int observadasNoTransmit = OSAT1.size() + OSAT2.size();
            int noObservadas = noTransmit - observadasNoTransmit;
            
            //checkeamos si hay alguna observacion disponible en la banda de alguno de los astros
            boolean bandaOk = false; 

            for(int i = 0; i < astrosPorObservar.size() && !bandaOk; i++){
                if(astrosPorObservar.get(i).banda == this.BandaSAT1[0] || astrosPorObservar.get(i).banda == this.BandaSAT1[1]){
                    bandaOk = true;
                }
                if(astrosPorObservar.get(i).banda == this.BandaSAT2[0] || astrosPorObservar.get(i).banda == this.BandaSAT2[1]){
                    bandaOk = true;
                }
            }

            if(bandaOk){
                hCalculado = (2*noObservadas + 1*observadasNoTransmit)/2;
            } else{
                hCalculado = (2*noObservadas + 1*observadasNoTransmit)/2 + 1; //por lo menos un satelite tendra que girar
            }
             

        }
        
        /*else if(this.problema.heuristica.equals("heuristica2")){
            //relajamos todo excepto los costos, que ahora sí se tienen en cuenta
            
            //hereda de la heuristica1
            int noTransmit = problema.astrosAObservar.size() - this.OT.size();
            int observadasNoTransmit = OSAT1.size() + OSAT2.size();
            int noObservadas = noTransmit - observadasNoTransmit;

            //calculos heuristica2
            int horasCargaPreObservacionSAT1 = this.problema.SAT1.coste_observar/this.problema.SAT1.beneficio_cargar; //tiempo en horas que se tarda en recargar la bateria lo suficiente como para poder realizar un observacion
            int horasCargaPreTransmisionSAT1 = this.problema.SAT1.coste_transmitir/this.problema.SAT1.beneficio_cargar; //tiempo en horas que se tarda en recargar la bateria lo suficiente como para poder realizar un transmision
            int horasCargaPreObservacionSAT2 = this.problema.SAT2.coste_observar/this.problema.SAT2.beneficio_cargar; //tiempo en horas que se tarda en recargar la bateria lo suficiente como para poder realizar un observacion
            int horasCargaPreTransmisionSAT2 = this.problema.SAT2.coste_transmitir/this.problema.SAT2.beneficio_cargar;
            
            //para controlar los impares
            int noObservadasSAT1, noObservadasSAT2;
            if((noObservadas/2) % 2==0){
                noObservadasSAT1=noObservadas/2;
                noObservadasSAT2=noObservadas/2;
            }else{
                noObservadasSAT1=(noObservadas/2) + 1; //decision de diseño
                noObservadasSAT2=noObservadas/2;
            }

            //respecto a SAT1
            if(noObservadas > 0 && this.CargaSAT1 >= this.problema.SAT1.coste_observar){ //faltan observaciones por realizar y tiene bateria
                hCalculado = Math.max(horasCargaPreObservacionSAT1* (noObservadasSAT1 - 1),0) + horasCargaPreTransmisionSAT1 * (noObservadasSAT1 + OSAT1.size());
            } else if(noObservadas > 0 && this.CargaSAT1 < this.problema.SAT1.coste_observar){ //faltan observaciones por realizar pero no tiene bateria
                hCalculado = horasCargaPreObservacionSAT1* noObservadasSAT1 + horasCargaPreTransmisionSAT1 * (noObservadasSAT1 + OSAT1.size());
            } else if(noObservadas == 0 && this.CargaSAT1 >= this.problema.SAT1.coste_transmitir){ //solo quedan pro transmitir, y hay bateria para ello
                hCalculado = Math.max(horasCargaPreTransmisionSAT1 * (OSAT1.size()-1),0);
            } else if(noObservadas == 0 && this.CargaSAT1 < this.problema.SAT1.coste_transmitir){ //solo quedan por transmitir, no hay bateria para ello
                hCalculado = horasCargaPreTransmisionSAT1 * OSAT1.size();
            }

            //respecto a SAT2
            if(noObservadas > 0 && this.CargaSAT2 >= this.problema.SAT2.coste_observar){ //faltan observaciones por realizar y tiene bateria
                hCalculado = hCalculado + Math.max(horasCargaPreObservacionSAT2* (noObservadasSAT2 - 1),0) + horasCargaPreTransmisionSAT2 * (noObservadasSAT2 + OSAT2.size());
            } else if(noObservadas > 0 && this.CargaSAT2 < this.problema.SAT2.coste_observar){ //faltan observaciones por realizar pero no tiene bateria
                hCalculado = hCalculado + horasCargaPreObservacionSAT2* noObservadasSAT2 + horasCargaPreTransmisionSAT2 * (noObservadasSAT2 + OSAT2.size());
            } else if(noObservadas == 0 && this.CargaSAT2 >= this.problema.SAT2.coste_transmitir){ //solo quedan pro transmitir, y hay bateria para ello
                hCalculado = hCalculado + Math.max(horasCargaPreTransmisionSAT2 * (OSAT2.size()-1),0);
            } else if(noObservadas == 0 && this.CargaSAT2 < this.problema.SAT2.coste_transmitir){ //solo quedan por transmitir, no hay bateria para ello
                hCalculado = hCalculado + horasCargaPreTransmisionSAT2 * OSAT2.size();
            }
        } */

        /*else if(this.problema.heuristica.equals("heuristica2")){
            //no relajamos el coste de transmision

            //hereda de la heuristica1
            int noTransmit = problema.astrosAObservar.size() - this.OT.size();
            int observadasNoTransmit = OSAT1.size() + OSAT2.size();
            int noObservadas = noTransmit - observadasNoTransmit;
 
            //calculos heuristica2
            int horasCargaPreTransmisionSAT1 = this.problema.SAT1.coste_transmitir/this.problema.SAT1.beneficio_cargar; //tiempo en horas que se tarda en recargar la bateria lo suficiente como para poder realizar un transmision
            int horasCargaPreTransmisionSAT2 = this.problema.SAT2.coste_transmitir/this.problema.SAT2.beneficio_cargar;

            //respecto a SAT1
            if(noObservadas > 0 ){ //faltan observaciones por realizar y tiene bateria
                hCalculado = horasCargaPreTransmisionSAT1 * (OSAT1.size());
            } else if(noObservadas == 0 && this.CargaSAT1 >= this.problema.SAT1.coste_transmitir){ //solo quedan pro transmitir, y hay bateria para ello
                hCalculado = Math.max(horasCargaPreTransmisionSAT1 * (OSAT1.size()-1),0);
            } else if(noObservadas == 0 && this.CargaSAT1 < this.problema.SAT1.coste_transmitir){ //solo quedan por transmitir, no hay bateria para ello
                hCalculado = horasCargaPreTransmisionSAT1 * OSAT1.size();
            }

            //respecta a SAT2
            if(noObservadas > 0 ){ //faltan observaciones por realizar y tiene bateria
                hCalculado = hCalculado + horasCargaPreTransmisionSAT2 * (OSAT2.size());
            } else if(noObservadas == 0 && this.CargaSAT2 >= this.problema.SAT2.coste_transmitir){ //solo quedan pro transmitir, y hay bateria para ello
                hCalculado = Math.max(hCalculado + horasCargaPreTransmisionSAT2 * (OSAT2.size()-1),0);
            } else if(noObservadas == 0 && this.CargaSAT2 < this.problema.SAT2.coste_transmitir){ //solo quedan por transmitir, no hay bateria para ello
                hCalculado = hCalculado + horasCargaPreTransmisionSAT2 * OSAT2.size();
            }
                     
        }*/


        return hCalculado;
    }

    int calcularCosteF(int g, int h){
        return g + h;
    }
    

    //funcion crear sucesores
    public ArrayList<Estado> crearSucesores(){
        ArrayList<Estado> sucesores = new ArrayList<Estado>();
        
        Estado estadoHijo = null;

        //intentamos ejecutar todas las combinaciones posibles, incluyendo las observaciones
        for(int i = 0; i < this.problema.ACCIONES.length; i++){ //para SAT1
            for (int j = 0; j < this.problema.ACCIONES.length; j++){ //para SAT2
                
                //Hay que pasar los posibles astros que se observen, los que se encuentran en problema.astrosAObservar FIXME: Pues habrá forma mas eficiente but
                if(this.problema.ACCIONES[i].equals("Observa") && this.problema.ACCIONES[j].equals("Observa")){
                    for(int k = 0; k < this.problema.astrosAObservar.size(); k++){
                        for(int l = 0; l < this.problema.astrosAObservar.size(); l++){
                            if(l != k){
                                estadoHijo = realizarAcciones(this.problema.ACCIONES[i], this.problema.astrosAObservar.get(k), this.problema.ACCIONES[j], this.problema.astrosAObservar.get(l));
                                if(estadoHijo != null){
                                    sucesores.add(estadoHijo); //FIXME: igual que estos sucesores ya se añadan en orden
                                }
                            }
                        }
                    }                   

                } else if(this.problema.ACCIONES[i].equals("Observa")){
                    for(int k = 0; k < this.problema.astrosAObservar.size(); k++){
                        estadoHijo = realizarAcciones(this.problema.ACCIONES[i], this.problema.astrosAObservar.get(k), this.problema.ACCIONES[j], null);
                        if(estadoHijo != null){
                            sucesores.add(estadoHijo);
                        }
                    }

                } else if(this.problema.ACCIONES[j].equals("Observa")){
                    for(int k = 0; k < this.problema.astrosAObservar.size(); k++){
                        estadoHijo = realizarAcciones(this.problema.ACCIONES[i], null, this.problema.ACCIONES[j], this.problema.astrosAObservar.get(k));
                        if(estadoHijo != null){
                            sucesores.add(estadoHijo);
                        }
                    }

                } else{
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
        Collections.sort(sucesores);
        return sucesores;
    }

    private Estado realizarAcciones(String operacionSAT1, AstroObservacion astro1, String operacionSAT2, AstroObservacion astro2){
        Estado estadoHijo = null;

        //Comprobamos que las precondiciones sean validas, en caso contrario devolvemos null
        if(!validPreconditions(operacionSAT1, astro1, 1) || !validPreconditions(operacionSAT2, astro2, 2)){
            return null;
        }

        //Si las precondiciones ok, creamos hijo pasando como parametro este estado (estado padre):
        estadoHijo = new Estado(this);

        //Aplicamos las postcondiciones efecto de las acciones
        implementEffects(estadoHijo, operacionSAT1, astro1, 1);
        implementEffects(estadoHijo, operacionSAT2, astro2, 2);

        //indicamos con que operaciones se obtuvo el estado hijo
        String sAstro1 = "", sAstro2 = "";
        if(astro1 != null) sAstro1 = " " +  astro1.id;
        if(astro2 != null) sAstro2 = " " + astro2.id;

        estadoHijo.operaciones = "SAT1: " + operacionSAT1 + sAstro1 + ", SAT2: " + operacionSAT2 + sAstro2;

        return estadoHijo;
    }

    //FIXME: Igual mejor comprobar primero que existan operaciones ambas y ya luego el resto

   
    private boolean validPreconditions(String operacionSAT, AstroObservacion astro, int numSAT){
        //diferenciamos entre SAT1 y SAT2
        if(numSAT != 1 && numSAT != 2){
            return false;
        }

        if(operacionSAT.equals(this.problema.ACCIONES[0])){
            //IDLE, no tiene precondiciones
            return true;

        } else if(operacionSAT.equals(this.problema.ACCIONES[1])){
            //OBSERVA
            //(condicion hora) && (bateria suficiente) && (hora astro correcta) &&(astro en su banda de obvs) && (obvs no realizada ni enviada ya)
            if(numSAT == 1 && (this.Hour >= 0 && this.Hour < 12) && (this.CargaSAT1 >= this.problema.SAT1.coste_observar) && this.Hour == astro.hora
             && (astro.banda == this.BandaSAT1[0] || astro.banda == this.BandaSAT1[1]) && (!this.OSAT1.contains(astro) && !this.OSAT2.contains(astro) && !this.OT.contains(astro))){
                return true;
            }

            if(numSAT == 2 && (this.Hour >= 0 && this.Hour < 12) && (this.CargaSAT2 >= this.problema.SAT2.coste_observar) && this.Hour == astro.hora
             && (astro.banda == this.BandaSAT2[0] || astro.banda == this.BandaSAT2[1]) && (!this.OSAT1.contains(astro) && !this.OSAT2.contains(astro) && !this.OT.contains(astro))){
                return true;
            }

        } else if(operacionSAT.equals(this.problema.ACCIONES[2])){
            //TRANSMITE. Se transmite el primer astro que este observado
            //(condicion hora) && (bateria suficiente) && (condicion de que haya observaciones)
            if(numSAT == 1 && (this.Hour >= 0 && this.Hour < 12) && (this.CargaSAT1 >= this.problema.SAT1.coste_transmitir) && (this.OSAT1.size() > 0)){
                return true;
            }

            if(numSAT == 2 && (this.Hour >= 0 && this.Hour < 12) && (this.CargaSAT2 >= this.problema.SAT2.coste_transmitir) && (this.OSAT2.size() > 0)){
                return true;
            }

        } else if(operacionSAT.equals(this.problema.ACCIONES[3])){
            //GIRA
            //(condicion hora) && (bateria suficiente)
            if(numSAT == 1 && (this.Hour >= 0 && this.Hour < 12) && (this.CargaSAT1 >= this.problema.SAT1.coste_girar)){
                return true;
            }

            if(numSAT == 2 && (this.Hour >= 0 && this.Hour < 12) && (this.CargaSAT2 >= this.problema.SAT2.coste_girar)){
                return true;
            }

        } else if(operacionSAT.equals(this.problema.ACCIONES[4])){
            //CARGA
            //(condicion hora) && (condicion bateria no cargada completamente)
            if(numSAT == 1 && (this.Hour >= 0 && this.Hour < 12) && (this.CargaSAT1 < this.problema.SAT1.bateria_maxima)){
                return true;
            }

            if(numSAT == 2 && (this.Hour >= 0 && this.Hour < 12) && (this.CargaSAT2 < this.problema.SAT2.bateria_maxima)){
                return true;
            }
        }

        return false;
    }

    private void implementEffects(Estado estadoHijo, String operacionSAT, AstroObservacion astro, int numSAT){
        //System.out.print(" * ");
        //efectos depende de las operaciones realizadas

        if(operacionSAT.equals(this.problema.ACCIONES[0])){
            //IDLE, no tiene efectos mas allá de la hora

        } else if(operacionSAT.equals(this.problema.ACCIONES[1])){
            //OBSERVA
            if(numSAT == 1){
                estadoHijo.CargaSAT1 = this.CargaSAT1 - this.problema.SAT1.coste_observar; //gasta energia
                estadoHijo.OSAT1.add(astro); //efecto guarda una nueva observacion
            }
            if(numSAT == 2){
                estadoHijo.CargaSAT2 = this.CargaSAT2 - this.problema.SAT2.coste_observar; //gasta energia
                estadoHijo.OSAT2.add(astro); //efecto guarda una nueva observacion
            }
            

        } else if(operacionSAT.equals(this.problema.ACCIONES[2])){
            //TRANSMITE, por defecto la obvs mas reciente
            if(numSAT == 1){
                estadoHijo.CargaSAT1 = this.CargaSAT1 - this.problema.SAT1.coste_transmitir; //gasta energia
                AstroObservacion astroAux = estadoHijo.OSAT1.get(0);
                estadoHijo.OSAT1.remove(0);
                estadoHijo.OT.add(astroAux);
            }
            if(numSAT == 2){
                estadoHijo.CargaSAT2 = this.CargaSAT2 - this.problema.SAT2.coste_transmitir; //gasta energia
                AstroObservacion astroAux = estadoHijo.OSAT2.get(0);
                estadoHijo.OSAT2.remove(0);
                estadoHijo.OT.add(astroAux);
            }

        } else if(operacionSAT.equals(this.problema.ACCIONES[3])){
            //caso GIRA

            if(numSAT == 1){
                estadoHijo.CargaSAT1 = this.CargaSAT1 - this.problema.SAT1.coste_girar; //gasta energia

                //comprobamos que banda tiene ahora, y asignamos la contraria
                if(Arrays.equals(this.BandaSAT1, this.problema.SAT1.bandas_admitidas[0])){
                    estadoHijo.BandaSAT1 = this.problema.SAT1.bandas_admitidas[1];
                } else{
                    estadoHijo.BandaSAT1 = this.problema.SAT1.bandas_admitidas[0];
                }
            }

            if(numSAT == 2){
                estadoHijo.CargaSAT2 = this.CargaSAT2 - this.problema.SAT2.coste_girar; //gasta energia

                //comprobamos que banda tiene ahora, y asignamos la contraria
                if(Arrays.equals(this.BandaSAT2, this.problema.SAT2.bandas_admitidas[0])){
                    estadoHijo.BandaSAT2 = this.problema.SAT2.bandas_admitidas[1];
                } else{
                    estadoHijo.BandaSAT2 = this.problema.SAT2.bandas_admitidas[0];
                }
            }

                
        } else if(operacionSAT.equals(this.problema.ACCIONES[4])){
            //caso CARGA
            if(numSAT == 1){
                estadoHijo.CargaSAT1 = this.CargaSAT1 + this.problema.SAT1.beneficio_cargar; //carga energia, sin pasarse de la maxima
                if(estadoHijo.CargaSAT1 > this.problema.SAT1.bateria_maxima){
                    estadoHijo.CargaSAT1 = this.problema.SAT1.bateria_maxima;
                }
            }

            if(numSAT == 2){
                estadoHijo.CargaSAT2 = this.CargaSAT2 + this.problema.SAT2.beneficio_cargar; //carga energia, sin pasarse de la maxima
                if(estadoHijo.CargaSAT2 > this.problema.SAT2.bateria_maxima){
                    estadoHijo.CargaSAT2 = this.problema.SAT2.bateria_maxima;
                }
            }

        }
    }

    @Override
    public int compareTo(Estado e2){ //Comparacion del coste f, dentro de la lista de sucesores
        if(this.f>e2.f) return 1;
        else if(this.f<e2.f) return -1;
        if(this.h>e2.h) return 1;
        if(this.h<e2.h) return -1; 
        return 0; //En caso de empate total, cogemos el mas nuevo
    }
}
