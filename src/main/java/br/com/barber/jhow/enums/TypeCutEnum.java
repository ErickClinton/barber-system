package br.com.barber.jhow.enums;

import br.com.barber.jhow.exceptions.enums.TypeCutNotFoundException;

public enum TypeCutEnum {

    HAIR("hair", 60),
    BEARD("beard", 30),
    HAIR_AND_BEARD("hair_and_beard", 90),
    KIDS_HAIR("kids_hair", 45);

    private final String typeCut;
    private final int time;

    TypeCutEnum(String typeCut, int time) {
        this.typeCut = typeCut;
        this.time = time;
    }

    public String getTypeCut() {
        return typeCut;
    }

    public int getTime() {
        return time;
    }

    public static TypeCutEnum fromTypeCut(String typeCut) {
        for (TypeCutEnum cut : values()) {
            if (cut.getTypeCut().equalsIgnoreCase(typeCut)) {
                return cut;
            }
        }
        throw new TypeCutNotFoundException("Type of cut not found: " + typeCut);
    }
}