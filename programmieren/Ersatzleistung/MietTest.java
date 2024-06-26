/**
 * Testprogramm fuer die Klasse MietObj.
 *
 * @author Nikola Kurzac (n.kurzac@ostfalia.de);
 *
 * @version 1.09, 23.11.19
 */
import java.io.*;
import de.vfh.gp1.bib.Konsole;



public class MietTest {

    /**
     * Main-Method zum Starten und Testen der Klasse MietObj. Diese main-Methode
     * ist der definierte Einstiegspunkt fuer die Ausfuehrung des Programs und
     * erlaubt das Programm zu testen.
     *
     * @param args
     *            die Argumente, die man im Allgemeinen fuer die Ausfuehrung
     *            geben kann
     */

    public static void main(String[] args) throws IOException {

		// Hinweis zum Programmzweck und Eingabe
		System.out.println("Dies ist ein Programm zur Berechnung von Kosten rund um die Miete");
		System.out.println("Geben Sie bitte bei Kommazahlen ein Punkt statt Kommar ein");

		// leere Zeile als Abstand
		System.out.println();

		// Fragen ob mit Default Mietzins oder eigenem rechnen.
		char wert = Konsole.getInputChar("Wollen Sie ein eigenen Mietzins angeben (j/n) ?");
		MietObj obj;
		int grundflaeche;


		switch (wert) {
			case 'j': 	grundflaeche = Konsole.getInputInt("Geben Sie Ihre Grundfl�che ein : ");
						float mietzins = Konsole.getInputFloat("Geben Sie Ihren Mietzins ein : ");
						obj = new MietObj(grundflaeche, mietzins);	// Hier werden alle rechnungen mit dem vorgegeben wert gerechnet.
						System.out.println("Kaltmiete: " + obj.kaltmiete());
						System.out.println("Verbrauchskosten: " + obj.verbrauchskosten());
						System.out.println("Heizkosten: " + obj.heizkosten());
						System.out.println("Warmmiete: " + obj.warmmiete());
				break;

			default:	// Hier mit dem angegebenen.
						// Fragen nach Grundflaeche
						grundflaeche = Konsole.getInputInt("Geben Sie Ihre Grundfl�che ein : ");

						obj = new MietObj(grundflaeche);
						System.out.println("Kaltmiete: " + obj.kaltmiete());
						System.out.println("Verbrauchskosten: " + obj.verbrauchskosten());
						System.out.println("Heizkosten: " + obj.heizkosten());
						System.out.println("Warmmiete: " + obj.warmmiete());
		}


		// Hier wird nach der Kaltmiete oder Warmmiete gefragt
		char miete = Konsole.getInputChar("Wollen Sie die Kalt- oder Warmmiete berrechnen? (k/w) ");
		if (miete == 'k') {
			System.out.println("Die Kaltmiete betr�gt : "+ obj.kaltmiete());
		}else{
			System.out.println("Die Warmmiete betr�gt : "+ obj.warmmiete());
		}

		/** Testdurchlauf 1:
		*	Der erste Durchlauf zeigt das bei einem vorgegebenen Mietzins bei Kaltmiete und Verbruachskosten
		*	keine Kosten anfallen, da aber der Betreitstellungsgetrag 15� betraegt dank der art und weise wie
		*	die Heizkosten und Warmmiete berechnet werden sollen trotz einer grundflaeche von 0 qm dennoch kosten
		* 	anfallen.
		*/
		obj = new MietObj(0);
		System.out.println();
		System.out.println("Test 1 mit grundflaeche = 0: ");
		System.out.println("Kaltmiete: " + obj.kaltmiete());
		System.out.println("Verbrauchskosten: " + obj.verbrauchskosten());
		System.out.println("Heizkosten: " + obj.heizkosten());
		System.out.println("Warmmiete: " + obj.warmmiete());

		/** Testdurchlauf 2:
		*	Hier sieht man das dank der art wie die kaltmiete betrechnet wird ein negativer betrag auftauch.
		*	Der nagative Mietzins hat keine auswirkungen auf  die verbrauchskosten oder die heizkosten, daf�r
		*	zeigt sich aber eine sehr geringe summe bei der warmmiete da dort der mietzins indirekt eine auswirkung hat.
		*/
		System.out.println();
		System.out.println("Test 2 mit grundflaeche = 50 und mietzins = -20: ");
		obj = new MietObj(50, -20);
		System.out.println("Kaltmiete: " + obj.kaltmiete());
		System.out.println("Verbrauchskosten: " + obj.verbrauchskosten());
		System.out.println("Heizkosten: " + obj.heizkosten());
		System.out.println("Warmmiete: " + obj.warmmiete());

		/** Testdurchlauf 3:
		*	Die Grundflaeche wurde nun auf 100 gesetzt und somit wirkt sich das auf alle Kosten aus die nicht mit dem
		*	Mietzins in direkter verbindung stehen. Die kaltmiete betraegt dank dem Mietzins von 0� weiterhin 0�, die
		*	anderen Kosten haben sich entstrechend der grundflaeche angepasst.
		*/
		System.out.println();
		System.out.println("Test 3 mit grundflaeche = 100 und mietzins = 0: ");
		obj = new MietObj(100, 0);
		System.out.println("Kaltmiete: " + obj.kaltmiete());
		System.out.println("Verbrauchskosten: " + obj.verbrauchskosten());
		System.out.println("Heizkosten: " + obj.heizkosten());
		System.out.println("Warmmiete: " + obj.warmmiete());

		/** Testdurchlauf 4:
		*	Beim letzten Durchlauf ist die grundflaeche wieder bei 0 und daf�r der Mietzins bei -100�, diese wirken sich
		* jedoch nciht auf die weiteren Kosten aus , deswegen sehen wir das selbe ergebnis wie bereits bei Druchlauf 1&2.
		*/
		System.out.println();
		System.out.println("Test 4 mit grundflaeche = 0 und mietzins = - 100: ");
		obj = new MietObj(0,-100);
		System.out.println("Kaltmiete: " + obj.kaltmiete());
		System.out.println("Verbrauchskosten: " + obj.verbrauchskosten());
		System.out.println("Heizkosten: " + obj.heizkosten());
		System.out.println("Warmmiete: " + obj.warmmiete());
	}
}
//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                