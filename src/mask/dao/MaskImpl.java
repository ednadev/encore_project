package mask.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import config.ServerInfo;
import mask.vo.Cart;
import mask.vo.Product;
import mask.vo.Consumer;

public class MaskImpl implements MaskTemplate{
	
	//공통된 로직
	public MaskImpl(String serverIp) throws ClassNotFoundException {
		Class.forName(ServerInfo.DRIVER_NAME);
		//System.out.println("드라이브 로딩 성공____ in MaskImpl");
	}
	
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
	
	@Override
	public Properties getProperties() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	//비지니스 로직
	/**addConsumer,login,getCart,deleteMask
	 * @author 소희
	 */
	@Override
	public void addConsumer(Consumer consumer) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(int id, String pass) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Cart> getCart(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMask(int id, Cart cart) {
		// TODO Auto-generated method stub
		
	}

	/**addMask,payment,updateMask
	 * @author 정용
	 */
	@Override
	public void addMask(int id, Cart cart) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delivery(int orderNum) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateMask(int id, Product product) {
		// TODO Auto-generated method stub
		
	}

	/**addProductMask,getProduct,updateProductMask
	 * @author 채은
	 */
	@Override
	public void addProductMask(Product product) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Product> getProducts() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProductMask(Product product) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**사이즈별 재고수량조회
	 * 분석함수
	 * @author 채은
	 */
	@Override
	public String getQuatityOverSize() throws Exception {
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
