import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyPressHandler implements NativeKeyListener {

    private Clicker c;
    private boolean running = false;
    private int keyevent;
    private boolean hotkeySelect = false;




    public KeyPressHandler(int keyevent) {
        this.keyevent = keyevent;
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        if(!this.hotkeySelect && e.getKeyCode() == keyevent && !running) {
            c = new Clicker(GUI.getInstance().getDelay());
            running = true;
            c.start();
            System.out.println("clicker on. interval: " + GUI.getInstance().getDelay() + "ms");
        } else if(!this.hotkeySelect && e.getKeyCode() == keyevent && running) {
            running = false;
            c.interrupt();
            System.out.println("clicker off.");
        } else if(this.hotkeySelect) {
            GUI.getInstance().setHotkey(e.getKeyCode());
            this.hotkeySelect = false;
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    public void hotkeySelect() {
        this.hotkeySelect = true;
    }

    public void setHotkey(int hotkey) {
        this.keyevent = hotkey;
    }
}