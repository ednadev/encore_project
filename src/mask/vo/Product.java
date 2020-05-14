package mask.vo;

public class Product {
	private int productNum;
	private String productName;
	private int quantity;
	private int size;
	
	public Product(int productNum, String productName, int quantity, int size) {
		this.productNum = productNum;
		this.productName = productName;
		this.quantity = quantity;
		this.size = size;
	}

	public Product(String productName, int quantity, int size) {
		this.productName = productName;
		this.quantity = quantity;
		this.size = size;
	}
	
	public Product(String productName, int size) {
		super();
		this.productName = productName;
		this.size = size;
	}

	public int getProductNum() {
		return productNum;
	}

	public void setProductNum(int productNum) {
		this.productNum = productNum;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Product [productNum=" + productNum + ", productName=" + productName + ", quantity=" + quantity
				+ ", size=" + size + "]";
	}
		
}
