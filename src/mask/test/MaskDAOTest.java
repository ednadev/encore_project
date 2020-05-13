package mask.test;

import mask.vo.Cart;
import mask.vo.Consumer;
import mask.vo.Product;

public class MaskDAOTest {

	public static void main(String[] args) {
		//[테스트 예시] 요거 넣어서 테스트 하시면 편할 듯 합니다.
		Consumer consum1=new Consumer(9509212, "ice", "Sillim", "abc123");
		Consumer consum2=new Consumer(9405131, "jjy", "ganseo", "abc124");
		Consumer consum3=new Consumer(9106012, "ksh", "Seoul", "abc125");
		Consumer consum4=new Consumer(9210232, "kmk", "Korea", "abc126");

		Product product1=new Product(20200513, "mask1", 1000, 1);
		Product product2=new Product(20200512, "mask2", 150, 2);
		Product product3=new Product(20200511, "mask3", 170, 3);
		
		Cart cart1=new Cart(20200513, 9509212 , 20200513, 3, 1, 0);
		Cart cart2=new Cart(20200511, 9210232 , 20200512, 1, 0, 0);

		
	}

}
