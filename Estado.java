import java.util.ArrayList;

public class Estado {
    
    Problem problema;

    ArrayList<AstroObservacion> OT = new ArrayList<AstroObservacion>();
    int Hour; //current time

    int CargaSAT1; //Charge (Carga) of the batery of CSAT1
    int BandaSAT1; //Observation Band of the SAT1
    ArrayList<AstroObservacion> OSAT1 = new ArrayList<AstroObservacion>();

    int CargaSAT2; //Charge (Carga) of the batery of CSAT2
    int BandaSAT2; //Observation Band of the SAT2
    ArrayList<AstroObservacion> OSAT2 = new ArrayList<AstroObservacion>();
    
    //valores a calcular sobre el estado
    int g; // Coste desde el estado S inicial 
    int h; // Coste heurístico desde este estado hasta algún estado final t
    int f; //suma de f y h 
    
    //Referencia al estado padre
    Estado padre;

      //to create the initial state
    public Estado(Problem problema){
        this.problema = problema;
        this.OT = null; //por ser estado inicial

        this.Hour = 0; //por ser estado inicial
        this.CargaSAT1 = problema.SAT1.bateria_maxima;
        this.BandaSAT1 = 01;
        this.OSAT1 = null; //por ser estado inicial

        this.CargaSAT2 = problema.SAT2.bateria_maxima;
        this.BandaSAT2 = 23;
        this.OSAT2 = null; //por ser estado inicial

        this.g = 0; //por ser estado inicial
        this.h = calcularCosteHeuristico();
        this.f = calcularCosteF(this.g, this.h);

        this.padre = null; //por ser estado inicial
    }

    public Estado(){
        //FIXME: Igual hay que cambiarlo. Dejar esto vacio y crear setters y getters pa to?
        this.problema = problema;
        this.OT = null; //por ser estado inicial

        this.Hour = 0; //por ser estado inicial
        this.CargaSAT1 = problema.SAT1.bateria_maxima;
        this.BandaSAT1 = 01;
        this.OSAT1 = null; //por ser estado inicial

        this.CargaSAT2 = problema.SAT2.bateria_maxima;
        this.BandaSAT2 = 23;
        this.OSAT2 = null; //por ser estado inicial

        this.g = 0; //por ser estado inicial
        this.h = calcularCosteHeuristico();
        this.f = calcularCosteF(this.g, this.h);

        this.padre = null; //por ser estado inicial
    }

    //funciones calculo de costes:
    //TODO: Aquí o sacarlo fuera por el coste del nodo?
    int calcularCosteG(int costeNodo){
        int gCalculado = 0;
        gCalculado = padre.g + costeNodo;
        return gCalculado;
    }
    
    
    int calcularCosteHeuristico(){
        int hCalculado = 0;
            //TODO: implementar
        return hCalculado;
    }

    int calcularCosteF(int g, int h){
        return g + h;
    }

    //funcion crear sucesores
    public ArrayList<Estado> crearSucesores (){
        ArrayList<Estado> sucesores = null;
        
        Estado estadoHijo;

        //intentamos ejecutar todas las combinaciones posibles
        for(int i = 0; i < this.problema.ACCIONES.length; i++){ //para SAT1
            for (int j = 0; j < this.problema.ACCIONES.length; i++){ //para SAT2
                estadoHijo = realizarAcciones(this.problema.ACCIONES[i], this.problema.ACCIONES[j]);
            }
            if(!estadoHijo.equals(null)){
                //lo metemos en la arrayList de sucesores
                //creamos los hijos de estados validos
                sucesores.add(estadoHijo);
            }
        }
    }

    private Estado realizarAcciones(String operaciónSAT1, String operaciónSAT2){
        //TODO: Implementar lo de los switch
        Estado estadoHijo = null;

        //Comprobamos precondiciones

        //Si ok:
        estadoHijo = new Estado();
        estadoHijo.padre = this;

        //Aplicamos las postcondiciones efecto de las acciones
        //estadoHijo.blabla.....


        return estadoHijo;
    }

}
