package mask.exception;
// 정보가 없는 경우 ex...해당 회원id가 없는 경우
public class RecordNotFoundException extends Exception {
	public RecordNotFoundException(String message) {
		super(message);
	}
	public RecordNotFoundException() {
		this("=======This is RecordNotFoundException~!!! =======");
	}
}
