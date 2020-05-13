package mask.dao;

import java.util.ArrayList;
import java.util.function.Consumer;

import mask.vo.Cart;
import mask.vo.Product;

public interface MaskTemplate {

	//1. 회원등록
	void addConsumer(Consumer consumer) ;
	//2. 로그인
	void login(int id, String pass) ;
	//3. 카트조회
	ArrayList<Cart> getCart() ;
	//4. 마스크 삭제
	void deleteMask(int id,int size);
	//5. 마스크 추가
	void addMask(int id,int size,int quantity);
	//6. 결제(ship_status : payment 대신 ship_status 가 O이면 결제하고 배송까지 된걸로)
	boolean payment(int shipStatus);
	//7. 배송여부조회(>> 결제랑 같은게 되어버림...ㅜㅠㅜㅠ)
	//7.마스크 사이즈 변경
	void updateMask(int id,int size);
	//8. 상품입고(insert/update)
	void addProductMask(int productNum,int size,int quantity);
	//9. 상품 조회
	ArrayList<Product> getProduct();
	//10.상품 출고
	void deleteProductMask(int productNum,int size,int quantity);
	//// 분석함수
	//1.사이즈별 재고수량조회 ex)"대 : 27개, 중 : 30개 ,소 : 10개" >> String 형식으로 출력하기
	String getQuatityOverSize();
	//2.매출 순위 (인기,품절임박...)
	String rankOfSales();
	//3. 마스크 사이즈별 회원 연령조회
	String getAgeOverSize();
	//4. 마스크 사이즈별 회원 성별조회
	String getGenderOverSize();
	//5. 구매 일자별 판매량
	String SalesOfDate();	
	}

