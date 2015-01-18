/*
 * The MIT License (MIT)
 *
 * Copyright © 2015, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package silvertiger.tutorial.lwjgl.math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 * This class represents a 2x2-Matrix. GLSL equivalent to mat2.
 *
 * @author Heiko Brumme
 */
public class Matrix2f {

    private float m00, m01;
    private float m10, m11;

    /**
     * Create a 2x2 identity matrix.
     */
    public Matrix2f() {
        setIdentity();
    }

    /**
     * Creates a 2x2 matrix with specified columns.
     *
     * @param col1 Vector with values of the first column
     * @param col2 Vector with values of the second column
     */
    public Matrix2f(Vector2f col1, Vector2f col2) {
        m00 = col1.x;
        m10 = col1.y;

        m01 = col2.x;
        m11 = col2.y;
    }

    /**
     * Sets this matrix to the identity matrix.
     */
    public final void setIdentity() {
        m00 = 1f;
        m11 = 1f;

        m01 = 0f;
        m10 = 0f;
    }

    /**
     * Adds this matrix to another matrix.
     *
     * @param other The other matrix
     * @return Sum of this + other
     */
    public Matrix2f add(Matrix2f other) {
        Matrix2f result = new Matrix2f();

        result.m00 = this.m00 + other.m00;
        result.m10 = this.m10 + other.m10;

        result.m01 = this.m01 + other.m01;
        result.m11 = this.m11 + other.m11;

        return result;
    }

    /**
     * Negates this matrix.
     *
     * @return Negated matrix
     */
    public Matrix2f negate() {
        return multiply(-1f);
    }

    /**
     * Subtracts this matrix from another matrix.
     *
     * @param other The other matrix
     * @return Difference of this - other
     */
    public Matrix2f subtract(Matrix2f other) {
        return this.add(other.negate());
    }

    /**
     * Multiplies this matrix with a scalar.
     *
     * @param scalar The scalar
     * @return Scalar product of this * scalar
     */
    public Matrix2f multiply(float scalar) {
        Matrix2f result = new Matrix2f();

        result.m00 = this.m00 * scalar;
        result.m10 = this.m10 * scalar;

        result.m01 = this.m01 * scalar;
        result.m11 = this.m11 * scalar;

        return result;
    }

    /**
     * Multiplies this matrix to a vector.
     *
     * @param vector The vector
     * @return Vector product of this * other
     */
    public Vector2f multiply(Vector2f vector) {
        float x = this.m00 * vector.x + this.m01 * vector.y;
        float y = this.m10 * vector.x + this.m11 * vector.y;
        return new Vector2f(x, y);
    }

    /**
     * Multiplies this matrix to another matrix.
     *
     * @param other The other matrix
     * @return Matrix product of this * other
     */
    public Matrix2f multiply(Matrix2f other) {
        Matrix2f result = new Matrix2f();

        result.m00 = this.m00 * other.m00 + this.m01 * other.m10;
        result.m10 = this.m10 * other.m00 + this.m11 * other.m10;

        result.m01 = this.m00 * other.m01 + this.m01 * other.m11;
        result.m11 = this.m10 * other.m01 + this.m11 * other.m11;

        return result;
    }

    /**
     * Transposes this matrix.
     *
     * @return Transposed matrix
     */
    public Matrix2f transpose() {
        Matrix2f result = new Matrix2f();

        result.m00 = this.m00;
        result.m10 = this.m01;

        result.m01 = this.m10;
        result.m11 = this.m11;

        return result;
    }

    /**
     * Returns the Buffer representation of this vector.
     *
     * @return Vector as FloatBuffer
     */
    public FloatBuffer getBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(m00).put(m10);
        buffer.put(m01).put(m11);
        buffer.flip();
        return buffer;
    }
}
