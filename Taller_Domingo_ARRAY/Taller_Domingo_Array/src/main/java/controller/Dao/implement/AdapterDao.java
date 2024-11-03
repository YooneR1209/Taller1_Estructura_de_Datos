package controller.Dao.implement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.ws.rs.core.Link;

import com.google.gson.Gson;

import controller.Dao.FamiliaDao;
import controller.tda.list.LinkedList;
import models.Familia;

public class AdapterDao<T> implements InterfazDao<T> {
    private Class<T> clazz;
    private Gson g;
    public static String filePath = "src/main/java/Data/"; // Ruta donde se guardan los archivos JSON

    public AdapterDao(Class<T> clazz) { // Constructor
        this.clazz = clazz;
        this.g = new Gson();
    }

    public T get(Integer id) throws Exception {
        LinkedList<T> list = listAll(); //Invoca el método listAll() para obtener la lista de objetos
        if(!list.isEmpty()){
            T [] matrix = list.toArray(); //Convierte la lista en un Array de objetos
            return matrix[id - 1]; //Devuelve el objeto en la posición id-1
        }
        return null; //Devuelve null si la lista está vacía
    }

    public LinkedList<T> listAll() {  //Convierte el String con formato Json en un Array de objetos
        LinkedList<T> list = new LinkedList<>();
        try {
            String data = readFile(); //Invoca el método readFile() para leer el archivo JSON y devolverlo en formato String
            T[] matrix = (T[]) g.fromJson(data, java.lang.reflect.Array.newInstance(clazz, 0).getClass()); //Deserializa el String JSON en un Array de objetos tipo T
            list.toList(matrix); //Envia matrix al método toList() de la clase LinkedList
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list; //devuelve LinkedList<T> list
    }

    public void merge(T object, Integer index) throws Exception {
        LinkedList<T> list = listAll(); //Invoca el método listAll() para obtener la lista de objetos
        list.update(object, index); //Actualiza el objeto en la posición index
        String info = g.toJson(list.toArray()); //Convierte la lista en un String JSON
        saveFile(info);
    }

    public void persist(T object) throws Exception {  //Guarda un objeto en un archivo JSON
        System.out.println("Persisting object: " + object); //Imprime el objeto a guardar
        LinkedList<T> list = listAll(); //Invoca el método listAll() para obtener la lista de objetos
        if (list == null) {
            System.out.println("La lista es null. Asegúrate de que el archivo JSON se esté leyendo correctamente.");
            return;
        }
        list.add(object); //Agrega el objeto a la lista
        String info = g.toJson(list.toArray()); //Convierte la lista en un String JSON
        System.out.println("Escribiendo datos al archivo: " + info); //Imprime el String JSON
        saveFile(info); //Guarda el String JSON en un archivo
    }

    public T getArray(Integer id) throws Exception {
        LinkedList<T> list = listAll(); //Invoca el método listAll() para obtener la lista de objetos
        if(!list.isEmpty()){
            T [] matrix = list.toArray(); //Convierte la lista en un Array de objetos
            return matrix[id - 1]; //Devuelve el objeto en la posición id-1
        }
        return null; //Devuelve null si la lista está vacía
    }
 

    public String readFile() throws Exception { //Lee el archivo JSON y lo convierte en un String
        File file = new File(filePath + clazz.getSimpleName() + ".json"); //Crea una instancia de File con la ruta del archivo JSON

        if (!file.exists()) {
            System.out.println("El archivo no existe, creando uno nuevo: " + file.getAbsolutePath());
            saveFile("[]"); 
        }

        StringBuilder sb = new StringBuilder();
        try (Scanner in = new Scanner(new FileReader(file))) { //Lee el archivo JSON
            while (in.hasNextLine()) { //Mientras haya una línea que leer
                sb.append(in.nextLine()).append("\n"); //Añade la línea al StringBuilder
            }
        }
        return sb.toString().trim(); //Devuelve el contenido del archivo en formato String
    }

    public void saveFile(String data) { // Guarda el String en un archivo JSON
    File file = new File(filePath + "Familia.json"); // Cambia el nombre del archivo si es necesario
    file.getParentFile().mkdirs(); // Crea los directorios necesarios para el archivo

    // Siempre crea el archivo nuevo, pero no sobrescribas el contenido
    try (FileWriter f = new FileWriter(file)) {  
        f.write(data); // Escribe el contenido de data en el buffer de escritura
        f.flush(); // Descarga el contenido del buffer al archivo
    } catch (Exception e) {
        System.out.println("Error al escribir en el archivo: " + e.getMessage());
    }
    }


    public Boolean supreme(int index) throws Exception {
        LinkedList<T> list = listAll(); //Invoca el método listAll() para obtener la lista de objetos
        list.remove(index); //Elimina el objeto en la posición index
        String info = g.toJson(list.toArray()); //Convierte la lista en un String JSON
        saveFile(info); //Guarda el String JSON en un archivo
        return true; //Retorna verdadero si se eliminó correctamente
    }
    
    
}
