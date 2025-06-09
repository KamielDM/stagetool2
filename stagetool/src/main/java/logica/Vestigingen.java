package logica;

public class Vestigingen {
    private int id;
    private int bedrijfId;
    private boolean hoofdzetel;
    private String naam;
    private String adres;
    private String gemeente;
    private int postcode;

    public Vestigingen(int id, int bedrijfId, boolean hoofdzetel, String naam, String adres, String gemeente, int postcode) {
        this.id = id;
        this.bedrijfId = bedrijfId;
        this.hoofdzetel = hoofdzetel;
        this.naam = naam;
        this.adres = adres;
        this.gemeente = gemeente;
        this.postcode = postcode;
    }

    public int getId() {
        return id;
    }

    public int getBedrijfId() {
        return bedrijfId;
    }

    public boolean isHoofdzetel() {
        return hoofdzetel;
    }

    public String getNaam() {
        return naam;
    }

    public String getAdres() {
        return adres;
    }

    public String getGemeente() {
        return gemeente;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBedrijfId(int bedrijfId) {
        this.bedrijfId = bedrijfId;
    }

    public void setHoofdzetel(boolean hoofdzetel) {
        this.hoofdzetel = hoofdzetel;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }
}
