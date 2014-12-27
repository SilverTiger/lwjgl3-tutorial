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

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The game class just initializes the game and starts the game loop. After
 * ending the loop it will get disposed.
 *
 * @author Heiko Brumme
 */
public abstract class Game {

    /**
     * The error callback for GLFW.
     */
    private GLFWErrorCallback errorCallback;

    /**
     * Shows if the game is running.
     */
    protected boolean running;
    /**
     * The GLFW window used by the game.
     */
    protected Window window;
    /**
     * Used for timing calculations.
     */
    protected Timer timer;

    /**
     * Default contructor for the game.
     */
    public Game() {
        timer = new Timer();
    }

    /**
     * This should be called to initialize and start the game.
     */
    public void start() {
        init();
        gameLoop();
        dispose();
    }

    /**
     * Releases resources that where used by the game.
     */
    public void dispose() {
        /* Release window and its callbacks */
        window.destroy();

        /* Terminate GLFW and release the error callback */
        glfwTerminate();
        errorCallback.release();
    }

    /**
     * Initializes the game.
     */
    public void init() {
        /* Set error callback */
        errorCallback = Callbacks.errorCallbackPrint();
        glfwSetErrorCallback(errorCallback);

        /* Initialize GLFW */
        glfwInit();

        /* Create GLFW window */
        window = new Window(640, 480, "Game");

        /* Set running to true and initalize timer */
        running = true;
        timer.init();
    }

    /**
     * The game loop. <br/>
     * For implementation take a look at <code>VariableDeltaGame</code> and
     * <code>FixedTimestepGame</code>.
     */
    public abstract void gameLoop();

    /**
     * Handles input.
     */
    public void input() {
        // TODO
    }

    /**
     * Updates the game.
     *
     * @param delta Time difference in seconds
     */
    public void update(float delta) {
        // TODO
    }

    /**
     * Renders the game.
     */
    public void render() {
        // TODO
    }

    /**
     * Renders the game.
     *
     * @param alpha Alpha value, needed for interpolation
     */
    public void render(float alpha) {
        // TODO
    }
}
