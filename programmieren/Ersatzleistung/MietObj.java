/**
* Dies ist die Ersatzaufgabe fuer die erste Praesenz.
* Eine Klasse zur Berechnung der Miete.
*
* @author Nikola Kurzac (n.kurzac@ostfalia.de)
* @version 1.00 23.11.19
*/
import de.vfh.gp1.bib.Tools;

public class MietObj {

/**
*	A1 & A2: Float sollte hier fuer diese art der Berechnungen aussreichen. Somit sind keine genauigkeits-
*	verluste beim umrechnen von double nach float noetig. Außerdem bietet sich float besondera an
*	da es im gegensatz zu double bei einem switch anwendbar ist.
*
*/
	private final float DEFAULT_BEREITSTELLUNGSBETRAG = 15.00f;
	private final float DEFAULT_HEIZBETRAG = 35.00f;
	private final float DEFAULT_MIETZINS = 10.00f;
	private int grundflaeche;
	private float mietzins;

	/**
	* Der erste Konstruktor hat nur die Grundflaeche als Parameter.
	*
	* @param grundflaeche
	*
	*/

	public MietObj(int grundflaeche) {
		this.grundflaeche = grundflaeche;
		this.mietzins = DEFAULT_MIETZINS;
	}

	/**
	* Der zweíte Konstruktor hat die Grundflaeche und auch den Mietzins als Parameter.
	*
	* @param grundflaeche
	*
	* @param mietzins
	*
	*/

	public MietObj(int grundflaeche, float mietzins) {
		this.grundflaeche = grundflaeche;
		this.mietzins = mietzins;
	}

	/**
	* Dies ist eine Methode zur Berechnung der Kaltmiete.
	*
	* Die Kaltmiete wird errechnet indem man die Grundflaeche in qm x dem Mietzins
	* in Euro / qm nimmt.
	*
	* Die Kaltmiete wird in Euro angegeben.
	*
	*/

	public float kaltmiete() {
		return Tools.roundFloat(grundflaeche * mietzins, 4);
	}

	/**
	* Dies ist eine Methode zur Berechnung der Verbrauchskosten.
	*
	* Die Verbrauchskosten werden errechnet indem man den Heizbetrag in Euro / qm
	* x der Grundflaeche in qm nimmt.
	*
	* Die Verbrauchskosten werden in Euro angegeben.
	*
	*/

	public float verbrauchskosten() {
		return Tools.roundFloat(DEFAULT_HEIZBETRAG * grundflaeche, 4);
	}

	/**
	* Dies ist eine Methode zur Berechnung  der Heizkosten.
	*
	* Die Heizkosten werden errechnet indem man den Bereitstellungsbetrag in Euro
	* mit den Verbrauchskosten in Euro addiert.
	*
	* Die Heizkosten werden in Euro angegeben.
	*
	*/

	public float heizkosten() {
		return Tools.roundFloat(DEFAULT_BEREITSTELLUNGSBETRAG + verbrauchskosten(), 4);
	}

	/**
	* Dies ist eine Methode zur Berechnung  der Warmmiete.
	*
	* Die Warmmiete wird errechnet indem man die Kaltmiete in Euro
	* mit den Heizkosten in Euro addiert.
	*
	* Die Warmmiete werden in Euro angegeben.
	*
	*/

	public float warmmiete() {
		return Tools.roundFloat(kaltmiete() + heizkosten(), 4);
	}

}
