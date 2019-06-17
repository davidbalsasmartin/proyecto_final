package appAdictive.util.enums;

public enum InfoTipo {
    ABA(2),
    ABC(3),
    ABAB(2),
    ABCD(4),
    ABCDE(5);
	
	private final int value;

    InfoTipo(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}

