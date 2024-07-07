package main.models;

import java.util.Date;
import java.util.Objects;

public class Applicant {
    private Integer id;
    private Integer vacancyId;
    private String name;
    private String phoneNumber;
    private String religion;
    private Integer gender;
    private String address;
    private Date dob;
    private Vacancy vacancy;

    public Applicant() {
    }

    public Applicant(Integer id, Integer vacancyId, String name, String phoneNumber, String religion, Integer gender, String address, Date dob) {
        this.id = id;
        this.vacancyId = vacancyId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.religion = religion;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
    }

    public Applicant(Integer id, Integer vacancyId, String name, String phoneNumber, String religion, Integer gender, String address, Date dob, Vacancy vacancy) {
        this.id = id;
        this.vacancyId = vacancyId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.religion = religion;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
        this.vacancy = vacancy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(Integer vacancyId) {
        this.vacancyId = vacancyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.vacancyId);
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.phoneNumber);
        hash = 37 * hash + Objects.hashCode(this.religion);
        hash = 37 * hash + Objects.hashCode(this.gender);
        hash = 37 * hash + Objects.hashCode(this.address);
        hash = 37 * hash + Objects.hashCode(this.dob);
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
        final Applicant other = (Applicant) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        String label = name + " | " + phoneNumber;
        
        if (vacancy != null) {
            label += " | " + vacancy.getPosition();
        }
        
        return label;
    }
}
