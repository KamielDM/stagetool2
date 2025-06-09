🎓 Swing-applicatie met MySQL database

📘 Context
Je ontwikkelt een grafische desktoptoepassing in Java (Swing) voor het beheren van stages binnen de opleiding Elektronica-ICT. De toepassing maakt gebruik van een bestaande MySQL-database (stagetool) en communiceert via JDBC met de Java applicatie.

De opgave heeft een gelijkaardig onderwerp als de voorbije opgaven, maar de concrete details zijn anders. Zo zal slechts een deel van de database effectief gebruikt worden binnen de Java applicatie om het repetitieve programmeerwerk te beperken. We leggen in deze opgave de nadruk op het ophalen en toevoegen van bepaalde gegevens, inclusief een basisvorm van gebruikersbeheer.

🎯 Doelstellingen
Een Java Swing GUI bouwen die communiceert met een MySQL-database via JDBC.
Gegevens ophalen en toevoegen aan de databank op een veilige en robuuste manier.
Werken met relaties tussen tabellen (bv. koppeling tussen mentor en gebruiker).
Object georiënteerd programmeren toepassen.
🧱 Technische vereisten
🛢 Database
Gebruik het meegeleverde model stagetool.mwb om de database aan te maken (Forward Engineering).

De data krijg je aangeleverd in enkele csv-bestanden. Je kan deze inlezen vanuit Java om ze aan de database toe te voegen (zie verder).

stagetool_v3.mwb
🔃 GIT
Maak voor je IntelliJ project een GIT-project aan, analoog aan de werkwijze gevolgd bij OO-programming. Je vindt de stappen nogmaals in het document ‘Stappenplan: IntelliJ naar Git’ bij de opgave op Toledo. Er zijn een aantal aanpassingen:

Maak een groep aan met volgende naam, rechtstreeks onder je eigen ikdoeict/voornaam.familienaam groep: ‘2425-dbprog’
Deel deze groep met 3 docenten: je labodocent(en), Evert-Jan Jacobs en Tim Vermeulen
Maak een nieuw leeg (blank) project aan binnen deze groep en noem dit ‘Stagetool’.
Je git-project zou nu volgende url moeten hebben: https://gitlab.com/ikdoeict/voornaam.familienaam/2425-dbprog/stagetool (maar dan met jouw naam).

Stappenplan IntelliJ naar GIT.pdf

🧩 Te implementeren functionaliteiten
1. Verbinding met de database
Maak voor je databaseverbinding in je code gebruik van volgende gegevens:

Gebruiker: dbprog
Wachtwoord: Azerty123
Database: stagetool
Host: localhost
Poort: 3306
De connectiegegevens mogen hardcoded in je code staan, maar maximaal op 1 plaats.

2. Inlezen van voorbeelddata uit csv-bestanden
Met een knop op de gui lees je de csv-bestanden in de resources map van je project in. Deze bevatten wat voorbeelddata om je database te vullen:

bedrijven.csv: 5 bedrijven
vestigingen.csv: 2 vestigingen per bedrijf
mentoren.csv: 5 stagementoren, voor enkele van de vestigingen van de bedrijven
De csv-bestanden worden enkel verwerkt als er nog geen bedrijven in de database zitten.

Zorg ervoor dat nieuwe bedrijven, vestigingen en mentoren ook weergegeven worden in de gui in de correcte lijsten.

bedrijven.csv
vestigingen.csv
mentoren.csv
3. Weergeven van bedrijven en vestigingen
Voorzie de mogelijkheid om bedrijven en vestigingen weer te geven in de GUI.

Toon een lijst van bedrijven in een JList.
Laat de gebruiker een bedrijf selecteren uit de lijst.
Toon de vestigingen van dat bedrijf in een tweede JList.
Het toevoegen, aanpassen en verwijderen van bedrijven en vestigingen hoef je niet te voorzien.

4. Gebruikersbeheer (toevoegen en opvragen)
Voorzie een basis gui voor het beheer van gebruikers. We implementeren enkel de Stagementoren in Java. De andere gebruikers zijn eerder herhaling en hoef je niet te voorzien.

Toon een overzicht van alle mentoren met hun vestiging en bijhorend bedrijf (JTable).
Voeg een formulier toe om een nieuwe mentor toe te voegen met:
voornaam, familienaam, e-mail, wachtwoord
vestiging (selecteerbare lijst, JComboBox)
Maak nieuwe mentoren ook beschikbaar om uit te kiezen bij het maken van een nieuwe stage.
Let op: gebruikers is een overkoepelende tabel. Bij het toevoegen van een mentor moet je eerst een record toevoegen aan gebruikers, en daarna aan de stagementoren tabel.

5. Stages
Voorzie ook een start voor het beheer van stages. We gaan niet alle functionaliteit implementeren en ook enkele belangrijke relaties zitten niet in het model.

Toon een JCombobox met alle jaren waarin stages doorgaan (haal dit uit de database) en gebruik deze als filter hieronder
Toon in een JTable een overzicht van alle stages in het gekozen jaar (JComboBox hierboven)
Voorzie een knop om een stage toe te voegen:
Met onderwerp, beschrijving, vakgebied (JComboBox), stagementor (JComboBox) en start (JTextField)
Het einde is automatisch 10 weken na de start
Zonder student en begeleidende docent
Voeg nieuwe stages ook toe in de lijst hierboven (JTable)
🖼 GUI-vereisten
grafische applicatie, met één main. Deze zit in een klasse MainFrame.
Gebruik JFrame, JPanel, JTable, JList, JTextField, JComboBox, JButton, JLabel, enz.
Zorg voor een eenvoudige navigatie tussen schermen (bijv. via tabbladen of knoppen).
Geen nood aan visuele pracht: focus ligt op functionaliteit en correct gebruik van JDBC.
✅ Beoordelingscriteria
Correct gebruik van JDBC (connectie, prepared statements, resultsets)
Objectgeoriënteerde structuur (bv. klassen voor Bedrijf, Vestiging, Mentor, enz.).
Correcte implementatie van relaties tussen objecten: overerving en compositie
Heldere en gestructureerde GUI
Correcte foutafhandeling en inputvalidatie bij de formulieren
