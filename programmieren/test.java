import java.util.Scanner;	//Scanner für die Eingabe des Users
/**
 * Dateiname: UebClockman.java 
 * Beschreibung: Ein erstes Programm zum Kennenlernen von Java.
 *
 * @author Nikola K. (nkurzac@ostfalia.de)
 * @version 1.01, 09/2022
 */



public class test {

    /**
     * Soll die Klasse von der Eingabekonsole aus aufgerufen werden koennen, dann
     * muss die hier aufgefuehrte main-Methode enthalten sein. Diese Methode ist der
     * definierte Einstiegspunkt fuer die Ausfuehrung.
     */


    /**
    *	UNICODE umlaute
    *	Ä, ä 		\u00c4, \u00e4
    *	Ö, ö 		\u00d6, \u00f6
    *	Ü, ü 		\u00dc, \u00fc
    *	ß 		\u00df
    */
    public static void main(String[] args) {
    	Scanner zahl = new Scanner(System.in); //SCanner objekt erstellen
    	System.out.println("geben Sie die erste Zahl ein");
    	int zahl1 = zahl.nextInt(); // Liest die eingabe vom User
    	System.out.println("geben Sie die zweite Zahl ein");
    	int zahl2 = zahl.nextInt();
    	int summe = zahl1 + zahl2;
    	int differenz = zahl1 - zahl2;
    	int multi = zahl1 * zahl2;
    	int teil = zahl1/zahl2;
    	int mod = zahl1%zahl2;

    	System.out.println("Die Summe von "+ zahl1 +" und "+ zahl2 +" betr\u00e4gt: "+summe);
    	System.out.println("Die Differenz von "+ zahl1 +" und "+ zahl2 +" betr\u00e4gt: "+differenz);
    	System.out.println("Die Multiplikation von "+ zahl1 +" und "+ zahl2 +" betr\u00e4gt: "+multi);
    	System.out.println("Die Division von "+ zahl1 +" und "+ zahl2 +" betr\u00e4gt: "+teil);
    	System.out.println("Das Modulo von "+ zahl1 +" und "+ zahl2 +" betr\u00e4gt: "+mod);
    
        int zahl3 = 6;
    	int ink = zahl3++;
    	System.out.println("Das Inkrement von 6++ ist: "+ink);
		System.out.println("Somit ist die Zahl nun: "+zahl3);

    	int zahl4 = 5;
    	int dek = --zahl4;
    	System.out.println("Das Dekrement --5 ist: "+dek);
    	System.out.println("Somit ist die Zahl nun: "+zahl4);


    	//while schleife
    	while(zahl3 >0){
    		zahl3--;
    		System.out.println(zahl3);
    	}

    	//if schleife

    	if (zahl1-zahl2>10) {
    		System.out.println("Die subtraktion beider Zahlen sind gr\u00f6ßer als 10");
    		
    	}
    	else if (zahl1-zahl2<10){
    		System.out.println("Die subtraktion beider Zahlen sind kleiner als 10");

    	}
    	else
        	System.out.println("Die subtraktion beider Zahlen ergitbt 10");

        // switch

        Scanner name = new Scanner(System.in); //SCanner objekt erstellen string
        System.out.println("geben Sie ein Wort ein");
        String vname = name.nextLine();
        System.out.println(vname.charAt(0));

        switch (vname.charAt(0)){ // funtkioniert nicht mit mehreren Strings in einem case
        	case "a":
        	System.out.println("schnief");
        	break;
        }

 
    } // Ende main-Methode.

} // Ende UebClockman.