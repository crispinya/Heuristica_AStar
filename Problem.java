import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
public class Problem {

    
    ArrayList<AstroObservacion> astrosAObservar = new ArrayList<AstroObservacion>(); //posibles observaciones
    SATelit SAT1; //satelite 1
    SATelit SAT2; //satelite 2

    Estado S; //estado inicial

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
        
        System.out.println("-------- COMIENZA PROBLEMA --------");
    }

    public boolean isFinal(Estado estadoActual){
        //devuelve true si ya se han transmitido todas las observaciones
        /*if(estadoActual.OT.equals(null)){
            return false;
        }*/

        if(this.astrosAObservar.size() == estadoActual.OT.size()){
            return true; //Al insertar nosotros en OT, el control de errores para evitar repetidos ya está implementado
        }
        return false;
    }
}
