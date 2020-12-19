import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.lang.Comparable;

public class Estado implements Comparable<Estado> {
    
    Problem problema;

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
        this.Hour = EstadoPadre.Hour + 1;

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
    
    int calcularCosteHeuristico(){
        int hCalculado = 0; //TODO: implementar
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
                
                //Hay que pasar los posibles astros que se observen, los que se encuentran en problema.astrosAObservar FIXME: Pues abrá forma mas eficiente but
                if(this.problema.ACCIONES[i].equals("Observa") && this.problema.ACCIONES[j].equals("Observa")){
                    for(int k = 0; k < this.problema.astrosAObservar.size(); k++){
                        for(int l = 0; l < this.problema.astrosAObservar.size() && l!=k; l++){
                            estadoHijo = realizarAcciones(this.problema.ACCIONES[i], this.problema.astrosAObservar.get(k), this.problema.ACCIONES[j], this.problema.astrosAObservar.get(l));
                            
                            if(estadoHijo != null){
                                //lo metemos en la arrayList de sucesores
                                //creamos los hijos de estados validos
                                System.out.println("SUCESOR: " + estadoHijo.imprimirEstado() + " " + this.problema.ACCIONES[i] + " " + this.problema.ACCIONES[j]);
            
                                sucesores.add(estadoHijo); //FIXME: igual que estos sucesores ya se añadan en orden
                            }
                        }
                    }                   

                } else if(this.problema.ACCIONES[i].equals("Observa")){
                    for(int k = 0; k < this.problema.astrosAObservar.size(); k++){
                        estadoHijo = realizarAcciones(this.problema.ACCIONES[i], this.problema.astrosAObservar.get(k), this.problema.ACCIONES[j], null);
                        //FIXME: el astrosAObservar.get lo ace bien
                        if(estadoHijo != null){
                            //lo metemos en la arrayList de sucesores
                            //creamos los hijos de estados validos
                            System.out.println("SUCESOR: " + estadoHijo.imprimirEstado() + " " + this.problema.ACCIONES[i] + " " + this.problema.ACCIONES[j]);
        
                            sucesores.add(estadoHijo); //FIXME: igual que estos sucesores ya se añadan en orden
                        }
                    }

                } else if(this.problema.ACCIONES[j].equals("Observa")){
                    for(int k = 0; k < this.problema.astrosAObservar.size(); k++){
                        estadoHijo = realizarAcciones(this.problema.ACCIONES[i], null, this.problema.ACCIONES[j], this.problema.astrosAObservar.get(k));

                        if(estadoHijo != null){
                            //lo metemos en la arrayList de sucesores
                            //creamos los hijos de estados validos
                            System.out.println("SUCESOR: " + estadoHijo.imprimirEstado() + " " + this.problema.ACCIONES[i] + " " + this.problema.ACCIONES[j]);
        
                            sucesores.add(estadoHijo); //FIXME: igual que estos sucesores ya se añadan en orden
                        }
                    }

                } else{
                    estadoHijo = realizarAcciones(this.problema.ACCIONES[i], null, this.problema.ACCIONES[j], null);

                    if(estadoHijo != null){
                        //lo metemos en la arrayList de sucesores
                        //creamos los hijos de estados validos
                        System.out.println("SUCESOR: " + estadoHijo.imprimirEstado() + " " + this.problema.ACCIONES[i] + " " + this.problema.ACCIONES[j]);
    
                        sucesores.add(estadoHijo); //FIXME: igual que estos sucesores ya se añadan en orden
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
            if(numSAT == 1 && (this.Hour >= 0 && this.Hour < 11) && (this.CargaSAT1 >= this.problema.SAT1.coste_observar) && this.Hour == astro.hora
             && (astro.banda == this.BandaSAT1[0] || astro.banda == this.BandaSAT1[1]) && (!this.OSAT1.contains(astro) && !this.OSAT2.contains(astro) && !this.OT.contains(astro))){
                return true;
            }

            if(numSAT == 2 && (this.Hour >= 0 && this.Hour < 11) && (this.CargaSAT2 >= this.problema.SAT2.coste_observar) && this.Hour == astro.hora
             && (astro.banda == this.BandaSAT2[0] || astro.banda == this.BandaSAT2[1]) && (!this.OSAT1.contains(astro) && !this.OSAT2.contains(astro) && !this.OT.contains(astro))){
                return true;
            }

        } else if(operacionSAT.equals(this.problema.ACCIONES[2])){
            //TRANSMITE. Se transmite el primer astro que este observado
            //(condicion hora) && (bateria suficiente) && (condicion de que haya observaciones)
            if(numSAT == 1 && (this.Hour >= 0 && this.Hour < 11) && (this.CargaSAT1 >= this.problema.SAT1.coste_transmitir) && (this.OSAT1.size() > 0)){
                return true;
            }

            if(numSAT == 2 && (this.Hour >= 0 && this.Hour < 11) && (this.CargaSAT2 >= this.problema.SAT2.coste_transmitir) && (this.OSAT2.size() > 0)){
                return true;
            }

        } else if(operacionSAT.equals(this.problema.ACCIONES[3])){
            //GIRA
            //(condicion hora) && (bateria suficiente)
            if(numSAT == 1 && (this.Hour >= 0 && this.Hour < 11) && (this.CargaSAT1 >= this.problema.SAT1.coste_girar)){
                return true;
            }

            if(numSAT == 2 && (this.Hour >= 0 && this.Hour < 11) && (this.CargaSAT2 >= this.problema.SAT2.coste_girar)){
                return true;
            }

        } else if(operacionSAT.equals(this.problema.ACCIONES[4])){
            //CARGA
            //(condicion hora) && (condicion bateria no cargada completamente)
            if(numSAT == 1 && (this.Hour >= 0 && this.Hour < 11) && (this.CargaSAT1 < this.problema.SAT1.bateria_maxima)){
                return true;
            }

            if(numSAT == 2 && (this.Hour >= 0 && this.Hour < 11) && (this.CargaSAT2 < this.problema.SAT2.bateria_maxima)){
                return true;
            }
        }

        return false;
    }

    private void implementEffects(Estado estadoHijo, String operacionSAT, AstroObservacion astro, int numSAT){
        System.out.print(" * ");
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
            System.out.println("-- EFECTOS TRANSMIT ---");
            if(numSAT == 1){
                estadoHijo.CargaSAT1 = this.CargaSAT1 - this.problema.SAT1.coste_transmitir; //gasta energia
                estadoHijo.OT.add(astro);
                estadoHijo.OSAT1.remove(astro); //lo eliminamos de observados
                System.out.println("-- FIN SAT1 EFECTOS TRANSMIT ---");
            }
            if(numSAT == 2){
                estadoHijo.CargaSAT2 = this.CargaSAT2 - this.problema.SAT2.coste_transmitir; //gasta energia
                estadoHijo.OT.add(astro);
                estadoHijo.OSAT2.remove(astro); //lo eliminamos de observados
            }
            //FIXME: System.out.println("-- FIN EFECTOS TRANSMIT " + astro.hora +  " " + astro.banda + "---");

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

    public String imprimirEstado(){
        String imprimir = "";

        String impOT = "";
        for(int i = 0; i <  this.OT.size(); i++){
            impOT = impOT + " (" + this.OT.get(i).banda + "," + this.OT.get(i).hora + ")"; 
        }

        String impBanda1 = "b: (" + this.BandaSAT1[0] + "," + this.BandaSAT1[1] + ") -"; 
        String impBanda2 = "b: (" + this.BandaSAT2[0] + "," + this.BandaSAT2[1] + ") - "; 

        String impObvs1 = "";
        for(int i = 0; i <  this.OSAT1.size(); i++){
            impObvs1 = impObvs1 + " (" + this.OSAT1.get(i).banda + "," + this.OSAT1.get(i).hora + ")"; 
        }    
        
        String impObvs2 = "";
        for(int i = 0; i <  this.OSAT2.size(); i++){
            impObvs2 = impObvs2 + " (" + this.OSAT2.get(i).banda + "," + this.OSAT2.get(i).hora + ")"; 
        }
        
        String imprimirSAT1 = "SAT1: " + this.CargaSAT1 + ", " + impBanda1 + impObvs1;
        String imprimirSAT2 = "SAT2: " + this.CargaSAT2 + ", " + impBanda2 + impObvs2;
        imprimir = "H: " + this.Hour + "; " + imprimirSAT1 + "; " + imprimirSAT2 + "; " + this.f;
        
        return imprimir;
    }
}
