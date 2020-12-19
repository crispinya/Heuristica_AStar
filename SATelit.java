public class SATelit {
    int[][] bandas_admitidas; //depende del SAT. SAT1 es (0,1) y (1, 2). SAT2 es (2, 3) y (1, 2)
    int coste_observar; 
    int coste_transmitir;
    int coste_girar;
    int beneficio_cargar;
    int bateria_maxima;

    public SATelit(int[] dataSAT){
        this.coste_observar = dataSAT[0];
        this.coste_transmitir = dataSAT[1];
        this.coste_girar = dataSAT[2];
        this.beneficio_cargar = dataSAT[3];
        this.bateria_maxima = dataSAT[4];
    }
}
