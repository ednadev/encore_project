package mask.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mask.vo.Cart;
import mask.vo.Product;
import mask.vo.Consumer;

public interface MaskTemplate {

	//1. 회원등록
	void addConsumer(Consumer consumer);
	//2. 로그인
	void login(Consumer consumer) ;
	//3. 카트조회
	ArrayList<Cart> getCart() ;
	//4. 마스크 삭제
	void deleteMask(Cart cart);
	//5. 마스크 추가
	void addMask(Cart cart);
	//6. 결제(ship_status : payment 대신 ship_status 가 O이면 결제하고 배송까지 된걸로)
	boolean payment(int shipStatus);
	//6. 배송여부조회(>> 결제랑 같은게 되어버림...ㅜㅠㅜㅠ)
	//7.마스크 사이즈 변경
	void updateMask(Cart cart);
	//8. 상품입고(insert/update)
	void addProductMask(Product product);
	//9. 상품 조회
	ArrayList<Product> getProduct();
	//10.상품 출고
	void updateProductMask(Product product);

	//// 분석함수
	//1.사이즈별 재고수량조회  ex)"대 : 27개, 중 : 30개 ,소 : 10개" >> String 형식으로 출력하기
	String getQuatityOverSize();
	//2.매출 순위 (인기,품절임박...)>> 매출=ship_status(int: 팔리면 1)의 합 
	ArrayList<String> rankOfSales();
	//3. 구매 날짜별 판매량 
	String SalesOfDate();
	Connection getConnect() throws SQLException;
	void closeAll(PreparedStatement ps, Connection conn) throws SQLException;
	void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException;
	
/*	//4. 마스크 사이즈별 회원 연령조회 >> 회원아이디에서
	String getAgeOverSize();
	//5. 마스크 사이즈별 회원 성별조회  >> 회원아이디에서 
	String getGenderOverSize();
*/	}
