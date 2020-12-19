//Main of the program
//once exported it should be executed as ./Cosmos.sh <problema.prob> <heuristica>
//main recieves problema and heuristica FIXME: no se why no va
//javac *.java crea las clases
import java.io.IOException;
import java.util.ArrayList;

public class CosmosMain{
    public static void main(String[] args) throws IOException { // No se puede poner dentro del corchete la longitud de
                                                                // args, para eso usamos control de errores

        //Control de errores de numero de argumentos.
        if(args.length != 2){
            throw new RuntimeException("Error: El numero de parametros debe ser igual a 2. Ejecute el programa como ./Cosmos.sh <problema.prob> <heuristica>");
        }
        
        //First, we read problema.prob and get the data info, storing it in a problem
        Problem problema = new Problem(args[0]);

        //we set the initial state
        problema.S = new Estado(problema);

        //then we setup the heuristic
    
        //Esto sería con h = 0. Cómo crear estados? No necesitan toda la info del problema.
        AStar(problema);

    }

    //TODO: Check datos iniciales por si tienen errores
    public static boolean AStar(Problem problema){
        ArrayList<Estado> abierta = new ArrayList<Estado>(); //lista abierta
        ArrayList<Estado> cerrada = new ArrayList<Estado>(); //lista cerrada
        boolean exito = false;  //Booleano exito, que nos dira si se llega a un estado final o no
        Estado estadoInicial = problema.S;
        abierta.add(estadoInicial);

        while(!abierta.isEmpty() || !exito){
            Estado nodoEscogido = nodoAExpandir(abierta, cerrada); //buscamos primer nodo de abierta que no este en cerrada
            System.out.println("EXPANDIDO: " + nodoEscogido.imprimirEstado());

            if(isEstadoFinal(nodoEscogido, problema)) exito = true;//Si es estado final significa que hemos encontrado la solucion
            
            else{
                abierta.remove(nodoEscogido); //Sacamos el nodo de abierta y lo introducimos en cerrada
                cerrada.add(nodoEscogido);
                ArrayList<Estado> sucesores = nodoEscogido.crearSucesores(); //Crear los sucesores del nodo escogido
                
                //Insertamos los sucesores en su posicion correspondiente de la lista abierta
                for(int i=0; i<sucesores.size(); i++){
                    Estado sucesorAInsertar = sucesores.get(i); 
                    abierta = ordenarEstados(abierta, sucesorAInsertar);
                }
            }   
        }
        return exito; //TODO: si es true, devuelve el camino desde el nodo final al inicio
        //El calculo de sucesores esta implementado dentro de la definicion de Estado
    }

    private static boolean isEstadoFinal (Estado estadoActual, Problem problema){
        return problema.isFinal(estadoActual);
    }

    private static ArrayList<Estado> ordenarEstados (ArrayList<Estado> listaAbierta, Estado estadoAInsertar ){ //Ordena los sucesores obtenidos dentro de la lista final de abierta
        boolean insertado = false;
        for(int i=0; i<listaAbierta.size(); i++){
            if(estadoAInsertar.f<listaAbierta.get(i).f){
                listaAbierta.add(i,estadoAInsertar);
                insertado=true;
            }
        }
        if(!insertado) listaAbierta.add(estadoAInsertar);
        return listaAbierta;
    }

    //buscamos el primer nodo de abierta que no este en cerrada
    private static Estado nodoAExpandir (ArrayList<Estado> listaAbierta, ArrayList<Estado> listaCerrada){
        for(int i=0; i<listaAbierta.size();i++){
            boolean encontrado = false;
            Estado nodoListaAbierta = listaAbierta.get(i);
            for(int j=0; !encontrado && j<listaCerrada.size();j++){
                Estado nodoListaCerrada = listaCerrada.get(j);
                //comprobamos que no haya un nodo igual en cerrada
                if(nodoListaAbierta.OT.equals(nodoListaCerrada.OT) && nodoListaAbierta.CargaSAT1==nodoListaCerrada.CargaSAT1 && 
                    nodoListaAbierta.CargaSAT2==nodoListaCerrada.CargaSAT2 && nodoListaAbierta.BandaSAT1.equals(nodoListaCerrada.BandaSAT1) && 
                    nodoListaAbierta.BandaSAT2.equals(nodoListaCerrada.BandaSAT2) && nodoListaAbierta.OSAT1.equals(nodoListaCerrada.OSAT1) && 
                    nodoListaAbierta.OSAT2.equals(nodoListaCerrada.OSAT2)){ //FIXME: Ver si se puede hacer mejor
                    encontrado = true;
                }
            }
            if(!encontrado) return nodoListaAbierta;
        }
        return null;
    }
    
}