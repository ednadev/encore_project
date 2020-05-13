package mask.test;

import java.util.InputMismatchException;
import java.util.Scanner;

import config.StringResource;
import mask.dao.MaskImpl;
import mask.vo.Consumer;

public class MaskTest {
	static StringResource str = new StringResource();
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		MaskImpl mi = new MaskImpl("127.0.0.1");
		
		while(true) {
			System.out.println(str.inventoryTitle);
			System.out.println(str.selectLoginToAdd);
			
			try {
				switch(sc.nextInt()) {
				case 1: //로그인
					login(mi);
					break;
				case 2: //회원가입
					addConsumer(mi);
					break;
				case 3: //종료
					System.exit(0);
					break;
				case 4: //관리자 모드 -- 표시 X
					admin(mi);
				default:
					System.out.println(str.inputMismatch);
			}
			} catch(InputMismatchException e) {
				System.out.println(str.inputMismatch);
				sc.nextLine();
			}
			
		}
	}
	
	//로그인
	public static void login(MaskImpl mi) {
		System.out.println(str.inputConsumerId);
		int id = sc.nextInt(); 
		System.out.println(str.inputConsumerPass);
		String pass = sc.next();
		mi.login(new Consumer(id, pass));
		System.out.println(str.successLogin);
		System.out.println(str.selectInfo);
		mi.getProduct();
		mi.getCart();
	}
	
	//회원가입
	public static void addConsumer(MaskImpl mi) {
		System.out.println(str.inputConsumerId);
		int id = sc.nextInt();
		System.out.println(str.inputConsumerName);
		String name = sc.next();
		System.out.println(str.inputConsumerAddr);
		String address = sc.next();
		System.out.println(str.inputConsumerPass);
		String pass = sc.next();
		mi.addConsumer(new Consumer(id, name, address, pass));
		// 다시 로그인? 아니면 바로 구매할 수 있도록?
	}
	
	//관리자모드
	public static void admin(MaskImpl mi) {
		//구매 날짜별 판매량
		
	}
}
