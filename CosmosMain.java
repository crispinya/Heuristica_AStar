//Main of the program
//once exported it should be executed as ./Cosmos.sh <problema.prob> <heuristica>
//main recieves problema and heuristica FIXME: no se why no va
//javac *.java crea las clases
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CosmosMain{
    public static void main(String[] args) throws IOException { // No se puede poner dentro del corchete la longitud de
                                                                // args, para eso usamos control de errores

        //Control de errores de numero de argumentos.
        if(args.length != 2){
            throw new RuntimeException("Error: El numero de parametros debe ser igual a 2. Ejecute el programa como ./Cosmos.sh <problema.prob> <heuristica>");
        }
        
        //First, we read problema.prob and get the data info, storing it in a problem
        Problem problema = new Problem(args[0]); 
        problema.S = new Estado(problema);

        //we set the initial state
        problema.S = new Estado(problema);

        //then we setup the heuristic
    
        //create the initial state. Esto sería con h = 0. Cómo crear estados? No necesitan toda la info del problema.
    

    }

    public void AStar(Problem problema){
        ArrayList<Estado> abierta = new ArrayList<Estado>(); //lista abierta
        ArrayList<Estado> cerrada = new ArrayList<Estado>(); //lista cerrada

        Estado estadoInicial = problema.S;

        //El calculo de sucesores esta implementado dentro de la definicion de Estado

    }


    private boolean isEstadoFinal (Estado estadoActual, Problem problema){
        return problema.isFinal(estadoActual);
    }
}