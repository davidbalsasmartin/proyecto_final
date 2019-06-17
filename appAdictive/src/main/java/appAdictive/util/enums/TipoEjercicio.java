package appAdictive.util.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoEjercicio {
    BASICO("Básico"),
    REFUERZO("Refuerzo"),
    AUXILIAR("Auxiliar");
    
    private String value;

    TipoEjercicio(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static TipoEjercicio fromValue(String value) {
        for (TipoEjercicio tipo :TipoEjercicio.values()){
            if (tipo.getValue().equals(value)){
                return tipo;
            }
        }
        throw new UnsupportedOperationException(
                value + " no es un tipo de ejercicio válido. ");
    }
}