package WT_OODP.exception;

/**
 * Thrown when a water usage amount is invalid (e.g. zero or negative).
 */
public class InvalidAmountException extends Exception {

    private final double attemptedValue;

    public InvalidAmountException(double attemptedValue) {
        super(String.format("Invalid amount: %.2f. Amount must be greater than 0.", attemptedValue));
        this.attemptedValue = attemptedValue;
    }

    public InvalidAmountException(String message) {
        super(message);
        this.attemptedValue = 0;
    }

    public double getAttemptedValue() {
        return attemptedValue;
    }
}
