package data;

import logica.*;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class DataLayer {
    private Connection con;

    // -------------------Verbinding met de database----------------------------------------------------------
    public DataLayer() {
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/stagetool", "dbprog", "Azerty123"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------------------Controleren of bedrijven al bestaan-------------------------------------------------
    public boolean zijnErBedrijven() {
        String query = "SELECT COUNT(*) FROM bedrijven";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // -------------------Inlezen CSV bestanden---------------------------------------------------------------
    public void importeerCsvBestandenAlsLeeg(String padBedrijven, String padVestigingen, String padMentoren) {
        if (zijnErBedrijven()) return;

        // Check of de bestanden bestaan
        if (!new File(padBedrijven).exists() || !new File(padVestigingen).exists() || !new File(padMentoren).exists()) {
            JOptionPane.showMessageDialog(null, "Eén of meerdere CSV-bestanden ontbreken!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(padBedrijven))) {
            String lijn;
            br.readLine(); // header overslaan
            while ((lijn = br.readLine()) != null) {
                String[] tokens = lijn.split(",");
                String sql = "INSERT INTO bedrijven (id, naam, telefoonnummer, email) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = con.prepareStatement(sql)) {
                    stmt.setInt(1, Integer.parseInt(tokens[0]));
                    stmt.setString(2, tokens[1]);
                    stmt.setString(3, tokens[2]);
                    stmt.setString(4, tokens[3]);
                    stmt.executeUpdate();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(padVestigingen))) {
            String lijn;
            br.readLine(); // header overslaan
            while ((lijn = br.readLine()) != null) {
                String[] tokens = lijn.split(",");
                String sql = "INSERT INTO vestigingen (id, naam, bedrijf_id, hoofdzetel) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = con.prepareStatement(sql)) {
                    stmt.setInt(1, Integer.parseInt(tokens[0]));
                    stmt.setString(2, tokens[1].replace("\"", "").trim());
                    stmt.setInt(3, Integer.parseInt(tokens[2]));
                    stmt.setBoolean(4, tokens[3].equals("1"));
                    stmt.executeUpdate();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(padMentoren))) {
            String lijn;
            br.readLine(); // header overslaan
            while ((lijn = br.readLine()) != null) {
                String[] tokens = lijn.split(",");
                String sql = "INSERT INTO gebruikers (id, voornaam, familienaam, email, wachtwoord) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = con.prepareStatement(sql)) {
                    stmt.setInt(1, Integer.parseInt(tokens[0]));
                    stmt.setString(2, tokens[1]);
                    stmt.setString(3, tokens[2]);
                    stmt.setString(4, tokens[3]);
                    stmt.setString(5, tokens[4]);
                    stmt.executeUpdate();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(padMentoren))) {
            String lijn;
            br.readLine(); // header overslaan
            while ((lijn = br.readLine()) != null) {
                String[] tokens = lijn.split(",");
                String sql = "INSERT INTO stagementoren (id, vestiging_id) VALUES (?, ?)";
                try (PreparedStatement stmt = con.prepareStatement(sql)) {
                    stmt.setInt(1, Integer.parseInt(tokens[0]));
                    stmt.setInt(2, Integer.parseInt(tokens[5]));
                    stmt.executeUpdate();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    // -------------------Vestiginglijst ophalen-------------------------------------------------------------
    public ArrayList<VestigingItem> getVestigingen() {
        ArrayList<VestigingItem> lijst = new ArrayList<>();
        String sql = """
            SELECT v.id, b.naam, v.naam
            FROM vestigingen v
            JOIN bedrijven b ON v.bedrijf_id = b.id
        """;
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lijst.add(new VestigingItem(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lijst;
    }

    // -------------------Mentorenlijst ophalen--------------------------------------------------------------
    public ArrayList<MentorItem> getMentoren() {
        ArrayList<MentorItem> lijst = new ArrayList<>();
        String sql = """
            SELECT g.id, g.voornaam, g.familienaam, v.naam AS vestiging, b.naam AS bedrijf
            FROM gebruikers g
            JOIN stagementoren s ON g.id = s.id
            JOIN vestigingen v ON s.vestiging_id = v.id
            JOIN bedrijven b ON v.bedrijf_id = b.id
            ORDER BY g.id ASC
        """;
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lijst.add(new MentorItem(
                        rs.getInt("id"),
                        rs.getString("voornaam"),
                        rs.getString("familienaam"),
                        rs.getString("vestiging"),
                        rs.getString("bedrijf")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lijst;
    }

    // -------------------Jaren van stages ophalen-----------------------------------------------------------
    public ArrayList<Integer> getJarenMetStages() {
        ArrayList<Integer> jaren = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(begin) AS jaar FROM stages ORDER BY jaar DESC";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                jaren.add(rs.getInt("jaar"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jaren;
    }

    // -------------------Stages per jaar ophalen-----------------------------------------------------------
    public ArrayList<Stages> getStagesVoorJaar(int jaar) {
        ArrayList<Stages> lijst = new ArrayList<>();
        String sql = """
            SELECT id, stagementor_id, onderwerp, beschrijving, vakgebied, begin, eind
            FROM stages
            WHERE YEAR(begin) = ?
        """;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, jaar);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lijst.add(new Stages(
                            rs.getInt("id"),
                            rs.getInt("stagementor_id"),
                            null,
                            null,
                            rs.getString("onderwerp"),
                            rs.getString("beschrijving"),
                            Vakgebied.valueOf(rs.getString("vakgebied")),
                            rs.getDate("begin"),
                            rs.getDate("eind")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lijst;
    }

    // -------------------Stage toevoegen--------------------------------------------------------------------
    public void insertStage(int mentorId, String onderwerp, String beschrijving,
                            Vakgebied vakgebied, Date startDatum) {
        long tienWeken = 10L * 7 * 24 * 60 * 60 * 1000;
        Date eindDatum = new Date(startDatum.getTime() + tienWeken);

        String sql = """
            INSERT INTO stages (stagementor_id, onderwerp, beschrijving, vakgebied, begin, eind)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, mentorId);
            stmt.setString(2, onderwerp);
            stmt.setString(3, beschrijving);
            stmt.setString(4, vakgebied.name());
            stmt.setDate(5, startDatum);
            stmt.setDate(6, eindDatum);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------------------Nieuwe mentor toevoegen------------------------------------------------------------
    public int insertMentor(String voornaam, String familienaam, String email, String wachtwoord, int vestigingId) {
        try {
            // Check op duplicate e-mail
            String checkSql = "SELECT COUNT(*) FROM gebruikers WHERE email = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Dit e-mailadres is al in gebruik.");
                return -1;
            }

            // Insert gebruiker
            String sql1 = "INSERT INTO gebruikers (voornaam, familienaam, email, wachtwoord) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt1 = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            stmt1.setString(1, voornaam);
            stmt1.setString(2, familienaam);
            stmt1.setString(3, email);
            stmt1.setString(4, wachtwoord);
            stmt1.executeUpdate();

            ResultSet generatedKeys = stmt1.getGeneratedKeys();
            if (generatedKeys.next()) {
                int gebruikerId = generatedKeys.getInt(1);
                String sql2 = "INSERT INTO stagementoren (id, vestiging_id) VALUES (?, ?)";
                PreparedStatement stmt2 = con.prepareStatement(sql2);
                stmt2.setInt(1, gebruikerId);
                stmt2.setInt(2, vestigingId);
                stmt2.executeUpdate();
                return gebruikerId;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Fout bij toevoegen mentor: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

}
