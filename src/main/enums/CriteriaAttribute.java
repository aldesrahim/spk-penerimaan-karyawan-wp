package main.enums;

/**
 *
 */
public enum CriteriaAttribute {
    BENEFIT,
    COST;

    public static CriteriaAttribute fromInt(int attribute) {
        return attribute == 1 ? BENEFIT : COST;
    }

    public int toInt() {
        return BENEFIT.equals(this) ? 1 : -1;
    }

    @Override
    public String toString() {
        return switch (this) {
            case BENEFIT -> "Benefit";
            case COST -> "Cost";
        };
    }
}
