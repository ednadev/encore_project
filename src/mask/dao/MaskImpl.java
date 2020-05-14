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
import mask.exception.PasswordMissmatchException;
import mask.vo.Cart;
import mask.vo.Product;
import mask.vo.Consumer;

public class MaskImpl implements MaskTemplate{
	
	//공통된 로직
	public MaskImpl(String serverIp) throws ClassNotFoundException {
		Class.forName(ServerInfo.DRIVER_NAME);
		System.out.println("드라이브 로딩 성공____ in MaskImpl");
	}
	
	public MaskImpl() {
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
	@Override
	public Properties getProperties() throws Exception{			
		Properties ppt=new Properties();
		ppt.load(new FileInputStream("src/config/jdbc.properties"));
		return ppt;
	}

	public boolean isConsumerExist(int id,Connection conn) throws Exception {
		Properties ppt=null;
		getProperties();
		String sql_isConsumerExist=ppt.getProperty("jdbc.sql.isConsumerExist");
		PreparedStatement ps=conn.prepareStatement(sql_isConsumerExist);
		ps.setInt(1,id);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}
	@Override
	public boolean isProductExist(int productNum,Connection conn) throws Exception {
		Properties ppt=null;
		getProperties();
		String sql_isProductExist=ppt.getProperty("jdbc.sql.isProductExist");
		PreparedStatement ps=conn.prepareStatement(sql_isProductExist);
		ps.setInt(1,productNum);
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
			//INSERT INTO consumer(id,name,address,pass) VALUES(?,?,?,?)
			ps.setInt(1,consumer.getId());
			ps.setString(2,consumer.getName());
			ps.setString(3,consumer.getAddress());
			ps.setString(4,consumer.getPass());
			System.out.println(consumer.getName()+ps.executeUpdate()+"분 회원등록 되었습니다.");
			}catch(SQLIntegrityConstraintViolationException e) {//회원 는 중복이 불가능하다는 java자체의 오류로 뜹니다.>>SQLIntegrityConstraintViolationException 
				System.out.println("기존에 사용중인 회원id입니다. "+e.getMessage());
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

	@Override
	public ArrayList<Cart> getCart(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMask(int id,Cart cart) {

	}

	/**addMask,payment,updateMask
	 * @author 정용
	 */
	@Override
	public void addMask(int id,Cart cart) {

		
	}

	@Override
	public boolean delivery(int orderNum) {
		return false;
		
	}
	
	public void payment(int shipStatus) {

	}

	@Override
	public void updateMask(int id, Product product) {

	}

	/**addProductMask,getProduct,updateProductMask
	 * @author 채은
	 * @throws DuplicateIdException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws FileNotFoundException 
	 */
	@Override
	public void addProductMask(Product product) throws Exception {
		Product prod=new Product();
		Connection conn=null;
		PreparedStatement ps=null;
		Properties ppt=getProperties();
		
		// 1) 기존 상품이 있는지 확인 --- 없으면 INSERT
		try {
		conn=getConnect();
		if(!isProductExist(product.getProductNum(),conn)) {
		//if(product.getProductNum()!=0) {
			
			//ppt.load(new FileInputStream("src/config/jdbc.properties"));
			String sql=ppt.getProperty("jdbc.sql.addProductMask_insert");
			ps=conn.prepareStatement(sql);
			
			ps.setString(1,product.getProductName());
			ps.setInt(2,product.getQuantity());
			ps.setInt(3,product.getSize());		
			System.out.println("===== "+ps.executeUpdate()+"개의 상품 입고 완료 ===== ");
		// 2) ---- 기존에 있는 상품이면 Update
		}else {
			int q=prod.getQuantity();
			int addQ=product.getQuantity();
			int newQuantity=addQ;
			String sql=ppt.getProperty("jdbc.sql.addProductMask_update");
			ps=conn.prepareStatement(sql);
			ps.setInt(4,product.getProductNum());
			ps.setInt(2,newQuantity);
			System.out.println(ps.executeUpdate()+"개 상품 추가 입고 완료 ===== ");
		}
		}finally {
			closeAll(ps,conn);
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

	@Override
	public void updateProductMask(Product product) throws Exception {//
		ArrayList<Product> products=null;
		Connection conn=null;
		PreparedStatement ps=null;
		Properties ppt=null;
		try {
			conn=getConnect();
			getProperties();
			String sql=ppt.getProperty("jdbc.sql.updateProductMask");
			ps=conn.prepareStatement(sql);
			ps.setInt(2,product.getProductNum());
			ps.setInt(1,product.getSize());
			System.out.println(ps.executeUpdate()+"개 상품 추가 입고 완료 ===== ");
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
		Properties ppt=null;
		getProperties();
		String message;
		try {
		conn=getConnect();
		String sql=ppt.getProperty("jdbc.sql.getQuatityOverSize");
		ps=conn.prepareStatement(sql);
		//int row=ps.executeUpdate();
		
		while(rs.next()) {
			//message="마스크 사이즈 별\n"+  :"+row+"\n중 : ";"
			}
		}finally {closeAll(rs,ps, conn);}
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
