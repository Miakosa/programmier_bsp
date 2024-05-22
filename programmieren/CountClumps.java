import java.util.*;

public class CountClumps {

/**    static  int rechne(int ganz1, int ganz2, char operator){
        int ergebnis = 0;
        switch (operator){
            case'+' :
                ergebnis = ganz1 + ganz2;
                break;
            case'-' :
                ergebnis = ganz1 - ganz2;
                break;
            case'*' :
                ergebnis = ganz1 * ganz2;
                break;
            case'/' :
                ergebnis = ganz1 / ganz2;
                break;
        }
        return(ergebnis);
    }
 

public static void main(String[] args) {
     
    System.out.println(rechne(2,2,'+'));
    System.out.println(rechne(2,2,'-'));
    System.out.println(rechne(2,2,'*'));
    System.out.println(rechne(2,2,'/'));
    }
*/

// Aufgabe 7b1  
public static void main(String[] args) {
    int[][] a = {{50,66,22},
                {34,40,17},
                {77,12,60}};
    
    int[][] result = transpose(a);
}
public static int [][] transpose(int [][] arr){
    int [][] result =new int [arr[0].length][arr.length];
    
    for (int zeile=0; zeile < arr.length; zeile++) {
        
        for ( int spalte =0; spalte <arr[0].length;spalte++){
                System.out.print(arr[spalte][zeile]+" ");
            }
            System.out.println();
        }
        return result;
}
}
     
/** Aufgabe 7b2 irgendwie
 public static void main(String[] args) {
    int[][] arr = {{50, 66, 22},
                   {34, 40, 17},
                   {77, 12, 60}};

    System.out.println(java.util.Arrays.toString(addition(arr)));
  }

  /**
  * Calculate the sum of diagonal elements.
  * @param a : 2-D array.
  * @return sum of diagonal elements.
/**  private static int [] addition(int[][] a) {
    int [] sum = new int [2];
   
    for (int i = 0; i < a.length; i++)
      for (int j = 0; j < a[0].length; j++) {
         // Check for main diagonal element.
         if (i == j) {
            sum[0]+= a[i][j];
         }
                
         // Check for secondary diagonal element.
         if (i + j == a.length - 1) {
            sum[1] += a[i][j];
         }
           
         }
          return sum;
         
  }
  */
   // Input vom User



 /**public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    
    System.out.print("Geben Sie bitte eine Zahl ein");
    int a = in.nextInt();
    
    
// Prüfung ob positiv oder negativ
    do {
        System.out.print("Geben Sie bitte eine weitere Zahl ein");
        int b = in.nextInt();
            
        }
        while (b != 0);
//Ausgabe 
    System.out.println("Zahlentest");
    System.out.println("Ganze Zahl eingeben: "+ a);
    System.out.println(a+ "ist" );
    System.out.println();
    System.out.println("Ganze Zahl eingeben: "+ b);
    System.out.println(b+ "ist" );
    System.out.println();
   
    
 }
 }     
            

*/

    




       
 

