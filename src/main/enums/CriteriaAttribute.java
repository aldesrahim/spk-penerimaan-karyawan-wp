package main.enums;

/**
 *
 */
public enum CriteriaAttribute {
    Benefit,
    Cost;

    public static CriteriaAttribute fromInt(int attribute) {
        return attribute == 1 ? Benefit : Cost;
    }

    public int toInt() {
        return Benefit.equals(this) ? 1 : -1;
    }

    @Override
    public String toString() {
        return switch (this) {
            case Benefit -> "Benefit";
            case Cost -> "Cost";
        };
    }
}
