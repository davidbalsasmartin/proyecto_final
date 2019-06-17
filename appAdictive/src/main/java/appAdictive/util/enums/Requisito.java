package appAdictive.util.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Requisito {
    NADA("Nada"),
    PESOSLIBRES("Pesos libres"),
    EQUIPOFUNCIONAL("Equipo Funcional"),
    MAQUINA("Máquina");
	
	private String value;

	Requisito(String value) {
        this.value = value;
    }

	@JsonValue
    public String getValue() {
        return value;
    }

    public static Requisito fromValue(String value) {
        for (Requisito requisito :Requisito.values()){
            if (requisito.getValue().equals(value)){
                return requisito;
            }
        }
        throw new UnsupportedOperationException(
                value + " no es un requisito de ejercicio válido. ");
    }
}