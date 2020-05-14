package mask.test;

import java.util.Scanner;

import mask.dao.MaskImpl;
import mask.vo.Cart;
import mask.vo.Consumer;
import mask.vo.Product;

public class MaskTest {
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		MaskImpl mi = new MaskImpl("127.0.0.1");
		
		//여기서 기능 테스트 하면 좋을 거 같아요! 테스트 해보고 싶은 기능만 주석 풀어서 테스트하세요~~
		//출력 부분을 일단 간단하게 작업한 상태라 혹시 수정하고픈 사항이 있으면..
		//각 출력부분 메소드 위에서 F3키 눌러서 이동하여 해당 메소드만 수정하시면 됩니다
		
		//login(mi);
		//addConsumer(mi);
		//getCart(mi);
		//deleteMask(mi);
		//addMask(mi);
		//delivery(mi);
		//updateMask(mi);
		//addProductMask(mi);
		//getProduct(mi);
		//updateProductMask(mi);
		//getQuatityOverSize(mi);
		//rankOfSales(mi);
		//salesOfDate(mi);		
		
		
		//-------------------------
		//기능 작업 되는 대로 이 부분을 추가할 생각이라 아래 부분은 항상 주석 처리해서 공유할게요!
		
		/*while(true) {
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
				case 4: //관리자 모드 -- 표시 X
					admin(mi);
				default:
					System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
			}
			} catch(InputMismatchException e) {
				System.out.println("잘못 입력하셨습니다. 다시 입력해주세요.");
				sc.nextLine();
			}
			
		}*/
	}
	
	//관리자모드
	public static void admin(MaskImpl mi) {
		
	}
	
	//로그인 - void login(int id, String pass)
	public static void login(MaskImpl mi) throws Exception {
		//입력받기로는 000000-0000000로 넘어가는 건 0000000 7개만 처리
		System.out.println("주민등록번호를 입력해주세요.");
		int id = sc.nextInt(); 
		System.out.println("비밀번호를 입력해주세요.");
		String pass = sc.next();
		mi.login(id, pass);
		System.out.println("로그인 성공!");
		System.out.println("1.마스크 구입  2.마이페이지");
	}
	
	//회원가입 - void addConsumer(Consumer consumer)
	public static void addConsumer(MaskImpl mi) throws Exception {
		// id값 정규화 처리 --> 입력받을 땐 000000-0000000이거나 0000000000000로만 입력받을 수 있음.. 0000000 7개 자리만 들어갈 수 있도록
		System.out.println("주민등록번호를 입력해주세요.");
		int id = sc.nextInt();
		System.out.println("이름을 입력해주세요.");
		String name = sc.next();
		System.out.println("주소를 입력해주세요.");
		String address = sc.next();
		System.out.println("비밀번호를 입력해주세요.");
		String pass = sc.next();
		mi.addConsumer(new Consumer(id, name, address, pass));
		// 바로 구매할 수 있도록
	}
	
	//카트조회 - ArrayList<Cart> getCart(int id)
	public static void getCart(MaskImpl mi) {
		System.out.println("주민등록번호를 입력해주세요.");
		int id = sc.nextInt();
		for(Cart c : mi.getCart(id)) {
			System.out.println(c.toString());
		}
	}
	
	//마스크 삭제 - void deleteMask(int id, Cart cart) <-- 이부분
	public static void deleteMask(MaskImpl mi) {
		System.out.println("주민등록번호를 입력해주세요.");
		int id = sc.nextInt();
		System.out.println("배송 취소할 마스크를 입력해주세요.");
		String productName = sc.next();

		mi.deleteMask(id, new Cart(productName));
		System.out.println("마스크 삭제");
	}
	
	//마스크 추가 - void addMask(int id, Cart cart) <-- 이부분
	public static void addMask(MaskImpl mi) {
		System.out.println("주민등록번호를 입력해주세요.");
		int id = sc.nextInt();
		System.out.println("구매할 마스크를 입력해주세요.");
		String productName = sc.next();
		mi.addMask(id, new Cart(productName));
		System.out.println("마스크 구매 완료!");
	}
	
	//결제(ship_status : payment 대신 ship_status 가 O이면 결제하고 배송까지 된걸로) - boolean delivery(int orderNum)
	//배송여부조회(>> 결제랑 같은게 되어버림...ㅜㅠㅜㅠ)
	public static void delivery(MaskImpl mi) {
		int orderNum = sc.nextInt();
		System.out.println("배송여부체크 : " + mi.delivery(orderNum));
	}
	
	//마스크 사이즈 변경 - void updateMask(int id, Cart cart) <-- 이부분
	public static void updateMask(MaskImpl mi) {
		System.out.print("주민등록번호를 입력해주세요.");
		int id = sc.nextInt();
		System.out.println("상품명을 입력해주세요.");
		String productName = sc.next();
		System.out.println("변경할 상품 사이즈를 입력해주세요.");
		int size = sc.nextInt();
		mi.updateMask(id, new Product(productName, size));
	}
	
	//상품입고(insert/update) - void addProductMask(Product product)
	public static void addProductMask(MaskImpl mi) throws Exception {
		System.out.println("상품명을 입력해주세요.");
		String productName = sc.next();
		System.out.println("상품 수량을 입력해주세요.");
		int quantity = sc.nextInt();
		System.out.println("상품 크기를 입력해주세요.");
		int size = sc.nextInt();
		mi.addProductMask(new Product(productName, quantity, size));
	}
	
	//상품 조회 - ArrayList<Product> getProducts()
	public static void getProduct(MaskImpl mi) throws Exception {
		System.out.println("상품 조회");
		for(Product p : mi.getProducts()) {
			System.out.println(p.toString());
		}
	}
	
	//상품 출고 - void updateProductMask(Product product)
	public static void updateProductMask(MaskImpl mi) throws Exception {
		System.out.println("상품명을 입력해주세요.");
		String productName = sc.next();
		System.out.println("상품 수량을 입력해주세요.");
		int quantity = sc.nextInt();
		System.out.println("상품 크기를 입력해주세요."); // 1: 소, 2: 중, 3: 대 
		System.out.println("1.소    2.중    3.대");
		int size = sc.nextInt();
		
		mi.updateProductMask(new Product(productName, quantity, size));
		System.out.println("상품 출고 되었습니다.");
	}
	
	//사이즈별 재고수량조회  ex)"대 : 27개, 중 : 30개 ,소 : 10개" >> String 형식으로 출력하기 - String getQuatityOverSize()
	public static void getQuatityOverSize(MaskImpl mi) throws Exception {
		System.out.println("사이즈별 재고수량: " + mi.getQuatityOverSize());
	}
	
	//매출 순위 (인기,품절임박...)>> 매출=ship_status(int: 팔리면 1)의 합 - ArrayList<String> rankOfSales()
	public static void rankOfSales(MaskImpl mi) {
		System.out.println("매출 순위 확인");
		for(String s : mi.rankOfSales()) {
			System.out.println(s.toString());
		}
	}
	
	//구매 날짜별 판매량  - String SalesOfDate()
	public static void salesOfDate(MaskImpl mi) {
		System.out.println("구매 날짜별 판매량 확인: " + mi.SalesOfDate());
	}			
				
	//이부분 시간되면..
	//마스크 사이즈별 회원 연령조회 >> 회원아이디에서 - String getAgeOverSize()
	public static void getAgeOverSize(MaskImpl mi) {
		
	}
	
	//마스크 사이즈별 회원 성별조회  >> 회원아이디에서 - String getGenderOverSize()
	public static void getGenderOverSize(MaskImpl mi) {

	}
}
