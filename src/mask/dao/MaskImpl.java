package mask.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.function.Consumer;

import config.ServerInfo;
import mask.vo.Cart;
import mask.vo.Product;

public class MaskImpl implements MaskTemplate{
	//공통된 로직
	public MaskImpl(String serverIp) throws ClassNotFoundException {
		Class.forName(ServerInfo.DRIVER_NAME);
		System.out.println("드라이브 로딩 성공____ in MaskImpl");
	}
	// 비지니스 로직
	@Override
	public Connection getConnect() throws SQLException {
		Connection conn=DriverManager.getConnection(ServerInfo.URL,ServerInfo.USER,ServerInfo.PASS);
		System.out.println("MaskImpl Connected ____ in MaskImpl");
		return conn;
	}

	@Override
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
		if(!(ps==null))ps.close();
		if(!(conn==null)) conn.close();
	}

	@Override
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException {
		if(!(rs==null)) rs.close();
		closeAll(ps,conn);
	}

	public void getProperties() throws FileNotFoundException, IOException{			
		Properties ppt=new Properties();
		ppt.load(new FileInputStream("src/config/jdbc.properties"));
	}
	
	//비지니스 로직
	/**addConsumer,login,getCart,deleteMask
	 * @author 소희
	 */
	
public class Consumer implements addConsumer, login {
	@Override
	public void addConsumer(Consumer consumer) {
		System.out.println("회원가입");
		
	}

	@Override
	public void login(Consumer consumer) {
		System.out.println("로그인");
		
	}
public class cart extends getCart implements  {
	@Override
	public ArrayList<Cart> getCart() {
		
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
