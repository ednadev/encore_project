package mask.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.spi.CalendarDataProvider;

import javax.naming.spi.DirStateFactory.Result;

import config.ServerInfo;

import mask.exception.DuplicateIdException;
import mask.exception.OutOfStockException;
import mask.exception.PasswordMissmatchException;
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

	/**추가된 공통 메서드
	 * @author 채은
	 * @param id
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */

	public Properties getProperties() throws Exception{			
		Properties ppt=new Properties();
		ppt.load(new FileInputStream("src/config/jdbc.properties"));
		return ppt;
	}
	
	public boolean isConsumerExist(int id,Connection conn) throws Exception {
		String sql_isConsumerExist="SELECT * FROM consumer where id=?";
		PreparedStatement ps=conn.prepareStatement(sql_isConsumerExist);
		ps.setInt(1,id);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}

	
	//비지니스 로직
	/**addConsumer,login,getCart,deleteMask
	 * @author 소희
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
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
				System.out.println("기존에 사용중인 회원id입니다. ");
			}finally {closeAll(ps, conn);}
	}

	@Override
	public void login(int id,String pass) throws Exception {
		Connection conn=null;
		PreparedStatement ps=null;
		Properties ppt=getProperties();
		ResultSet rs;
		
		try {
			conn=getConnect();
			System.out.println("Id와 password를 입력해주세요");
			ps=conn.prepareStatement(ppt.getProperty("jdbc.sql.login"));
			ps.setInt(1,id);
			rs=ps.executeQuery();
			rs.next();
			if(id!=rs.getInt("id")) throw new SQLException();
			else {
				if(pass.equals(rs.getString("pass"))) System.out.println("로그인 성공했습니다~!");
				else throw new PasswordMissmatchException();
			}
		}finally {closeAll(ps, conn);}
	}

		@Override	// 여기까지
	public ArrayList<Cart> getCart(int id) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = null;
		ResultSet rs = null;
		ArrayList<Cart> list = new ArrayList<>();
		
		try {
			conn = getConnect();
			ppt=getProperties();//추가된 코드
			String query = ppt.getProperty("jdbc.sql.getCart");
			ps= conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new Cart(rs.getInt("order_num"),
								id,rs.getInt("product_num"),rs.getInt("order_status"),
								rs.getInt("ship_status"),rs.getInt("quantity"),
								rs.getInt("size")));// 채은 수정...이거는 제가 작성하다가 고친거라 변경하셔도 됩니ㅏㄷㅇ...
				}
		}finally {
			closeAll(rs, ps, conn);
		}	
		return list;
	}

	@Override
	//장바구니에서 삭제... 주문취소/배송취소와 다름
	public void deleteMask(int id,int productNum) throws RecordNotFoundException, Exception{//없앨 주문번호 직접 입력
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = null;
		ResultSet rs = null;
		try {
		conn = getConnect();
		if(!isConsumerExist(id, conn)) System.out.println("없는 고객입니다.");
		else{
			if(!isCartExist(id, conn)) System.out.println("장바구니에 담은 상품이 없습니다.");
			else {
				ppt=getProperties();//추가된 코드
				String query = ppt.getProperty("jdbc.sql.delete_delete");
				ps = conn.prepareStatement(query);
				ps.setInt(1, id);
				ps.setInt(2, productNum);
				if(ps.executeUpdate()==0) throw new RecordNotFoundException("해당 상품을 장바구니에 담지 않았습니다.");
				else System.out.println(ps.executeUpdate()+"MaskDelete...Delete");		
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
				ppt=getProperties();//추가된 코드
				String query = ppt.getProperty("jdbc.sql.addMask");
				ps = conn.prepareStatement(query);
				rs= ps.executeQuery();
				
				if(rs.next()) {	// 있으면  업데이트
					int q = rs.getInt(1);
					int newQuantity = q + cart.getProduct().getStock();
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
					ps.setInt(3, cart.getProduct().getStock());
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
	/**payment,chaeckOrderStatus
	 * @author Im chae eun
	 * @return 
	 * @throws Exception 
	 * 
	 */
	@Override
	public void payment(int id, int productNum)  throws RecordNotFoundException, SQLException ,Exception {		
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnect();
			ppt=getProperties();
			System.out.println("====== transation start =======");
			conn.setAutoCommit(false);
			if(isCartExist(id, conn)) {
				if(checkOrderStatus(id,productNum,conn)==0) {
				// 결제하기 : order_status를 1로 update	
				String sql_orderStatus= ppt.getProperty("jdbc.sql.pay_order_status");
				ps = conn.prepareStatement(sql_orderStatus);			
				ps.setInt(1,InputOrderNum());ps.setInt(2,1);ps.setInt(3,2); 
				ps.setInt(4,id);ps.setInt(5,productNum);
				System.out.println(ps.executeUpdate()+", "+id+"님, 상품번호 : "+ productNum+"을 결제하셨습니다.");	
				checkOrderStatus(id,productNum,conn);
				ps.close();
				//재고에서 물건 빼기
				String sql="select * from cart where consumer_id=? and product_num=? and ship_status=2";
				ps=conn.prepareStatement(sql);
				ps.setInt(1,id);ps.setInt(2,productNum);
				rs=ps.executeQuery();
				if(rs.next()) {
					updateProductMask(new Product(productNum, rs.getInt("quantity"),rs.getInt("size")));
					}ps.close();
				// delivery update하기
				if(checkShipStatus(id,productNum,conn)==2) {
					String sql2=ppt.getProperty("jdbc.sql.pay_ship_status");
					ps=conn.prepareStatement(sql2);
					ps.setInt(1,id); ps.setInt(2,productNum);
					System.out.println(ps.executeUpdate()+"개 상품 배송완료");
				}
				conn.commit();
				System.out.println("======== Transaction End ========");
				}else System.out.println("이미 결제한 상품입니다.");				
			}else throw new RecordNotFoundException();	
		}finally {
			closeAll(rs, ps, conn);
		}
	}

	public int InputOrderNum() {
		Calendar cal = Calendar.getInstance();
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		int day=cal.get(Calendar.DAY_OF_YEAR);
		return day*100+hour;
	}
		
	// order_status 확인을 위한 메서드
	public int checkOrderStatus(int id, int productNum, Connection conn) throws Exception {
		Properties ppt=getProperties();
		String query= ppt.getProperty("jdbc.sql.pay_select");
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1,id);ps.setInt(2,productNum);
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
	
	public int checkShipStatus(int id, int productNum, Connection conn) throws Exception {
		Properties ppt=getProperties();
		String query= ppt.getProperty("jdbc.sql.pay_select");
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1,id); ps.setInt(2,productNum);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) System.out.println("shipStatus : "+rs.getInt("ship_status"));
			return rs.getInt("ship_status");
		}
			
	@Override
	public void delivery(int orderNum) throws Exception {	// 1=배송준비 2=배송중 3=배송완료
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = null;
		ResultSet rs = null;

		try {
			conn = getConnect();
			ppt=getProperties();
			String query= ppt.getProperty("jdbc.sql.delivery");
			ps = conn.prepareStatement(query);
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
					
					ps.setInt(1, cart.getProduct().getStock());
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
	 * @throws DuplicateIdException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 */

	public boolean isProductExist(String productName, int size,Connection conn) throws Exception {
		String sql_isProductExist="SELECT * FROM product where product_name=? and size=?";
		PreparedStatement ps=conn.prepareStatement(sql_isProductExist);
		ps.setString(1,productName); ps.setInt(2,size);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}
	
	@Override
	public void addProductMask(Product product) throws Exception {
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Properties ppt=getProperties();
		// 1) 기존 상품이 있는지 확인 --- 있으면 update
		try {
			conn=getConnect();
			if(!isProductExist(product.getProductName(),product.getSize(),conn)) {
				String sql_addProductMask_insert=ppt.getProperty("jdbc.sql.addProductMask_insert");
				PreparedStatement ps2=conn.prepareStatement(sql_addProductMask_insert);
				ps2.setString(1,product.getProductName());
				ps2.setInt(2,product.getStock());
				ps2.setInt(3,product.getSize());		
				System.out.println("===== "+ps2.executeUpdate()+"개의 상품 입고 완료 ===== ");}
			else {
				ps=conn.prepareStatement("select stock from product where product_name=? and size=?");
				ps.setInt(1,product.getProductNum()); ps.setInt(2,product.getSize());
				rs=ps.executeQuery();
				int q=0;
				while(rs.next()) {q=rs.getInt("stock");}
				int newQ=q+product.getStock();
				rs.close();
				ps.close();
				String sql_addProductMask_update=ppt.getProperty("jdbc.sql.addProductMask_update");
				PreparedStatement ps1=conn.prepareStatement(sql_addProductMask_update);
				ps1.setString(2,product.getProductName());
				ps1.setInt(3,product.getSize());
				ps1.setInt(1,newQ);
				// 아래 코드에서 Exception in thread "main" com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation:
				System.out.println(ps1.executeUpdate()+"개 상품 추가 입고 완료 ===== ");
		// 2) ---- 기존에 없는 상품이면 insert
		}	
		}finally {
			closeAll(rs,ps,conn);
		}
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
										rs.getInt("size")));
				}
			System.out.println("====== 모든 상품 조회 ======");
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
	public void getQuatityOverSize() throws Exception {
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Properties ppt=getProperties();
		
		String result = "";
		try {
		conn=getConnect();
		String sql=ppt.getProperty("jdbc.sql.getQuatityOverSize");
		ps=conn.prepareStatement(sql);	
		rs=ps.executeQuery();
		while(rs.next()) {
			if(rs.getInt("size")==1) result= " 소  : "+rs.getInt("stock")+"개";
			else if(rs.getInt("size")==2) result= " 중 : "+rs.getInt("stock")+"개";
			else if(rs.getInt("size")==3) result= " 대  : "+rs.getInt("stock")+"개";
			System.out.println(result);
		}
		}finally {
			closeAll(rs,ps, conn);
		}
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
		
		try { 
			conn = getConnect();
			Properties ppt=getProperties();
			String query =ppt.getProperty("jdbc.sql.rankOfSales");
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) list.add(new String(rs.getString("product_name")+rs.getInt("sales")));
		}finally {
			closeAll(rs, ps, conn);
		}
		return list;
	}

	/**구매 날짜별 판매량 
	 * 분석함수
	 * @author 소희
	 * @throws Exception 
	 */
	@Override
	public void SalesOfDate() throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result="";
		try { 
			conn = getConnect();
			Properties ppt=getProperties();
			String query =ppt.getProperty("jdbc.sql.SalesOfDate");
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println("날짜 : "+rs.getInt("date")+"/ 판매 수량 :"+rs.getInt("quantity"));
			}
		}finally {
			closeAll(rs, ps, conn);
		}
	}
	
	@Override
	public void updateAddress(int consumerid, String address) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Properties ppt = getProperties();
		
		try {
			conn = getConnect();
			if(!isConsumerExist(consumerid, conn)) {
				throw new RecordNotFoundException("해당 고객번호가 존재하지 않습니다.");
			}else {	
				String query =ppt.getProperty("jdbc.sql.updateAddress") ;
				ps = conn.prepareStatement(query);
				ps.setString(1, address);
				ps.setInt(2, consumerid);
				System.out.println(ps.executeUpdate()+"건이 수정되었습니다.");
			}
		}finally{ 
			closeAll(ps, conn);
		}
	}
	
}
