import java.io.IOException;

public class problema {

    // Definition of the problem given as a problema.prob file

    satelite SAT1;
    satelite SAT2;
    //int Hour = 0; //TODO: Same duda quue la de abajo. Pero exolciar en memoria why no se considera de problema si no dee stado
    // String[] OT; //TODO: Observations already transmited to the main base. Esto
    // es mas de cada estado que de esto creo

    public problema(String problema_prob) throws IOException {
        
        parser problemParser = new parser();
        
        SAT1 = new satelite(problemParser.parseSAT(problema_prob, "SAT1"));
        SAT2= new satelite(problemParser.parseSAT(problema_prob, "SAT2"));
        
    }
}
