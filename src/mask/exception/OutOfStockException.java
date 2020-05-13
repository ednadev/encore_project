package mask.exception;
// 재고가 없는 경우
public class OutOfStockException extends Exception {
	public OutOfStockException(String message) {
		super(message);
	}
	public OutOfStockException() {
		this("=======This is NoStockException~!!! =======");
	}

}
