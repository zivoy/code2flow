package Elements;

import java.awt.*;

public interface Drawable {
    void draw(Graphics2D g);

    Color getColor();

    Color getBorderColor();
}
