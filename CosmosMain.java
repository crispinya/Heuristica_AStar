//Main of the program
//once exported it should be executed as ./Cosmos.sh <problema.prob> <heuristica>
//javac *.java crea las clases
import java.io.IOException;
import java.util.ArrayList;

public class CosmosMain{
    public static void main(String[] args) throws IOException { 

        //Control de errores de numero de argumentos.
        if(args.length != 2){
            throw new RuntimeException("Error: El numero de parametros debe ser igual a 2. Ejecute el programa como ./Cosmos.sh <problema.prob> <heuristica>");
        }
        
        //Leemos el .prob pasado como parametro y creamos un objeto Problema con la información
        Problem problema = new Problem(args[0]);

        //Configuramos la heurística, cuyos valores son "heuristica1" o "heuristica2"
        if(args[1].equals("heuristica1") || args[1].equals("heuristica2")){
            problema.heuristica = args[1];
        } else{
            System.out.println("Error, heuristica no valida. Se ejecutará con h=0 para todo nodo");
            problema.heuristica = "";
        }
     
        //Creamos el estado inicial del problema
        problema.S = new Estado(problema);

        //ejecutamos el algoritmo A*
        AStar(problema);

    }

    //Algoritmo A*
    public static void AStar(Problem problema) throws IOException {
        
        //tomamos tiempos
        long tiempoInicio,tiempoFinal,tiempoTranscurrido;
        tiempoInicio=System.currentTimeMillis();

        //contabilizamos la cantidad de nodos expandidos
        int numExpandidos = 0;

        //creamos las listas de abierta y cerrada
        ArrayList<Estado> abierta = new ArrayList<Estado>();
        ArrayList<Estado> cerrada = new ArrayList<Estado>();

        //añadimos a abierta el estado inicial
        abierta.add(problema.S);

        boolean exito = false;
        Estado nodoEscogido = null;
        
        //ejecutamos el algoritmo mientras queden nodos en abierta o hasta que se encuentre la solución
        while(!abierta.isEmpty() && !exito){
            //expandimos el primer nodo de abierta
            nodoEscogido = abierta.get(0);
            abierta.remove(0);
            numExpandidos ++;
            //System.out.println("EXPANDIDO: " + Printer.imprimirEstado(nodoEscogido));
            
            //comprobamos si es estado final
            if(problema.isFinal(nodoEscogido)){
                exito = true;
            }
            
            //en caso contrario, generamos sus sucesores
            else{
                cerrada.add(nodoEscogido);
                ArrayList<Estado> sucesores = nodoEscogido.crearSucesores(); // El calculo de sucesores esta implementado dentro de la clase Estado
                
                for(int i=0; i<sucesores.size(); i++){

                    //si el sucesor esta en cerrada, se ignora
                    if(getIndiceNodoEnLista(sucesores.get(i), cerrada) != -1) {

                    // si esta en abierta, y f del nuevo sucesor es mejor, se elimina el que ya estaba en abierta
                    } else if (getIndiceNodoEnLista(sucesores.get(i), abierta) != -1){
                        int indiceAbierta = getIndiceNodoEnLista(sucesores.get(i), abierta);
                        if(abierta.get(indiceAbierta).f > sucesores.get(i).f){
                            abierta.remove(i); //eliminamos el anterior nodo igual
                            abierta = insertarOrden(sucesores.get(i), abierta);//insertamos el nuevo de forma ordenada en abierta
                        }

                    // si no esta ni en abierta ni en cerrada, se inserta en orden en abierta
                    } else{
                        abierta = insertarOrden(sucesores.get(i), abierta);
                    } 
                }
            }
        }

        if (exito) {
            tiempoFinal = System.currentTimeMillis();
            tiempoTranscurrido = tiempoFinal - tiempoInicio;

            //calculamos el camino final
            ArrayList<Estado> caminoFinal = calcularCaminoFinal(nodoEscogido);

            //Se ha encontrado solucion, se crean los archivo resultado
            Printer.printSolution(caminoFinal, numExpandidos, tiempoTranscurrido);

        } else{
            // No exite solución o no se puede encontrar
            Printer.printNoSolution();   
        }       
    }

    //metodo para obtener la posicion de un estado en una lista
    private static int getIndiceNodoEnLista(Estado sucesor, ArrayList<Estado> lista) {
        for(int i = 0; i < lista.size(); i++){
            Estado nodoLista = lista.get(i);
            if(sucesor.OT.equals(nodoLista.OT)
            && sucesor.CargaSAT1 == nodoLista.CargaSAT1
            && sucesor.CargaSAT2 == nodoLista.CargaSAT2
            && sucesor.BandaSAT1.equals(nodoLista.BandaSAT1)
            && sucesor.BandaSAT2.equals(nodoLista.BandaSAT2)
            && sucesor.OSAT1.equals(nodoLista.OSAT1)
            && sucesor.OSAT2.equals(nodoLista.OSAT2)
            && sucesor.Hour == nodoLista.Hour){
                return i; //devuelve la posicion en la lista
            }
        }
        return -1;
    }

    //metodo para insertar un estado de forma ordenada
    private static ArrayList<Estado> insertarOrden(Estado estadoInsertar, ArrayList<Estado> lista){
        //ordenamos de menor a mayor f. Si la f es del mismo tamaño, la insertamos despues
        int fInsertar = estadoInsertar.f;
        boolean insertado = false;

        for(int i = 0; i < lista.size() && !insertado; i++){
            if(lista.get(i).f > fInsertar){
                lista.add(i, estadoInsertar);
                insertado = true;
            }
        }

        if(!insertado){
            lista.add(estadoInsertar);
        }

        return lista;
    }

    //metodo para calcular el camino desde un estado inicial a un estado final
    private static ArrayList<Estado> calcularCaminoFinal(Estado estadoFinal){
        //recuperamos los nodos y se guardan en orden
        ArrayList<Estado> camino = new ArrayList<Estado>();
        Estado estadoAux = estadoFinal;

        while(estadoAux != null){
            camino.add(0, estadoAux);
            estadoAux = estadoAux.padre;
        }

        return camino;
    }   
}