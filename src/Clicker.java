import java.awt.Robot;
import java.awt.event.InputEvent;

public class Clicker extends Thread {

    private int delay;
    private Robot bot = null;
    private int mask;


    public Clicker(int delay, int mask) {
        this.mask = mask;
        this.delay = delay;
        try {
            bot = new Robot();
        } catch (Exception failed) {
            System.err.println("Failed instantiating Robot: " + failed);
            System.exit(42);
        }


    }

    public void run() {

        while (true) {
            click(mask);


            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                break;
            }
        }
        return;

    }


    void click(int mask) {
        bot.mousePress(mask);
        bot.mouseRelease(mask);
    }

}
