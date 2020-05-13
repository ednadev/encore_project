package mask.vo;

public class Consumer {
	private int id;
	private String name;
	private String address;
	private String pass;
	private Cart cart;
	
	public Consumer(int id, String name, String address, String pass) {		
		this.id = id;
		this.name = name;
		this.address = address;
		this.pass = pass;
	}
	
	public Consumer(int id,String pass) {		
		this.id = id;
		this.pass = pass;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@Override
	public String toString() {
		return "Consumer [id=" + id + ", name=" + name + ", address=" + address + ", pass=" + pass + "]";
	}
	
}
