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

/**
 *
 * @author Heiko Brumme
 */
public class FixedTimestepGame extends Game {

    @Override
    public void gameLoop() {
        float delta;
        float accumulator = 0f;
        float interval = 1f / 30f;
        float alpha;

        while (running) {
            if (window.isClosing()) {
                running = false;
            }

            delta = timer.getDelta();
            accumulator += delta;

            input();

            while (accumulator >= interval) {
                update(interval);
                timer.updateUPS();
                accumulator -= interval;
            }

            alpha = accumulator / interval;
            render(alpha);
            timer.updateFPS();

            timer.update();
            window.setTitle("Game | FPS: " + timer.getFPS()
                    + ", UPS: " + timer.getUPS());

            window.update();
        }
    }
}
