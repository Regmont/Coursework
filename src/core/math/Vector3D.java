package core.math;

/**
 * Неизменяемый радиус-вектор в трехмерном пространстве.
 *
 * @param x координата по оси X
 * @param y координата по оси Y
 * @param z координата по оси Z
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public record Vector3D(double x, double y, double z) {
    /**
     * Нулевой вектор.
     */
    public static final Vector3D zeroVector = new Vector3D(0, 0, 0);

    /**
     * Вектор единиц по всем осям координат.
     */
    public static final Vector3D oneVector = new Vector3D(1, 1, 1);

    /**
     * Вычисляет длину (модуль) вектора.
     *
     * @return длина вектора
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Возвращает нормализованную копию вектора.
     * Если вектор нулевой, возвращает его же.
     *
     * @return единичный вектор того же направления
     */
    public Vector3D normalize() {
        double length = length();

        if (length == 0) {
            return zeroVector;
        }

        return new Vector3D(x / length, y / length, z / length);
    }

    /**
     * Вычисляет скалярное произведение этого вектора с другим.
     *
     * @param other другой вектор (не может быть {@code null})
     * @return скалярное произведение векторов
     * @throws NullPointerException если {@code other} равен {@code null}
     */
    public double dot(Vector3D other) {
        if (other == null) {
            throw new NullPointerException("given vector cannot be null");
        }

        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Вычитает вектор из данного.
     *
     * @param other другой вектор (не может быть {@code null})
     * @return вектор разности данного вектора с другим
     * @throws NullPointerException если {@code other} равен {@code null}
     */
    public Vector3D subtract(Vector3D other) {
        if (other == null) {
            throw new NullPointerException("given vector cannot be null");
        }

        return new Vector3D(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Складывает вектор с данным.
     *
     * @param other другой вектор (не может быть {@code null})
     * @return вектор суммы данного вектора с другим
     * @throws NullPointerException если {@code other} равен {@code null}
     */
    public Vector3D add(Vector3D other) {
        if (other == null) {
            throw new NullPointerException("given vector cannot be null");
        }

        return new Vector3D(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Вычисляет векторное произведение этого вектора с другим.
     *
     * @param other другой вектор (не может быть {@code null})
     * @return векторное произведение векторов
     * @throws NullPointerException если {@code other} равен {@code null}
     */
    public Vector3D cross(Vector3D other) {
        if (other == null) {
            throw new NullPointerException("given vector cannot be null");
        }

        return new Vector3D(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3D vector3D)) return false;

        return Double.compare(x, vector3D.x) == 0 &&
                Double.compare(y, vector3D.y) == 0 &&
                Double.compare(z, vector3D.z) == 0;
    }

    @Override
    public String toString() {
        return String.format("Vector3D(%.2f, %.2f, %.2f)", x, y, z);
    }
}
