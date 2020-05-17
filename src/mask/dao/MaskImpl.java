package mask.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Properties;

import config.ServerInfo;

import mask.exception.DuplicateIdException;
import mask.exception.RecordNotFoundException;
import mask.vo.Cart;
import mask.vo.Consumer;
import mask.vo.Product;

public class MaskImpl implements MaskTemplate{
	
	//공통된 로직
	public MaskImpl(String serverIp) throws ClassNotFoundException {
		Class.forName(ServerInfo.DRIVER_NAME);
		//System.out.println("드라이브 로딩 성공____ in MaskImpl");
	}
	
	public MaskImpl() {
	}
	
	public boolean isCartExist(int id, Connection conn) throws SQLException {
		//있는지 없는지 존재유무 확인...	
		String sql ="SELECT * FROM cart WHERE consumer_id=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1,id);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}
	
	// 비지니스 로직

	@Override
	public Connection getConnect() throws SQLException {
		Connection conn=DriverManager.getConnection(ServerInfo.URL,ServerInfo.USER,ServerInfo.PASS);
		//System.out.println("MaskImpl Connected ____ in MaskImpl");
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

	/**추가된 공통 메서드
	 * @author 채은
	 * @param id
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Override
	public Properties getProperties() throws Exception{			
		Properties ppt=new Properties();
		ppt.load(new FileInputStream("src/config/jdbc.properties"));
		return ppt;
	}

	public boolean isConsumerExist(int id,Connection conn) throws Exception {
		String sql_isConsumerExist="SELECT id FROM consumer where id=?";
		PreparedStatement ps=conn.prepareStatement(sql_isConsumerExist);
		ps.setInt(1,id);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}

	
	//비지니스 로직
	/**addConsumer,login,getCart,deleteMask
	 * @author 소희
	 * @throws Exception 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
	public Consumer selectConsumer(int ssn_i) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();
		ResultSet rs = null;
		Consumer consumer = new Consumer();
		
		try {
			conn = getConnect();
			String sql = ppt.getProperty("jdbc.sql.selectConsumer");
			ps = conn.prepareStatement(sql);
			ps.setInt(1, ssn_i);
			rs = ps.executeQuery();
			if(rs.next()) {
				consumer.setId(rs.getInt("id"));
				consumer.setPass(rs.getString("pass"));
				consumer.setName(rs.getString("name"));
				consumer.setAddress(rs.getString("address"));
			}
		} finally {
			closeAll(rs, ps, conn);
		}
		return consumer;
	}
	
	public void updateConsumer(Consumer consumer) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();
		
		try {
			conn = getConnect();
			String sql = ppt.getProperty("jdbc.sql.updateConsumer");
			ps = conn.prepareStatement(sql);
			ps.setString(1, consumer.getAddress());
			ps.setString(2, consumer.getPass());
			ps.setInt(3, consumer.getId());
			ps.executeUpdate();
		} finally {
			closeAll(ps, conn);
		}
	}
	
	@Override
	public void addConsumer(Consumer consumer) throws Exception {
		Connection conn=null;
		PreparedStatement ps=null;
		Properties ppt=getProperties();
		
		try {
			conn=getConnect();
			String sql=ppt.getProperty("jdbc.sql.addConsumer_insert");
			ps=conn.prepareStatement(sql);
			ps.setInt(1,consumer.getId());
			ps.setString(2,consumer.getName());
			ps.setString(3,consumer.getAddress());
			ps.setString(4,consumer.getPass());
			System.out.println(consumer.getName()+"고객님 "+ps.executeUpdate()+"분을 회원으로 등록 했습니다.");
		}catch(SQLIntegrityConstraintViolationException e) {//회원 는 중복이 불가능하다는 java자체의 오류로 뜹니다.>>SQLIntegrityConstraintViolationException 
			System.out.println("이미 가입된 회원입니다. 로그인해주세요.");
		}finally {closeAll(ps, conn);}
	}

	@Override
	public boolean login(int id,String pass) throws Exception {
		Connection conn=null;
		PreparedStatement ps=null;
		Properties ppt=getProperties();
		ResultSet rs;
		
		try {
			conn=getConnect();
			
			ps = conn.prepareStatement(ppt.getProperty("jdbc.sql.login"));
			ps.setInt(1, id);
			ps.setString(2, pass);
			rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println("로그인 성공했습니다~!");
				return true;
			} else {
				System.out.println("로그인 실패!");
				return false;
			}
		}finally {closeAll(ps, conn);}
	}
	
	public ArrayList<Cart> getPaymentList(int id) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();
		ResultSet rs = null;
		ArrayList<Cart> list = new ArrayList<>();
		Product product = new Product();
		
		try {
			conn = getConnect();
			String query = ppt.getProperty("jdbc.sql.getPaymentList");
			
			ps= conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new Cart(id, rs.getInt("c.order_num"), rs.getString("c.date"), 
						rs.getInt("c.quantity"), rs.getInt("c.order_status"), rs.getInt("c.ship_status"), new Product(
						rs.getString("p.product_name"),
						rs.getString("p.size")
						)));
			}
		}finally {
			closeAll(rs, ps, conn);
		}	
		
		return list;
	}

	@Override	// 여기까지
	public ArrayList<Cart> getCart(int id) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();
		ResultSet rs = null;
		ArrayList<Cart> list = new ArrayList<>();
		Product product = new Product();
		
		try {
			conn = getConnect();
			String query = ppt.getProperty("jdbc.sql.getCart");
			
			ps= conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new Cart(id, rs.getInt("c.order_num"), rs.getString("c.date"), 
						rs.getInt("c.quantity"), rs.getInt("c.order_status"), rs.getInt("c.ship_status"), new Product(
						rs.getString("p.product_name"),
						rs.getString("p.size")
						)));
			}
		}finally {
			closeAll(rs, ps, conn);
		}	
		return list;
	}
	
	
	

	@Override
	//장바구니에서 삭제... 주문취소/배송취소와 다름
	public void deleteMask(int id,int orderNum) throws RecordNotFoundException, Exception{//없앨 주문번호 직접 입력
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();

		try {
		conn = getConnect();
		if(!isConsumerExist(id, conn)) System.out.println("없는 고객입니다.");
		else{
			if(!isCartExist(id, conn)) System.out.println("장바구니에 담은 상품이 없습니다.");
			else {
				
				String query = ppt.getProperty("jdbc.sql.delete_delete");
				ps = conn.prepareStatement(query);
				ps.setInt(1, id);
				ps.setInt(2, orderNum);
				if(ps.executeUpdate()==0) throw new RecordNotFoundException("해당 상품을 장바구니에 담지 않았습니다.");
				else System.out.println("장바구니에서 삭제!");		
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
	public void addMask(Cart cart) throws Exception {
		Properties ppt=getProperties();
		Connection conn = null;
		PreparedStatement ps= null;
		
		
		try {
			conn = getConnect();
			
			String sql=ppt.getProperty("jdbc.sql.addMask");
			ps = conn.prepareStatement(sql);
			ps.setInt(1, cart.getConsumerId());
			ps.setInt(2, cart.getProductNum());
			ps.setInt(3, cart.getQuantity());
			ps.setInt(4, 0);
			ps.setInt(5, 0);
			ps.executeUpdate();
		}finally {
			closeAll(ps, conn);
		}
	}
	
	/**payment,chaeckOrderStatus
	 * @author Im chae eun
	 * @return 
	 * @throws Exception 
	 * 
	 */
	@Override
	public void payment(int id, int orderNum)  throws RecordNotFoundException, SQLException ,Exception {		
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();
		ResultSet rs = null;
		Cart cart = new Cart();

		try {
			conn = getConnect();
			//Transaction -- Start : status 1로 변경함 + 재고에서 수정
			conn.setAutoCommit(false);
			
			//status 1로 변경
			String query1 = ppt.getProperty("jdbc.sql.pay_order_status");
			ps = conn.prepareStatement(query1);
			ps.setInt(1,orderNum);
			ps.setInt(2,id); 
			ps.executeUpdate();
			
			//
			String query2 = ppt.getProperty("jdbc.sql.pay_get_cart");
			ps = conn.prepareStatement(query2);
			ps.setInt(1, orderNum);
			rs = ps.executeQuery();
			if(rs.next()) {
				cart.setProductNum(rs.getInt("product_num"));
				cart.setQuantity(rs.getInt("quantity"));
			}
			
			//재고에서 -UPDATE product SET stock=stock-? WHERE product_num=?
			String query3 = ppt.getProperty("jdbc.sql.pay_order_stock");
			ps = conn.prepareStatement(query3);
			ps.setInt(1, cart.getQuantity());
			ps.setInt(2, cart.getProductNum());
			ps.executeUpdate();
			
			conn.commit(); 
		} catch(Exception e) {
			conn.rollback();
	    } finally {
			conn.setAutoCommit(true);
			closeAll(ps, conn);
		}
		
		/*try {
			conn = getConnect();
			//System.out.println("====== transation start =======");
			conn.setAutoCommit(false);
			if(isCartExist(id, conn)) {
				if(chaeckOrderStatus(id,orderNum,conn)==0) {
					ps = conn.prepareStatement(sql_orderStatus);			
					
					System.out.println(ps.executeUpdate()+", "+id+"님, 주문번호 : "+ orderNum+"을 결제하셨습니다.");	
					chaeckOrderStatus(id,orderNum,conn);
					ps.close();
					
					//재고에서 물건 빼기
					String sql="select * from cart where consumer_id=? and product_num=? and ship_status=2";
					ps=conn.prepareStatement(sql);
					ps.setInt(1,id);
					ps.setInt(2,orderNum);
					rs=ps.executeQuery();
					if(rs.next()) {
						//updateProductMask(new Product(productNum, rs.getInt("quantity"),rs.getInt("size")));
						}ps.close();
					// delivery update하기
					if(chaeckShipStatus(id,orderNum,conn)==2) {
						String sql2=ppt.getProperty("jdbc.sql.pay_ship_status");
						ps=conn.prepareStatement(sql2);
						ps.setInt(1,id); ps.setInt(2,orderNum);
						System.out.println(ps.executeUpdate()+"개 상품 배송완료");
					}
					conn.commit(); // ---------- Transaction End.. 커밋 말고 또 하나..
					
					System.out.println("======== Transaction End ========");
				}else System.out.println("이미 결제한 상품입니다.");				
			}else throw new RecordNotFoundException();	
		}finally {
			closeAll(rs, ps, conn);
		}*/
							
	}

	// order_status 확인을 위한 메서드
	public int chaeckOrderStatus(int id, int productNum, Connection conn) throws Exception {
		Properties ppt=getProperties();
		String query= ppt.getProperty("jdbc.sql.pay_select");
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1,id);
		ps.setInt(2,productNum);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			System.out.println("consumerId : "+rs.getInt("consumer_id")+
							", productNum : "+rs.getString("product_num")+
							", quantity : "+rs.getInt("quantity")+
							", size : "+rs.getInt("size")+
							", orderStatus : "+rs.getInt("order_status")+
							", shipStatus : "+rs.getInt("ship_status"));
			}
		return rs.getInt("order_status");
		
	}
	public int chaeckShipStatus(int id, int productNum, Connection conn) throws Exception {
		Properties ppt=getProperties();
		String query= ppt.getProperty("jdbc.sql.pay_select");
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1,id);
		ps.setInt(2,productNum);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			System.out.println("shipStatus : "+rs.getInt("ship_status"));
			}
			return rs.getInt("ship_status");
		}
		
	
	@Override // 배송준비 --> 배송중으로 변경, 배송중 --> 배송완료로 변경 (ship_status 변경)
	public void delivery(int orderNum) throws Exception {	// 1=배송준비 2=배송중 3=배송완료
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt=getProperties();
		ResultSet rs = null;
		Cart cart = new Cart();

		try {
			conn = getConnect();
			String query1 = "SELECT * FROM cart WHERE order_num=?";
			ps = conn.prepareStatement(query1);
			ps.setInt(1, orderNum);
			rs = ps.executeQuery();
			if(rs.next()) {
				cart.setOrderNum(rs.getInt("order_num"));
				cart.setShipStatus(rs.getInt("ship_status"));
			}
			
			String query2 = ppt.getProperty("jdbc.sql.delivery");
			//jdbc.sql.delivery = UPDATE cart SET ship_status=? AND order_num=?
			ps = conn.prepareStatement(query2);
			
			if(cart.getShipStatus()==0) {
				ps.setInt(1, 1);
				ps.setInt(2, orderNum);
				ps.executeUpdate();
			} else if(cart.getShipStatus()==1) {
				ps.setInt(1, 2);
				ps.setInt(2, orderNum);
				ps.executeUpdate();
			}
			
			
			/*
			ps.setInt(1, orderNum);
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
			}*/
			
		}finally {
			closeAll(rs, ps, conn);
		}
	}

	@Override	// 수정필요함.
	public void updateMask(Cart cart) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();
		
		try {
			conn = getConnect();
			String query = ppt.getProperty("jdbc.sql.updateMask");
			ps = conn.prepareStatement(query);
			ps.setInt(1, cart.getProductNum());
			ps.setInt(2, cart.getQuantity());
			ps.setInt(3, cart.getOrderNum());
			ps.setInt(4, cart.getConsumerId());
			ps.executeUpdate();
			
			/*if(!isCartExist(cart.getProductNum(), conn)) {	// productNum이 cart에 없을 경우
				throw new RecordNotFoundException("장바구니에 담긴 내용이 없습니다. 상품을 추가해주세요..");
			}else {	// productNum이 cart에 있을 경우 >>  update
				ps.setInt(1, cart.getProduct().getStock());
				//ps.setInt(2, cart.getProduct().getSize());
				ps.setInt(3, cart.getProductNum());
				
				System.out.println(ps.executeUpdate()+"건이 수정되었습니다.");
			}*/
		}finally{ 
			closeAll(ps, conn);
		}
	}
		
	/**addProductMask,getProduct,updateProductMask
	 * @author 채은
	 * @throws DuplicateIdException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 */
	
	@Override
	public boolean isProductExist(String productName,Connection conn) throws Exception {
		String sql_isProductExist="SELECT * FROM product where product_name=?";
		PreparedStatement ps=conn.prepareStatement(sql_isProductExist);
		ps.setString(1,productName);
		ResultSet rs = ps.executeQuery();
		//if(rs.next()) System.out.println(rs.getString("product_name"));
		return rs.next();
	}
	
	public ArrayList<Cart> getCartAll() throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Properties ppt = getProperties();
		ArrayList<Cart> list = new ArrayList<>();
		
		try {
			conn = getConnect();
			String query = ppt.getProperty("jdbc.sql.getCartAll");
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new Cart(
						rs.getInt("c.order_num"),
						rs.getString("c.date"),
						rs.getInt("c.quantity"),
						rs.getInt("c.ship_status"),
						new Product(rs.getString("p.product_name"), rs.getString("p.size"))
				));
			}
		} finally {
			closeAll(rs, ps, conn);
		}
		
		return list;
	}
	
	public void paymentCancel(int id, int orderNum) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();
		ResultSet rs = null;
		Cart cart = new Cart();
		try {
			conn = getConnect();
			//Transaction -- Start : cart에서 삭제, 재고 다시 추가
			conn.setAutoCommit(false);
			
			//
			String query2 = ppt.getProperty("jdbc.sql.pay_get_cart");
			ps = conn.prepareStatement(query2);
			ps.setInt(1, orderNum);
			rs = ps.executeQuery();
			if(rs.next()) {
				cart.setProductNum(rs.getInt("product_num"));
				cart.setQuantity(rs.getInt("quantity"));
			}
			
			//재고에서 -UPDATE product SET stock=stock-? WHERE product_num=?
			String query3 = ppt.getProperty("jdbc.sql.pay_order_stock_plus");
			ps = conn.prepareStatement(query3);
			ps.setInt(1, cart.getQuantity());
			ps.setInt(2, cart.getProductNum());
			ps.executeUpdate();
			
			//cart에서 삭제
			//DELETE FROM cart WHERE consumer_id=? AND order_num=?
			String query1 = ppt.getProperty("jdbc.sql.pay_order_cancel");
			ps = conn.prepareStatement(query1);
			ps.setInt(1,id);
			ps.setInt(2,orderNum); 
			ps.executeUpdate();
			
			conn.commit(); 
		} catch(Exception e) {
			conn.rollback();
	    } finally {
			conn.setAutoCommit(true);
			closeAll(ps, conn);
		}
	}
	
	@Override
	public void addProductMask(int productNum, int quantity) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();
		
		try {
			conn = getConnect();
			String query = ppt.getProperty("jdbc.sql.updateProductMask");
			ps = conn.prepareStatement(query);
			ps.setInt(1, quantity);
			ps.setInt(2, productNum);
			ps.executeUpdate();
		} finally {
			closeAll(ps, conn);
		}
		/*
		// 1) 기존 상품이 있는지 확인 --- 없으면 INSERT
		try {
		conn=getConnect();
		if(isProductExist(product.getProductName(), conn)) {
			//if(rs.getInt("size")==product.getSize()){
				int q=rs.getInt("stock");
				int addQ=product.getStock();
				int newQuantity=addQ+q;
				String sql=ppt.getProperty("jdbc.sql.addProductMask_update");
				ps=conn.prepareStatement(sql);
				ps.setString(2,product.getProductName());
				//ps.setInt(3,product.getSize());
				ps.setInt(1,newQuantity);
				System.out.println(ps.executeUpdate()+"개 상품 추가 입고 완료 ===== ");}	
			// 2) ---- 기존에 있는 상품이면 Update
			else {
				String sql=ppt.getProperty("jdbc.sql.addProductMask_insert");
				ps=conn.prepareStatement(sql);
				ps.setString(1,product.getProductName());
				ps.setInt(2,product.getStock());
				//ps.setInt(3,product.getSize());		
				System.out.println("===== "+ps.executeUpdate()+"개의 상품 입고 완료 ===== ");}
		}
		}finally {
			closeAll(rs,ps,conn);
		}*/
	}

	@Override
	public ArrayList<Product> getProducts() throws Exception{
		ArrayList<Product> products=new ArrayList<Product>();
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Properties ppt=null;
		
		try {
			conn=getConnect();
			ppt=getProperties();
			String sql=ppt.getProperty("jdbc.sql.getProducts");
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()) {
				products.add(new Product(rs.getInt("product_num"),
						rs.getString("product_name"), 
						rs.getInt("stock"), 
						rs.getString("size")));
			}
		}finally {
			closeAll(rs,ps,conn);
		}
		return products;
	}

	@Override // updateProductMask는 출고 시 수량 제거하는 걸로 했어여~!!
	public void updateProductMask(Product product) throws Exception {
		Connection conn=null;
		PreparedStatement ps=null;
		Properties ppt=getProperties();
		ResultSet rs=null;
		try {
			conn=getConnect();
			String sql="select * from product where product_num=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1,product.getProductNum());
			rs=ps.executeQuery();
			int s =0;
			if(rs.next()) { 
				System.out.println(rs.getString("product_name")+"/"+rs.getInt("stock"));
				s=rs.getInt("stock");}
			int newStock=s-product.getStock();
			ps.close();
			String sql1=ppt.getProperty("jdbc.sql.updateProductMask_stock");
			ps=conn.prepareStatement(sql1);
			ps.setInt(1,newStock);
			ps.setInt(2,product.getProductNum());
			System.out.println(ps.executeUpdate()+", 상품 출고 완료 ===== ");	
		}finally {
			closeAll(ps,conn);
		}
	}

	
	
	/**사이즈별 재고수량조회
	 * 분석함수
	 * @author 채은
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Override
	public String getQuatityOverSize() throws Exception {
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Properties ppt=getProperties();
		getProperties();
		String message = "";
		try {
		conn=getConnect();
		String sql=ppt.getProperty("jdbc.sql.getQuatityOverSize"); // null..
		ps=conn.prepareStatement(sql);
		rs = ps.executeQuery();
		
			while(rs.next()) {
				message += rs.getString("p.size") + ": " + rs.getString("count") + "    ";
			}
		}finally {
			closeAll(rs,ps, conn);
		}
		return message;
	}

	/**매출 순위
	 * 분석함수
	 * @author 정용
	 * @throws Exception 
	 */
	@Override
	public ArrayList<String> rankOfSales() throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		Properties ppt=getProperties();
		
		try { 
			conn = getConnect();
			String query = ppt.getProperty("jdbc.sql.rankOfSales");

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(new String(rs.getString("p.size")+": "+rs.getInt("c.sales")));
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
	public ArrayList<String> SalesOfDate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//p.size, sum(quantity) over(partition by p.product_num) sizeTotal, sum(quantity) over() total
	
	public ArrayList<String> sizeTotal() throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		Properties ppt=getProperties();
		
		try { 
			conn = getConnect();
			String query = ppt.getProperty("jdbc.sql.total");

			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while(rs.next()) {
				list.add(new String(rs.getString("p.size")+": "+rs.getInt("sizeTotal") + ", 총합계 : " + rs.getInt("total")));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
		return list;

	}

}
