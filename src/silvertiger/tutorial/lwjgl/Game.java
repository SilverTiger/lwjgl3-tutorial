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
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *
 * @author Heiko Brumme
 */
public abstract class Game {

    private GLFWKeyCallback keyCallback;
    private GLFWErrorCallback errorCallback;

    protected boolean running;
    protected long window;
    protected Timer timer;

    public Game() {
        timer = new Timer();
    }

    public void start() {
        init();
        gameLoop();
        end();
    }

    public void end() {
        glfwDestroyWindow(window);
        keyCallback.release();
        glfwTerminate();
        errorCallback.release();
    }

    public void init() {
        errorCallback = Callbacks.errorCallbackPrint();
        glfwSetErrorCallback(errorCallback);
        glfwInit();
        glfwDefaultWindowHints();
        window = glfwCreateWindow(640, 480, "Game", NULL, NULL);
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,
                (GLFWvidmode.width(vidmode) - 640) / 2,
                (GLFWvidmode.height(vidmode) - 480) / 2
        );
        glfwMakeContextCurrent(window);
        GLContext.createFromCurrent();
        glfwSwapInterval(1);
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    glfwSetWindowShouldClose(window, GL_TRUE);
                }
            }
        };
        glfwSetKeyCallback(window, keyCallback);

        running = true;
        timer.init();
    }

    public abstract void gameLoop();

    public void input() {
        // TODO
    }

    public void update(float delta) {
        // TODO
    }

    public void render() {
        // TODO
    }

    public void render(float alpha) {
        // TODO
    }
}
