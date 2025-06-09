package presentatie;

import data.DataLayer;
import logica.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    private JList<String> bedrijfList;
    private JList<String> vestigingList;

    private JTable mentorTable;
    private JTextField voornaamField, familienaamField, emailField, wachtwoordField;
    private JComboBox<VestigingItem> vestigingComboBox;

    private JComboBox<Integer> jaarComboBox;
    private JTable stageTable;
    private JTextField onderwerpField, beschrijvingField, startDatumField;
    private JComboBox<Vakgebied> vakgebiedComboBox;
    private JComboBox<MentorItem> mentorComboBox;

    private final DataLayer data;

    public MainFrame() {

        data = new DataLayer();
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        JButton laadCsvButton = new JButton("Laad CSV Data");
        laadCsvButton.addActionListener(e -> {
            data.importeerCsvBestandenAlsLeeg(
                    "src/main/resources/bedrijven.csv",
                    "src/main/resources/vestigingen.csv",
                    "src/main/resources/mentoren.csv"
            );
            refreshData();
        });
        mainPanel.add(laadCsvButton, BorderLayout.SOUTH);

        setupBedrijvenVestigingenTab();
        setupMentorenTab();
        setupStagesTab();

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(mainPanel);

        setTitle("Stagetool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupBedrijvenVestigingenTab() {
        JPanel panel = new JPanel(new BorderLayout());
        bedrijfList = new JList<>();
        vestigingList = new JList<>();

        bedrijfList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String geselecteerd = bedrijfList.getSelectedValue();
                DefaultListModel<String> vestigingModel = new DefaultListModel<>();
                for (VestigingItem v : data.getVestigingen()) {
                    if (v.getBedrijfsNaam().equals(geselecteerd)) {
                        vestigingModel.addElement(v.getVestigingsNaam());
                    }
                }
                vestigingList.setModel(vestigingModel);
            }
        });

        DefaultListModel<String> bedrijfModel = new DefaultListModel<>();
        for (VestigingItem v : data.getVestigingen()) {
            if (!bedrijfModel.contains(v.getBedrijfsNaam())) {
                bedrijfModel.addElement(v.getBedrijfsNaam());
            }
        }

        bedrijfList.setModel(bedrijfModel);
        panel.add(new JScrollPane(bedrijfList), BorderLayout.WEST);
        panel.add(new JScrollPane(vestigingList), BorderLayout.CENTER);
        tabbedPane.add("Bedrijven & Vestigingen", panel);
    }

    private void setupMentorenTab() {
        JPanel panel = new JPanel(new BorderLayout());

        mentorTable = new JTable();
        updateMentorTable();

        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        voornaamField = new JTextField();
        familienaamField = new JTextField();
        emailField = new JTextField();
        wachtwoordField = new JTextField();
        vestigingComboBox = new JComboBox<>(data.getVestigingen().toArray(new VestigingItem[0]));

        formPanel.add(new JLabel("Voornaam:")); formPanel.add(voornaamField);
        formPanel.add(new JLabel("Familienaam:")); formPanel.add(familienaamField);
        formPanel.add(new JLabel("E-mail:")); formPanel.add(emailField);
        formPanel.add(new JLabel("Wachtwoord:")); formPanel.add(wachtwoordField);
        formPanel.add(new JLabel("Vestiging:"));
        vestigingComboBox = new JComboBox<>();
        for (VestigingItem v : data.getVestigingen()) {
            vestigingComboBox.addItem(v);
        }
        formPanel.add(vestigingComboBox);

        JButton voegToeButton = new JButton("Voeg toe");
        voegToeButton.addActionListener(e -> {
            String voornaam = voornaamField.getText();
            String familienaam = familienaamField.getText();
            String email = emailField.getText();
            String wachtwoord = wachtwoordField.getText();
            VestigingItem geselecteerdeVestiging = (VestigingItem) vestigingComboBox.getSelectedItem();

            if (voornaam.isBlank() || familienaam.isBlank() || email.isBlank() || wachtwoord.isBlank() || geselecteerdeVestiging == null) {
                JOptionPane.showMessageDialog(this, "Gelieve alle velden in te vullen.");
                return;
            }

            int id = data.insertMentor(voornaam, familienaam, email, wachtwoord, geselecteerdeVestiging.getId());
            updateMentorTable();
            mentorComboBox.addItem(new MentorItem(id, voornaam, familienaam, geselecteerdeVestiging.getVestigingsNaam(), geselecteerdeVestiging.getBedrijfsNaam()));
        });

        formPanel.add(new JLabel());
        formPanel.add(voegToeButton);

        panel.add(new JScrollPane(mentorTable), BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);
        tabbedPane.add("Mentoren", panel);
    }

    private void updateMentorTable() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Voornaam", "Familienaam", "Vestiging", "Bedrijf"}, 0);
        for (MentorItem m : data.getMentoren()) {
            model.addRow(new Object[]{
                    m.getId(), m.getVoornaam(), m.getFamilienaam(), m.getVestigingNaam(), m.getBedrijfNaam()
            });
        }
        mentorTable.setModel(model);
    }

    private void setupStagesTab() {
        JPanel panel = new JPanel(new BorderLayout());

        // Filter (jaar selecteren)
        JPanel filterPanel = new JPanel(new FlowLayout());
        ArrayList<Integer> jaren = data.getJarenMetStages();
        jaarComboBox = new JComboBox<>(jaren.toArray(new Integer[0]));
        jaarComboBox.addActionListener(e -> updateStageTable());
        filterPanel.add(new JLabel("Jaar:"));
        filterPanel.add(jaarComboBox);

        // Stage overzicht
        stageTable = new JTable();
        if (!jaren.isEmpty()) {
            jaarComboBox.setSelectedIndex(0);
            updateStageTable();
        } else {
            stageTable.setModel(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"ID", "Onderwerp", "Beschrijving", "Begin", "Eind", "Vakgebied", "Stagementor"}
            ));
        }

        // Formulier voor nieuwe stage
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        onderwerpField = new JTextField();
        beschrijvingField = new JTextField();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String vandaag = LocalDate.now().format(formatter);
        startDatumField = new JTextField(vandaag);
        vakgebiedComboBox = new JComboBox<>(Vakgebied.values());
        mentorComboBox = new JComboBox<>();
        for (MentorItem m : data.getMentoren()) {
            mentorComboBox.addItem(m);
        }

        formPanel.add(new JLabel("Onderwerp:")); formPanel.add(onderwerpField);
        formPanel.add(new JLabel("Beschrijving:")); formPanel.add(beschrijvingField);
        formPanel.add(new JLabel("Startdatum:")); formPanel.add(startDatumField);
        formPanel.add(new JLabel("Vakgebied:")); formPanel.add(vakgebiedComboBox);
        formPanel.add(new JLabel("Stagementor:")); formPanel.add(mentorComboBox);
        formPanel.add(new JLabel());
        JButton voegToeBtn = new JButton("Voeg stage toe");
        formPanel.add(voegToeBtn);

        // Toevoeg-knop actie
        voegToeBtn.addActionListener(e -> {
            try {
                String onderwerp = onderwerpField.getText().trim();
                String beschrijving = beschrijvingField.getText().trim();
                String datumText = startDatumField.getText().trim();
                Vakgebied vakgebied = (Vakgebied) vakgebiedComboBox.getSelectedItem();
                MentorItem mentor = (MentorItem) mentorComboBox.getSelectedItem();

                if (onderwerp.equals("") || datumText.equals("") || vakgebied == null || mentor == null) {
                    JOptionPane.showMessageDialog(this, "Gelieve alle velden correct in te vullen.", "Fout", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate start = LocalDate.parse(datumText, formatter);
                Date sqlStart = Date.valueOf(start);

                data.insertStage(mentor.getId(), onderwerp, beschrijving, vakgebied, sqlStart);

                // Refresh comboBox en tabel
                ArrayList<Integer> nieuweJaren = data.getJarenMetStages();
                jaarComboBox.setModel(new DefaultComboBoxModel<>(nieuweJaren.toArray(new Integer[0])));
                jaarComboBox.setSelectedItem(start.getYear());
                updateStageTable();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Fout bij toevoegen stage:\nGebruik het datumformaat dd-MM-yyyy.",
                        "Fout", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Onverwachte fout:\n" + ex.getMessage(),
                        "Fout", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(stageTable), BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);
        tabbedPane.add("Stages", panel);
    }


    private void updateStageTable() {
        Object geselecteerd = jaarComboBox.getSelectedItem();
        if (geselecteerd == null) {
            stageTable.setModel(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"ID", "Onderwerp", "Beschrijving", "Begin", "Eind", "Vakgebied", "Stagementor"}
            ));
            return;
        }

        int jaar = ((Integer) geselecteerd).intValue();
        ArrayList<Stages> stages = data.getStagesVoorJaar(jaar);
        ArrayList<MentorItem> alleMentoren = data.getMentoren();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DefaultTableModel model = new DefaultTableModel(new Object[]{
                "ID", "Onderwerp", "Beschrijving", "Begin", "Eind", "Vakgebied", "Stagementor"
        }, 0);

        for (Stages s : stages) {
            String beginStr = s.getBegin().toLocalDate().format(formatter);
            String eindStr = s.getEind().toLocalDate().format(formatter);

            String mentorNaam = "Onbekend";
            for (MentorItem m : alleMentoren) {
                if (m.getId() == s.getMentorId()) {
                    mentorNaam = m.toString();
                    break;
                }
            }

            model.addRow(new Object[]{
                    s.getId(),
                    s.getOnderwerp(),
                    s.getBeschrijving(),
                    beginStr,
                    eindStr,
                    s.getVakgebied(),
                    mentorNaam
            });
        }

        stageTable.setModel(model);
    }
    private void refreshData() {
        // Bedrijven en vestigingen
        DefaultListModel<String> bedrijfModel = new DefaultListModel<>();
        for (VestigingItem v : data.getVestigingen()) {
            if (!bedrijfModel.contains(v.getBedrijfsNaam())) {
                bedrijfModel.addElement(v.getBedrijfsNaam());
            }
        }
        bedrijfList.setModel(bedrijfModel);
        vestigingList.setModel(new DefaultListModel<>());

        // Formulieren
        vestigingComboBox.removeAllItems();
        for (VestigingItem v : data.getVestigingen()) {
            vestigingComboBox.addItem(v);
        }

        mentorComboBox.removeAllItems();
        for (MentorItem m : data.getMentoren()) {
            mentorComboBox.addItem(m);
        }

        updateMentorTable();
        updateStageTable();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

