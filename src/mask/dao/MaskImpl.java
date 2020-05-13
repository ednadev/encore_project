package mask.dao;

import java.util.ArrayList;
import java.util.function.Consumer;

import mask.vo.Cart;
import mask.vo.Product;

public class MaskImpl implements MaskTemplate{
	/**addConsumer,login,getCart,deleteMask
	 * @author 소희
	 */
	@Override
	public void addConsumer(Consumer consumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(Consumer consumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Cart> getCart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMask(Cart cart) {
		// TODO Auto-generated method stub
		
	}
	
	/**addMask,payment,updateMask
	 * @author 정용
	 */
	@Override
	public void addMask(Cart cart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean payment(int shipStatus) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateMask(Cart cart) {
		// TODO Auto-generated method stub
		
	}
	
	/**addProductMask,getProduct,updateProductMask
	 * @author 채은
	 */
	@Override
	public void addProductMask(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Product> getProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProductMask(Product product) {
		// TODO Auto-generated method stub
		
	}
	
	/**사이즈별 재고수량조회
	 * 분석함수
	 * @author 채은
	 */
	@Override
	public String getQuatityOverSize() {
		// TODO Auto-generated method stub
		return null;
	}
	/**매출 순위
	 * 분석함수
	 * @author 정용
	 */
	@Override
	public ArrayList<String> rankOfSales() {
		// TODO Auto-generated method stub
		return null;
	}
	/**구매 날짜별 판매량 
	 * 분석함수
	 * @author 소희
	 */
	@Override
	public String SalesOfDate() {
		// TODO Auto-generated method stub
		return null;
	}

}
