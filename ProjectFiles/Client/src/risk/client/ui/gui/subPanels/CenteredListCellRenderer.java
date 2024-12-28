package risk.client.ui.gui.subPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A custom {@link DefaultListCellRenderer} subclass that centers text horizontally within each cell of a {@link JList}.
 * <p>
 * This renderer adjusts the horizontal alignment of the cell's content, ensuring that the text is centered, which enhances the visual appeal and readability of lists within the UI. It overrides the {@code getListCellRendererComponent} method to modify the default label component used to render each cell.
 * <p>
 * Usage of this class is particularly beneficial in scenarios where list items are short and center alignment would provide a more aesthetically pleasing layout.
 *
 * @see DefaultListCellRenderer
 * @see JList
 */
public class CenteredListCellRenderer extends DefaultListCellRenderer {

    /**
     * Returns a component that has been configured to display the specified value. The component's text is centered horizontally.
     * <p>
     * This method is called by JList to obtain a renderer that is configured to display the specified value. The method configures a JLabel, which is returned by the superclass's {@code getListCellRendererComponent} method, to center its text horizontally. This customization enhances the visual appeal of the list by ensuring that the text within each cell is aligned centrally.
     * <p>
     * @param list the JList that displays the value. This parameter is used to obtain the necessary configuration for rendering.
     * @param value the value to be rendered. This is the object that is returned by the ListModel of the JList.
     * @param index the cell's index.
     * @param isSelected true if the specified cell is selected.
     * @param cellHasFocus true if the specified cell has the focus.
     * @return a component whose paint method will render the specified value.
     *
     * @see JLabel#setHorizontalAlignment(int)
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}
