package lolo.autoclicker;

import javax.swing.JRadioButton;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RadioButton extends JRadioButton {

  public RadioButton(String text, boolean selected, int mouseEvent) {
    super(text, selected);
    this.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
          GUI.getInstance().setMouseButton(mouseEvent);
        }
      }
    });
  }
}
