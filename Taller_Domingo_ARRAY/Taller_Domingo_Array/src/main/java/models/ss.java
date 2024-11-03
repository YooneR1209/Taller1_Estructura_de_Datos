package models;

import controller.Dao.implement.AdapterDao;

public class ss {
    public static void main(String[] args) {
        try {
            // Crear una instancia de Censo
            Censo censo = new Censo(50);

            // Crear y registrar algunas familias
            // Crear un generador
            Generador generador1 = new Generador(1500.0f, 2.5f, 5.0f, "Uso doméstico");

            Familia familia1 = new Familia(0, "Loja", "Matailo" , "Mora", 4, true, generador1);
            Familia familia2 = new Familia(0, "Cuenca", "Ruiz" , "Mora", 4, true);
            Familia familia3 = new Familia(0, "Malacatos", "Veintimilla" , "Mora", 4, true);
            
            censo.registrarFamilia(familia1);
            censo.registrarFamilia(familia2);
            censo.registrarFamilia(familia3);
            
            // Mostrar estadísticas
            censo.mostrarEstadisticas();

            // Convertir familias a JSON
            String jsonData = censo.convertirFamiliasAJson();
            System.out.println("Datos en formato JSON:");
            System.out.println(jsonData);

            // Guardar en un archivo (asumiendo que tienes el método saveFile)
            AdapterDao<Familia> ad = new AdapterDao<>(Familia.class);

            ad.saveFile(jsonData);
            System.out.println("Los datos se han guardado exitosamente en el archivo JSON.");

        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}
