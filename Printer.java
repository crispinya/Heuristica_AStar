import java.io.IOException;
import java.util.ArrayList;

public class Printer {

    public void printSolution(ArrayList<Estado> caminoFinal, int numExpandidos, long tiempoTranscurrido) throws IOException {

        String tiempo = "Tiempo total: " + tiempoTranscurrido + " milisegundos";
        String costeTotal = "Coste total: " + (caminoFinal.size()-1); //FIXME: Esto es..? Es -1 pq camino final incluye al nodo inicial
        String longitudPlan = "Longitud del plan: " + (caminoFinal.size()-1); //FIXME: Esto es...? Es -1 pq camino final incluye al nodo inicial
        String nodosExpandidos = "Nodos expandidos: " + numExpandidos;
        
        FileManager escritor = new FileManager();

        String texto = tiempo + "\n" + costeTotal + "\n" + longitudPlan + "\n" + nodosExpandidos;
        escritor.escribirArchivo("problema.prob.statistics", texto);

        texto = printOperaciones(caminoFinal);
        escritor.escribirArchivo("problema.prob.output", texto);
    }

    public static String printOperaciones(ArrayList<Estado> caminoFinal){
        String caminoSeguido = "";
        //Camino seguido. Empezamos en 1 ya que el 0 es el estado inicial, y el .operaciones indica con qué operaciones se creó el estado
        for(int i = 1; i < caminoFinal.size(); i++){
            if(i == caminoFinal.size() - 1){
                caminoSeguido = caminoSeguido + i + ". " + caminoFinal.get(i).operaciones; 
            } else{
                caminoSeguido = caminoSeguido + i + ". " + caminoFinal.get(i).operaciones + "\n"; 
            }
            //caminoSeguido = caminoSeguido + i + ". " + caminoFinal.get(i).operaciones + " " + caminoFinal.get(i).imprimirEstado() + "\n"; //FIXME: borrar
        }
        return caminoSeguido;
    }

}
