//Main of the program
//once exported it should be executed as ./Cosmos.sh <problema.prob> <heuristica>
//javac *.java crea las clases
import java.io.IOException;
import java.util.ArrayList;

public class copia_main{
    public static void main(String[] args) throws IOException { 

        //Control de errores de numero de argumentos.
        if(args.length != 2){
            throw new RuntimeException("Error: El numero de parametros debe ser igual a 2. Ejecute el programa como ./Cosmos.sh <problema.prob> <heuristica>");
        }
        
        //First, we read problema.prob and get the data info, storing it in a problem
        Problem problema = new Problem(args[0]);

        //we set the initial state
        problema.S = new Estado(problema);
        problema.heuristica = "";

        //Configuramos la heurística, cuyos valores son "heuristica1" o "heuristica2" TODO:
        /*if(args[1].equals("heuristica1") || args[1].equals("heuristica2")){
            problema.heuristica = args[1];
        } else{
            System.out.println("Error, heuristica no valida. Se ejecutará con h=0 para todo nodo");
            problema.heuristica = "heuristica nula";
        }*/
     
        AStar(problema);

    }

    public static void AStar(Problem problema) throws IOException {
        //tomamos tiempos
        long tiempoInicio,tiempoFinal,tiempoTranscurrido;
        tiempoInicio=System.currentTimeMillis();

        ArrayList<Estado> abierta = new ArrayList<Estado>(); //lista abierta
        ArrayList<Estado> cerrada = new ArrayList<Estado>(); //lista cerrada

        Estado estadoInicial = problema.S;
        Estado nodoEscogido = null;
        int numExpandidos = 0; //Cantidad de nodos expandidos
        abierta.add(estadoInicial);
        boolean exito = false;

        while(!abierta.isEmpty() && !exito){
            nodoEscogido = abierta.get(0);
            abierta.remove(0);
            
            /*if(isEstadoFinal(nodoEscogido, problema)){//Si es estado final significa que hemos encontrado la solucion
                exito = true;
            }*/

            System.out.println("EXPANDIDO: " + nodoEscogido.imprimirEstado());
            numExpandidos ++; //expandimos otro nodo

            if(isEstadoFinal(nodoEscogido, problema)){
                exito = true;
            }
            
            else{
                cerrada.add(nodoEscogido);
                ArrayList<Estado> sucesores = nodoEscogido.crearSucesores(); //Crear los sucesores del nodo escogido
                
                for(int i=0; i<sucesores.size(); i++){
                    if(getIndiceNodoEnLista(sucesores.get(i), cerrada) != -1) {
                        //si s esta en cerrada, se ignora
                    } else if (getIndiceNodoEnLista(sucesores.get(i), abierta) != -1){
                        // si esta en abierta, y f de s es mejor, se elimina el que ya estaba en abierta
                        int indiceAbierta = getIndiceNodoEnLista(sucesores.get(i), abierta);
                        if(abierta.get(indiceAbierta).f > sucesores.get(i).f){
                            abierta.remove(i); //eliminamos el anterior
                            abierta = insertarOrden(sucesores.get(i), abierta);//insertamos el nuevo de forma ordenada en abierta
                        }

                    } else{
                        abierta = insertarOrden(sucesores.get(i), abierta);
                        // si s no esta ni en abierta ni en cerrada, se inserta en orden en abierta
                    } 
                }
            }
        }

        if (exito) {
            tiempoFinal = System.currentTimeMillis();
            tiempoTranscurrido = tiempoFinal - tiempoInicio;

            //calculamos el camino final
            ArrayList<Estado> caminoFinal = calcularCaminoFinal(nodoEscogido);

            // Se ha encontrado solucion, se imprime el resultado
            Printer impresora = new Printer();
            impresora.printSolution(caminoFinal, numExpandidos, tiempoTranscurrido);
        }

        // No exite solución o no se puede encontrar
        // El calculo de sucesores esta implementado dentro de la clase Estado
    }

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

