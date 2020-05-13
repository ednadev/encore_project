package mask.exception;
// 사이즈를 대/중/소 외에 나머지 입력한 경우
public class InvalidSizeException extends Exception {
	public InvalidSizeException(String message) {
		super(message);
	}
	public InvalidSizeException() {
		this("=======This is InvalidSizeException~!!! =======");
	}
}
