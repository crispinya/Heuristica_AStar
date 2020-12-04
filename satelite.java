public class satelite {
    
    int coste_observar;
    int coste_transmitir;
    int coste_girar;
    int beneficio_cargar;
    int bateria_disponible; //FIXME: No se si se refiere a esto
    String[] observaciones_realizadas; //TODO: Ver si definir como array list o le que, pq crecerá

    public satelite(int obvs, int trans, int giro, int carga, int bateria){
        //TODO: Check control de errores de numeros negativos
        this.coste_observar = obvs;
        this.coste_transmitir = trans;
        this.coste_girar = giro;
        this.beneficio_cargar = carga;
        this.bateria_disponible = bateria;
    }
}
