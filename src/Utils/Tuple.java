package Utils;

/**
 * 存储（词，词性）二元组
 * @param <T>
 * @param <U>
 */
public class Tuple<T, U> {

    private final T word;
    private final U nature;

    public Tuple(T word, U nature) {
        this.word = word;
        this.nature = nature;
    }

    public T getWord() {
        return word;
    }

    public U getNature() {
        return nature;
    }
}
