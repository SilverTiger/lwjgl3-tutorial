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
import silvertiger.tutorial.lwjgl.graphic.Renderer;
import silvertiger.tutorial.lwjgl.graphic.Texture;
import silvertiger.tutorial.lwjgl.math.Vector2f;

/**
 * This class represents an game entity.
 *
 * @author Heiko Brumme
 */
public abstract class Entity {

    protected Vector2f previousPosition;
    protected Vector2f position;

    protected final AABB aabb;

    protected final float speed;
    protected Vector2f direction;

    protected final Color color;
    protected final Texture texture;

    protected final int width;
    protected final int height;

    protected final int tx;
    protected final int ty;

    public Entity(Color color, Texture texture, float x, float y, float speed, int width, int height, int tx, int ty) {
        previousPosition = new Vector2f(x, y);
        position = new Vector2f(x, y);

        aabb = new AABB(this);

        this.speed = speed;
        direction = new Vector2f();

        this.color = color;
        this.texture = texture;

        this.width = width;
        this.height = height;

        this.tx = tx;
        this.ty = ty;
    }

    /**
     * Handles input of the entity.
     */
    public void input() {
        input(null);
    }

    /**
     * Handles input of the entity.
     *
     * @param entity Can be used for the AI
     */
    public abstract void input(Entity entity);

    /**
     * Updates the entity.
     *
     * @param delta Time difference in seconds
     */
    public void update(float delta) {
        previousPosition = new Vector2f(position.x, position.y);
        if (direction.length() != 0) {
            direction = direction.normalize();
        }
        Vector2f velocity = direction.scale(speed);
        position = position.add(velocity.scale(delta));

        aabb.min.x = position.x;
        aabb.min.y = position.y;
        aabb.max.x = position.x + width;
        aabb.max.y = position.y + height;
    }

    /**
     * Renders the entity.
     *
     * @param renderer Renderer for batching
     * @param alpha    Alpha value, needed for interpolation
     */
    public void render(Renderer renderer, float alpha) {
        Vector2f interpolatedPosition = previousPosition.lerp(position, alpha);
        float x = interpolatedPosition.x;
        float y = interpolatedPosition.y;
        renderer.drawTextureRegion(texture, x, y, tx, ty, width, height, color);
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

    public AABB getAABB() {
        return aabb;
    }

}
