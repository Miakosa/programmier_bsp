public class Book extends Article{
	String titel;
	String autor ;
	String verlag ;
	int erscheinungsjahr ;
	
	public Book(){
		
		this.titel = titel;
		this.autor = autor;
		this.verlag = verlag;
		this.erscheinungsjahr = erscheinungsjahr; 
	}
	

	void showInfo(){
		super.showInfo();
		System.out.println("Der Titel:  "+ titel);
		System.out.println("Der Autor:  "+ autor);
		System.out.println("Der Verlag: "+ verlag);
		System.out.println("Das Erscheinungsjahr: "+ erscheinungsjahr);
		System.out.println();

	}
}