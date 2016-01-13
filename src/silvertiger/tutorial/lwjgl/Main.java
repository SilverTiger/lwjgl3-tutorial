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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Configuration;
import silvertiger.tutorial.lwjgl.core.Game;
import silvertiger.tutorial.lwjgl.core.FixedTimestepGame;

/**
 * The main class creates a fixed timestep game and starts it.
 *
 * @author Heiko Brumme
 */
public class Main {

    /**
     * Main function.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Load native libraries */
        try {
            unpackNatives();
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to initialize LWJGL", ex);
        }

        /* Start game */
        Game game = new FixedTimestepGame();
        try {
            game.start();
        } finally {
            /* GLFW has to be terminated or else the application will run in
             * background */
            GLFW.glfwTerminate();
        }
    }

    /**
     * This method loads the natives libraries required by LWJGL. The specified
     * libraries will get extracted into the folder native.
     *
     * @throw IOException if an error occurs during extracting the libraries
     */
    private static void unpackNatives() throws IOException {
        Path libraryPath = Paths.get("native");

        /* Only unpack if native folder doesn't exist */
        if (!Files.exists(libraryPath)) {
            /* Get OS name and architecture */
            String os = System.getProperty("os.name").toLowerCase();
            String arch = System.getProperty("os.arch").toLowerCase();

            /* Check if JVM is 64 bit */
            boolean is64bit = arch.equals("amd64") || arch.equals("x86_64");

            /* Extract libraries */
            if (!Files.isDirectory(libraryPath)) {
                Files.createDirectory(libraryPath);
            } else if (Files.exists(libraryPath)) {
                return;
            }
            String[] libraries;
            if (os.contains("win")) {
                /* Windows */
                if (is64bit) {
                    libraries = new String[]{
                        "lwjgl.dll",
                        "glfw.dll",
                        "OpenAL.dll",
                        "jemalloc.dll"
                    };
                } else {
                    libraries = new String[]{
                        "lwjgl32.dll",
                        "glfw32.dll",
                        "OpenAL32.dll",
                        "jemalloc32.dll"
                    };
                }
            } else if (os.contains("mac")) {
                /* Mac OS X */
                libraries = new String[]{
                    "liblwjgl.dylib",
                    "libglfw.dylib",
                    "libopenal.dylib",
                    "libjemalloc.dylib"
                };

                /* Mac OS X needs headless mode for AWT */
                System.setProperty("java.awt.headless", "true");
            } else if (os.contains("nix") || os.contains("nux") || os.indexOf("aix") > 0) {
                /* Linux */
                if (is64bit) {
                    libraries = new String[]{
                        "liblwjgl.so",
                        "libglfw.so",
                        "libopenal.so",
                        "libjemalloc.so"
                    };
                } else {
                    libraries = new String[]{
                        "liblwjgl32.so",
                        "libglfw32.so",
                        "libopenal32.so",
                        "libjemalloc32.so"
                    };
                }
            } else {
                /* Not supported */
                throw new RuntimeException("Operating System "
                                           + System.getProperty("os.name") + " is not supported");
            }
            for (String library : libraries) {
                Files.copy(Main.class.getResourceAsStream("/" + library), libraryPath.resolve(library),
                           StandardCopyOption.REPLACE_EXISTING);
            }
        }

        /* Set LWJGL library path */
        Configuration.LIBRARY_PATH.set(libraryPath.toString());
    }

}
