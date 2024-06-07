import javax.swing.*;
import java.awt.*;

/**
 * Trieda GUIUtils poskytuje užitočné metódy pre prácu s komponentami GUI.
 */
public class GUIUtils {

    /**
     * Nastaví veľkosť komponentu na zadanú šírku a výšku.
     *
     * @param component komponent GUI, ktorému sa má nastaviť veľkosť
     * @param width     šírka komponentu
     * @param height    výška komponentu
     */
    public static void setComponentSize(JComponent component, int width, int height) {
        Dimension dimension = new Dimension(width, height);
        component.setPreferredSize(dimension);
        component.setMinimumSize(dimension);
        component.setMaximumSize(dimension);
    }
}
