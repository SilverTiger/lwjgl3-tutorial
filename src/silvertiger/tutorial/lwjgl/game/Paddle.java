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

import org.lwjgl.glfw.GLFW;
import silvertiger.tutorial.lwjgl.graphic.Color;
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
public class Paddle extends Entity {

    private final boolean player;

    public Paddle(Color color, Texture texture, float x, float y, float speed, boolean player) {
        super(color, texture, x, y, speed, 20, 100, 0, 0);

        this.player = player;
    }

    /**
     * Handles input of the paddle.
     *
     * @param entity Only needed for the AI
     */
    @Override
    public void input(Entity entity) {
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
            float ballCenter = entity.getY() + entity.getHeight() / 2f;
            float paddleCenter = position.y + this.height / 2f;

            if (ballCenter > paddleCenter) {
                direction.y = 1f;
            }
            if (ballCenter < paddleCenter) {
                direction.y = -1f;
            }
        }
    }

    /**
     * Checks if the paddle collided with the game border.
     *
     * @param gameHeight Height of the game field
     *
     * @return Direction constant of the collision
     */
    public int checkBorderCollision(int gameHeight) {
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

}
