package mask.exception;

public class DuplicateIdException extends Exception {
	public DuplicateIdException(String message) {
		super(message);
	}
	public DuplicateIdException() {
		this("=======This is DuplicateIdException~!!! =======");
	}
}
