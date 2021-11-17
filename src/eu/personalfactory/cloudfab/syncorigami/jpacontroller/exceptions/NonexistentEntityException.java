package eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions;

@SuppressWarnings("serial")
public class NonexistentEntityException extends Exception {
    public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public NonexistentEntityException(String message) {
        super(message);
    }
}
