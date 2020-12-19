import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
public class Problem {

    // Definition of the problem given as a problema.prob file

    ArrayList<AstroObservacion> astros = new ArrayList<AstroObservacion>(); //posibles observaciones
    SATelit SAT1;
    SATelit SAT2;

    Estado S;

    //acciones que puede realizar cada satelite
    final String[] ACCIONES = {"IDLE", "Observa", "Transmite", "Gira", "Carga"};

    public Problem(String problema_prob) throws IOException {
        
        Parseador problemParser = new Parseador();
        
        this.astros = problemParser.parseAstros(problema_prob);

        this.SAT1 = new SATelit(problemParser.parseSAT(problema_prob, "SAT1"));
        this.SAT2 = new SATelit(problemParser.parseSAT(problema_prob, "SAT2"));
        
    }

    public boolean isFinal(Estado estadoActual){
        //devuelve true si ya se han transmitido todas las observaciones
        return this.astros.equals(estadoActual.OT); 
    }
}
