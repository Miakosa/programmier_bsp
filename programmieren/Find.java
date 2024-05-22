public class Find{
	

	 static int findPattern(){
	 	char [] array =  {'a','s','d','b','c','e','c','b','c'};
	 	char [] pattern = {'b','c'};
	 	final String FEHLER =  "Es konnte kein pattern gefunden werden!";
	 	int stelle = 0 ;
		// wenn Patter > array 
		if (array.length< pattern.length) {
			System.out.println(FEHLER);
			
		}
		 else {
		// erste Schleife durchlaeuft array-pattern laenge
		for (int i = 0;i <= array.length-pattern.length ; i++ ) {
			// vergleich ob erstes zeichen von pattern enthalten
				if (array[i] == pattern[0]) {
					
					/* wenn gefunden  zweite Schleife betreten
					*  zweite Schleife durchlaeuft pattern ab zweiten Zeile
					*/ 
					for (int j= 1;j <= pattern.length-1 ;j++ ) {
						if (array[i+1]==pattern[j]){
							i++;
						stelle = i;
						
				}	
				
				 else {
					stelle = 0;

				}
				
			}
		}
		
	} }
	
	 	return stelle;
	 } 
	 public static void main(String[] args) {
	 	Find a = new Find();
	 	System.out.println(Find.findPattern());
}
}