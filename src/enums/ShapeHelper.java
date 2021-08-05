package enums;

public class ShapeHelper {
    private ShapeHelper() {}

    public static Shape parseShapeFromString(String id) {
        switch (id) {
            case "I":
                return Shape.I;
            case "J":
                return Shape.J;
            case "L":
                return Shape.L;
            case "O":
                return Shape.O;
            case "S":
                return Shape.S;
            case "T":
                return Shape.T;
            case "Z":
                return Shape.Z;
            default:
                return Shape.DUMMY;
        }
    }

    public static Shape parseShapeFromIntIdentifier(int id) {
        switch (id) {
            case 0:
                return Shape.I;
            case 1:
                return Shape.J;
            case 2:
                return Shape.L;
            case 3:
                return Shape.O;
            case 4:
                return Shape.S;
            case 5:
                return Shape.T;
            case 6:
                return Shape.Z;
            default:
                return Shape.DUMMY;
        }
    }
}
