package controller.Dao.servicies;
import controller.tda.list.LinkedList;
import models.Familia;
import controller.Dao.FamiliaDao;

public class FamiliaServicies {
    private FamiliaDao obj;
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
}
