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

import silvertiger.tutorial.lwjgl.graphic.Color;
import silvertiger.tutorial.lwjgl.graphic.Texture;
import silvertiger.tutorial.lwjgl.math.Vector2f;

import static silvertiger.tutorial.lwjgl.state.GameState.*;

/**
 * This class represents a ball for pong.
 *
 * @author Heiko Brumme
 */
public class Ball extends Entity {

    public Ball(Color color, Texture texture, float x, float y, float speed) {
        super(color, texture, x, y, speed, 20, 20, 20, 40);

        double rand = Math.random();
        if (rand < 0.25) {
            direction.x = (float) -Math.cos(Math.toRadians(45.0));
            direction.y = (float) -Math.sin(Math.toRadians(45.0));
        } else if (rand < 0.5) {
            direction.x = (float) -Math.cos(Math.toRadians(45.0));
            direction.y = (float) Math.sin(Math.toRadians(45.0));
        } else if (rand < 0.75) {
            direction.x = (float) Math.cos(Math.toRadians(45.0));
            direction.y = (float) -Math.sin(Math.toRadians(45.0));
        } else {
            direction.x = (float) Math.cos(Math.toRadians(45.0));
            direction.y = (float) Math.sin(Math.toRadians(45.0));
        }
    }

    /**
     * Handles input of the ball.
     *
     * @param entity Not used here
     */
    @Override
    public void input(Entity entity) {
        /* Nothing to do here */
    }

    /**
     * Checks if the ball collided with the game border.
     *
     * @param gameWidth  Width of the game field
     * @param gameHeight Height of the game field
     *
     * @return Direction constant of the collision
     */
    public int checkBorderCollision(int gameWidth, int gameHeight) {
        if (position.y < 0) {
            position.y = 0;
            direction.y = -direction.y;
            return COLLISION_BOTTOM;
        }
        if (position.y > gameHeight - this.height) {
            position.y = gameHeight - this.height;
            direction.y = -direction.y;
            return COLLISION_TOP;
        }
        if (position.x < 0) {
            reset((gameWidth - width) / 2f, (gameHeight - width) / 2f);
            return COLLISION_LEFT;
        }
        if (position.x > gameWidth - this.width) {
            reset((gameWidth - width) / 2f, (gameHeight - width) / 2f);
            return COLLISION_RIGHT;
        }
        return NO_COLLISION;
    }

    /**
     * Check if this ball collides with a paddle.
     *
     * @param paddle The paddle
     *
     * @return true if a collision occured, else false
     */
    public boolean collidesWith(Paddle paddle) {
        if (aabb.intersects(paddle.getAABB())) {

            if (position.x < paddle.getX()) {
                /* Collision with right paddle */
                position.x = paddle.getX() - width;
            }

            if (position.x > paddle.getX()) {
                /* Collision with left paddle */
                position.x = paddle.getX() + paddle.getWidth();
            }

            direction.x = -direction.x;

            return true;
        }
        return false;
    }

    /**
     * Resets the ball to the center.
     *
     * @param x X coordinate
     * @param y y coordinate
     */
    public void reset(float x, float y) {
        previousPosition = new Vector2f(x, y);
        position = new Vector2f(x, y);

        double rand = Math.random();
        if (rand < 0.25) {
            direction.x = (float) -Math.cos(Math.toRadians(45.0));
            direction.y = (float) -Math.sin(Math.toRadians(45.0));
        } else if (rand < 0.5) {
            direction.x = (float) -Math.cos(Math.toRadians(45.0));
            direction.y = (float) Math.sin(Math.toRadians(45.0));
        } else if (rand < 0.75) {
            direction.x = (float) Math.cos(Math.toRadians(45.0));
            direction.y = (float) -Math.sin(Math.toRadians(45.0));
        } else {
            direction.x = (float) Math.cos(Math.toRadians(45.0));
            direction.y = (float) Math.sin(Math.toRadians(45.0));
        }
    }

}
