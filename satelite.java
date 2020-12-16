public class Satelite {
    
    int coste_observar;
    int coste_transmitir;
    int coste_girar;
    int beneficio_cargar;
    int bateria_maxima;

    public Satelite (int[] dataSAT){
        this.coste_observar = dataSAT[0];
        this.coste_transmitir = dataSAT[1];
        this.coste_girar = dataSAT[2];
        this.beneficio_cargar = dataSAT[3];
        this.bateria_maxima = dataSAT[4];
    }
}
