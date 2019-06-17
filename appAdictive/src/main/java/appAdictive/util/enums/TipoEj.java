package appAdictive.util.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoEj {
    HIPERTROFIA("hipertrofia", 4),
    FUERZA("fuerza", 5),
    DEFINICION("definición", 3),
    FUNCIONAL("funcional", 3),
    DESCANSO("descanso", 1),
    MIXTO("mixto", 4);
	
    private String value;
    private int duracion;

	TipoEj(String value, int duracion) {
        this.value = value;
        this.duracion = duracion;
    }
	
	public int getDuracion() {
		return duracion;
	}

	@JsonValue
    public String getValue() {
        return value;
    }

    public static TipoEj fromValue(String value) {
        for (TipoEj tipoEj :TipoEj.values()){
            if (tipoEj.getValue().equals(value)){
                return tipoEj;
            }
        }
        throw new UnsupportedOperationException(
                value + " no es un enfoque de ejercicio válido. ");
    }
}