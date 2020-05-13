package mask.vo;

public class Cart {
	private int orderNum;
	private int consumerid;
	private int productNum;
	private int quantity;
	private int orderStatus;
	private int shipStatus;

	public Cart(int orderNum, int consumerid, int productNum, int quantity, int orderStatus, int shipStatus) {
		super();
		this.orderNum = orderNum;
		this.consumerid = consumerid;
		this.productNum = productNum;
		this.quantity = quantity;
		this.orderStatus = orderStatus;
		this.shipStatus = shipStatus;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public int getConsumerid() {
		return consumerid;
	}

	public void setConsumerid(int consumerid) {
		this.consumerid = consumerid;
	}

	public int getProductNum() {
		return productNum;
	}

	public void setProductNum(int productNum) {
		this.productNum = productNum;
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
		return "Cart [orderNum=" + orderNum + ", consumerid=" + consumerid + ", productNum=" + productNum
				+ ", quantity=" + quantity + ", orderStatus=" + orderStatus + ", shipStatus=" + shipStatus + "]";
	}
	
}
