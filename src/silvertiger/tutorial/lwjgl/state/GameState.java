/*
 * The MIT License (MIT)
 *
 * Copyright © 2015-2016, Heiko Brumme
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

import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import silvertiger.tutorial.lwjgl.game.Ball;
import silvertiger.tutorial.lwjgl.game.Paddle;
import silvertiger.tutorial.lwjgl.graphic.Color;
import silvertiger.tutorial.lwjgl.graphic.Renderer;
import silvertiger.tutorial.lwjgl.graphic.Texture;

import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * This class contains a simple game.
 *
 * @author Heiko Brumme
 */
public class GameState implements State {

    public static final int NO_COLLISION = 0;
    public static final int COLLISION_TOP = 1;
    public static final int COLLISION_BOTTOM = 2;
    public static final int COLLISION_RIGHT = 3;
    public static final int COLLISION_LEFT = 4;

    private Texture texture;
    private final Renderer renderer;

    private Paddle player;
    private Paddle opponent;
    private Ball ball;

    private int playerScore;
    private int opponentScore;
    private int gameWidth;
    private int gameHeight;

    public GameState(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void input() {
        player.input();
        opponent.input(ball);
    }

    @Override
    public void update(float delta) {
        /* Update position */
        player.update(delta);
        opponent.update(delta);
        ball.update(delta);

        /* Check for collisions */
        player.checkBorderCollision(gameHeight);
        ball.collidesWith(player);
        opponent.checkBorderCollision(gameHeight);
        ball.collidesWith(opponent);

        /* Update score if necessary */
        switch (ball.checkBorderCollision(gameWidth, gameHeight)) {
            case COLLISION_LEFT:
                opponentScore++;
                break;
            case COLLISION_RIGHT:
                playerScore++;
                break;
        }
    }

    @Override
    public void render(float alpha) {
        /* Clear drawing area */
        renderer.clear();

        /* Draw game objects */
        texture.bind();
        renderer.begin();
        player.render(renderer, alpha);
        opponent.render(renderer, alpha);
        ball.render(renderer, alpha);
        renderer.end();

        /* Draw score */
        String scoreText = "Score";
        int scoreTextWidth = renderer.getTextWidth(scoreText);
        int scoreTextHeight = renderer.getTextHeight(scoreText);
        float scoreTextX = (gameWidth - scoreTextWidth) / 2f;
        float scoreTextY = gameHeight - scoreTextHeight - 5;
        renderer.drawText(scoreText, scoreTextX, scoreTextY, Color.BLACK);

        String playerText = "Player | " + playerScore;
        int playerTextWidth = renderer.getTextWidth(playerText);
        int playerTextHeight = renderer.getTextHeight(playerText);
        float playerTextX = gameWidth / 2f - playerTextWidth - 50;
        float playerTextY = scoreTextY - playerTextHeight;
        renderer.drawText(playerText, playerTextX, playerTextY, Color.BLACK);

        String opponentText = opponentScore + " | Opponent";
        int opponentTextWidth = renderer.getDebugTextWidth(playerText);
        int opponentTextHeight = renderer.getTextHeight(playerText);
        float opponentTextX = gameWidth / 2f + 50;
        float opponentTextY = scoreTextY - opponentTextHeight;
        renderer.drawText(opponentText, opponentTextX, opponentTextY, Color.BLACK);
    }

    @Override
    public void enter() {
        /* Get width and height of framebuffer */
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long window = GLFW.glfwGetCurrentContext();
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
            width = widthBuffer.get();
            height = heightBuffer.get();
        }

        /* Load texture */
        texture = Texture.loadTexture("resources/pong.png");

        /* Initialize game objects */
        float speed = 250f;
        player = new Paddle(Color.GREEN, texture, 5f, (height - 100) / 2f, speed, true);
        opponent = new Paddle(Color.RED, texture, width - 25f, (height - 100) / 2f, speed, false);
        ball = new Ball(Color.BLUE, texture, (width - 20) / 2f, (height - 20) / 2f, speed * 1.5f);

        /* Initialize variables */
        playerScore = 0;
        opponentScore = 0;
        gameWidth = width;
        gameHeight = height;

        /* Set clear color to gray */
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
    }

    @Override
    public void exit() {
        texture.delete();
    }

}
