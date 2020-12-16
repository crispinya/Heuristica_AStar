import java.io.IOException;

public class Problema {

    // Definition of the problem given as a problema.prob file

    Astro[] astros; //TODO: Observaciones posibles. Implementar en parser
    Satelite SAT1;
    Satelite SAT2;

    public Problema(String problema_prob) throws IOException {
        
        Parser problemParser = new Parser();
        
        astros = problemParser.parseAstros(problema_prob);

        SAT1 = new Satelite(problemParser.parseSAT(problema_prob, "SAT1"));
        SAT2= new Satelite(problemParser.parseSAT(problema_prob, "SAT2"));
        
    }
}
