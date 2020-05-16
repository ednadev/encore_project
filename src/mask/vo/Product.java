package mask.vo;

public class Product {
	private int productNum;
	private String productName;
	private int stock;
	private int size;
	
	public Product(int productNum, String productName, int stock, int size) {
		this.productNum = productNum;
		this.productName = productName;
		this.stock= stock;
		this.size = size;
	}

	public Product(int productNum, int stock, int size) {
		this.productNum= productNum;
		this.stock= stock;
		this.size = size;
	}
	
	public Product() {}

	public Product(int productNum) {
		this.productNum=productNum;
	}

	public Product(String productName, int stock, int size) {
		this.productName= productName;
		this.stock= stock;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public int getStock() {
		return stock;
	}

	public int setStock(int stock) {
		return stock;
	}

	@Override
	public String toString() {
		return "Product [productNum=" + productNum + ", productName=" + productName + ", stock=" + stock
				+ ", size=" + size + "]";
	}
		
}
