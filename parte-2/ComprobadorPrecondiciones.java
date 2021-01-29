public class ComprobadorPrecondiciones {
    
    public static boolean puedeObservar(Estado estado, String operacionSAT, MuestraObservacion muestra, int numSAT){
        
        //(condicion hora) && (bateria suficiente) && (hora astro correcta) &&(astro en su banda de obvs) && (obvs no realizada ni enviada ya)
        if(numSAT == 1 && (estado.Hour >= 0 && estado.Hour < 12) && (estado.CargaSAT1 >= estado.problema.SAT1.coste_observar) && estado.Hour == muestra.hora
            && (muestra.banda == estado.BandaSAT1[0] || muestra.banda == estado.BandaSAT1[1]) && (!estado.OSAT1.contains(muestra) && !estado.OSAT2.contains(muestra) && !estado.OT.contains(muestra))){
            return true;
        }

        if(numSAT == 2 && (estado.Hour >= 0 && estado.Hour < 12) && (estado.CargaSAT2 >= estado.problema.SAT2.coste_observar) && estado.Hour == muestra.hora
            && (muestra.banda == estado.BandaSAT2[0] || muestra.banda == estado.BandaSAT2[1]) && (!estado.OSAT1.contains(muestra) && !estado.OSAT2.contains(muestra) && !estado.OT.contains(muestra))){
            return true;
        }

        return false;
    }

    public static boolean puedeTransmitir(Estado estado, String operacionSAT, int numSAT){
        //TRANSMITE. Se transmite la muestras que se haya observado hace mas tiempo
        //(condicion hora) && (bateria suficiente) && (condicion de que haya observaciones)
        if(numSAT == 1 && (estado.Hour >= 0 && estado.Hour < 12) && (estado.CargaSAT1 >= estado.problema.SAT1.coste_transmitir) && (estado.OSAT1.size() > 0)){
            return true;
        }

        if(numSAT == 2 && (estado.Hour >= 0 && estado.Hour < 12) && (estado.CargaSAT2 >= estado.problema.SAT2.coste_transmitir) && (estado.OSAT2.size() > 0)){
            return true;
        }        

        return false;
    }

    public static boolean puedeGirar(Estado estado, String operacionSAT, int numSAT){
        //GIRA
        //(condicion hora) && (bateria suficiente)
        if(numSAT == 1 && (estado.Hour >= 0 && estado.Hour < 12) && (estado.CargaSAT1 >= estado.problema.SAT1.coste_girar)){
            return true;
        }

        if(numSAT == 2 && (estado.Hour >= 0 && estado.Hour < 12) && (estado.CargaSAT2 >= estado.problema.SAT2.coste_girar)){
            return true;
        }

        return false;
    }

    public static boolean puedeCargar(Estado estado, String operacionSAT, int numSAT){
        //CARGA
        //(condicion hora) && (condicion bateria no cargada completamente)
        if(numSAT == 1 && (estado.Hour >= 0 && estado.Hour < 12) && (estado.CargaSAT1 < estado.problema.SAT1.bateria_maxima)){
            return true;
        }

        if(numSAT == 2 && (estado.Hour >= 0 && estado.Hour < 12) && (estado.CargaSAT2 < estado.problema.SAT2.bateria_maxima)){
            return true;
        }

        return false;
    }

}
