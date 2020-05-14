package mask.vo;

public class Cart {
	private int orderNum;
	private int consumerid;
	private int productNum;
	private int orderStatus;
	private int shipStatus;
	private Product product;
	
	public Cart(int orderNum, int consumerid, int productNum, int orderStatus, int shipStatus,Product product) {
		
		this.orderNum = orderNum;
		this.consumerid = consumerid;
		this.productNum = productNum;
		this.orderStatus = orderStatus;
		this.shipStatus = shipStatus;
		this.product=product;
	}

	public Cart(int orderNum, int consumerid, int productNum) {
		
		this.orderNum = orderNum;
		this.consumerid = consumerid;
		this.productNum = productNum;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "Cart [orderNum=" + orderNum + ", consumerid=" + consumerid + ", productNum=" + productNum
				+ ", orderStatus=" + orderStatus + ", shipStatus=" + shipStatus + ", product=" + product + "]";
	}

	
	
}
