public class LoneSum {
    public static int loneSum(int[] eingabearray){
        int zaehler = 0;
        // alle Werte gleich -> zähler=0
            if (eingabearray[0] == eingabearray[1] && eingabearray[0]==eingabearray[2]){
                zaehler=0;
            }
        // 0&1 wert gleich
            else if (eingabearray[0]==eingabearray[1]&&eingabearray[0]!=eingabearray[2]){
                zaehler=eingabearray[0]+eingabearray[2];
            }
        //  0&2 wert gleich
            else if (eingabearray[0]!= eingabearray[1]&&eingabearray[0]==eingabearray[2]){
                zaehler=eingabearray[0]+eingabearray[1];
            }
        // 1&2 gleich
            else if (eingabearray[0]!= eingabearray[1]&&eingabearray[0]!=eingabearray[2]&&eingabearray[1]==eingabearray[2]) {
                zaehler=eingabearray[0]+eingabearray[2];
            }
        // alle unterschiedlich
                else 
                    zaehler=eingabearray[0]+eingabearray[1]+eingabearray[2];
                return zaehler;
    }
	public static void main(String[] args) {
        LoneSum newSum = new LoneSum();
        int [] arr = {3,3,3};
                
            
            System.out.println(newSum.loneSum(arr));

           }
           
        }
        
  