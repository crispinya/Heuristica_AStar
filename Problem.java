import java.io.IOException;
import java.util.ArrayList;
public class Problem {

    
    ArrayList<AstroObservacion> astrosAObservar = new ArrayList<AstroObservacion>(); //posibles observaciones
    SATelit SAT1; //satelite 1
    SATelit SAT2; //satelite 2

    Estado S; //estado inicial
    String heuristica; //nombre de la heurística con la que se va a resolver

    //acciones que puede realizar cada satelite
    final String[] ACCIONES = {"IDLE", "Observa", "Transmite", "Gira", "Carga"};

    //constructor con el problema.prob de parametro
    public Problem(String problema_prob) throws IOException {
        
        Parseador problemParser = new Parseador();
       
        this.astrosAObservar = problemParser.parseAstros(problema_prob);
        
        this.SAT1 = new SATelit(problemParser.parseSAT(problema_prob, "SAT1"));
        this.SAT2 = new SATelit(problemParser.parseSAT(problema_prob, "SAT2"));
        this.SAT1.bandas_admitidas = new int[][] {{0,1}, {1, 2}};
        this.SAT2.bandas_admitidas = new int[][]  {{2,3}, {1, 2}};
    }

    public boolean isFinal(Estado estadoActual){
        if(this.astrosAObservar.size() == estadoActual.OT.size()){
            return true; //Al insertar nosotros en OT, el control de errores para evitar repetidos ya está implementado
        }
        return false;
    }
}
