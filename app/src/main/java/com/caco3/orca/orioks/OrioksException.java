package com.caco3.orca.orioks;

import com.caco3.orca.util.Preconditions;

/**
 * Thrown by {@link Orioks} when <code>!({@link OrioksResponseJson#error} instanceof Boolean)</code>
 * If it's not boolean it's String with detailed error message.
 */
public class OrioksException extends Exception {
    /**
     * Error message returned by {@link Orioks}
     */
    private final String orioksErrorMessage;

    /**
     * Constructs new {@link OrioksException} with provided orioks detailed message
     * (which is in the {@link OrioksResponseJson#error})
     * @param message detailed message
     * @param orioksErrorMessage message returned by {@link Orioks},
     *                           it's saved for later retrieval via {@link #getOrioksErrorMessage()}
     * @throws NullPointerException <code>if (orioksErrorMessage == null)</code>
     */
    public OrioksException(String message, String orioksErrorMessage) {
        super(message);
        this.orioksErrorMessage = Preconditions.checkNotNull(orioksErrorMessage,
                "orioksErrorMessage == null");
    }

    /**
     * Returns original orioks error message
     * @return string
     */
    public String getOrioksErrorMessage(){
        return orioksErrorMessage;
    }
}
