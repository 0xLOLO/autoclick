import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import java.awt.FlowLayout;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
  private int delay = 200;
  private KeyPressHandler handler;
  private JButton hotkeyButton = new JButton(NativeKeyEvent.getKeyText(STANDARD_KEY));
  private JSlider slider = new JSlider(10, 2000);
  private JLabel sliderLabel = new JLabel(1000 / this.slider.getValue() + "cps");
  private int mouseButton = InputEvent.BUTTON1_DOWN_MASK;


  /**
   * GUI constructor. Instantiates the swing GUI and registers NativeHook + KeyPressHandler
   * TODO: Mouse Button Select
   */
  public GUI(String title) {
    super(title);

    this.nativehookInit();

    this.swingInit();
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
    sliderLabel.setText(String.valueOf(((float) Math.round(((float) 1000/delay) * 10 ) / 10 + "cps")));
    this.delay = delay;
  }

  private void nativehookInit() {
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
  }

  private void swingInit() {
    //meta
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(280, 100);
    this.setResizable(false);

    //panel
    JPanel outerPanel = new JPanel();

    //slider
    JPanel sliderPanel = new JPanel();
    sliderPanel.add(sliderLabel);
    sliderPanel.add(slider);
    slider.addChangeListener(event -> {
      this.setDelay(((JSlider) event.getSource()).getValue());
    });
    slider.setValue(200);
    outerPanel.add(sliderPanel);

    //hotkey button
    hotkeyButton.addActionListener(event -> {
      hotkeyButton.setText("press any key...");
      handler.hotkeySelect();
    });
    outerPanel.add(new JPanel().add(this.hotkeyButton));


    //mb select
    RadioButton[] rb = {
        new RadioButton("L", true, InputEvent.BUTTON1_DOWN_MASK),
        new RadioButton("M", false, InputEvent.BUTTON2_DOWN_MASK),
        new RadioButton("R", false, InputEvent.BUTTON3_DOWN_MASK)
    };
    ButtonGroup mbSelect = new ButtonGroup();
    JPanel buttonPanel = new JPanel(new FlowLayout());
    for(RadioButton e : rb) {
      mbSelect.add(e);
      buttonPanel.add(e);
    }
    outerPanel.add(buttonPanel);



    //adding and making visible
    this.getContentPane().add(outerPanel);
    this.setVisible(true);
  }

  public void setMouseButton(int mb) {
    this.mouseButton = mb;
  }

  public int getButton() {
    return this.mouseButton;
  }
}
