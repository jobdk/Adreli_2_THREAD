


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class Server implements Runnable {
	/** Objektvariable {@link #path} --> Pfad zum Speichern und Lesen */
	private Path path;
	/** {@link #pisPipe1} Input für Pipe 1 {@link #dateiSichern(boolean)} */
	private PipedInputStream pisPipe1;
	/** {@link #posPipe2} Output für Pipe 2 {@linkplain #dateiLaden(boolean)} */
	private PipedOutputStream posPipe2;
	/** {@link #downloadNummerieren} --> Zwischenspeicher zur Nummerierung */
	private ArrayList<Person> downloadNummerieren;
	
	
	/**
	 * Konstruktor für die Klasse
	 * @param path
	 * @param pisPipe1
	 * @param posPipe2
	 */
	public Server(Path path, PipedInputStream pisPipe1,
			PipedOutputStream posPipe2) {
		this.path = path;
		this.pisPipe1 = pisPipe1;
		this.posPipe2 = posPipe2;
	}
	
	
	public void run() {
		boolean immer = true;
		while (immer) {
			try {
				int auswahl = pisPipe1.read();
				if (auswahl == 3) {
					this.dateiSichern(true);
				}
				else if (auswahl == 4) {
					this.dateiLaden(true);
				}
				else if (auswahl == 6) {
					this.nummerieren();
				}
			}
			catch (IOException ioe) {
				System.out.println(ioe.toString());
			}
		}
	}
	
	
	/**
	 * dateiSichern
	 * @param sichern
	 * void
	 * 
	 * Für Verwaltungsfunktion 3 {@link User#personSichern()}
	 * @param sichern = true;
	 * 3.1 Erstellung Array-List personenSichern --> jeder Aufruf neuer Speicher
	 * 3.2 Erstellung gepufferter Eingabestrom --> mit Append
	 * 3.3 Daten werden von Pipe 1 gelesen
	 * 3.4 objektweises Schreiben der Daten in Datei mit {@link #path}
	 * 
	 * Für Verwaltungsfunktion 6 {@link User#nummerieren()}
	 * @param sichern = false;
	 * 4.1 Erstellung gepufferer Eingabestrom --> ohne Append --> Überschreiben
	 * 4.2 objektweises Schreiben der Nummer + Daten in Datei mi {@link #path}
	 */
	public void dateiSichern(boolean sichern) {
		if (sichern) {
			ArrayList<Person> personenSichern = new ArrayList<>();		//3.1
			try (BufferedWriter dos = new BufferedWriter(
					new OutputStreamWriter(
							Files.newOutputStream(path, 
									StandardOpenOption.APPEND,
									StandardOpenOption.CREATE)));) {	//3.2
				
				ObjectInputStream ois = new ObjectInputStream(
						new BufferedInputStream((pisPipe1)));
				personenSichern = (ArrayList<Person>) ois.readObject(); //3.3
				
				for (Person person : personenSichern) {
					dos.write(person.getName()+";");
					dos.write(person.getVorname()+";");
					dos.write(person.getAnrede()+";");
					dos.write(person.getStrasse()+";");
					dos.write(person.getPlz()+";");
					dos.write(person.getOrt()+";");
					dos.write(person.getTelefon()+";");
					dos.write(person.getFax()+";");
					dos.write(person.getBemerkung()+";");
					dos.newLine();
				}														//3.4
			}
			catch (ClassNotFoundException cnf) {
				cnf.printStackTrace();
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		else {
			try (BufferedWriter dos1 = new BufferedWriter(new OutputStreamWriter
					(Files.newOutputStream(path, StandardOpenOption.CREATE)))) {
																		//4.1
				int numberCounter = 1;
				for (Person person : downloadNummerieren) {
					dos1.write(Integer.toString(numberCounter++)+";");
					dos1.write(person.getName()+";");
					dos1.write(person.getVorname()+";");
					dos1.write(person.getAnrede()+";");
					dos1.write(person.getStrasse()+";");
					dos1.write(person.getPlz()+";");
					dos1.write(person.getOrt()+";");
					dos1.write(person.getTelefon()+";");
					dos1.write(person.getFax()+";");
					dos1.write(person.getBemerkung()+";");
					dos1.newLine();
				}														//4.2
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	
	/**
	 * dateiLaden
	 * @param laden
	 * void
	 * 
	 * Für beide Verwaltungsfunktionen
	 * Die Daten werden eingelesen durch einen gepufferten Einlesestrom
	 * mit {@link #path}. Wenn Nummerierung bereits stattgefunden hat, lese
	 * die Daten von Spalte B - J ein. Wenn keine Nummerierung vorliegt, lese
	 * die Daten von Spalte A - I ein.
	 * 
	 * Für Verwaltungsfunktion 4 {@link User#personenLaden()}
	 * @param sichern = true;
	 * 4.1 Deklaration + Initialisierung Speicher zum Download der Datensätze
	 * 4.2 Hinzufügen der Datensätze zum Speicher
	 * 4.3 Ganzer Datensatz wird über Pipe 2 und {@link #posPipe2} an 
	 * {@link User} übergeben.
	 * 
	 * 
	 * Für Verwaltungsfunktion 6 {@link User#nummerieren()}
	 * @param sichern = false;
	 * 6.1 Initialiserung Zwischenspeicher zur Nummerierung
	 * 6.2 Hinzufügen der Datensätze zum Zwischenspeicher
	 * {@link #downloadNummerieren}
	 * 
	 */
	public void dateiLaden(boolean laden) {
		this.downloadNummerieren = new ArrayList<Person>();				//4.1
		ArrayList<Person> downloaden = new ArrayList<>();				//6.1
		try (BufferedReader d = new BufferedReader(new InputStreamReader(
				Files.newInputStream(path)))) {
			int numberCounter = 1;
			String einlesen = null;
			while ((einlesen = d.readLine()) != null) {
				String einzelneInfos[] = einlesen.split(";");
				Person person = new Person(false);
				if (einzelneInfos[0].equals(Integer.toString(numberCounter))) {
					person.setName(einzelneInfos[1]);
					person.setVorname(einzelneInfos[2]);
					person.setAnrede(einzelneInfos[3]);
					person.setStrasse(einzelneInfos[4]);
					person.setPlz(einzelneInfos[5]);
					person.setOrt(einzelneInfos[6]);
					person.setTelefon(einzelneInfos[7]);
					person.setFax(einzelneInfos[8]);
					person.setBemerkung(einzelneInfos[9]);
				} else {
					person.setName(einzelneInfos[0]);
					person.setVorname(einzelneInfos[1]);
					person.setAnrede(einzelneInfos[2]);
					person.setStrasse(einzelneInfos[3]);
					person.setPlz(einzelneInfos[4]);
					person.setOrt(einzelneInfos[5]);
					person.setTelefon(einzelneInfos[6]);
					person.setFax(einzelneInfos[7]);
					person.setBemerkung(einzelneInfos[8]);
				}
				if (laden) {  
					downloaden.add(person);								//4.2
				}
				else {
					this.downloadNummerieren.add(person);				//6.2
				}
				numberCounter++;
			}
			if (laden) {
				ObjectOutputStream oos = new ObjectOutputStream(
						new BufferedOutputStream(posPipe2));
				oos.writeObject(downloaden);
				oos.flush();
			}															//4.3
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	/**
	 * Für Verwaltungsfunktion 6 {@link User#nummerieren()}
	 * nummerieren
	 * void
	 * 
	 * 1. Die Datensätze werden zuerst heruntergeladen.
	 * 2. Die Datensätze in der Datei werden mit Nummer überschrieben.
	 */
	public void nummerieren() {
		this.dateiLaden(false);											//1
		this.dateiSichern(false);										//2
	}
}
