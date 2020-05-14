package config;
/*
 *  디비서버 정보의 상수값으로 구성된 인터페이스. 
 *  1. Driver FQCN
 *  2. DBServer URL
 *  3. DBServer USER
 *  4. DBServer PASS
 */
public interface ServerInfo {
	String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";	// 인터페이스는 필드가 없기 때문에 자동적으로 상수처리됨. 
	// String 앞에 public static final이 있다고 생각하면됨
	String URL = "jdbc:mysql://127.0.0.1:3306/scott?serverTimezone=UTC&useUnicode=yes&characterEncoding=UTF-8";
	String USER = "root";
	String PASS = "1234";
	
}
