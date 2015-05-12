package Main.Exceptions;

/**
 * Created by jeff on 5/11/15.
 */
public class UserNotInGameException extends Exception {

        public UserNotInGameException()
        {
        }

        public UserNotInGameException(String message)
        {
            super(message);
        }

        public UserNotInGameException(Throwable cause)
        {
            super(cause);
        }

        public UserNotInGameException(String message, Throwable cause)
        {
            super(message, cause);
        }

        public UserNotInGameException(String message, Throwable cause,
                                      boolean enableSuppression, boolean writableStackTrace)
        {
            super(message, cause, enableSuppression, writableStackTrace);
        }
}
