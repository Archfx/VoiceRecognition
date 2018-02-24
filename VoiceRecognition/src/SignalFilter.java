
/**
 * General interface for a filter that processes a stream of signal sample values.
 */
public interface SignalFilter {

    /**
     * Processes an input signal value and returns the next output signal value.
     */
    public double step (double inputValue);

}