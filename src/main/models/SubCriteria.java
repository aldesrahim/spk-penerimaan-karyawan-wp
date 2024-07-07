
package main.models;

import java.util.Objects;

/**
 *
 */
public class SubCriteria {
    private Integer id;
    private Integer weightId;
    private Integer criteriaId;
    private String name;

    public SubCriteria() {
    }

    public SubCriteria(Integer id, Integer weightId, Integer criteriaId, String name) {
        this.id = id;
        this.weightId = weightId;
        this.criteriaId = criteriaId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWeightId() {
        return weightId;
    }

    public void setWeightId(Integer weightId) {
        this.weightId = weightId;
    }

    public Integer getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(Integer criteriaId) {
        this.criteriaId = criteriaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.weightId);
        hash = 37 * hash + Objects.hashCode(this.criteriaId);
        hash = 37 * hash + Objects.hashCode(this.name);
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
        final SubCriteria other = (SubCriteria) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return name;
    }
}
