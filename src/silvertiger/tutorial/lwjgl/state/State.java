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
package silvertiger.tutorial.lwjgl.state;

import silvertiger.tutorial.lwjgl.core.Game;

/**
 * States are used for the current game state.
 *
 * @author Heiko Brumme
 */
public interface State {

    /**
     * Handles input of the state.
     */
    public void input();

    /**
     * Updates the state (fixed timestep).
     */
    public default void update() {
        update(1f / Game.TARGET_UPS);
    }

    /**
     * Updates the state (variable timestep)
     *
     * @param delta Time difference in seconds
     */
    public void update(float delta);

    /**
     * Renders the state (no interpolation).
     */
    public default void render() {
        render(1f);
    }

    /**
     * Renders the state (with interpolation).
     *
     * @param alpha Alpha value, needed for interpolation
     */
    public void render(float alpha);

    /**
     * Gets executed when entering the state, useful for initialization.
     */
    public void enter();

    /**
     * Gets executed when leaving the state, useful for disposing.
     */
    public void exit();

}
