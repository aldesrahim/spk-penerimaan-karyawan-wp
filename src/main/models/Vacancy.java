package main.models;

import java.util.Objects;

public class Vacancy {
    private Integer id;
    private String position;
    private Integer quota;

    public Vacancy() {
    }

    public Vacancy(Integer id, String position, Integer quota) {
        this.id = id;
        this.position = position;
        this.quota = quota;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.position);
        hash = 79 * hash + Objects.hashCode(this.quota);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vacancy other = (Vacancy) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return position;
    }
}
