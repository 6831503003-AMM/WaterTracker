package WT_OODP.exception;

/**
 * Thrown when a category index or name is not recognised.
 */
public class InvalidCategoryException extends Exception {

    private final String attemptedCategory;

    public InvalidCategoryException(String attemptedCategory) {
        super("Invalid category: \"" + attemptedCategory + "\". Please choose a valid category.");
        this.attemptedCategory = attemptedCategory;
    }

    public String getAttemptedCategory() {
        return attemptedCategory;
    }
}
