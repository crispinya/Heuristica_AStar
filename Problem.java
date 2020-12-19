import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
public class Problem {

    // Definition of the problem given as a problema.prob file

    ArrayList<AstroObservacion> astrosAObservar = new ArrayList<AstroObservacion>(); //posibles observaciones
    SATelit SAT1;
    SATelit SAT2;

    Estado S;

    //acciones que puede realizar cada satelite
    final String[] ACCIONES = {"IDLE", "Observa", "Transmite", "Gira", "Carga"};

    public Problem(String problema_prob) throws IOException {
        
        Parseador problemParser = new Parseador();
        
        this.astrosAObservar = problemParser.parseAstros(problema_prob);

        this.SAT1 = new SATelit(problemParser.parseSAT(problema_prob, "SAT1"));
        this.SAT2 = new SATelit(problemParser.parseSAT(problema_prob, "SAT2"));
        this.SAT1.bandas_admitidas = new int[][] {{0,1}, {1, 2}};
        this.SAT2.bandas_admitidas = new int[][]  {{2,3}, {1, 2}};
        
    }

    public boolean isFinal(Estado estadoActual){
        //devuelve true si ya se han transmitido todas las observaciones
        return this.astrosAObservar.equals(estadoActual.OT); 
    }
}
