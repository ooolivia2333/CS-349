import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class LowerLeg extends Sprite{

    public LowerLeg(double start_x, double start_y, int width, int height, boolean left){
        super();
        this.initialize(start_x, start_y, width, height, left);
    }

    private void initialize(double start_x, double start_y, double width, double height, boolean left){
        x = 0;
        y = 0;
        sx = start_x;
        sy = start_y;
        w = width;
        h = height;
        curr_height = height;
        pivot = new Point2D(x+w/2, y);
        initial_pivot = new Point2D(x+w/2, y);
        if (left) image = new Image("left_lower_leg.png");
        else image = new Image("right_lower_leg.png");
    }

    @Override
    public Point2D get_pivot() {
        return getFullMatrix().transform(pivot);
    }

    @Override
    protected boolean valid_rotation() {
        return degree <= 90 && degree >= -90;
    }

    @Override
    protected String get_type() {
        return "lower_leg";
    }

    @Override
    protected boolean contains(Point2D p) {
        try {
            // Use inverted matrix to move the mouse click so that it's
            // relative to the shape model at the origin.
            Point2D pointAtOrigin = getFullMatrix().createInverse().transform(p);

            // Perform the hit test relative to the shape model's
            // untranslated coordinates at the origin
            return x <= pointAtOrigin.getX() && x + w >= pointAtOrigin.getX() &&
                    y <= pointAtOrigin.getY() && y + h >= pointAtOrigin.getY();

        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void draw(GraphicsContext gc) {
        // save the current graphics context so that we can restore later
        Affine oldMatrix = gc.getTransform();

        // make sure we have the correct transformations for this shape
        gc.setTransform(getFullMatrix());
        gc.drawImage(image, x, y, w, h);

        // draw children
        for (Sprite child : children) {
            child.draw(gc);
        }

        // set back to original value since we're done with this branch of the scene graph
        gc.setTransform(oldMatrix);
    }
}
