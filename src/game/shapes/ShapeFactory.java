package game.shapes;

import enums.Shape;
import game.shapes.implementations.*;

public class ShapeFactory implements IShapeFactory {
    @Override
    public IShape construct(Shape identifier, int level) {
        return construct(identifier, level, false);
    }

    @Override
    public IShape constructNext(Shape identifier, int level) {
        return construct(identifier, level, true);
    }

    private static IShape construct(Shape identifier, int level, boolean next) {
        switch (identifier) {
            case I:
                return new I(next, level);
            case J:
                return new J(next, level);
            case L:
                return new L(next, level);
            case O:
                return new O(next, level);
            case S:
                return new S(next, level);
            case T:
                return new T(next, level);
            case Z:
                return new Z(next, level);
            case DUMMY:
                break;
        }
        return null;
    }
}
