package logica;

public class Personen {
    private int id;
    private String email;
    private String voornaam;
    private String familienaam;
    private String wachtwoord;

    public Personen(int id, String email, String voornaam, String familienaam, String wachtwoord) {
        this.id = id;
        this.email = email;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.wachtwoord = wachtwoord;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public void setFamilienaam(String familienaam) {
        this.familienaam = familienaam;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }
}
