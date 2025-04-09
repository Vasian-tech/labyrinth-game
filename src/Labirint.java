public class Labirint {
	int[][] rrjetiLabirint = new int[20][20];
	// 0 perfaqeson rrugen qe ndjek lojtari, 1 jane muret ku nuk mund te kalohet, 2 jane thesaret dhe 3 eshte dalja
	
	int fillim_rresht;
	int fund_rresht;
	int fillim_kolone;
	int fund_kolone;
	
	public Labirint(int[][] rrjetiLabirint, int fillim_rresht, int fund_rresht, int fillim_kolone, int fund_kolone) {
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				if(this.rrjetiLabirint[i][j] != rrjetiLabirint[i][j]) {
					this.rrjetiLabirint[i][j] = rrjetiLabirint[i][j];
				}
			}
		}
		this.fillim_rresht = fillim_rresht;
		this.fund_rresht = fund_rresht;
		this.fillim_kolone = fillim_kolone;
		this.fund_kolone = fund_kolone;
	}
	
	public void gjeneroLabirint(int pozicion_rresht, int pozicion_kolone) {
		if(pozicion_rresht == fund_rresht-3 && fund_rresht < 20) {
			fillim_rresht++;
			fund_rresht++;
		} else if(pozicion_kolone == fund_kolone-3 && fund_kolone < 20) {
			fillim_kolone++;
			fund_kolone++;
		} else if(pozicion_rresht == fillim_rresht+3 && fillim_rresht > 0) {
			fillim_rresht--;
			fund_rresht--;
		} else if(pozicion_kolone == fillim_kolone+3 && fillim_kolone > 0) {
			fillim_kolone--;
			fund_kolone--;
		}
	}
	
	public boolean eshteMur(int pozicion_rresht, int pozicion_kolone) {
		if(rrjetiLabirint[pozicion_rresht][pozicion_kolone] == 1) {
			return true;
		}
		return false;
	}
	
	public boolean eshteThesar(int pozicion_rresht, int pozicion_kolone) {
		if(rrjetiLabirint[pozicion_rresht][pozicion_kolone] == 2) {
			return true;
		}
		return false;
	}
	
	public boolean eshteDalje(int pozicion_rresht, int pozicion_kolone) {
		if(rrjetiLabirint[pozicion_rresht][pozicion_kolone] == 3) {
			return true;
		}
		return false;
	}
}
