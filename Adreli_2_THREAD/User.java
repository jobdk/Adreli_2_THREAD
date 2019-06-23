
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @version Adreli_2_Threads_Master
 * 
 * pScrumSprint2I
 * UserThread.java
 * 
 * @author Jan-Hendrik Hausner
 * @author John Budnik
 * @author Luca Weinmann
 * 
 * 22.04.2019
 */

public class User implements Runnable {
	/** {@link #personenListe} -> lokaler Speicher des User für Personen */
	private ArrayList<Person> personenListe;
	/** {@link #posPipe1} Output für Pipe 1 {@link #personSichern()}*/
	private PipedOutputStream posPipe1;
	/** {@link #pisPipe2} Input für Pipe 2 {@link #personenLaden()}*/
	private PipedInputStream pisPipe2;

	
	/**
	 * Konstruktor der Klasse
	 * @param posPipe1
	 * @param pisPipe2
	 */
	public User(PipedOutputStream posPipe1, PipedInputStream pisPipe2) {
		this.personenListe = new ArrayList<>();
		this.posPipe1 = posPipe1;
		this.pisPipe2 = pisPipe2;
	}

	
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			this.menu();
		}
		/* Solange die Interrupt-Flag nicht true ist, wird der Thread nicht
		 * zum Dämon 
		 */
	}
	
	
	/**
	 * Hauptmenü
	 * menu
	 * void
	 * 
	 * Der User sieht zuerst das Hauptmenü.
	 * Anschließend kann der Benutzer die gewünschte Zahl eingeben um die
	 * dafür vorgesehene Methode aufzurufen:
	 * Verwaltungsfunktion 1 {@link #personAufnehmen()}
	 * Verwaltungsfunktion 2 {@link #personenAuflisten()}
	 * Verwaltungsfunktion 3 {@link #personSichern()}
	 * Verwaltungsfunktion 4 {@link #personenLaden()}
	 * Verwaltungsfunktion 5 {@link #personenSortieren()}
	 * Verwaltungsfunktion 6 {@link #nummerieren()}
	 * Verwaltungsfunktion 7 {@link #programmVerlassen()}
	 */
	public void menu() {
		System.out.println(" ____________________________________________");
		System.out.println("|                                            |");
		System.out.println("|	   ADRELI - Adressverwaltung         |");
		System.out.println("|--------------------------------------------|");
	    System.out.println("|       Wollen Sie...                        |");
		System.out.println("|                                            |");
		System.out.println("|             Eine neue Person aufnehmen > 1 |");
		System.out.println("|                      Records auflisten > 2 |");
		System.out.println("|          Records in eine Datei sichern > 3 |");
		System.out.println("|          Records aus einer Datei laden > 4 |");
		System.out.println("|            in-memory Records sortieren > 5 |");
		System.out.println("|               Datei Zeilen nummerieren > 6 |");
		System.out.println("|                 Das Programm verlassen > 7 |");
		System.out.println("|____________________________________________|");
		System.out.println();
		try {
			Scanner sc = new Scanner(System.in);
			int auswahl = sc.nextInt();
			switch (auswahl) {
				case 1 : this.personAufnehmen();
					break;
				case 2 : this.personenAuflisten();
					break;
				case 3 : this.personSichern();
					break;
				case 4 : this.personenLaden();
					break;
				case 5 : this.personenSortieren();
					break;
				case 6 : this.nummerieren();
					break;
				case 7 : this.programmVerlassen();
					break;
				default : System.out.println
							("Sie haben keine Zahl von 1-7 eingegeben!");
			}
		}
		catch (InputMismatchException ime) {
			System.out.println("Keine Zahl eingegeben!");
		}
	}
	
	
	/**
	 * Verwaltungsfunktion 1
	 * personAufnehmen
	 * void
	 * 
	 * Zuerst wird ein neues Objekt der Klasse {@link Person} erstellt.
	 * Der Konstruktur der Klasse {@link Person} steuert die Eingabe.
	 * 
	 * Es folgen zwei Fragen an den Benutzer:
	 * 1. "Stimmt's? (J/N) mit Varialbe weiter1
	 * 2. "Noch eine Person aufnehmen? (J/N)" mit Variable weiter2
	 * Danach wird mit {@link Pattern#matches()} die Eingabe auf die Ausdrücke
	 * "[nN]" und "[Jj]" überprüft.
	 * Die Variablen weiter1 und weiter2 werden true gesetzt, falls der Benutzer
	 * keinen der Ausdrücke benutzt hat.
	 * --> Die Eingabe muss wiederholt werden.
	 */
	public void personAufnehmen() {
		System.out.println("Geben sie bitte die Daten ein:"+"\n");
		Person p = new Person(true);

		boolean weiter1;
		do {
			System.out.println("Stimmt's (J/N)");
			Scanner sc = new Scanner(System.in);
			String eingabe1 = sc.nextLine();
			if (eingabe1.matches("[nN]")) {
				weiter1 = false;
				this.personAufnehmen();
			}
			else if (eingabe1.matches("[jJ]")) {
				weiter1 = false;
				this.personenListe.add(p);
				boolean weiter2 = true;
				do {
					System.out.println("Noch eine Person aufnehmen? (J/N)");
					String eingabe2 = sc.nextLine();
					if (eingabe2.matches("[Jj]")) {
						this.personAufnehmen();
						weiter2 = false;
					}
					else if (eingabe2.matches("[nN]")) {
						weiter2 = false;
					}
				}
				while (weiter2);
			}
			else {
				weiter1 = true;
			}
		}
		while (weiter1);
	}
	
	
	/**
	 * Verwaltungsfunktion 2
	 * personenAuflisten
	 * void
	 * 
	 * Die Datensätze werden objektweise aus dem lokalen Speicher
	 * {@link #personenListe} geladen und mit der Funktion 
	 * {@link Person#personenAuflisten()} dem User angezeigt.
	 * 
	 * Es wird überprüft, ob noch ein weiterer Datensatz im Speicher
	 * {@link #personenListe} vorliegt. Um zum nächsten Datensatz zu gelangen, 
	 * muss die Enter-Taste gedrückt werden.
	 */
	public void personenAuflisten() {
		int anzahl = this.personenListe.size();
		for (Iterator<Person> ausgabePerson = this.personenListe.iterator();
				ausgabePerson.hasNext(); ) {
			ausgabePerson.next().personenAuflisten();
			
			Scanner scanner = new Scanner(System.in);
			if (anzahl > 1) {
				System.out.println("Es gibt noch "+(--anzahl)+" Datensätze.");
				System.out.println("Weiter mit \"Enter\"");
				String eingabe = scanner.nextLine();
			}
			else {
				System.out.println("Keine Datensätze mehr da!");
				System.out.println("Weiter mit \"Enter\"");
				String eingabe = scanner.nextLine();
			}
		}
	}
	
	
	/**
	 * Verwaltungsfunktion 3
	 * personSichern
	 * void
	 * 
	 * 1. Mit der 3 in {@link #posPipe1} wird die Methode im
	 * {@link Server#dateiSichern()} aufgerufen.
	 * 2. Der lokale Speicher {@link #personenListe} in die Pipe
	 * {@link #posPipe1} übergeben,
	 */
	public void personSichern() {
		try {
			posPipe1.write(3); //1
			
			ObjectOutputStream oos = new ObjectOutputStream(
					new BufferedOutputStream((posPipe1)));
			oos.writeObject(personenListe);
			oos.flush();
			//2
		}
		catch (IOException ioe) {
			System.out.println(ioe.toString());
		}
	}
	
	
	/**
	 * Verwaltungsfunktion 4
	 * personenLaden
	 * void
	 * 
	 * 1. Mit der 4 in {@link #posPipe1} wird die Methode im
	 * {@link Server#dateiLaden()} aufgerufen.
	 * 2. Die Personen in der {@link #pisPipe2} werden in den
	 * Zwischenspeicher zwischenListe gespeichert.
	 * 3. Die Personen werden vom Zwischenspeicher zwischenListe
	 * in die {@link #personenListe} objektweise hinzugefügt.
	 */
	public void personenLaden() {
		ArrayList<Person> zwischenListe = new ArrayList<>();
		try {
			posPipe1.write(4); //1
			
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream((pisPipe2)));
			zwischenListe = (ArrayList<Person>) ois.readObject();//2
			
			for (Person zwischenPerson : zwischenListe) {
				this.personenListe.add(zwischenPerson);
			}//3
		}
		catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	
	/**
	 * Verwaltungsfunktion 5
	 * personenSortieren
	 * void
	 */
	public void personenSortieren() {
		Collections.sort(this.personenListe, 
				Comparator.comparing(Person::getName));
	}

	
	/**
	 * Verwaltungsfunktion 6
	 * nummerieren
	 * void
	 * 
	 * Die Funktion {@link Server#nummerieren()} im ModellThread
	 * wird durch die 6 in {@link #posPipe1} informiert, dass die Datensätze
	 * in der Datei nummeriert werden sollen.
	 */
	public void nummerieren() {
		try {
			this.posPipe1.write(6);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	/**
	 * Verwaltungsfunktion 7
	 * programmVerlassen
	 * void
	 * 
	 * Der Interrupt-Flag des jeweiligen Threads wird true gesetzt.
	 * 
	 */
	public void programmVerlassen() {
		System.out.println("Das Programm wurde verlassen!");
		Thread.currentThread().interrupt();
	}
}
