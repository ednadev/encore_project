package mask.vo;

public class Product {
	private int productNum;
	private String productName;
	private int quatity;
	private int size;

	public Product(int productNum, String productName, int quatity,int size) {
		super();
		this.productNum = productNum;
		this.productName = productName;
		this.quatity = quatity;
		this.size=size;
	}
	
	public Product(int productNum, int size, int quatity) {
		this.productNum = productNum;
		this.quatity = quatity;
		this.size=size;
	}
	
	public Product() {}

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
	public int getQuatity() {
		return quatity;
	}
	public void setQuatity(int quatity) {
		this.quatity = quatity;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Product [productNum=" + productNum + ", productName=" + productName + ", quatity=" + quatity + "]";
	}
		
}
