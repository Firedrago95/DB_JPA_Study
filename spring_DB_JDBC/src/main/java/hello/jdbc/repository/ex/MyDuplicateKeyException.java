package hello.jdbc.repository.ex;

import hello.jdbc.repository.ex.MyDbException;

public class MyDuplicateKeyException extends MyDbException {

    public MyDuplicateKeyException() {
        super();
    }

    public MyDuplicateKeyException(String message) {
        super(message);
    }

    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }

    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
