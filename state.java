public class state {
    
    String OT[]; //Observations already transmited to main base
    int Hour; //current time

    int CSAT1; //Charge (Carga) of the batery of CSAT1
    int BSAT1; //Observation Band of the SAT1
    String OSAT1[]; //Observations done and not yet transmited by SAT1

    int CSAT2; //Charge (Carga) of the batery of CSAT2
    int BSAT2; //Observation Band of the SAT2
    String OSAT2[]; //Observations done and not yet transmited by SAT2
    
    int g, f; //valores calculados, TODO: definirlos mejor
    //TODO: anadir puntero a padre

    public state(){
        //igual esto con un cosntructor vacio va bien
    }

}
