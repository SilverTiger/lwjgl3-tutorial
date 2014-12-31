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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

/**
 * The game class just initializes the game and starts the game loop. After
 * ending the loop it will get disposed.
 *
 * @author Heiko Brumme
 */
public abstract class Game {

    protected static final int TARGET_FPS = 75;
    protected static final int TARGET_UPS = 30;

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
     * Used for rendering.
     */
    protected Renderer renderer;

    /**
     * Default contructor for the game.
     */
    public Game() {
        timer = new Timer();
        renderer = new Renderer();
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
        /* Dipose renderer */
        renderer.dispose();
        
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
        window = new Window(640, 480, "Game", true);

        /* Initialize timer */
        timer.init();

        /* Initializing done, set running to true */
        running = true;
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
        // TODO input
    }

    /**
     * Updates the game.
     */
    public void update() {
        // TODO fixed timestep update
    }

    /**
     * Updates the game.
     *
     * @param delta Time difference in seconds
     */
    public void update(float delta) {
        // TODO variable timestep update
    }

    /**
     * Renders the game.
     */
    public void render() {
        // TODO render without interpolation
    }

    /**
     * Renders the game.
     *
     * @param alpha Alpha value, needed for interpolation
     */
    public void render(float alpha) {
        // TODO render with interpolation
    }

    /**
     * Synchronizes the game at specified frames per second.
     *
     * @param fps Frames per second
     */
    public void sync(int fps) {
        double lastLoopTime = timer.getLastLoopTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            /* This is optional if you want your game to stop consuming too much
             CPU but you will loose some accuracy because Thread.sleep(1) could
             sleep longer than 1 millisecond */
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }

            now = timer.getTime();
        }
    }
}
