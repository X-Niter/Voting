package io.github.zellfrey.forgevotifier.api;


/**
 * Original author: UpcraftLP
 */
public class RewardException extends Exception {

    public RewardException() {
    }

    public RewardException(String message) {
        super(message);
    }

    public RewardException(String message, Throwable cause) {
        super(message, cause);
    }

    public RewardException(Throwable cause) {
        super(cause);
    }

    public RewardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
