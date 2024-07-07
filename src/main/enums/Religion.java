package main.enums;

public enum Religion {
    Islam,
    Kristen,
    Protestan,
    Katolik,
    Buddha,
    Hindu,
    Konghucu,
    Other;

    public static Religion fromString(String religion) {
        return Religion.valueOf(religion);
    };

    public String toString() {
        return this.equals(Other) ? "Lainnya" : name();
    };
}
