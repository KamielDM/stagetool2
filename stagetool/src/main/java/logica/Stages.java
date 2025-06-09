package logica;

import java.sql.Date;

public class Stages {
    private int id;
    private int mentorId;
    private Integer toegewezenStudentId;     // mag null zijn
    private Integer begeleidendeDocentId;    // mag null zijn
    private String onderwerp;
    private String beschrijving;
    private Vakgebied vakgebied;
    private Date begin;
    private Date eind;
    private String mentorNaam;

    // Constructor met mentorNaam (voor weergeven in JTable)
    public Stages(int id, int mentorId,
                  String onderwerp, String beschrijving, Vakgebied vakgebied,
                  Date begin, Date eind, String mentorNaam) {
        this.id = id;
        this.mentorId = mentorId;
        this.onderwerp = onderwerp;
        this.beschrijving = beschrijving;
        this.vakgebied = vakgebied;
        this.begin = begin;
        this.eind = eind;
        this.mentorNaam = mentorNaam;
    }

    // Originele constructor behouden (voor uitbreidbaarheid)
    public Stages(int id, int mentorId, Integer toegewezenStudentId, Integer begeleidendeDocentId,
                  String onderwerp, String beschrijving, Vakgebied vakgebied,
                  Date begin, Date eind) {
        this.id = id;
        this.mentorId = mentorId;
        this.toegewezenStudentId = toegewezenStudentId;
        this.begeleidendeDocentId = begeleidendeDocentId;
        this.onderwerp = onderwerp;
        this.beschrijving = beschrijving;
        this.vakgebied = vakgebied;
        this.begin = begin;
        this.eind = eind;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getMentorId() {
        return mentorId;
    }

    public Integer getToegewezenStudentId() {
        return toegewezenStudentId;
    }

    public Integer getBegeleidendeDocentId() {
        return begeleidendeDocentId;
    }

    public String getOnderwerp() {
        return onderwerp;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public Vakgebied getVakgebied() {
        return vakgebied;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEind() {
        return eind;
    }

    public String getMentorNaam() {
        return mentorNaam;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public void setToegewezenStudentId(Integer toegewezenStudentId) {
        this.toegewezenStudentId = toegewezenStudentId;
    }

    public void setBegeleidendeDocentId(Integer begeleidendeDocentId) {
        this.begeleidendeDocentId = begeleidendeDocentId;
    }

    public void setOnderwerp(String onderwerp) {
        this.onderwerp = onderwerp;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public void setVakgebied(Vakgebied vakgebied) {
        this.vakgebied = vakgebied;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public void setEind(Date eind) {
        this.eind = eind;
    }

    public void setMentorNaam(String mentorNaam) {
        this.mentorNaam = mentorNaam;
    }

    @Override
    public String toString() {
        return onderwerp + " (" + vakgebied + ")";
    }
}
