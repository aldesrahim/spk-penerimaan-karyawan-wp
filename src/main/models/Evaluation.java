package main.models;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Evaluation {
    private Integer id;
    private Integer applicantId;
    private List<EvaluationDetail> evaluationDetails;

    public Evaluation() {
    }

    public Evaluation(Integer id, Integer applicantId) {
        this.id = id;
        this.applicantId = applicantId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
    }

    public List<EvaluationDetail> getEvaluationDetails() {
        return evaluationDetails;
    }

    public void setEvaluationDetails(List<EvaluationDetail> evaluationDetails) {
        this.evaluationDetails = evaluationDetails;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.applicantId);
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
        final Evaluation other = (Evaluation) obj;
        return Objects.equals(this.id, other.id);
    }
}
