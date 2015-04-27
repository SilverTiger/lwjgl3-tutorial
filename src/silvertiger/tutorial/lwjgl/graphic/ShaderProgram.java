/*
 * The MIT License (MIT)
 *
 * Copyright © 2014, Heiko Brumme
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
package silvertiger.tutorial.lwjgl.graphic;

import silvertiger.tutorial.lwjgl.math.*;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

/**
 * This class represents a shader program.
 *
 * @author Heiko Brumme
 */
public class ShaderProgram {

    /**
     * Stores the handle of the program.
     */
    private final int id;

    /**
     * Creates a shader program.
     */
    public ShaderProgram() {
        id = glCreateProgram();
    }

    /**
     * Attach a shader to this program.
     *
     * @param shader Shader to get attached
     */
    public void attachShader(Shader shader) {
        glAttachShader(id, shader.getID());
    }

    /**
     * Binds the fragment out color variable.
     *
     * @param number Color number you want to bind
     * @param name Variable name
     */
    public void bindFragmentDataLocation(int number, CharSequence name) {
        glBindFragDataLocation(id, number, name);
    }

    /**
     * Link this program.
     */
    public void link() {
        glLinkProgram(id);

        checkStatus();
    }

    /**
     * Gets the location of an attribute variable with specified name.
     *
     * @param name Attribute name
     * @return Location of the attribute
     */
    public int getAttributeLocation(CharSequence name) {
        return glGetAttribLocation(id, name);
    }

    /**
     * Enables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    public void enableVertexAttribute(int location) {
        glEnableVertexAttribArray(location);
    }

    /**
     * Disables a vertex attribute.
     *
     * @param location Location of the vertex attribute
     */
    public void disableVertexAttribute(int location) {
        glDisableVertexAttribArray(location);
    }

    /**
     * Sets the vertex attribute pointer.
     *
     * @param location Location of the vertex attribute
     * @param size Number of values per vertex
     * @param stride Offset between consecutive generic vertex attributes in
     * bytes
     * @param offset Offset of the first component of the first generic vertex
     * attribute in bytes
     */
    public void pointVertexAttribute(int location, int size, int stride, int offset) {
        glVertexAttribPointer(location, size, GL_FLOAT, false, stride, offset);
    }

    /**
     * Gets the location of an uniform variable with specified name.
     *
     * @param name Uniform name
     * @return Location of the uniform
     */
    public int getUniformLocation(CharSequence name) {
        return glGetUniformLocation(id, name);
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, int value) {
        glUniform1i(location, value);
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Vector2f value) {
        glUniform2fv(location, value.getBuffer());
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Vector3f value) {
        glUniform3fv(location, value.getBuffer());
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Vector4f value) {
        glUniform4fv(location, value.getBuffer());
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Matrix2f value) {
        glUniformMatrix2fv(location, false, value.getBuffer());
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Matrix3f value) {
        glUniformMatrix3fv(location, false, value.getBuffer());
    }

    /**
     * Sets the uniform variable for specified location.
     *
     * @param location Uniform location
     * @param value Value to set
     */
    public void setUniform(int location, Matrix4f value) {
        glUniformMatrix4fv(location, false, value.getBuffer());
    }

    /**
     * Use this shader program.
     */
    public void use() {
        glUseProgram(id);
    }

    /**
     * Checks if the program was linked successfully.
     */
    public void checkStatus() {
        int status = glGetProgrami(id, GL_LINK_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(id));
        }
    }

    /**
     * Deletes the shader program.
     */
    public void delete() {
        glDeleteProgram(id);
    }
}
