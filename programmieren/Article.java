abstract class Article {
	String artikelnr = "444444";
	String kurzinfo = "Hier ist die Kurzinfo";
	double preis= 34; 

		void showInfo(){
			System.out.println("Die Artikelnummer lautet: "+ artikelnr);
			System.out.println("Die Kurzinfo des Arikels: "+ kurzinfo);
			System.out.println("Der Preis betraegt: "+ preis);

		}

		String getArticleLine(){
			return kurzinfo;
		}
		public static void main(String[] args) {
			
			Article book = new Book();
			Article cd = new CD();

			book.showInfo();
			cd.showInfo();
		}
}		
