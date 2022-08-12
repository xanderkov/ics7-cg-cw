package raymarching

import java.awt.Desktop
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.Renderer

import carlvbn.raytracing.rendering.Renderer


const val MAX_MARCHING_STEPS = 255;
const val MIN_DIST = 0.0001;
const val MAX_DIST = 100.0;
const val EPSILON = 0.0001;

fun draw_circle() {

}

fun renderToImage(width: Int, height: Int) {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        println("Rendering to image...")
        if (postProcessing) Renderer.renderScenePostProcessed(
                scene,
                image.graphics,
                width,
                height,
                1f
        ) else Renderer.renderScene(scene, image.graphics, width, height, 1f)
        val imgFile = File("output.png")
        ImageIO.write(image, "PNG", FileOutputStream(imgFile))
        println("Image saved.")
        Desktop.getDesktop().open(imgFile)
}