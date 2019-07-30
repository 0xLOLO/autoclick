import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUI extends JFrame {

  /**
   * Main method is obsolete due to the GUI instantiating itself
   */
  public static void main(String[] args) {
  }

  //Singleton gui instance
  private static final GUI instance = new GUI("AutoClicker");
  private static final int STANDARD_KEY = NativeKeyEvent.VC_CAPS_LOCK;
  private JSlider slider = new JSlider(10, 2000);
  private JLabel sliderLabel = new JLabel(slider.getValue() + "ms");
  private JButton hotkeyButton = new JButton(NativeKeyEvent.getKeyText(STANDARD_KEY));
  private int delay = 200;
  private KeyPressHandler handler;


  /**
   * GUI constructor. Instantiates the swing GUI and registers NativeHook + KeyPressHandler
   * TODO: Mouse Button Select
   */
  public GUI(String title) {
    super(title);

    Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.WARNING);
    try {
      GlobalScreen.registerNativeHook();
      handler = new KeyPressHandler(NativeKeyEvent.VC_CAPS_LOCK);
      GlobalScreen.addNativeKeyListener(handler);
      System.out.println("Registered NativeHook and KeyListener.");
    } catch (NativeHookException ex) {
      System.err.println("There was a problem registering the NativeHook.");
      System.err.println(ex.getMessage());

      System.exit(1);
    }

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(280, 100);
    this.setResizable(false);
    JPanel outerPanel = new JPanel();
    JPanel sliderPanel = new JPanel();
    sliderPanel.add(sliderLabel);
    sliderPanel.add(slider);
    slider.addChangeListener(event -> {
      this.setDelay(((JSlider) event.getSource()).getValue());
    });
    slider.setValue(200);
    this.hotkeyButton.addActionListener(event -> {
      this.hotkeyButton.setText("press any key...");
      handler.hotkeySelect();
    });
    outerPanel.add(sliderPanel);
    outerPanel.add(new JPanel().add(this.hotkeyButton));
    this.getContentPane().add(outerPanel);
    this.setVisible(true);
  }

  public static GUI getInstance() {
    return instance;
  }

  public void setHotkey(int hotkey) {
    hotkeyButton.setText(NativeKeyEvent.getKeyText(hotkey));
    handler.setHotkey(hotkey);
  }

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    sliderLabel.setText(String.valueOf(delay) + "ms");
    this.delay = delay;
  }
}
