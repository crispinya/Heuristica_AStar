import java.io.File;
import java.io.FileWriter;
import java.io.IOException; 

public class FileManager {

    //metodo para la escritura en archivos
    public void escribirArchivo(String name, String texto) throws IOException {
        File archivo = new File(name);
        FileWriter escritor = new FileWriter(archivo);
        escritor.write(texto);
        escritor.close();
    }

}
