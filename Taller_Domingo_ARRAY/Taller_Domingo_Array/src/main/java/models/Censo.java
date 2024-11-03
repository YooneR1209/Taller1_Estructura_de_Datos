package models;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class Censo {
    private Familia[] familias;
    private int contadorFamilias;
    private static final String filePath = "src/main/java/Data/Familia.json";

    public Censo(int capacidad) {     // Constructor que inicializa el arreglo de familias y carga los datos desde el archivo JSON
        familias = new Familia[capacidad];
        contadorFamilias = 0;
        cargarDatosDesdeJson();
    }
    
    public void registrarFamilia(Familia familia) {
   
        if (contadorFamilias >= familias.length) {    // Si el arreglo está lleno, duplicamos su tamaño
            duplicarCapacidad();
        }
        
        familia.setId(contadorFamilias + 1);      // Asignamos el ID basado en el contador
        
        this.familias[this.contadorFamilias++] = familia;

        saveAll(); // Guardamos los cambios en el archivo JSON
    }


    private void duplicarCapacidad() {
        int nuevaCapacidad = familias.length * 2;
        Familia[] nuevo = new Familia[nuevaCapacidad];
        
        for (int i = 0; i < familias.length; i++) {         // Copiamos los elementos del arreglo actual al nuevo
            nuevo[i] = familias[i];
        }
        
        familias = nuevo;
        System.out.println("Capacidad duplicada a: " + nuevaCapacidad);
    }

    public void mostrarEstadisticas() {
        int familiasConGenerador = 0;
        for (int i = 0; i < contadorFamilias; i++) {
            if (familias[i].getTieneGenerador()) {
                familiasConGenerador++;
            }
        }
        System.out.println("Total de familias: " + contadorFamilias);
        System.out.println("Total de familias con generador: " + familiasConGenerador);
    }

    public Familia getFamiliaById(int id) {
        for (int i = 0; i < contadorFamilias; i++) {
            if (familias[i] != null && familias[i].getId() == id) { 
                return familias[i];
            }
        }
        return null; // Si no se encuentra ninguna familia con ese id
    }
    

    public boolean updateFamilia(int id, Familia nuevaFamilia) { 
        for (int i = 0; i < contadorFamilias; i++) {
            if (familias[i].getId().equals(id)) {
                
                familias[i].setCanton(nuevaFamilia.getCanton()); // Actualizamos los atributos de la familia
                familias[i].setApellidoPaterno(nuevaFamilia.getApellidoPaterno());
                familias[i].setApellidoMaterno(nuevaFamilia.getApellidoMaterno());
                familias[i].setIntegrantes(nuevaFamilia.getIntegrantes());
                familias[i].setTieneGenerador(nuevaFamilia.getTieneGenerador());
    
                
                if (nuevaFamilia.getTieneGenerador()) { // Verificamos si la familia tiene generador
                    if (familias[i].getGenerador() == null) {
                        
                        familias[i].setGenerador(new Generador()); // Crea un nuevo generador si no existe
                    }
                    
                    Generador generador = familias[i].getGenerador(); // Actualizamos los datos del generador
                    Generador nuevoGenerador = nuevaFamilia.getGenerador();
                    generador.setCosto(nuevoGenerador.getCosto());
                    generador.setConsumoXHora(nuevoGenerador.getConsumoXHora());
                    generador.setEnergiaGenerada(nuevoGenerador.getEnergiaGenerada());
                    generador.setUso(nuevoGenerador.getUso());
                } else {
                    
                    familias[i].setGenerador(null); // Si la familia no tiene generador, se establece el generador como null
                }
    
                saveAll();
                return true;
            }
        }
        return false; // Si no se encuentra la familia
    }
    
    public boolean deleteFamilia(int id) {
        for (int i = 0; i < contadorFamilias; i++) {
            if (familias[i] != null && familias[i].getId() == id) {

                for (int j = i; j < contadorFamilias - 1; j++) {    // Mueve todas las familias hacia adelante para llenar el espacio eliminado
                    familias[j] = familias[j + 1];
                }
                familias[contadorFamilias - 1] = null; // Eliminamos la última referencia
                contadorFamilias--; 
                saveAll(); 
                return true;
            }
        }
        return false; 
    }
    

    public String convertirFamiliasAJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
 
        Familia[] familiasRegistradas = new Familia[this.contadorFamilias];        // Solo convertir las familias registradas, no los espacios vacíos
        System.arraycopy(this.familias, 0, familiasRegistradas, 0, contadorFamilias);
        
        return gson.toJson(familiasRegistradas);
    }

    public void saveAll() {
        try (FileWriter writer = new FileWriter(filePath)) {
            String json = convertirFamiliasAJson(); // Método que convierte el arreglo a JSON
            writer.write(json); // Escribimos el JSON en el archivo
            writer.flush(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosDesdeJson() {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type familiaArrayType = new TypeToken<Familia[]>() {}.getType();    // Asignamos el tipo de datos al arreglo
            Familia[] familiasCargadas = gson.fromJson(reader, familiaArrayType);
            
            if (familiasCargadas != null) {

                if (familiasCargadas.length > familias.length) {      // Si el arreglo existente no es lo suficientemente grande, lo redimensionamos
                    familias = new Familia[familiasCargadas.length]; 
                }
                
                for (int i = 0; i < familiasCargadas.length; i++) {       // Copiamos las familias cargadas al arreglo existente (o recién creado)
                    familias[i] = familiasCargadas[i];
                }
                
                contadorFamilias = familiasCargadas.length;
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo JSON: " + e.getMessage());
        }
    }
    
    

    
    
}
