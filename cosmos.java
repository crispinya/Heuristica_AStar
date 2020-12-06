//Main of the program
//once exported it should be executed as ./Cosmos.sh <problema.prob> <heuristica>
//main recieves problema and heuristica FIXME: no se why no va
//javac *.java crea las clases
import java.io.IOException;

public class cosmos{
    public static void main(String[] args) throws IOException { // No se puede poner dentro del corchete la longitud de
                                                                // args, para eso usamos control de errores

        //TODO: Control de errores de numero de argumentos.
        /*if(args.length != 2){
            throw new RuntimeException("Error: El numero de parametros debe ser igual a 2. Ejecute el programa como ./Cosmos.sh <problema.prob> <heuristica>");
        }*/
        //First, we read problema.prob and get the data info

        Problema problema = new Problema("problema.prob"); 
        System.out.print(problema.SAT2.bateria_maxima);
    
        //then we setup the heuristic
    
        //create the initial state. Esto sería con h = 0. Cómo crear estados? No necesitan toda la info del problema.
    
    }
}
