package logica;

public class MentorItem {
    private int id;
    private String voornaam;
    private String familienaam;
    private String vestigingNaam;
    private String bedrijfNaam;

    public MentorItem(int id, String voornaam, String familienaam, String vestigingNaam, String bedrijfNaam) {
        this.id = id;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.vestigingNaam = vestigingNaam;
        this.bedrijfNaam = bedrijfNaam;
    }

    public int getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public String getVestigingNaam() {
        return vestigingNaam;
    }

    public String getBedrijfNaam() {
        return bedrijfNaam;
    }

    @Override
    public String toString() {
        return voornaam + " " + familienaam;
    }
}