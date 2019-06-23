


import java.io.Serializable;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @version Adreli_2_Threads_Master
 * 
 * pScrumSprint2I
 * Person.java
 * 
 * @author Jan-Hendrik Hausner
 * @author John Budnik
 * @author Luca Weinmann
 * 
 * 22.04.2019
 *  
 */
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String vorname;
	private String anrede;
	private String strasse;
	private String plz;
	private String ort;
	private String telefon;
	private String fax;
	private String bemerkung;
	/** Array zur Ausgabe der Variablennamen auf dem Bildschirm */
	public static final String [] AUSGABE = {
			new String("     Name: "),
			new String("  Vorname: "),
			new String("   Anrede: "),
			new String("  Strasse: "),
			new String("      PLZ: "),
			new String("      Ort: "),
			new String("  Telefon: "),
			new String("      Fax: "),
			new String("Bemerkung: ")
	};
	
	/**
	 * Konstuktor der Klasse Person
	 * 
	 * Für Verwaltungsfunktion 2 {@link User#personAufnehmen()}
	 * ist
	 * @param eingabe = true;
	 * --> Die Eingabe der Daten erfolgt
	 * 
	 * Für verwaltungsfunktion 4 {@link Server #dateiLaden()} ist
	 * @param eingabe = false;
	 * --> Das Speichern der Personendatensätze erfolgt mit Setter-Methoden
	 * 
	 * Für Verwaltungsfunktion 6 {@link Server #nummerieren()} ist
	 * @param eingabe = false;
	 * --> Das Zwischenspeichern der Pesonendatensätz erfolgt mit
	 * Setter-Methoden
	 */
	public Person(boolean eingabe) {
		if (eingabe) {
			this.eingabeName();
			this.eingabeVorname();
			this.eingabeAnrede();
			this.eingabeStrasse();
			this.eingabePlz();
			this.eingabeOrt();
			this.eingabeTelefon();
			this.eingabeFax();
			this.eingabeBemerkung();
		}
	}

	
	/**
	 * Hilfsfunktion für {@link User#personenAuflisten()}
	 * personenAuflisten
	 * void
	 * 
	 * Die Variablen werden nacheinander durch folgende Syntax ausgegeben
	 * {@link #AUSGABE} + Varialbenwert
	 */
	public void personenAuflisten() {
		 System.out.println(AUSGABE[0].concat(this.name));
		 System.out.println(AUSGABE[1].concat(this.vorname));
		 System.out.println(AUSGABE[2].concat(this.anrede));
		 System.out.println(AUSGABE[3].concat(this.strasse));
		 System.out.println(AUSGABE[4].concat(this.plz));
		 System.out.println(AUSGABE[5].concat(this.ort));
		 System.out.println(AUSGABE[6].concat(this.telefon));
		 System.out.println(AUSGABE[7].concat(this.fax));
		 System.out.println(AUSGABE[8].concat(this.bemerkung)+"\n");
	}

	
	/**
	 * Funktion für {@link User#personAufnehmen()}
	 * eingabeName
	 * void
	 * Wenn Eingabe Ausdruck nicht trifft erfolgt Rekursion
	 */
	public void eingabeName() {
		Scanner sc = new Scanner(System.in);
		System.out.print(Person.AUSGABE[0]);
		this.name = sc.nextLine().trim().replaceAll(" +"," ");
		if (!Pattern.matches("([A-ZÜÖÄa-züöäß -])+", this.name)) {
			this.eingabeName();
		}
	}
	
	
	/**
	 * Funktion für {@link User#personAufnehmen()}
	 * eingabeVorname
	 * void
	 * 
	 * Wenn Eingabe Ausdruck nicht trifft erfolgt Rekursion
	 */
	public void eingabeVorname() {
		Scanner sc = new Scanner(System.in);
		System.out.print(Person.AUSGABE[1]);
		this.vorname = sc.nextLine().trim().replaceAll(" +", " ");
		if (!Pattern.matches("([A-ZÜÖÄa-züöäß -])+", this.vorname)) {
			this.eingabeVorname();
		}
	}
	
	
	/**
	 * Funktion für {@link User#personAufnehmen()}
	 * eingabeOrt
	 * void
	 * 
	 * Wenn Eingabe Ausdruck nicht trifft erfolgt Rekursion
	 */
	public void eingabeOrt( ) {
		Scanner sc = new Scanner(System.in);
		System.out.print(Person.AUSGABE[5]);
		this.ort = sc.nextLine().trim().replaceAll(" +"," ");
		if (!Pattern.matches("([A-ZÜÖÄa-züöäß -])+", this.ort)) {
			this.eingabeOrt();
		}
	}
	
	
	/**
	 * Funktion für {@link User#personAufnehmen()}
	 * eingabeAnrede
	 * void
	 * 
	 * Wenn Eingabe Ausdruck nicht trifft erfolgt Rekursion
	 */
	public void eingabeAnrede() {
		Scanner sc = new Scanner(System.in);
		System.out.print(Person.AUSGABE[2]);
		this.anrede = sc.nextLine().trim().replaceAll(" +"," ");
		String upperAnrede = this.anrede.toUpperCase();
		if (!Pattern.matches("(HERR|FRAU|DIVERS)",upperAnrede)) {
			this.eingabeAnrede();
		}
	}
	
	
	/**
	 * Funktion für {@link User#personAufnehmen()}
	 * eingabeStrasse
	 * void
	 * 
	 * Wenn Eingabe Ausdruck nicht trifft erfolgt Rekursion
	 */
	public void eingabeStrasse() {
		Scanner sc = new Scanner(System.in);
		System.out.print(Person.AUSGABE[3]);
		this.strasse = sc.nextLine().trim().replaceAll(" +", " ");
		if (!Pattern.matches("([A-Za-zäöüß -])*(\\d){1,4}([ ])*([A-Za-z])?",
				this.strasse)) {
			this.eingabeStrasse();
		}
	}
	
	
	/**
	 * Funktion für {@link User#personAufnehmen()}
	 * eingabeBemerkung
	 * void
	 * 
	 * Wenn Eingabe Ausdruck nicht trifft erfolgt Rekursion
	 */
	public void eingabeBemerkung() {
		Scanner sc = new Scanner(System.in);
		System.out.print(Person.AUSGABE[8]);
		this.bemerkung = sc.nextLine().trim().replaceAll(" +", " ");
	}
	
	
	/**
	 * Funktion für {@link User#personAufnehmen()}
	 * eingabePlz
	 * void
	 * 
	 * Wenn Eingabe Ausdruck nicht trifft erfolgt Rekursion
	 */
	public void eingabePlz() {
		Scanner scanner = new Scanner(System.in);
		System.out.print(Person.AUSGABE[4]);
		this.plz = scanner.nextLine().trim().replaceAll(" +","");
		if (!Pattern.matches("\\d{5}", this.plz)) {
			this.eingabePlz();
		}
	}
	
	
	/**
	 * Funktion für {@link User#personAufnehmen()}
	 * eingabeTelefon
	 * void
	 * 
	 * Wenn Eingabe Ausdruck nicht trifft erfolgt Rekursion
	 */
	public void eingabeTelefon() {
		Scanner scanner = new Scanner(System.in);
		System.out.print(Person.AUSGABE[6]);
		this.telefon = scanner.nextLine().trim().replaceAll(" +","");
		if (!Pattern.matches("(\\+)?(\\d){2}?(\\(0\\))?([0-9 -])+",
				this.telefon)) {
			this.eingabeTelefon();
		}
	}
	
	
	/**
	 * Funktion für {@link User#personAufnehmen()}
	 * eingabeFax
	 * void
	 * 
	 * Wenn Eingabe Ausdruck nicht trifft erfolgt Rekursion
	 */
	public void eingabeFax() {
		Scanner scanner = new Scanner(System.in);
		System.out.print(Person.AUSGABE[7]);
		this.fax = scanner.nextLine().trim().replaceAll(" +","");
		if (!Pattern.matches("(\\+)?(\\d){2}?(\\(0\\))?([0-9 -])+", this.fax)) {
			this.eingabeFax();
		}
	}
	
	
	/** Getter- und Setter-Methoden für alle Objektvariablen 
	 *  gebraucht in
	 *  {@link Server#dateiSichern()}
	 *  {@link Server#dateiLaden()}
	 *  {@link Server#nummerieren()}
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	
	
	public String getVorname() {
		return this.vorname;
	}
	
	
	public void setAnrede(String anrede) {
		this.anrede = anrede;
	}
	
	
	public String getAnrede() {
		return this.anrede;
	}
	
	
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}
	
	
	public String getStrasse() {
		return this.strasse;
	}
	
	
	public void setPlz(String plz) {
		this.plz = plz;
	}
	
	
	public String getPlz() {
		return this.plz;
	}
	
	
	public void setOrt(String ort) {
		this.ort = ort;
	}
	
	
	public String getOrt() {
		return this.ort;
	}
	
	
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	
	
	public String getTelefon() {
		return this.telefon;
	}
	
	
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	
	public String getFax() {
		return this.fax;
	}
	
	
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	
	public String getBemerkung() {
		return this.bemerkung;
	}
}
