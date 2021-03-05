
public class Inventory {
	private int coins;
	
	public Inventory() {
		coins = 0;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public boolean setCoins(int coins) {
		if (coins >= 0) {
			this.coins = coins;
			return true;
		} else {
			return false;
		}
	}
	
}
