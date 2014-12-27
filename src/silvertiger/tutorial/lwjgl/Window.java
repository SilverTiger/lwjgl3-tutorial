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
package silvertiger.tutorial.lwjgl;

import java.nio.ByteBuffer;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This class represents a GLFW window.
 *
 * @author Heiko Brumme
 */
public class Window {

    /**
     * Stores the window handle.
     */
    long id;

    /**
     * Key callback for the window.
     */
    private final GLFWKeyCallback keyCallback;

    /**
     * Creates a GLFW window with the specified width, height and title.
     *
     * @param width Width of the drawing area
     * @param height Height of the drawing area
     * @param title Title of the window
     */
    public Window(int width, int height, CharSequence title) {
        /* Reset window hints and create window */
        glfwDefaultWindowHints();
        id = glfwCreateWindow(width, height, title, NULL, NULL);

        /* Center window on screen */
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(id,
                (GLFWvidmode.width(vidmode) - width) / 2,
                (GLFWvidmode.height(vidmode) - height) / 2
        );

        /* Create OpenGL context */
        glfwMakeContextCurrent(id);
        GLContext.createFromCurrent();

        /* Enable v-sync */
        glfwSwapInterval(1);

        /* Set key callback */
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window, GL_TRUE);
                }
            }
        };
        glfwSetKeyCallback(id, keyCallback);
    }

    /**
     * Returns if the window is closing.
     *
     * @return true if the window should close, else false
     */
    public boolean isClosing() {
        return glfwWindowShouldClose(id) == GL_TRUE;
    }

    /**
     * Sets the window title
     *
     * @param title New window title
     */
    public void setTitle(CharSequence title) {
        glfwSetWindowTitle(id, title);
    }

    /**
     * Updates the screen.
     */
    public void update() {
        glfwSwapBuffers(id);
        glfwPollEvents();
    }

    /**
     * Destroys the window an releases its callbacks.
     */
    public void destroy() {
        glfwDestroyWindow(id);
        keyCallback.release();
    }
}
