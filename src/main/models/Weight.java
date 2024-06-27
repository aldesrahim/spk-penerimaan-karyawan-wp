
package main.models;

import java.util.Objects;

/**
 *
 */
public class Weight {
    private Integer id;
    private Integer weight;
    private String description;

    public Weight() {
    }

    public Weight(Integer id, Integer weight, String description) {
        this.id = id;
        this.weight = weight;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.weight);
        hash = 59 * hash + Objects.hashCode(this.description);
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
        final Weight other = (Weight) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return this.description;
    }
}
