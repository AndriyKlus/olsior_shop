package olsior.shop.telegram.handler;

public interface Handler<T> {
    void choose(T t);
}
