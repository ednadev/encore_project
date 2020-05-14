package mask.test;

import mask.dao.MaskImpl;
import mask.vo.Cart;
import mask.vo.Consumer;
import mask.vo.Product;

public class MaskDAOTest {

	public static void main(String[] args) throws Exception {
		//[테스트 예시] 요거 넣어서 테스트 하시면 편할 듯 합니다.
		MaskImpl mi = new MaskImpl("127.0.0.1");
		
		Consumer consum1=new Consumer(9509212, "ice", "Sillim", "abc123");
		Consumer consum2=new Consumer(9405131, "jjy", "ganseo", "abc124");
		Consumer consum3=new Consumer(9106012, "ksh", "Seoul", "abc125");
		Consumer consum4=new Consumer(9210232, "kmk", "Korea", "abc126");
		
		Product product1=new Product(1, "mask1", 1000, 1);
		Product product2=new Product(2, "mask2", 150, 2);
		Product product3=new Product(3, "mask3", 140, 3);
		Product product4=new Product(4, "mask4", 50, 3);
		
//		mi.addMask(new Cart(orderNum, consumerid, productNum, quantity, orderStatus, shipStatus));
		
		//mi.rankOfSales();
		
		
	}

}
