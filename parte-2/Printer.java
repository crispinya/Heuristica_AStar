import java.io.IOException;
import java.util.ArrayList;

public class Printer {

    //Metodo para imprimir la solucion completa de un problema
    public static void printSolution(ArrayList<Estado> caminoFinal, int numExpandidos, long tiempoTranscurrido) throws IOException {

        String tiempo = "Tiempo total: " + tiempoTranscurrido + " milisegundos";
        String costeTotal = "Coste total: " + (caminoFinal.size()-1); //El coste es en horas, al transcurrir una hora cada vez que se genera un nodo el coste es igual a la cantidad de nodos del camino -1
        String longitudPlan = "Longitud del plan: " + (caminoFinal.size()-1); //Se realizan ccaminoFinal - 1 acciones
        String nodosExpandidos = "Nodos expandidos: " + numExpandidos;
        
        FileManager escritor = new FileManager();

        String texto = tiempo + "\n" + costeTotal + "\n" + longitudPlan + "\n" + nodosExpandidos;
        escritor.escribirArchivo("problema.prob.statistics", texto);

        texto = printOperaciones(caminoFinal);
        escritor.escribirArchivo("problema.prob.output", texto);
    }

    //imprimir las operaciones realizadas en un camino
    public static String printOperaciones(ArrayList<Estado> caminoFinal){
        String caminoSeguido = "";
        //Camino seguido. Empezamos en 1 ya que el 0 es el estado inicial;el .operaciones indica con qué operaciones se creó el estado
        for(int i = 1; i < caminoFinal.size(); i++){
            if(i == caminoFinal.size() - 1){
                caminoSeguido = caminoSeguido + i + ". " + caminoFinal.get(i).operaciones; 
            } else{
                caminoSeguido = caminoSeguido + i + ". " + caminoFinal.get(i).operaciones + "\n"; 
            }
        }
        return caminoSeguido;
    }

    //metodo para escribir solucion no encontrada
    public static void printNoSolution() throws IOException {
        
        FileManager escritor = new FileManager();
        escritor.escribirArchivo("problema.prob.statistics", "No se ha encontrado solución al problema.");
        escritor.escribirArchivo("problema.prob.output", "No se ha encontrado solución al problema.");
    }    

    //metodo para imprimir toda la informacion de un estado
    public static String imprimirEstado(Estado estadoImprimir){
        String imprimir = "";

        //observaciones transmitidas a base
        String impOT = "";
        for(int i = 0; i <  estadoImprimir.OT.size(); i++){
            impOT = impOT + " (" + estadoImprimir.OT.get(i).banda + "," + estadoImprimir.OT.get(i).hora + ")"; 
        }

        //banda de cada satelite
        String impBanda1 = "(" + estadoImprimir.BandaSAT1[0] + "," + estadoImprimir.BandaSAT1[1] + ") -"; 
        String impBanda2 = "(" + estadoImprimir.BandaSAT2[0] + "," + estadoImprimir.BandaSAT2[1] + ") - "; 

        //observaciones realizadas pero no transmitidas del SAT1
        String impObvs1 = "";
        for(int i = 0; i <  estadoImprimir.OSAT1.size(); i++){
            impObvs1 = impObvs1 + " (" + estadoImprimir.OSAT1.get(i).banda + "," + estadoImprimir.OSAT1.get(i).hora + ")"; 
        }    
        
        //observaciones realizadas pero no transmitidas del SAT2
        String impObvs2 = "";
        for(int i = 0; i <  estadoImprimir.OSAT2.size(); i++){
            impObvs2 = impObvs2 + " (" + estadoImprimir.OSAT2.get(i).banda + "," + estadoImprimir.OSAT2.get(i).hora + ")"; 
        }
        
        String imprimirSAT1 = "SAT1: Carga " + estadoImprimir.CargaSAT1 + ", banda " + impBanda1 + " Observ " + impObvs1;
        String imprimirSAT2 = "SAT2: Carga " + estadoImprimir.CargaSAT2 + ", banda " + impBanda2 + " Observ "+ impObvs2;
        imprimir = "Hora: " + estadoImprimir.Hour + "; " + imprimirSAT1 + "; " + imprimirSAT2 + "; " + " Observ Trans" + impOT + "; f " + estadoImprimir.f;
        
        return imprimir;
    }

}
