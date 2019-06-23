
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @version Adreli_2_Threads_Master
 * 
 * pScrumSprint2I
 * ControllerThread.java
 * 
 * @author Jan-Hendrik Hausner
 * @author John Budnik
 * @author Luca Weinmann
 * 
 * 22.04.2019
 */
public class Controler {
	/** {@link #pfad} Variable für den Dateipfad*/
	private static Path pfad = Paths.get("adreli.csv");
	
	/** {@link #POSPIPE1} Output für Pipe 1
	 *  {@link #PISPIPE1} Input für Pipe 1
	 *  Beide kleineren Pipes zusammen realisieren 
	 *  Verwaltungsfunktion 3 {@link User#personSichern()}
	 */
	public static final PipedOutputStream POSPIPE1= new PipedOutputStream();
	public static final PipedInputStream PISPIPE1 = new PipedInputStream();
	
	/** 
	 *  {@link #POSPIPE2} Output für Pipe 2
	 *  {@link #PISPIPE2} Input für Pipe 1
	 *  Beide kleineren Pipes zusammen realiseren
	 *  Verwaltungsfunktion 4 {@link User#personenLaden()}
	 */
	public static final PipedOutputStream POSPIPE2 = new PipedOutputStream();
	public static final PipedInputStream PISPIPE2 = new PipedInputStream();

	
	/**
	 * main
	 * @param args
	 * void
	 * 
	 * Startpunkt des Programms
	 * 1 .Die Pipe 1 und Pipe 2 werden miteinander verbunden
	 * 2. Es werden die Threads der Klassen {@link User} und
	 * {@link ModelThread} erstellt.
	 * 3. Der erste Thread mit dem User-Interface wird gestartet.
	 *    Der zweite Thread threadModel wird gestartet.
	 * 	Das Main-Methode kann erst weiterlaufen, sobald der erste Thread
	 *  ein Dämon ist. Erst dann kann der zweite Thread vom User gestartet 
	 *  werden.
	 *  Dies hat den Grund, dass dem Entwicklerteam nur eine virtuelle
	 *  Maschine zur Verfügung steht. Sobald das Team eine zweite virtuelle 
	 *  Maschine zur Verfügung steht, werden alle bisher ungetesteten
	 *  Akzeptanzkriterien geprüft 
	 *  und ggf. der Programmcode angepasst (Synchronisation)
	 *  4. Das Programm wird verlassen, wenn kein Thread mehr aktiv ist.
	 */
	public static void main(String[]args) {
		try {
			POSPIPE1.connect(PISPIPE1);
			PISPIPE2.connect(POSPIPE2); //1

			Thread threadView1 = new Thread(new User(POSPIPE1, PISPIPE2));
			Thread threadView2 = new Thread(new User(POSPIPE1, PISPIPE2));
			Thread threadModel = new Thread(
					new Server(pfad, PISPIPE1, POSPIPE2)); //2

			threadView1.start();
			threadModel.start();
			threadView1.join(); 
			threadView2.start();
			threadView2.join(); //3
		}
		catch (InterruptedException ie) {
			System.out.println(ie.toString());
		}
		catch (IOException ioe) {
			System.out.println(ioe.toString());
		}
		System.exit(0); //4
	}	
}