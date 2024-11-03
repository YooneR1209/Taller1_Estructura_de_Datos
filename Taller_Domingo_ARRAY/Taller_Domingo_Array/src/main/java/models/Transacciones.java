package models;
import java.time.LocalDateTime;

public class Transacciones {
    private String tipoTransaccion;
    private String fecha;
    private Integer id;

    public Transacciones(String tipoTransaccion, Integer idEntidad) {
        this.tipoTransaccion = tipoTransaccion;
        this.fecha = LocalDateTime.now().toString();
        this.id = idEntidad;
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "tipoTransaccion='" + tipoTransaccion + '\'' +
                ", timestamp=" + fecha +
                ", idEntidad=" + id + 
                "\n}";
    }
}
