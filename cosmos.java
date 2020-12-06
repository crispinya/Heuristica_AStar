//Main of the program
//once exported it should be executed as ./Cosmos.sh <problema.prob> <heuristica>
//main recieves problema and heuristica FIXME: no se why no va
import java.io.IOException;

public class cosmos{

   //private static int costeSAT1[] = new int[5];
   //private static int costeSAT2[] = new int[5];

    /*public static void parseFiles(String path) throws IOException {
        String infoSAT1[] = new String[5]; //Obtendremos los costes de cada satelite por el fichero leido
        String infoSAT2[] = new String[5];
        String linea = "";
        FileReader fichero = new FileReader(path); //Abrimos el fichero, si no lo encuentra saltara un error
        BufferedReader buffer = new BufferedReader(fichero);
        while((linea = buffer.readLine()) != null){  //Abrimos fichero, y un buffer para leer linea a linea lo que tenemos
            String tipoInformacion = linea.substring(0,4);
            if(tipoInformacion.equals("OBS:")){ //Miramos si estamos en la linea de observacion, de costes de SAT1 o de SAT2
                //TODO: WTF como pasamos esto
            }else if(tipoInformacion.equals("SAT1")){
                infoSAT1 = extractInformation(linea, 6); //El 6 es la posicion donde empiezan a aparecer los datos en SAT, en OBS empieza en 5
                costeSAT1 = convertToInt(infoSAT1);
            }else if(tipoInformacion.equals("SAT2")){
                infoSAT2 = extractInformation(linea, 6);
                costeSAT2 = convertToInt(infoSAT2);
            }else{ //Si no es ninguna de estas opciones, significa que el fichero contiene informacion erronea y por tanto no nos sirve
                buffer.close();
                throw new RuntimeException("Error: la especificacion de datos es erronea");
            }
        }
        buffer.close();
        fichero.close();
    }^*/

 /*   private static String[] extractInformation(String line, int substring){ //Extraemos la informacion de cada linea en forma de string. Cada posicion del array será cada uno de los costes
        String inicioLectura = line.substring(substring);
        return inicioLectura.split(";");
    }

    private static int[] convertToInt (String[] information){ //Parseamos cada string a un integer, para manipular con mayor facilidad los datos obtenidos
        int[] result = new int[information.length];
        for(int i=0 ; i< information.length;i++){
            try{
                result[i]= Integer.parseInt(information[i]); //Convertimos cada posicion en un entero
                
            }catch(Exception ex){
                throw new RuntimeException("Error: El dato a convertir no es de tipo entero"); //Si lo que hay no es un entero, marcara error
            }
            if(result[i]<0){
                throw new RuntimeException("Error: El entero a pasar debe ser mayor o igual que 0"); //El coste debe ser como mínimo 0
            }
        }
        return result;
    }
*/

    public static void main(String[] args) throws IOException { // No se puede poner dentro del corchete la longitud de
                                                                // args, para eso usamos control de errores

        //TODO: Control de errores de numero de argumentos.
        if(args.length != 2){
            throw new RuntimeException("Error: El numero de parametros debe ser igual a 2. Ejecute el programa como ./Cosmos.sh <problema.prob> <heuristica>");
        }
        //First, we read problema.prob and get the data info
        //parseFiles(args[0]);


        /*for(int i = 0; i<costeSAT1.length;i++){
            System.out.print(costeSAT1[i]);
        }
        System.out.println("\n");
        for(int i = 0; i<costeSAT2.length;i++){
            System.out.print(costeSAT2[i]);
        }*/

        problema problema = new problema(args[0]); 
        //esto para probar:
        System.out.print(problema.SAT1.bateria_disponible);
    
        //then we setup the heuristic
    
        //create the initial state. Esto sería con h = 0. Cómo crear estados? No necesitan toda la info del problema.
    
    }
}
