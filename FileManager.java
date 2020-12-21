import java.io.File;
import java.io.FileWriter;
import java.io.IOException; 

public class FileManager {

    public void escribirArchivo(String name, String texto) throws IOException {
        File archivo = new File(name); //intenta crear el archivo por si aun no existe
        FileWriter escritor = new FileWriter(archivo);
        escritor.write(texto);
        escritor.close();
    }

}
