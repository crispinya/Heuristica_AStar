import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//parseador para obtener los datos iniciales del archivo .prob pasado como parametro
public class Parseador {

    //metodo para obtener los datos de un satelite
    public static int[] parseSAT(String path, String SATname) throws IOException {
        String infoSAT[] = new String[5]; //Obtendremos los costes de cada satelite por el fichero leido
        int dataSAT[] = new int[5];
        String linea = "";
        FileReader fichero = new FileReader(path); //Abrimos el fichero, si no lo encuentra saltara un error
        BufferedReader buffer = new BufferedReader(fichero);
        while((linea = buffer.readLine()) != null){  //Abrimos fichero, y un buffer para leer linea a linea lo que tenemos
            String tipoInformacion = linea.substring(0,4);
            if(tipoInformacion.equals(SATname)){
                infoSAT = extractInformation(linea, 6); //El 6 es la posicion donde empiezan a aparecer los datos en SAT
                dataSAT = convertToInt(infoSAT);
            }
        }
        buffer.close();
        fichero.close();

        if(dataSAT.equals(null)){ //Si no es ninguna de estas opciones, significa que el fichero contiene informacion erronea y por tanto no nos sirve
            buffer.close();
            throw new RuntimeException("Error: la especificacion de datos es erronea"); 
        }

        return dataSAT;
    }

    //metodo para obtener los datos de una muestra a tomar
    public static ArrayList<MuestraObservacion> parseMuestra(String path) throws IOException {
        String linea = "";
        FileReader fichero = new FileReader(path); //Abrimos el fichero, si no lo encuentra saltara un error
        BufferedReader buffer = new BufferedReader(fichero);
        ArrayList <MuestraObservacion> muestras = new ArrayList<>();

        while((linea = buffer.readLine()) != null){  //Abrimos fichero, y un buffer para leer linea a linea lo que tenemos
            String tipoInformacion = linea.substring(0,4);
            if(tipoInformacion.equals("OBS:")){
                String infoMuestras[] = extractInformation(linea, 5); //El 5 es la posicion donde empiezan a aparecer los datos en OBS
                //tenemos en cada posicion del array la info de tipo (hora,banda) 
                //creamos las muestras a observar
                for(int i = 0; i < infoMuestras.length; i++){
                    int banda, hora;
                    String id = "O" + (i+1);
                    banda = Integer.parseInt(infoMuestras[i].substring(1, 2));
                    hora = Integer.parseInt(infoMuestras[i].substring(3, 4));
                    MuestraObservacion nuevoAstro = new MuestraObservacion(id, banda, hora);
                    muestras.add(nuevoAstro);
                } 

                buffer.close();
                fichero.close();

                return muestras;
            }
        }

        buffer.close();
        fichero.close();

        return null;
    }

    //Extraemos la informacion de cada linea en forma de string. Cada posicion del array sera cada uno de los costes
    private static String[] extractInformation(String line, int substring){ 
        String inicioLectura = line.substring(substring);
        return inicioLectura.split(";");
    }

    //Parseamos cada string a un integer, para manipular con mayor facilidad los datos obtenidos
    private static int[] convertToInt (String[] information){ 
        int[] result = new int[information.length];
        for(int i=0 ; i< information.length;i++){
            try{
                result[i]= Integer.parseInt(information[i]); //Convertimos cada posicion en un entero
                
            }catch(Exception ex){
                throw new RuntimeException("Error: El dato a convertir no es de tipo entero"); //Si lo que hay no es un entero, marcara error
            }
            if(result[i]<0){
                throw new RuntimeException("Error: El entero a pasar debe ser mayor o igual que 0"); //El coste debe ser como mínimo 0
            }
        }
        return result;
    }
}
