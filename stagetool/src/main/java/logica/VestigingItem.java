package logica;

public class VestigingItem {
    private int id;
    private String bedrijfsNaam;
    private String vestigingsNaam;

    public VestigingItem(int id, String bedrijfsNaam, String vestigingsNaam) {
        this.id = id;
        this.bedrijfsNaam = bedrijfsNaam;
        this.vestigingsNaam = vestigingsNaam;
    }

    public int getId() {
        return id;
    }

    public String getBedrijfsNaam() {
        return bedrijfsNaam;
    }

    public String getVestigingsNaam() {
        return vestigingsNaam;
    }

    @Override
    public String toString() {
        return vestigingsNaam + " (" + bedrijfsNaam + ")";
    }
}
