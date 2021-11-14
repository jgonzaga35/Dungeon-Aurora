package dungeonmania.exceptions;

/**
 * An invalid action exception. 
 * Thrown when the player attempts an invalid action.
 */
public class InvalidActionException extends RuntimeException {
    public InvalidActionException(String message) {
        super(message);
    }
}
