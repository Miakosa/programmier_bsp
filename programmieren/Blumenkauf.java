/**
 * Dateiname: Blumenkauf.java
 * Beschreibung: Ein Programm, das ausrechnet, wie viel
 * ein Kunde f�r den Kauf in einem Blumenladen zu zahlen hat.
 *
 * @author J. Altenau (j.altenau@ostfalia.de); Julia hungerland (j.hungerland@ostfalia.de);
 *		   Nancy Bienert (n.bienert@ostfalia.de); Nikola K.(n.kurzac@ostfalia.de
 * @version 1.0
 *	Esa 1
 */
import java.util.Scanner;

public class Blumenkauf {

    /**
     * a) Legen Sie drei Variablen an f�r die Anzahl der Rosen,
     *    Tulpen und Nelken, die ein Kunde in seinem Strau� haben m�chte.
     *
     * @param int rose, tulpe, nelke
     * Datentyp int gew�hlt, da nicht erwartet wird das der Wertebereich
     *	von int �berschritten wird.
     */
    public static void main(String[] args) {

		int rose;
 		int tulpe;
 		int nelke;

 	/**
 	* b) Lassen Sie den Nutzer oder die Nutzerin diese drei Anzahlen
 	*    gew�nschter Blumen eingeben.
 	*
 	*	Dazu wird der Scanner eingebaut um die Eingabe des Kunden einlesen
 	*	zu k�nnen.
 	*/

 		System.out.println("Herzlich Willkommen");

		Scanner tastatur;
		        tastatur = new Scanner(System.in);
		        tastatur.useDelimiter(System.lineSeparator());

		System.out.println("Bitte geben Sie die gew\u00fcnschte Anzahl Rosen an: ");
		rose = tastatur.nextInt();

		System.out.println("Bitte geben Sie die gew\u00fcnschte Anzahl Tulpen an: ");
		tulpe = tastatur.nextInt();

		System.out.println("Bitte geben Sie die gew\u00fcnschte Anzahl Nelken an: ");
		nelke = tastatur.nextInt();

	/**
	* c) Legen Sie Variablen f�r die Preise der einzelnen Blumen an:
	*	 eine Rose kostet 2,49 �, eine Tulpe 1,66 � und eine Nelke 0,99 �.
	*
	* @param double preisr, preist, preisn
	* Datentyp double gew�hlt, da hier nun flie�kommazahlen genutzt werden,
	* es w�re auch m�glich float zu w�hlen da es nur zwei Nachkommastellen
	* gibt.
	*/

		double preisr = 2.49;
		double preist = 1.66;
		double preisn = 0.99;

	/**
	* d) Berechnen Sie, wie viel der Kunde jeweils f�r Rosen, Tulpen und
	*	 Nelken zu zahlen hat. Benutzen Sie, wo immer es sinnvoll ist,
	*	 die eben definierten Variablen. Die Berechnung soll auch dann korrekt
	* 	 funktionieren, wenn sich die Preise in der Zukunft �ndern sollten.
	*
	* @param double kostenr, kostent, kostenn
	* Datentyp double gew�hlt, da hier nun flie�kommazahlen genutzt werden,
	* es w�re auch m�glich float zu w�hlen da es nur zwei Nachkommastellen
	* gibt. Allerdings wird hier Math.round verwendet um dem Kunden eine
	* R�ckmeldung in Form 00.00� zu geben, dazu wird double gebraucht.
	*/

		double kostenr;
		double kostent;
		double kostenn;

		kostenr = Math.round((preisr * rose)*100.0)/100.0;
		kostent = Math.round((preist * tulpe)*100.0)/100.0;
		kostenn = Math.round((preisn * nelke*100.0)/100.0);

	/**
	* e) Geben Sie die drei Gesamtpreise der einzelnen Blumensorten aus.
	*/

		System.out.println("Hier ist Ihre Rechnung: \nRosen " + kostenr + " Euro \nTulpen " + kostent +
		" Euro \nNelken "+ kostenn + " Euro " );

	/**
	* f) Berechnen Sie nun auch noch die Gesamtsumme und geben Sie diese aus.
	*
	* Hier wurde Math.round benutzt um einen Gesammtpreis mit mehr als 2 Nachkommastellen
	* zu vermeiden.
	*/

		double kostengesamt = Math.round((kostenr + kostent + kostenn)*100.0)/ 100.0;

		System.out.println("Die Gesamtsumme betr\u00e4gt: " + kostengesamt + "Euro" );
	}

}