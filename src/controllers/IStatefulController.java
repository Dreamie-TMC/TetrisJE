package controllers;

public interface IStatefulController<T extends Enum<T>> extends IController {
    void updateState(T state);
}
