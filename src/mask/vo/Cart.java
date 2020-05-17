package mask.vo;
public class Cart {
	private int consumerId;
	private int productNum;
	private int orderNum;
	private String date;
	private int quantity;
	private int orderStatus;
	private int shipStatus;
	
	private Product product;
	
	public Cart() {
	}
	
	public Cart(int orderNum, String date, int quantity, int shipStatus, Product product) {
		this.orderNum = orderNum;
		this.date = date;
		this.quantity = quantity;
		this.shipStatus = shipStatus;
		this.product = product;
	}
	
	public Cart(int consumerId, int orderNum, String date, int quantity, int orderStatus, int shipStatus, Product product) {
		this.consumerId = consumerId;
		this.orderNum = orderNum;
		this.date = date;
		this.quantity = quantity;
		this.orderStatus = orderStatus;
		this.shipStatus = shipStatus;
		this.product = product;
	}
	
	public Cart(int consumerId, int productNum, int orderNum, int quantity) {
		this.consumerId = consumerId;
		this.productNum = productNum;
		this.orderNum = orderNum;
		this.quantity = quantity;
	}

	public Cart(int consumerId, int productNum, int quantity) {
		this.consumerId = consumerId;
		this.productNum = productNum;
		this.quantity = quantity;
	}
	public Cart(int consumerId, int productNum, int orderNum, String date, int quantity, int orderStatus,
			int shipStatus) {
		this.consumerId = consumerId;
		this.productNum = productNum;
		this.orderNum = orderNum;
		this.date = date;
		this.quantity = quantity;
		this.orderStatus = orderStatus;
		this.shipStatus = shipStatus;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(int consumerId) {
		this.consumerId = consumerId;
	}

	public int getProductNum() {
		return productNum;
	}

	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(int shipStatus) {
		this.shipStatus = shipStatus;
	}

	@Override
	public String toString() {
		return "Cart [consumerId=" + consumerId + ", productNum=" + productNum + ", orderNum=" + orderNum + ", date="
				+ date + ", quantity=" + quantity + ", orderStatus=" + orderStatus + ", shipStatus=" + shipStatus + "]";
	}
	
}