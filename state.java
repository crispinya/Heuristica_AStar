public class state {
    
    Problema problema;
    
    Astro OT[]; //Observations already transmited to main base
    int Hour; //current time

    int CargaSAT1; //Charge (Carga) of the batery of CSAT1
    int BandaSAT1; //Observation Band of the SAT1
    Astro OSAT1[]; //Observations done and not yet transmited by SAT1

    int CargaSAT2; //Charge (Carga) of the batery of CSAT2
    int BandaSAT2; //Observation Band of the SAT2
    Astro OSAT2[]; //Observations done and not yet transmited by SAT2
    
    int g, f, h; //valores calculados, TODO: definirlos mejor
    //TODO: anadir puntero a padre

    //to create the initial state
    public state(Problema problema){
        this.problema = problema;

        Hour = 0;
        CargaSAT1 = problema.SAT1.bateria_maxima;
        BandaSAT1 = 01;
        OSAT1 = null;

        CargaSAT2 = problema.SAT2.bateria_maxima;
        BandaSAT2 = 23;
        OSAT2 = null;
    }

}
