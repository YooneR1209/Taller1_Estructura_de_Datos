package models;

public class Generador {
    private float costo;
    private float consumoXHora;
    private float energiaGenerada;
    private String uso;

    public Generador() {
    }
    
    
    public Generador(float costo, float consumoXHora, float energiaGenerada, String uso) {
        this.costo = costo;
        this.consumoXHora = consumoXHora;
        this.energiaGenerada = energiaGenerada;
        this.uso = uso;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public float getConsumoXHora() {
        return consumoXHora;
    }

    public void setConsumoXHora(float consumoXHora) {
        this.consumoXHora = consumoXHora;
    }

    public float getEnergiaGenerada() {
        return energiaGenerada;
    }

    public void setEnergiaGenerada(float energiaGenerada) {
        this.energiaGenerada = energiaGenerada;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

}
