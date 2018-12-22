import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Bubbles extends JApplet implements Runnable, KeyEventDispatcher
{    
    int NUM  = Integer.parseInt(JOptionPane.showInputDialog("Enter Number of Bars: "));
    public int list[] = new int [NUM];  // List of random numbers
    private int width;       // Applet size:
    private int height;      // Applet size:
    private int delay=100;   //delay
    private int showGreen=0; // Index of value that is currently being examined; it will be green
    private int showRed=0;   // Index of value that is currently being swapped; it will be red
    private boolean restart=false;
    float factor;
    private Image backbuffer; // Background drawing buffer:
    private Graphics bg;
    
    public void init()
    {
        width=this.getBounds().width;
        height=this.getBounds().height;
        makeList();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        backbuffer = createImage(width, height);
        bg = backbuffer.getGraphics();
    }

    public void start()
    {
        Thread th = new Thread (this);
        th.start ();
    }

    public void paint(Graphics g)
    {
        showList(bg);
        g.drawImage(backbuffer,0,0,this);
    }

    public void makeList ()
    {
        for (int i = 0 ; i < NUM ; i++)
        {
            list [i] = (int) (Math.random() * 100); //hmm
        }
    }

    public void showList (Graphics g)
    {
        int x;
        g.clearRect(0,0,width,height);
        factor = (1/((float)NUM)); 
        for (int i = 0 ; i < NUM ; i++){
            x=list[i]*height/100;
            if (i==showGreen){
                g.setColor(Color.green);
            } else if (i==showRed){
                g.setColor(Color.red);
            } else{
                float hue = (factor * i); //hue
                g.setColor(Color.getHSBColor(hue, 1.0f, 1.0f));
            }
            g.fillRect(i*width/NUM,height-x-1,width/NUM,x);
            //g.setColor(Color.black);  //Black Borders
            //g.drawRect(i*width/NUM,height-x-1,width/NUM,x);
            g.setColor(Color.black);
            g.drawString("Bublesort!!!", 20, 40);
            g.drawString("Up & Down arrows to change delay!", 20, 60);
            g.drawString("Delay: "+delay, 20, 80);
        }
    }

    public void run(){
        int i, j, temp;
        while (true){
            // Do repetitive things here
            for (j = 0 ; j < NUM - 1 ; j++){
                for (i = 0 ; i < NUM - j- 1 ; i++){
                    if (restart){
                        i=0;
                        j=0;
                        restart=false;
                    }

                    // Show the value currently being examined
                    showGreen=i;
                    if (list [i] > list [i + 1]){
                        // Highlight the value that is going to be swapped
                        showRed=i+1;
                    }else{
                        // Set to -1 so nothing highlights if no swap is taking place
                        showRed=-1;
                    }
                    repaint();
                    try{
                        Thread.sleep (delay);
                    }catch (InterruptedException e){
                        // do nothing
                    }
                    // Now actually do the swap
                    if (list [i] > list [i + 1]){
                        temp = list [i];
                        list [i] = list [i + 1];
                        list [i + 1] = temp;
                    }
                }
            }

            try{
                Thread.sleep (delay);
            }catch (InterruptedException e)
            {
            }
            repaint();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent e)
    {
        int key=e.getKeyCode();
        if (e.getID()==KeyEvent.KEY_PRESSED) {
            if (key==38){ 
                if (delay >= 2){ 
                    delay = delay - 2;  //up arrow speeds up delay
                }
            }else if (key ==40){
                delay = delay + 2;  //down arrow slows down delay
            }else if (key==32) //Space restarts
            {
                makeList();
                restart=true;
            }
            return true;
        }
        return false;
    }
}