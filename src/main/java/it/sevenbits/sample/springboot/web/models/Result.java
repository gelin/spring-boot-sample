package it.sevenbits.sample.springboot.web.models;

/**
 * Genetir representation of the result.
 * @param <T> type of the result
 */
public class Result<T> {

    private final T result;

    public Result(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

}
