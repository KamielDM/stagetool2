package logica;

public class Mentoren {
    private int persoonId;
    private int vestigingId;

    public Mentoren(int persoonId, int vestigingId) {
        this.persoonId = persoonId;
        this.vestigingId = vestigingId;
    }

    public int getPersoonId() {
        return persoonId;
    }

    public int getVestigingId() {
        return vestigingId;
    }

    public void setPersoonId(int persoonId) {
        this.persoonId = persoonId;
    }

    public void setVestigingId(int vestigingId) {
        this.vestigingId = vestigingId;
    }
}
