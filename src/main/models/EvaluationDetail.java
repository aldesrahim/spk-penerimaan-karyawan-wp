package main.models;

import java.util.Objects;

public class EvaluationDetail {
    private Integer id;
    private Integer evaluationId;
    private Integer criteriaId;
    private Integer subCriteriaId;

    public EvaluationDetail() {
    }

    public EvaluationDetail(Integer id, Integer evaluationId, Integer criteriaId, Integer subCriteriaId) {
        this.id = id;
        this.evaluationId = evaluationId;
        this.criteriaId = criteriaId;
        this.subCriteriaId = subCriteriaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Integer evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Integer getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(Integer criteriaId) {
        this.criteriaId = criteriaId;
    }

    public Integer getSubCriteriaId() {
        return subCriteriaId;
    }

    public void setSubCriteriaId(Integer subCriteriaId) {
        this.subCriteriaId = subCriteriaId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.evaluationId);
        hash = 61 * hash + Objects.hashCode(this.criteriaId);
        hash = 61 * hash + Objects.hashCode(this.subCriteriaId);
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
        final EvaluationDetail other = (EvaluationDetail) obj;
        return Objects.equals(this.id, other.id);
    }
}
