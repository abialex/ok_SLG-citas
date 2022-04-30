/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.stage.FileChooser;

/**
 *
 * @author yalle
 */
public class FileImagUtil {

    FileChooser filechooserImagPerfil = new FileChooser();
    byte[] imagen = new byte[1024 * 100];
    String URLdestino="images/";
    
    String initialDirectory;
    String title;
    
   public FileImagUtil(String id,String title){
       this.initialDirectory=id;
       this.title=title;
       
   }
    
    

    public File buscarArchivo() throws FileNotFoundException, IOException {
        filechooserImagPerfil.setInitialDirectory(new File((initialDirectory)));//para iniciar la carpeta
        filechooserImagPerfil.getExtensionFilters().clear();
        filechooserImagPerfil.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.*"));
        filechooserImagPerfil.setTitle(title);
        File fileImag = filechooserImagPerfil.showOpenDialog(null);
        FileInputStream FileEntrada = new FileInputStream(fileImag);
        FileEntrada.read(imagen);
         try {
            if (FileEntrada != null) {
                FileEntrada.close();
            }
        } catch (IOException E) {
            System.out.println("problema cerrando File entrada");
        }
        return fileImag;
    }

    public String guardarImagen(File file) throws FileNotFoundException, IOException {
        File fileImagSalida = new File(URLdestino + file.getName());
        FileOutputStream FileSalida = new FileOutputStream(fileImagSalida);
        FileSalida.write(imagen);
        try {
            if (FileSalida != null) {
                FileSalida.close();
            }
        } catch (IOException E) {
            System.out.println("problema cerrando File salida");
        }
        return fileImagSalida.getAbsolutePath();
    }

}
