
public class D {
    private int zahl;
    private String wort;

    public D(int zwort) {
        this.zahl = 1;
        if (zwort == 1){
            this.wort = "eins";
        } else if (zwort == 2){
            this.wort = "zwei";
        } else {
            this.wort = "";
        }
    }

    public int getZahl() {
        return zahl;
    }

    public String getWort() {
        return wort;
    }

    public int methD() {
        zahl = 2 * zahl;
        return zahl;
    }

	public static void main(String[] args) {
		D[] array = new D[5];
		array[0]= new D(1);
		array[1]= new D(2);
		array[0].methD();
		array[1].methD();
		array[1].methD();
		
		 for (int i = 0; i < array.length; i++) {
        System.out.print("\n" + i + " : ");
        if (array[i] != null) {
            System.out.print(array[i].getZahl());
            System.out.print(array[i].getWort());
        }
    }

	}
}