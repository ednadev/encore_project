package mask.test;

import config.ServerInfo;
import mask.dao.MaskImpl;
import mask.exception.PasswordMissmatchException;
import mask.vo.Cart;
import mask.vo.Consumer;
import mask.vo.Product;

public class MaskDAOTest {
	public MaskDAOTest(String serverIp) throws ClassNotFoundException {
		Class.forName(ServerInfo.DRIVER_NAME);
		System.out.println("드라이브 로딩 성공____ in MaskImpl");
	}
	public static void main(String[] args) throws Exception{
		//[테스트 예시] 요거 넣어서 테스트 하시면 편할 듯 합니다.
		Consumer consum1=new Consumer(9509212, "ice", "Sillim", "abc123");
		Consumer consum2=new Consumer(9405131, "jjy", "ganseo", "abc124");
		Consumer consum3=new Consumer(9106012, "ksh", "Seoul", "abc125");
		Consumer consum4=new Consumer(9210232, "kmk", "Korea", "abc126");
		Consumer consum5=new Consumer(690711, "bsj", "Gyungnam", "abc127");
		
		Product product1=new Product(1, "mask1", 1000, 1);
		Product product2=new Product(2, "mask2", 150, 2);
		Product product3=new Product(3, "mask3", 140, 3);
		Product product4=new Product(4, "mask4", 50, 3);
		
		Cart cart1=new Cart(20200513, 9509212 , 1);
		Cart cart2=new Cart(20200513, 9210232 ,3);
	
	
		MaskImpl mask=new MaskImpl();
		Product product=new Product();
		
		try{
			mask.addProductMask(product1);
			}catch(NullPointerException e) {
				System.out.println("Null인거 같은뎅?");}
		
		try {
		mask.login(9509212,"abc123");}
		catch(PasswordMissmatchException e) {
			System.out.println(e.getMessage());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		for(Product p : mask.getProducts()){
			System.out.println(p);
		}
				
	}

}
