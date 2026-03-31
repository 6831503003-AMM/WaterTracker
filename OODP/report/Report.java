package OODP.report;

/**
 * Abstraction for any printable report.
 * Satisfies the Inheritance / Interface requirement (2.2).
 *
 * Every concrete report must provide a title and a {@code display()} method
 * that writes its content to standard output.
 */
public interface Report {

    /** Short human-readable title shown in the header. */
    String getTitle();

    /** Print the full report to standard output. */
    void display();
}