    private static ArrayList<Estado> insertarOrden(Estado estadoInsertar, ArrayList<Estado> lista){
        //ordenamos de menor a mayor f. Si la f es del mismo tamanio, la insertamos despues
        int fInsertar = estadoInsertar.f;
        int indice = 0;
        
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).f > fInsertar){
                break;
            }
            indice ++; 
        }

        lista.add(indice, estadoInsertar);
        return lista;
    }



    //TODO: Check datos iniciales por si tienen errores
    /*public static boolean AStar(Problem problema) throws IOException {
        //tomamos tiempos
        long tiempoInicio,tiempoFinal,tiempoTranscurrido;
        tiempoInicio=System.currentTimeMillis();

        ArrayList<Estado> abierta = new ArrayList<Estado>(); //lista abierta
        ArrayList<Estado> cerrada = new ArrayList<Estado>(); //lista cerrada

        Estado estadoInicial = problema.S;
        Estado nodoEscogido = null;
        int numExpandidos = 0; //Cantidad de nodos expandidos
        abierta.add(estadoInicial);
        boolean exito = false;

        while(!abierta.isEmpty() || !exito){
            nodoEscogido = nodoAExpandir(abierta, cerrada); //buscamos primer nodo de abierta que no este en cerrada
            System.out.println("EXPANDIDO: " + nodoEscogido.imprimirEstado());

            if(isEstadoFinal(nodoEscogido, problema)){//Si es estado final significa que hemos encontrado la solucion
                exito = true;
            }
            
            else{
                numExpandidos ++; //expandimos otro nodo
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

        if(exito){
            tiempoFinal= System.currentTimeMillis();
            tiempoTranscurrido= tiempoFinal - tiempoInicio;
        
            //Se ha encontrado solucion, se imprime el resultado
            printSolution(nodoEscogido, numExpandidos, tiempoTranscurrido);
            return true;
        }

        return false; // No exite solución o no se puede encontrar
        // El calculo de sucesores esta implementado dentro de la clase Estado
    }
    */

    private static boolean isEstadoFinal(Estado estadoActual, Problem problema) {
        return problema.isFinal(estadoActual);
    }

    //Ordena los sucesores obtenidos dentro de la lista final abierta
    /*private static ArrayList<Estado> ordenarEstados(ArrayList<Estado> listaAbierta, Estado estadoAInsertar) {
        boolean insertado = false;
        for (int i = 0; i < listaAbierta.size(); i++) {
            if (estadoAInsertar.f < listaAbierta.get(i).f) {
                listaAbierta.add(i, estadoAInsertar);
                insertado = true;
            }
        }
        if (!insertado)
            listaAbierta.add(estadoAInsertar);
        return listaAbierta;
    }*/

    // buscamos el primer nodo de abierta que no este en cerrada
    /*private static Estado nodoAExpandir(ArrayList<Estado> listaAbierta, ArrayList<Estado> listaCerrada) {
        for (int i = 0; i < listaAbierta.size(); i++) {
            boolean encontrado = false;
            Estado nodoListaAbierta = listaAbierta.get(i);
            for (int j = 0; !encontrado && j < listaCerrada.size(); j++) {
                Estado nodoListaCerrada = listaCerrada.get(j);
                // comprobamos que no haya un nodo igual en cerrada
                if (nodoListaAbierta.OT.equals(nodoListaCerrada.OT)
                        && nodoListaAbierta.CargaSAT1 == nodoListaCerrada.CargaSAT1
                        && nodoListaAbierta.CargaSAT2 == nodoListaCerrada.CargaSAT2
                        && nodoListaAbierta.BandaSAT1.equals(nodoListaCerrada.BandaSAT1)
                        && nodoListaAbierta.BandaSAT2.equals(nodoListaCerrada.BandaSAT2)
                        && nodoListaAbierta.OSAT1.equals(nodoListaCerrada.OSAT1)
                        && nodoListaAbierta.OSAT2.equals(nodoListaCerrada.OSAT2)
                        && nodoListaAbierta.Hour == nodoListaCerrada.Hour) { // FIXME: Ver si se puede hacer mejor
                    encontrado = true;
                    listaAbierta.remove(i); //eliminamos de abierta el nodo que está en cerrada
                }
            }
            if (!encontrado)
                return nodoListaAbierta;
        }
        return null;
    }*/

    
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