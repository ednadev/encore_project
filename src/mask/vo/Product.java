package mask.vo;

public class Product {
	private int productNum;
	private String productName;
	private int stock;
	private String size;
	
	public Product() {}
	public Product(int productNum, String productName, int stock, String size) {
		this.productNum = productNum;
		this.productName = productName;
		this.stock = stock;
		this.size = size;
	}
	public Product(String productName, String size) {
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
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		return "Product [productNum=" + productNum + ", productName=" + productName + ", stock=" + stock + ", size="
				+ size + "]";
	}
}
