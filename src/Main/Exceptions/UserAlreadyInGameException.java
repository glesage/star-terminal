package Main.Exceptions;

/**
 * Created by jeff on 5/11/15.
 */
public class UserAlreadyInGameException extends Exception {

        public UserAlreadyInGameException()
        {
        }

        public UserAlreadyInGameException(String message)
        {
            super(message);
        }

        public UserAlreadyInGameException(Throwable cause)
        {
            super(cause);
        }

        public UserAlreadyInGameException(String message, Throwable cause)
        {
            super(message, cause);
        }

        public UserAlreadyInGameException(String message, Throwable cause,
                               boolean enableSuppression, boolean writableStackTrace)
        {
            super(message, cause, enableSuppression, writableStackTrace);
        }
}
