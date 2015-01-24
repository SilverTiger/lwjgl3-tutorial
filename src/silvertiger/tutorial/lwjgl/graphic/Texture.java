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
package silvertiger.tutorial.lwjgl.graphic;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;

/**
 * This class represents a texture.
 *
 * @author Heiko Brumme
 */
public class Texture {

    /**
     * Stores the handle of the texture.
     */
    private final int id;

    /**
     * Width of the texture.
     */
    private final int width;
    /**
     * Height of the texture.
     */
    private final int height;

    /**
     * Creates a texture with specified width, height and data.
     *
     * @param width Width of the texture
     * @param height Height of the texture
     * @param data Picture Data in RGBA format
     */
    public Texture(int width, int height, ByteBuffer data) {
        id = glGenTextures();
        this.width = width;
        this.height = height;

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
    }

    /**
     * Binds the texture.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Delete the texture.
     */
    public void delete() {
        glDeleteTextures(id);
    }

    /**
     * Gets the texture width.
     *
     * @return Texture width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the texture height.
     *
     * @return Texture height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Load texture from file.
     *
     * @param path File path of the texture
     * @return Texture from specified file
     */
    public static Texture loadTexture(String path) {
        BufferedImage image = null;
        try {
            InputStream in = new FileInputStream(path);
            image = ImageIO.read(in);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load a texture file!"
                    + System.lineSeparator() + ex.getMessage());
        }
        if (image != null) {
            /* Flip image Horizontal to get the origin to bottom left */
            AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
            transform.translate(0, -image.getHeight());
            AffineTransformOp operation = new AffineTransformOp(transform,
                    AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = operation.filter(image, null);

            /* Get width and height of image */
            int width = image.getWidth();
            int height = image.getHeight();

            /* Get pixel data of image */
            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            /* Put pixel data into a ByteBuffer */
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    /* Pixel as RGBA: 0xAARRGGBB */
                    int pixel = pixels[y * width + x];
                    /* Red component 0xAARRGGBB >> 16 = 0x0000AARR */
                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    /* Green component 0xAARRGGBB >> 8 = 0x00AARRGG */
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    /* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */
                    buffer.put((byte) (pixel & 0xFF));
                    /* Alpha component 0xAARRGGBB >> 24 = 0x000000AA */
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            /* Do not forget to flip the buffer! */
            buffer.flip();

            return new Texture(width, height, buffer);
        } else {
            throw new RuntimeException("File extension not supported!"
                    + System.lineSeparator() + "The following file extensions "
                    + "are supported: "
                    + Arrays.toString(ImageIO.getReaderFileSuffixes()));
        }
    }
}
