package mask.exception;
// 로그인할 때 비밀번호 불일치
public class PasswordMissmatchException extends Exception {
	public PasswordMissmatchException(String message) {
		super(message);
	}
	public PasswordMissmatchException() {
		this("=======This is PasswordMissmatchException~!!! =======");
	}
}
