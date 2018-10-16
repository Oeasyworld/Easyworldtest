package ng.com.easyworld.easyworld;

/**
 * Created by Oisrael on 3/8/2018.
 */



import android.graphics.Point;
import android.opengl.Matrix;



public class RendererCommon {
    // Types of video scaling:
    // SCALE_ASPECT_FIT - video frame is scaled to fit the size of the view by
    //    maintaining the aspect ratio (black borders may be displayed).
    // SCALE_ASPECT_FILL - video frame is scaled to fill the size of the view by
    //    maintaining the aspect ratio. Some portion of the video frame may be
    //    clipped.
    // SCALE_ASPECT_BALANCED - Compromise between FIT and FILL. Video frame will fill as much as
    // possible of the view while maintaining aspect ratio, under the constraint that at least
    // |BALANCED_VISIBLE_FRACTION| of the frame content will be shown.
    public static enum ScalingType { SCALE_ASPECT_FIT, SCALE_ASPECT_FILL, SCALE_ASPECT_BALANCED }
    // The minimum fraction of the frame content that will be shown for |SCALE_ASPECT_BALANCED|.
    // This limits excessive cropping when adjusting display size.
    private static float BALANCED_VISIBLE_FRACTION = 0.56f;
    /**
     * Calculates a texture transformation matrix based on rotation, mirror, and video vs display
     * aspect ratio.
     */
    public static void getTextureMatrix(float[] outputTextureMatrix, float rotationDegree,
                                        boolean mirror, float videoAspectRatio, float displayAspectRatio) {
        // The matrix stack is using post-multiplication, which means that matrix operations:
        // A; B; C; will end up as A * B * C. When you apply this to a vertex, it will result in:
        // v' = A * B * C * v, i.e. the last matrix operation is the first thing that affects the
        // vertex. This is the opposite of what you might expect.
        Matrix.setIdentityM(outputTextureMatrix, 0);
        // Move coordinates back to [0,1]x[0,1].
        Matrix.translateM(outputTextureMatrix, 0, 0.5f, 0.5f, 0.0f);
        // Rotate frame clockwise in the XY-plane (around the Z-axis).
        Matrix.rotateM(outputTextureMatrix, 0, -rotationDegree, 0, 0, 1);
        // Scale one dimension until video and display size have same aspect ratio.
        if (displayAspectRatio > videoAspectRatio) {
            Matrix.scaleM(outputTextureMatrix, 0, 1, videoAspectRatio / displayAspectRatio, 1);
        } else {
            Matrix.scaleM(outputTextureMatrix, 0, displayAspectRatio / videoAspectRatio, 1, 1);
        }
        // TODO(magjed): We currently ignore the texture transform matrix from the SurfaceTexture.
        // It contains a vertical flip that is hardcoded here instead.
        Matrix.scaleM(outputTextureMatrix, 0, 1, -1, 1);
        // Apply optional horizontal flip.
        if (mirror) {
            Matrix.scaleM(outputTextureMatrix, 0, -1, 1, 1);
        }
        // Center coordinates around origin.
        Matrix.translateM(outputTextureMatrix, 0, -0.5f, -0.5f, 0.0f);
    }
    /**
     * Calculate display size based on scaling type, video aspect ratio, and maximum display size.
     */
    public static Point getDisplaySize(ScalingType scalingType, float videoAspectRatio,
                                       int maxDisplayWidth, int maxDisplayHeight) {
        return getDisplaySize(convertScalingTypeToVisibleFraction(scalingType), videoAspectRatio,
                maxDisplayWidth, maxDisplayHeight);
    }
    /**
     * Each scaling type has a one-to-one correspondence to a numeric minimum fraction of the video
     * that must remain visible.
     */
    private static float convertScalingTypeToVisibleFraction(ScalingType scalingType) {
        switch (scalingType) {
            case SCALE_ASPECT_FIT:
                return 1.0f;
            case SCALE_ASPECT_FILL:
                return 0.0f;
            case SCALE_ASPECT_BALANCED:
                return BALANCED_VISIBLE_FRACTION;
            default:
                throw new IllegalArgumentException();
        }
    }
    /**
     * Calculate display size based on minimum fraction of the video that must remain visible,
     * video aspect ratio, and maximum display size.
     */
    private static Point getDisplaySize(float minVisibleFraction, float videoAspectRatio,
                                        int maxDisplayWidth, int maxDisplayHeight) {
        // If there is no constraint on the amount of cropping, fill the allowed display area.
        if (minVisibleFraction == 0 || videoAspectRatio == 0) {
            return new Point(maxDisplayWidth, maxDisplayHeight);
        }
        // Each dimension is constrained on max display size and how much we are allowed to crop.
        final int width = Math.min(maxDisplayWidth,
                (int) (maxDisplayHeight / minVisibleFraction * videoAspectRatio));
        final int height = Math.min(maxDisplayHeight,
                (int) (maxDisplayWidth / minVisibleFraction / videoAspectRatio));
        return new Point(width, height);
    }
}
