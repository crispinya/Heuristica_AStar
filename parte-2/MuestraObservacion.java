public class MuestraObservacion {
    //Muestra que puede ser observada por un satelite
    
    String id; //id identificativo, O1, O2, O3...

    int banda; //banda a la que se puede observar
    int hora; //hora a la que se puede observar
    
    public MuestraObservacion(String id, int banda, int hora){
        this.id = id;
        this.banda = banda;
        this.hora = hora;
    }

}
