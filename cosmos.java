//Main of the program
//once exported it should be executed as ./Cosmos.sh <problema.prob> <heuristica>
//main recieves problema and heuristica FIXME: no se why no va
public class cosmos{

    public static void main(String[] args){ //No se puede poner dentro del corchete la longitud de args, para eso usamos control de errores

        //TODO: Control de errores de numero de argumentos.
        System.out.println("Hello world¡");
        //First, we read problema.prob and get the data info
        
        for(int i = 0; i<args.length;i++){
            System.out.println(args[i]);
        }
        //problema = new problema(args[1]); 
    
        //then we setup the heuristic
    
        //create the initial state. Esto sería con h = 0. Cómo crear estados? No necesitan toda la info del problema.
    
    }
}
