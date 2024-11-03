package controller.Dao.servicies;
import controller.tda.list.LinkedList;
import models.Censo;
import models.Familia;
import models.Generador;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter;

import controller.Dao.FamiliaDao;
import controller.Dao.implement.AdapterDao;

public class FamiliaServicies {
    private FamiliaDao obj;
    Censo c = new Censo(50); // Crea una instancia de Censo
    public FamiliaServicies(){ //Constructor de la clase
        obj = new FamiliaDao(); //Instancia un objeto de la clase FamiliaDao
    }
    public Boolean update() throws Exception{ //Guarda la variable familia en la lista de objetos
        return obj.update(); //Invoca el método save() de la clase FamiliaDao
    }

    public Boolean save() throws Exception{ //Guarda la variable familia en la lista de objetos
        return obj.save(); //Invoca el método save() de la clase FamiliaDao
    }

    public LinkedList<Familia> listAll(){ //Obtiene la lista de objetos LinkedList<T>
        return obj.getlistAll(); //Invoca el método getlistAll() de la clase FamiliaDao

    }

    public Familia getFamilia(){ //Obtiene la familia
        return obj.getFamilia(); //Invoca el método getFamilia() de la clase FamiliaDao
    }

    public void setFamilia( Familia familia){ //Recibe un objeto Familia
        obj.setFamilia(familia); //Invoca el método setFamilia() de la clase FamiliaDao y envía el objeto Familia
    }

    public Familia get(Integer id) throws Exception{ //Obtiene un objeto Familia por su id
        return obj.get(id); //Invoca el método get() de la clase FamiliaDao y envía el id
    }

    public Boolean delete(int index) throws Exception{ //Elimina un objeto Familia por su índice
        return obj.delete(index); //Invoca el método delete() de la clase FamiliaDao y envía el índice
    }
    
    public int contarFamiliasConGenerador() {
        return obj.contarFamiliasConGenerador();
    }

    public Familia[] arrayAll() {
        return obj.getArrayAll();
    }

    public Familia getArray(Integer id) throws Exception{ //Obtiene un objeto Familia por su id
        return obj.getArray(id); //Invoca el método get() de la clase FamiliaDao y envía el id
    }

    public String obtenerFamiliasEnJson() {
        return c.convertirFamiliasAJson(); // Convierte el arreglo de familias a JSON
    }

    public void saveAll() {
        AdapterDao<Familia> dao = new AdapterDao<>(Familia.class); // Crea una instancia de AdapterDao
        String jsonData = c.convertirFamiliasAJson(); // Convierte el arreglo de familias a JSON
        dao.saveFile(jsonData); // Guarda el JSON en el archivo usando AdapterDao
    }

}
