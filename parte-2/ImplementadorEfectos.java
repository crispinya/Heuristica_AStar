import java.util.Arrays;

public class ImplementadorEfectos {
    
    public static void efectosObservar(Estado estadoPadre, Estado estadoHijo, MuestraObservacion muestra, int numSAT){
        //OBSERVA
        if(numSAT == 1){
            estadoHijo.CargaSAT1 = estadoPadre.CargaSAT1 - estadoPadre.problema.SAT1.coste_observar; //gasta energia
            estadoHijo.OSAT1.add(muestra); //efecto guarda una nueva observacion
        }
        if(numSAT == 2){
            estadoHijo.CargaSAT2 = estadoPadre.CargaSAT2 - estadoPadre.problema.SAT2.coste_observar; //gasta energia
            estadoHijo.OSAT2.add(muestra); //efecto guarda una nueva observacion
        }
    }

    public static void efectosTransmitir(Estado estadoPadre, Estado estadoHijo, int numSAT){
        if(numSAT == 1){
            estadoHijo.CargaSAT1 = estadoPadre.CargaSAT1 - estadoPadre.problema.SAT1.coste_transmitir; //gasta energia
            MuestraObservacion astroAux = estadoHijo.OSAT1.get(0);
            estadoHijo.OSAT1.remove(0); //elimina lo observacion de OSAT1
            estadoHijo.OT.add(astroAux); //añade la observacion a OT, observaciones ya transmitidas
        }
        if(numSAT == 2){
            estadoHijo.CargaSAT2 = estadoPadre.CargaSAT2 - estadoPadre.problema.SAT2.coste_transmitir; //gasta energia
            MuestraObservacion astroAux = estadoHijo.OSAT2.get(0);
            estadoHijo.OSAT2.remove(0); //elimina lo observacion de OSAT1
            estadoHijo.OT.add(astroAux); //añade la observacion a OT, observaciones ya transmitidas
        }
    }

    public static void efectosGirar(Estado estadoPadre, Estado estadoHijo, int numSAT){
        //GIRAR

        if(numSAT == 1){
            estadoHijo.CargaSAT1 = estadoPadre.CargaSAT1 - estadoPadre.problema.SAT1.coste_girar; //gasta energia

            //comprobamos que banda tenia el satelite, y asignamos la contraria
            if(Arrays.equals(estadoPadre.BandaSAT1, estadoPadre.problema.SAT1.bandas_admitidas[0])){
                estadoHijo.BandaSAT1 = estadoPadre.problema.SAT1.bandas_admitidas[1];
            } else{
                estadoHijo.BandaSAT1 = estadoPadre.problema.SAT1.bandas_admitidas[0];
            }
        }

        if(numSAT == 2){
            estadoHijo.CargaSAT2 = estadoPadre.CargaSAT2 - estadoPadre.problema.SAT2.coste_girar; //gasta energia

            //comprobamos que banda tenia el satelite, y asignamos la contraria
            if(Arrays.equals(estadoPadre.BandaSAT2, estadoPadre.problema.SAT2.bandas_admitidas[0])){
                estadoHijo.BandaSAT2 = estadoPadre.problema.SAT2.bandas_admitidas[1];
            } else{
                estadoHijo.BandaSAT2 = estadoPadre.problema.SAT2.bandas_admitidas[0];
            }
        }
    }

    public static void efectosCargar(Estado estadoPadre, Estado estadoHijo, int numSAT){
        //CARGAR
        if(numSAT == 1){
            estadoHijo.CargaSAT1 = estadoPadre.CargaSAT1 + estadoPadre.problema.SAT1.beneficio_cargar; //carga energia, sin pasarse de la maxima
            if(estadoHijo.CargaSAT1 > estadoPadre.problema.SAT1.bateria_maxima){
                estadoHijo.CargaSAT1 = estadoPadre.problema.SAT1.bateria_maxima;
            }
        }

        if(numSAT == 2){
            estadoHijo.CargaSAT2 = estadoPadre.CargaSAT2 + estadoPadre.problema.SAT2.beneficio_cargar; //carga energia, sin pasarse de la maxima
            if(estadoHijo.CargaSAT2 > estadoPadre.problema.SAT2.bateria_maxima){
                estadoHijo.CargaSAT2 = estadoPadre.problema.SAT2.bateria_maxima;
            }
        }
    }
    
}
