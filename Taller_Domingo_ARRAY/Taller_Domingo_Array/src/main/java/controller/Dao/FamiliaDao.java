package controller.Dao;

import models.Familia;
import models.Censo;

import java.util.List;

import com.google.gson.Gson;

import controller.Dao.implement.AdapterDao;
import controller.tda.list.LinkedList;

public class FamiliaDao extends AdapterDao<Familia> {
    private Familia familia = new Familia(); 
    private LinkedList listAll;
    
    public FamiliaDao(){
        super(Familia.class);
    }
    public Familia getFamilia(){ //Obtiene la familia
        if (familia == null) {
            familia = new Familia(); //En caso de que la familia sea nula, crea una nueva instancia de Familia
        }
        return this.familia; //Devuelve la familia
    }

    public void setFamilia(Familia familia){ //Establece la familia con un objeto Familia
        this.familia = familia; //Asigna el objeto Familia a la variable familia
    }

    public LinkedList getlistAll(){  //Obtiene la lista de objetos
        if (listAll == null) { //Si la lista es nula 
            this.listAll = listAll(); //Invoca el método listAll() para obtener la lista de objetos
        }
        return listAll; //Devuelve la lista de objetos de la variable listAll
    }
    public Boolean save() throws Exception{ //Guarda la variable familia en la lista de objetos
        Integer id = getlistAll().getSize()+1; //Obtiene el tamaño de la lista y le suma 1 para asignar un nuevo id
        familia.setId(id); //Asigna el id a familia
        this.persist(this.familia); //Guarda la familia en la lista de objetos LinkedList y en el archivo JSON
        this.listAll = listAll(); //Actualiza la lista de objetos
        return true; //Retorna verdadero si se guardó correctamente
    }

    public Boolean update() throws Exception{ //Actualiza el nodo Familia en la lista de objetos
        this.merge(getFamilia(), getFamilia().getId() -1);  //Envia la familia a actualizar con su index 
        this.listAll = listAll();  //Actualiza la lista de objetos
        return true; 
    }

    public Boolean delete(int index) throws Exception { //Elimina un objeto Familia por su índice
        this.supreme(index);
        this.listAll = listAll(); // Actualiza la lista de objetos
        return true; // Retorna verdadero si se eliminó correctamente
    }
    
    public int contarFamiliasConGenerador() {
        int contador = 0;
        LinkedList<Familia> familias = listAll(); // lista todas las familias
        Familia[] familiasArray = familias.toArray(); // Convierte la lista enlazada en un arreglo

        for (Familia familia : familiasArray) { // Usa el bucle for-each en el arreglo
            if (familia.getTieneGenerador()) { // Verifica si la familia tiene generador
                contador++;
            }
        }
        return contador;
    }

    public Familia[] getArrayAll() {
        try {
            Gson g = new Gson();
            AdapterDao<Familia> ad = new AdapterDao<>(Familia.class);
    
            String data = ad.readFile();  // Lee el archivo JSON y devuelve el contenido como un String
            if (data == null || data.isEmpty()) {
                return new Familia[0]; // Si no hay datos, devuelve un arreglo vacío
            }
            
            // Convierte el String JSON en un arreglo de objetos Familia
            return g.fromJson(data, Familia[].class);
        } catch (Exception e) {
            e.printStackTrace();
            return new Familia[0]; // En caso de error, devuelve un arreglo vacío
        }
    }
    

}

    



