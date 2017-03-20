/*
 * The MIT License (MIT)
 *
 * Copyright © 2015-2017, Heiko Brumme
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
package silvertiger.tutorial.lwjgl.state;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import silvertiger.tutorial.lwjgl.graphic.Shader;
import silvertiger.tutorial.lwjgl.graphic.ShaderProgram;
import silvertiger.tutorial.lwjgl.graphic.VertexBufferObject;
import silvertiger.tutorial.lwjgl.math.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/**
 * This state is for the rendering tutorial.
 *
 * @author Heiko Brumme
 */
public class LegacyExampleState implements State {

    private final CharSequence vertexSource
                               = "#version 120\n"
                                 + "\n"
                                 + "attribute vec3 position;\n"
                                 + "attribute vec3 color;\n"
                                 + "\n"
                                 + "varying vec3 vertexColor;\n"
                                 + "\n"
                                 + "uniform mat4 model;\n"
                                 + "uniform mat4 view;\n"
                                 + "uniform mat4 projection;\n"
                                 + "\n"
                                 + "void main() {\n"
                                 + "    vertexColor = color;\n"
                                 + "    mat4 mvp = projection * view * model;\n"
                                 + "    gl_Position = mvp * vec4(position, 1.0);\n"
                                 + "}";
    private final CharSequence fragmentSource
                               = "#version 120\n"
                                 + "\n"
                                 + "varying vec3 vertexColor;\n"
                                 + "\n"
                                 + "void main() {\n"
                                 + "    gl_FragColor = vec4(vertexColor, 1.0);\n"
                                 + "}";

    private VertexBufferObject vbo;
    private Shader vertexShader;
    private Shader fragmentShader;
    private ShaderProgram program;

    private int uniModel;
    private float previousAngle = 0f;
    private float angle = 0f;
    private final float angelPerSecond = 50f;

    @Override
    public void input() {
        /* Nothing to do here */
    }

    @Override
    public void update(float delta) {
        previousAngle = angle;
        angle += delta * angelPerSecond;
    }

    @Override
    public void render(float alpha) {
        glClear(GL_COLOR_BUFFER_BIT);

        vbo.bind(GL_ARRAY_BUFFER);
        specifyVertexAttributes();
        program.use();

        float lerpAngle = (1f - alpha) * previousAngle + alpha * angle;
        Matrix4f model = Matrix4f.rotate(lerpAngle, 0f, 0f, 1f);
        program.setUniform(uniModel, model);

        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    @Override
    public void enter() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            /* Vertex data */
            FloatBuffer vertices = stack.mallocFloat(3 * 6);
            vertices.put(-0.6f).put(-0.4f).put(0f).put(1f).put(0f).put(0f);
            vertices.put(0.6f).put(-0.4f).put(0f).put(0f).put(1f).put(0f);
            vertices.put(0f).put(0.6f).put(0f).put(0f).put(0f).put(1f);
            vertices.flip();

            /* Generate Vertex Buffer Object */
            vbo = new VertexBufferObject();
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        }

        /* Load shaders */
        vertexShader = Shader.createShader(GL_VERTEX_SHADER, vertexSource);
        fragmentShader = Shader.createShader(GL_FRAGMENT_SHADER, fragmentSource);

        /* Create shader program */
        program = new ShaderProgram();
        program.attachShader(vertexShader);
        program.attachShader(fragmentShader);
        program.link();
        program.use();

        specifyVertexAttributes();

        /* Get uniform location for the model matrix */
        uniModel = program.getUniformLocation("model");

        /* Set view matrix to identity matrix */
        Matrix4f view = new Matrix4f();
        int uniView = program.getUniformLocation("view");
        program.setUniform(uniView, view);

        /* Get width and height for calculating the ratio */
        float ratio;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long window = GLFW.glfwGetCurrentContext();
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, width, height);
            ratio = width.get() / (float) height.get();
        }

        /* Set projection matrix to an orthographic projection */
        Matrix4f projection = Matrix4f.orthographic(-ratio, ratio, -1f, 1f, -1f, 1f);
        int uniProjection = program.getUniformLocation("projection");
        program.setUniform(uniProjection, projection);
    }

    @Override
    public void exit() {
        vbo.delete();
        vertexShader.delete();
        fragmentShader.delete();
        program.delete();
    }

    /**
     * Specifies the vertex attributes.
     */
    private void specifyVertexAttributes() {
        /* Specify Vertex Pointer */
        int posAttrib = program.getAttributeLocation("position");
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 3, 6 * Float.BYTES, 0);

        /* Specify Color Pointer */
        int colAttrib = program.getAttributeLocation("color");
        program.enableVertexAttribute(colAttrib);
        program.pointVertexAttribute(colAttrib, 3, 6 * Float.BYTES, 3 * Float.BYTES);
    }

}
