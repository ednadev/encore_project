package mask.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mask.dao.MaskImpl;
import mask.exception.RecordNotFoundException;
import mask.vo.Cart;
import mask.vo.Consumer;
import mask.vo.Product;

public class MaskTest {
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		MaskImpl mi = new MaskImpl("127.0.0.1");	
		
		while(true) {
			System.out.println("Mask Shopping Mall"); 
			System.out.println("1.로그인      2.회원가입    3. 종료");
			
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
					case 0: //관리자 모드 -- 표시 X
						admin(mi);
						break;
					default:
						System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
				}
			} catch(InputMismatchException e) {
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
				sc.nextLine();
			}
		}
	}
	
	//관리자모드 - 무조건 0번 (화면에는 보이지 않습니다) 
	public static void admin(MaskImpl mi) throws Exception {
		System.out.println("관리자페이지입니다.");
		System.out.println("1.배송관리    2.재고관리    3.매출현황");
		switch(sc.nextInt()) {
			case 1: //배송관리 - Cart
				getCartAll(mi);
				break;
			case 2: //재고관리 - Product
				for(Product product : mi.getProducts()) {
					System.out.println("상품명: " + product.getProductName() + ", "
							+ "사이즈: " + product.getSize() + ", "
							+ "재고량: " + product.getStock());
				}
				System.out.println("재고를 추가할 사이즈를 선택해주세요.");
				System.out.println("1.대형    2.중형    3.소형");
				int productNum = sc.nextInt();
				System.out.println("추가할 수량을 입력해주세요.");
				int quantity = sc.nextInt();
				mi.addProductMask(productNum, quantity);
				System.out.println("재고가 추가되었습니다.");
				break;
			case 3: //매출현황
				System.out.println("사이즈별 매출 순위 및 판매수량");
				String[] array = null;
				for(int i=0; i<mi.sizeTotal().size(); i++) {
					array = mi.sizeTotal().get(i).split(", ");
					System.out.println(array[0]);
				}
				System.out.println(array[1]);
				break;
			default:
		}
	}
	
	public static void getCartAll(MaskImpl mi) throws Exception {
		for(Cart c : mi.getCartAll()) {
			String date = c.getDate();
			String shipStatus="";
			switch(c.getShipStatus()) {
				case 0:
					shipStatus = "배송전";
					break;
				case 1:
					shipStatus = "배송중";
					break;
				case 2:
					shipStatus = "배송완료";
					break;
			}
			
			System.out.println(
					"주문번호 : " + c.getOrderNum() + ", "
					+ "판매날짜 : " + date.substring(0,10) + ", "
					+ "상품명 : " + c.getProduct().getProductName() + ", "
					+ "사이즈 : " + c.getProduct().getSize() + ", "
					+ "수량 : " + c.getQuantity() + ", "
					+ "배송현황 : " + shipStatus
			);
		}
		System.out.println("배송할 주문번호를 입력해주세요.");
		int orderNum = sc.nextInt();
		mi.delivery(orderNum);
		System.out.println("배송 현황이 변경되었습니다.");
	}


	
	
	//------------Consumer Mode
	//회원가입 - void addConsumer(Consumer consumer)
	public static void addConsumer(MaskImpl mi) throws Exception {
		System.out.println("주민등록번호를 입력해주세요. ('-' 표시)");
		String ssn = sc.next();
		Pattern ssn_p = Pattern.compile("^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$");
		Matcher s = ssn_p.matcher(ssn);
		
		if(s.matches()) {
			ssn = ssn.replaceAll("-", "");
			int ssn_i = Integer.parseInt(ssn.substring(0, 7));
			System.out.println("이름을 입력해주세요.");
			String name = sc.next();
			System.out.println("주소를 입력해주세요.");
			String address = sc.next();
			System.out.println("비밀번호를 입력해주세요.");
			String pass = sc.next();
			mi.addConsumer(new Consumer(ssn_i, name, address, pass));
			System.out.println("회원가입이 완료됐습니다.");
			loginSuccess(mi, ssn_i);
		} else {
			System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
	}
	
	//로그인 - void login(int id, String pass)
	public static void login(MaskImpl mi) throws Exception {
		System.out.println("주민등록번호를 입력해주세요. ('-' 표시)");
		String ssn = sc.next();
		Pattern ssn_p = Pattern.compile("^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$");
		Matcher s = ssn_p.matcher(ssn);
		
		if(s.matches()) {
			ssn = ssn.replaceAll("-", "");
			int ssn_i = Integer.parseInt(ssn.substring(0, 7));
			System.out.println("비밀번호를 입력해주세요.");
			String pass = sc.next();
			if(mi.login(ssn_i, pass)) {
				loginSuccess(mi, ssn_i);
			}
		} else {
			System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
	}
	
	public static void loginSuccess(MaskImpl mi, int ssn_i) throws Exception {
		System.out.println("1.마스크 구입    2.마이페이지    3.종료");
		switch(sc.nextInt()) {
			case 1: //마스크 구입
				getProducts(mi); //마스크 현황
				addMask(mi, ssn_i);
				break;
			case 2: //마이페이지
				myPage(mi, ssn_i);
				break;
			case 3: //종료
				System.exit(0);
				break;
			case 0:
				admin(mi);
				break;
			default:
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
	}
	
	//상품 조회 - ArrayList<Product> getProducts()
	public static void getProducts(MaskImpl mi) throws Exception {
		ArrayList<Product> products = mi.getProducts();
		System.out.println("제품명 : " + products.get(0).getProductName());
		System.out.println("사이즈 : ");
		for(int i=0; i<products.size(); i++) {
			System.out.println(i+1 + ". " + products.get(i).getSize());
		}
	}
	
	//마스크 추가 - void addMask(int id, Cart cart) <-- 이부분
	public static void addMask(MaskImpl mi, int ssn_i) throws Exception {
		System.out.println("구매할 마스크 사이즈를 선택해주세요.");
		int productNum = sc.nextInt();
		System.out.println("구매할 수량을 입력해주세요.");
		int quantity = sc.nextInt();
		mi.addMask(new Cart(ssn_i, productNum, quantity));
		System.out.println("마스크 구매 완료!");
		loginSuccess(mi, ssn_i);
	}
	
	//마이페이지 - 회원정보수정, 장바구니, 주문내역
	public static void myPage(MaskImpl mi, int ssn_i) throws Exception {
		System.out.println("1.회원정보수정    2.장바구니    3.주문내역    4.종료");
		switch(sc.nextInt()) {
			case 1: //회원정보수정
				updateConsumer(mi, ssn_i);
				break;
			case 2: //장바구니
				getCart(mi, ssn_i); // 어떤 방식으로 할 건지는 여기 다시 정하기.. 일단 구매하기가 되고 나서 시작!
				break;
			case 3: //주문내역
				getPaymentList(mi, ssn_i);
				break;
			case 4: 
				System.exit(0);
				break;
			case 0:
				admin(mi);
				break;
			default:
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
	}
	
	public static void updateConsumer(MaskImpl mi, int ssn_i) throws Exception {
		Consumer consumer = new Consumer();
		consumer = mi.selectConsumer(ssn_i);
		System.out.println("회원번호 : " + consumer.getId());
		System.out.println("비밀번호 : " + consumer.getPass());
		System.out.println("이름 : " + consumer.getName());
		String name = consumer.getName();
		System.out.println("주소 : " + consumer.getAddress());
		System.out.println();
		System.out.println("수정할 주소를 입력해주세요.");
		String address = sc.next();
		System.out.println("수정할 비밀번호를 입력해주세요.");
		String pass = sc.next();
		consumer = new Consumer(ssn_i, name, address, pass);
		mi.updateConsumer(consumer);
		System.out.println("회원 정보가 수정되었습니다.");
		myPage(mi, ssn_i);
	}
	
	//카트조회 - ArrayList<Cart> getCart(int id)
	public static void getCart(MaskImpl mi, int ssn_i) throws Exception {
		if(mi.getCart(ssn_i).size()!=0) {
			for(Cart c : mi.getCart(ssn_i)) {
				if(c.getOrderStatus()==0) {
					String date = c.getDate();
					System.out.println(
							"주문번호 : " + c.getOrderNum() + ", "
							+ "주문날짜 : " + date.substring(0,10) + ", "
							+ "상품명 : " + c.getProduct().getProductName() + ", "
							+ "사이즈 : " + c.getProduct().getSize() + ", "
							+ "수량 : " + c.getQuantity());
				} 
			}
			getCartUpdate(mi, ssn_i);
		} else {
			System.out.println("장바구니가 비어있습니다.");
			loginSuccess(mi, ssn_i);
		}
		
		
	}
	
	//상세내역
	public static void getPaymentList(MaskImpl mi, int ssn_i) throws Exception {
		if(mi.getPaymentList(ssn_i).size()!=0) {
			for(Cart c : mi.getPaymentList(ssn_i)) {
				if(c.getOrderStatus()==1) {
					String date = c.getDate();
					String shipStatus="";
					switch(c.getShipStatus()) {
						case 0:
							shipStatus = "배송전";
							break;
						case 1:
							shipStatus = "배송중";
							break;
						case 2:
							shipStatus = "배송완료";
							break;
					}
					
					System.out.println(
							"주문번호: " + c.getOrderNum() + ", "
							+ "구매날짜 : " + date.substring(0,10) + ", "
							+ "상품명 : " + c.getProduct().getProductName() + ", "
							+ "사이즈 : " + c.getProduct().getSize() + ", "
							+ "수량 : " + c.getQuantity() + ", "
							+ "배송현황 : " + shipStatus
					);
				}
			}
			System.out.println("배송전에는 취소 가능합니다. 취소할 주문번호가 있으신가요?");
			System.out.println("1.Yes    2.No");
			if(sc.nextInt()==1) {
				System.out.println("취소할 주문번호를 입력해주세요.");
				int orderNum = sc.nextInt();
				mi.paymentCancel(ssn_i, orderNum);
				System.out.println("주문이 취소되었습니다.");
			}
			loginSuccess(mi, ssn_i);
		} else {
			System.out.println();
		}
		
	}
	
	//카트 수정 - 삭제, 수정, 구매
	public static void getCartUpdate(MaskImpl mi, int ssn_i) throws RecordNotFoundException, Exception {
		System.out.println("1.삭제    2.수정    3.구매");
		switch(sc.nextInt()) {
			case 1: //삭제
				deleteMask(mi, ssn_i);
				break;
			case 2: //수정
				updateMask(mi, ssn_i);
				break;
			case 3: //구매
				payment(mi, ssn_i);
				break;
			case 0:
				admin(mi);
				break;
			default:
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
		}
		
		loginSuccess(mi, ssn_i);
	}

	//마스크 삭제 - void deleteMask(int id, Cart cart) <-- 이부분
	public static void deleteMask(MaskImpl mi, int ssn_i) throws RecordNotFoundException, Exception {
		System.out.println("장바구니에서 없앨 주문 번호를 입력해주세요.");
		int orderNum = sc.nextInt();
		try {
			mi.deleteMask(ssn_i, orderNum);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		loginSuccess(mi, ssn_i);
	}	
	
	//마스크 사이즈, 마스크 종류 변경 - void updateMask(int id, Cart cart) <-- 이부분
	public static void updateMask(MaskImpl mi, int ssn_i) throws Exception {
		System.out.println("장바구니에서 수정할 주문 번호를 입력해주세요.");
		int orderNum = sc.nextInt();
		System.out.println("변경할 상품 사이즈를 입력해주세요.");
		System.out.println("1.대형    2.중형    3.소형");
		int productNum = sc.nextInt();
		System.out.println("변경할 마스크 수량을 입력해주세요.");
		int quantity = sc.nextInt();
		mi.updateMask(new Cart(ssn_i, productNum, orderNum, quantity));
		System.out.println("상품 수정 완료!");
		loginSuccess(mi, ssn_i);
	}
		
	//마스크 구매 -- 상세 내역으로 넘어감 (order_status 1로 변경), 재고에서 빠짐
	public static void payment(MaskImpl mi, int ssn_i) throws RecordNotFoundException, Exception {
		try {
			System.out.println("결제를 진행할 주문번호를 입력해주세요.");
			int orderNum = sc.nextInt();
			mi.payment(ssn_i, orderNum);
			System.out.println("결제 완료!");
			loginSuccess(mi, ssn_i);
		}
		catch(RecordNotFoundException e) {
			System.out.println(e.getMessage());
		}catch(SQLException e) {
			System.out.println("상품번호를 잘못 입력하셨습니다.");
		}
	}

}
