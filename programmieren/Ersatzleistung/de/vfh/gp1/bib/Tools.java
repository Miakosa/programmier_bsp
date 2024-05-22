package de.vfh.gp1.bib;

/**
 * Ueberschrift:   Konsole Bibliothek
 * Copyright:     Copyright (c) BHT Berlin. All Rights Reserved.
 * Organisation:  BHT Berlin - Virtuelle FH
 */

 import java.math.BigDecimal;

/** Methoden zur Erleichterung der Programmierarbeit.
 * Diese Klasse enth&auml;lt unterschiedliche Methoden als Hilfestellung fuer die
 * Programmierarbeit.
 *
 * @author Anka Shergowski (ankaski@yahoo.de)
 * @author Alexander Schmidt (aschmidt@tfh-berlin.de)
 * @version 1.1
 */
public class Tools {

  /** Methode zum Runden von Fliesskommatypen
   * Diese Methode erh&auml;lt als Parameter einen <tt>Wert</tt> vom Typ <tt>float</tt>
   * auf die in <tt>stellen</tt> angegebene Anzahl von Nachkommastellen gerundet
   * wird.
   *
   *  @param wert Enth&auml;lt den Wert, der gerundet werden soll.
   *  @param stellen Enth&auml;lt die Anzahl der Stellen, auf die gerundet werden soll.
   *  @return Einen Wert vom Typ <tt>float</tt> der den gerundeten Wert enth&auml;lt.
   */
  public static float roundFloat (float wert, int stellen)
  {
    int faktor = (int)Math.pow(10,stellen);
    return ((float)Math.round(wert*faktor))/faktor;
  }
}