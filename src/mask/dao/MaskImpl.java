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

import config.ServerInfo;
import mask.exception.RecordNotFoundException;
import mask.vo.Cart;
import mask.vo.Consumer;
import mask.vo.Product;

public class MaskImpl implements MaskTemplate{
	//공통된 로직
	public MaskImpl(String serverIp) throws ClassNotFoundException {
		Class.forName(ServerInfo.DRIVER_NAME);
		System.out.println("드라이브 로딩 성공____ in MaskImpl");
	}
	
	public boolean isCartExist(int productNum, Connection conn) throws SQLException {
		//있는지 없는지 존재유무 확인...
		
		String sql ="SELECT product_Num FROM cart WHERE product_Num=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1,productNum);
		ResultSet rs = ps.executeQuery();
		return rs.next();
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
	@Override
	public void addConsumer(Consumer consumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(int id,String pass) {
		// TODO Auto-generated method stub
		
	}

	@Override	// 여기까지
	public ArrayList<Cart> getCart(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = null;
		ResultSet rs = null;
		
		ArrayList<Cart> list = new ArrayList<>();
		
		try {
			conn = getConnect();
			
			String query = "jdbc.sql.getCart";
			ps= conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(new Cart(rs.getInt("consumer_id")));
			}
		}finally {
			closeAll(rs, ps, conn);
		}	
		return list;
	}

	@Override
	public void deleteMask(int id,Cart cart) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			
			if(id==cart.getConsumerid()) {
				
				String query = "jdbc.sql.delete";
				ps = conn.prepareStatement(query);
				rs= ps.executeQuery();
				
				if(rs.next()) {	// 있으면  삭제
					
					String query1 = ppt.getProperty("jdbc.sql.delete_delete");
					
					ps = conn.prepareStatement(query1);
					ps.setInt(1, id);
					
					System.out.println(ps.executeUpdate()+"MaskDelete...Delete");
					
			}else {
				System.out.println("다시 확인해주세요.");
			}
			}
			}finally{ 
			closeAll(ps, conn);
		}
}
	
	/**addMask,payment,updateMask
	 * @author 정용
	 */
	@Override	// 수정필요.
	public void addMask(int id,Cart cart) throws Exception {
		Connection conn = null;
		PreparedStatement ps= null;
		Properties ppt= null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			
			if(id==cart.getConsumerid()) {
				
				String query = "jdbc.sql.addMask";
				ps = conn.prepareStatement(query);
				rs= ps.executeQuery();
				
				if(rs.next()) {	// 있으면  업데이트
					int q = rs.getInt(1);
					int newQuantity = q + cart.getProduct().getQuatity();
					int s = rs.getInt(2);
					int newSize = s + cart.getProduct().getSize();
					
					String query1 = ppt.getProperty("jdbc.sql.addMask_update");
					
					ps = conn.prepareStatement(query1);
					ps.setInt(1, newQuantity);
					ps.setInt(2, newSize);
					ps.setInt(3, cart.getProduct().getProductNum());
					
					System.out.println(ps.executeUpdate()+"MaskADD ......update");
				}else {	// 없으면 추가
					String query2 = ppt.getProperty("jdbc.sql.addMask_insert");
					ps = conn.prepareStatement(query2);
					ps.setInt(1, cart.getConsumerid());
					ps.setInt(2, cart.getProduct().getProductNum());
					ps.setInt(3, cart.getProduct().getQuatity());
					ps.setInt(4, cart.getProduct().getSize());
					
					System.out.println(ps.executeUpdate()+"MaskADD ......add");
				}
			}else {
				System.out.println("아이디를 다시 확인해주세요.");
			}
		}finally{ 
			closeAll(ps, conn);
		}
	}

	@Override
	public void payment(int orderStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delivery(int order_num) throws SQLException, RecordNotFoundException {	// 1=배송준비 2=배송중 3=배송완료
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			
			String query= ppt.getProperty("jdbc.sql.delivery");
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, order_num);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getInt("ship_status")==1) {
					System.out.println("배송 준비 중 입니다.");
				}else if(rs.getInt("ship_status")==2) {
					System.out.println("배송 중 입니다. ");
				}else {
					System.out.println("배송 완료 되었습니다.");
				}
			}else {
				throw new RecordNotFoundException("주문 번호를 다시 확인해주세요");
			}
			
		}finally {
			closeAll(rs, ps, conn);
		}
	}
	
	@Override	// 수정필요함.
	public void updateMask(int id, Cart cart) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = null;
		
			try {
				conn = getConnect();
				
				if(!isCartExist(cart.getProductNum(), conn)) {	// productNum이 cart에 없을 경우
					throw new RecordNotFoundException("장바구니에 담긴 내용이 없습니다. 상품을 추가해주세요..");
				}else {	// productNum이 cart에 있을 경우 >>  update
					getProperties();
					String query =ppt.getProperty("jdbc.sql.updateMask") ;
					ps = conn.prepareStatement(query);
					
					ps.setInt(1, cart.getProduct().getQuatity());
					ps.setInt(2, cart.getProduct().getSize());
					ps.setInt(3, cart.getProductNum());
					
					System.out.println(ps.executeUpdate()+"건이 수정되었습니다.");
				}
			}finally{ 
				closeAll(ps, conn);
			}
	}
		
	/**addProductMask,getProduct,updateProductMask
	 * @author 채은
	 */
	@Override
	public void addProductMask(Product product) {
		// TODO Auto-generated method stub
		
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
	 * @throws SQLException 
	 */
	@Override
	public ArrayList<String> rankOfSales() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		
		try { 
			conn = getConnect();
			String query = "jdbc.sql.rankOfSales";

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(new String(rs.getString("product_name")+rs.getInt("sales")));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		return list;
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

	@Override
	public ArrayList<Product> getProducts() throws SQLException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		return null;
	}







}
