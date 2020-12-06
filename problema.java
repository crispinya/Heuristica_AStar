import java.io.IOException;

public class Problema {

    // Definition of the problem given as a problema.prob file

    Astro[] astros; //TODO: Observaciones posibles. Implementar en parser
    satelite SAT1;
    satelite SAT2;

    public Problema(String problema_prob) throws IOException {
        
        parser problemParser = new parser();
        
        //astros = problemParser.parseAstros(problema_prob);

        SAT1 = new satelite(problemParser.parseSAT(problema_prob, "SAT1"));
        SAT2= new satelite(problemParser.parseSAT(problema_prob, "SAT2"));
        
    }
}
