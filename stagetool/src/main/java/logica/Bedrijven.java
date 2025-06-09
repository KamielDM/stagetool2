package logica;

public class Bedrijven {
    private int id;
    private String naam;
    private String email;
    private String telefoon;

    public Bedrijven(int id, String naam, String email, String telefoon) {
        this.id = id;
        this.naam = naam;
        this.email = email;
        this.telefoon = telefoon;
    }

    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefoon() {
        return telefoon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefoon(String telefoon) {
        this.telefoon = telefoon;
    }
}
