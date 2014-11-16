
public class Player {

	private String name;
	private float money;
	
	public Player(int money, String name) {
		this.money = money;
		this.name = name;
	}

	public String getName() { return name; }
	public float getMoney() { return money; }
	
	public void setMoney(float newMoney) {
		money = newMoney;
	}
}
