public class State {
    
    Problema problema;
    
    Astro OT[]; //Observations already transmited to main base
    int Hour; //current time

    int CargaSAT1; //Charge (Carga) of the batery of CSAT1
    int BandaSAT1; //Observation Band of the SAT1
    Astro OSAT1[]; //Observations done and not yet transmited by SAT1

    int CargaSAT2; //Charge (Carga) of the batery of CSAT2
    int BandaSAT2; //Observation Band of the SAT2
    Astro OSAT2[]; //Observations done and not yet transmited by SAT2
    
    //valores a calcular sobre el estado
    int g; // Coste desde el estado S inicial 
    int h; // Coste heurístico desde este estado hasta algún estado final t
    int f; //suma de f y h 
    
    //TODO: anadir puntero a padre

    //to create the initial state
    public State(Problema problema){
        this.problema = problema;

        this.Hour = 0;
        this.CargaSAT1 = problema.SAT1.bateria_maxima;
        this.BandaSAT1 = 01;
        this.OSAT1 = null;

        this.CargaSAT2 = problema.SAT2.bateria_maxima;
        this.BandaSAT2 = 23;
        this.OSAT2 = null;

        this.g = 0;
        this.h = calcularCosteHeuristico();
        this.f = calcularCosteF(this.g, this.h);
    }



    //funciones calculo de costes:

    int calcularCosteG(){
        int gCalculado = 0;
            //TODO: implementar
            // igual a g del padre + coste arco padre-estado (depende del tipo de funcion)
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

}
