public class satelite {
    
    int coste_observar;
    int coste_transmitir;
    int coste_girar;
    int beneficio_cargar;
    int bateria_disponible; //FIXME: No se si se refiere a esto
    //String[] observaciones_realizadas; // TODO: Esto creo que no iría aquí, es de estado

    public satelite(int[] dataSAT){
        this.coste_observar = dataSAT[0];
        this.coste_transmitir = dataSAT[1];
        this.coste_girar = dataSAT[2];
        this.beneficio_cargar = dataSAT[3];
        this.bateria_disponible = dataSAT[4];
    }
}
