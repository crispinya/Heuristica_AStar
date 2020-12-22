import java.io.IOException;
import java.util.ArrayList;
public class Problem {

    
    ArrayList<MuestraObservacion> muestrasAObservar = new ArrayList<MuestraObservacion>(); //posibles observaciones
    SATelit SAT1; //satelite 1
    SATelit SAT2; //satelite 2

    Estado S; //estado inicial
    String heuristica; //nombre de la heurística con la que se va a resolver: heuristica1, heuristica2 o  nula

    //acciones que puede realizar cada satelite
    final String[] ACCIONES = {"IDLE", "Observa", "Transmite", "Gira", "Carga"};

    //constructor con el problema.prob como parametro
    public Problem(String problema_prob) throws IOException {
       
        this.muestrasAObservar = Parseador.parseMuestra(problema_prob);
        
        this.SAT1 = new SATelit(Parseador.parseSAT(problema_prob, "SAT1"));
        this.SAT2 = new SATelit(Parseador.parseSAT(problema_prob, "SAT2"));
        this.SAT1.bandas_admitidas = new int[][] {{0,1}, {1, 2}};
        this.SAT2.bandas_admitidas = new int[][]  {{2,3}, {1, 2}};
    }

    public boolean isFinal(Estado estadoActual){
        if(this.muestrasAObservar.size() == estadoActual.OT.size()){
            return true; //Al insertar nosotros en OT, el control de errores para evitar repetidos ya está implementado
        }
        return false;
    }
}
