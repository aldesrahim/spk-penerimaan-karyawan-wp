package main.enums;

public enum Gender {
    Male,
    Female,
    Other;

    public static Gender fromInt(int gender) {
        return switch (gender) {
            case 1 -> Male;
            case 2 -> Female;
            default -> Other;
        };
    }

    public int toInt() {
        return switch (this) {
            case Male -> 1;
            case Female -> 2;
            case Other -> 3;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case Male -> "Laki-Laki";
            case Female -> "Perempuan";
            case Other -> "Lainnya";
        };
    }
}
