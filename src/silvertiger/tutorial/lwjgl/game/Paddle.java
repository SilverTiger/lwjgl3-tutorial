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
package silvertiger.tutorial.lwjgl.game;

import java.awt.Color;
import org.lwjgl.glfw.GLFW;
import silvertiger.tutorial.lwjgl.graphic.Renderer;
import silvertiger.tutorial.lwjgl.graphic.Texture;
import silvertiger.tutorial.lwjgl.math.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static silvertiger.tutorial.lwjgl.state.GameState.*;

/**
 * This class represents a paddle for pong.
 *
 * @author Heiko Brumme
 */
public class Paddle {

    private Vector2f previousPosition;
    private Vector2f position;

    private final float speed;
    private Vector2f direction;

    private final Color color;
    private final Texture texture;

    private final boolean player;

    private final int width = 20;
    private final int height = 100;

    public Paddle(Color color, Texture texture, float x, float y, float speed, boolean player) {
        previousPosition = new Vector2f(x, y);
        position = new Vector2f(x, y);

        this.speed = speed;
        direction = new Vector2f();

        this.color = color;
        this.texture = texture;

        this.player = player;
    }

    /**
     * Handles input of the paddle.
     *
     * @param ball Only needed for the AI
     */
    public void input(Ball ball) {
        direction = new Vector2f();
        if (player) {
            /* Player input */
            long window = GLFW.glfwGetCurrentContext();
            if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
                direction.y = 1f;
            }
            if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
                direction.y = -1f;
            }
        } else {
            /* AI */
            float ballCenter = (ball.getY() + ball.getHeight()) / 2f;
            float paddleCenter = (position.y + this.height) / 2f;

            if (ballCenter > paddleCenter) {
                direction.y = 1f;
            }
            if (ballCenter < paddleCenter) {
                direction.y = -1f;
            }
        }
    }

    /**
     * Updates the paddle.
     *
     * @param delta Time difference in seconds
     */
    public void update(float delta) {
        previousPosition = new Vector2f(position.x, position.y);
        if (direction.x != 0 || direction.y != 0) {
            direction = direction.normalize();
        }
        Vector2f velocity = direction.scale(speed * delta);
        position = position.add(velocity);
    }

    /**
     * Checks if the paddle collided with the game border.
     *
     * @param gameHeight Height of the game field
     * @return Direction of the collision
     */
    public int checkCollision(int gameHeight) {
        if (position.y < 0) {
            position.y = 0;
            return COLLISION_BOTTOM;
        }
        if (position.y > gameHeight - this.height) {
            position.y = gameHeight - this.height;
            return COLLISION_TOP;
        }
        return NO_COLLISION;
    }

    /**
     * Renders the paddle.
     *
     * @param renderer Renderer for batching
     * @param alpha Alpha value, needed for interpolation
     */
    public void render(Renderer renderer, float alpha) {
        Vector2f interpolatedPosition = previousPosition.lerp(position, alpha);
        float x = interpolatedPosition.x;
        float y = interpolatedPosition.y;
        renderer.drawTextureRegion(texture, x, y, 0, 0, width, height, color);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
