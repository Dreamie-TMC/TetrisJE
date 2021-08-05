package game.shapes;

import enums.Shape;

public interface IShapeFactory {
    IShape construct(Shape identifier, int level);

    IShape constructNext(Shape identifier, int level);
}
