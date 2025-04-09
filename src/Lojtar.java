public class Lojtar {
	int pozicioni_rresht;
	int pozicioni_kolone;
	int piket;
	int[] piketVektor;
	int vektorCount;
	String gameUsername;
	int status = 0; // 0 - eshte duke luajtur, 1 - ka fituar, 2 - ka humbur
	
	public Lojtar(){
		this.pozicioni_rresht = 1;
		this.pozicioni_kolone = 1;
		this.piket = 0;
	}
	
	public Lojtar(int pozicioni_rresht, int pozicioni_kolone, int piket){
		this.pozicioni_rresht = pozicioni_rresht;
		this.pozicioni_kolone = pozicioni_kolone;
		this.piket = piket;
	}
}
