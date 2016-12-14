package styloconnecte;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.PixelGrabber;

import javax.swing.JPanel;
import static styloconnecte.StyloConnecte.saveImageWords;

/**
 * The canvas where the user draws the letters
 */
public class Entry extends JPanel {

    protected Image entryImage;
    protected Graphics entryGraphics;
    protected int lastX = -1;
    protected int lastY = -1;
    protected int downSampleLeft;
    protected int downSampleRight;
    protected int downSampleTop;
    protected int downSampleBottom;
    protected double ratioX;
    protected double ratioY;
    protected int pixelMap[];

    Entry() {
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK
                | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.COMPONENT_EVENT_MASK);
    }

    public void clear() {
        this.entryGraphics.setColor(Color.white);
        this.entryGraphics.fillRect(0, 0, getWidth(), getHeight());
        this.downSampleBottom = this.downSampleTop = this.downSampleLeft = this.downSampleRight = 0;
        repaint();
    }

    public void downSample() {
        final int w = this.entryImage.getWidth(this);
        final int h = this.entryImage.getHeight(this);

        final PixelGrabber grabber = new PixelGrabber(this.entryImage, 0, 0, w,
                h, true);
        try {
            grabber.grabPixels();
            this.pixelMap = (int[]) grabber.getPixels();
            findBounds(w, h);

            //after downsampling set the slope array of downsample
        } catch (final InterruptedException e) {
        }

    }

    protected boolean downSampleRegion(final int x, final int y) {
        final int w = this.entryImage.getWidth(this);
        final int startX = (int) (this.downSampleLeft + (x * this.ratioX));
        final int startY = (int) (this.downSampleTop + (y * this.ratioY));
        final int endX = (int) (startX + this.ratioX);
        final int endY = (int) (startY + this.ratioY);

        for (int yy = startY; yy <= endY; yy++) {
            for (int xx = startX; xx <= endX; xx++) {
                final int loc = xx + (yy * w);

                if (this.pixelMap[loc] != -1) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void findBounds(final int w, final int h) {
        // top line
        for (int y = 0; y < h; y++) {
            if (!hLineClear(y)) {
                this.downSampleTop = y - 1;
                if (y - 1 < 0) {
                    downSampleTop = y;
                }
                break;
            }

        }
        // bottom line
        for (int y = h - 1; y >= 0; y--) {
            if (!hLineClear(y)) {
                this.downSampleBottom = y + 1;
                if (y + 1 > h - 1) {
                    downSampleBottom = y;
                }
                break;
            }
        }
        // left line
        for (int x = 0; x < w; x++) {
            if (!vLineClear(x)) {
                this.downSampleLeft = x - 1;
                if (x - 1 < 0) {
                    downSampleLeft = x;
                }
                break;
            }
        }

        // right line
        for (int x = w - 1; x >= 0; x--) {
            if (!vLineClear(x)) {
                this.downSampleRight = x + 1;
                if (x + 1 > w - 1) {
                    downSampleRight = x;
                }
                break;
            }
        }
    }

    protected boolean hLineClear(final int y) {
        final int w = this.entryImage.getWidth(this);
        for (int i = 0; i < w; i++) {
            if (this.pixelMap[(y * w) + i] != -1) {
                return false;
            }
        }
        return true;
    }

    protected void initImage() {
        this.entryImage = createImage(getWidth(), getHeight());
        this.entryGraphics = this.entryImage.getGraphics();
        this.entryGraphics.setColor(Color.white);
        this.entryGraphics.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void paint(final Graphics g) {
        if (this.entryImage == null) {
            initImage();
        }
        g.drawImage(this.entryImage, 0, 0, this);
        g.setColor(Color.black);
        g.drawRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.red);
        g.drawRect(this.downSampleLeft, this.downSampleTop,
                this.downSampleRight - this.downSampleLeft,
                this.downSampleBottom - this.downSampleTop);

    }

    @Override
    protected void processMouseEvent(final MouseEvent e) {
        if (e.getID() != MouseEvent.MOUSE_PRESSED) {
            /* --- Stylo n'ecrit plus --- */
            Point p = MouseInfo.getPointerInfo().getLocation();
            int px = p.x, py = p.y;
            int tempsAttente = 0;

            while (true) {
                p = MouseInfo.getPointerInfo().getLocation();
                if ((px == p.x) && (py == p.y)) {
                    /* --- Stylo bouge pas --- */
                    tempsAttente = tempsAttente + 1;
                    System.out.println(tempsAttente);

                    if (tempsAttente == 70) {
                        try {
                            saveImageWords(StyloConnecte.setXFrame, StyloConnecte.setYFrame,
                                    StyloConnecte.setWidthFrame, StyloConnecte.setHeightFrame);
                            clear();
                            break;
                        } catch (Exception c) {
                            throw new RuntimeException(c);
                        }
                    }
                } else {
                    break;
                }
                /* --- On quitte le prog --- */
                px = p.x;
                py = p.y;
                StyloConnecte.robot.delay(20);
            }
        }
        this.lastX = e.getX();
        this.lastY = e.getY();
    }

    @Override
    protected void processMouseMotionEvent(final MouseEvent e) {
        if (e.getID() != MouseEvent.MOUSE_DRAGGED) {
            /* --- Stylo n'ecrit plus --- */
            Point p = MouseInfo.getPointerInfo().getLocation();
            int px = p.x, py = p.y;
            int tempsAttente = 0;

            while (true) {
                p = MouseInfo.getPointerInfo().getLocation();
                if ((px == p.x) && (py == p.y)) {
                    /* --- Stylo bouge pas --- */
                    tempsAttente = tempsAttente + 1;
                    System.out.println(tempsAttente);

                    if (tempsAttente == 70) {
                        try {
                            saveImageWords(StyloConnecte.setXFrame, StyloConnecte.setYFrame,
                                    StyloConnecte.setWidthFrame, StyloConnecte.setHeightFrame);
                            clear();
                            break;
                        } catch (Exception c) {
                            throw new RuntimeException(c);
                        }
                    }
                } else {
                    break;
                }
                /* --- On quitte le prog --- */

                px = p.x;
                py = p.y;
                StyloConnecte.robot.delay(20);
            }
            return;
        }

        this.entryGraphics.setColor(Color.black);
        this.entryGraphics.drawLine(this.lastX, this.lastY, e.getX(), e.getY());
        getGraphics().drawImage(this.entryImage, 0, 0, this);
        this.lastX = e.getX();
        this.lastY = e.getY();
    }

    protected boolean vLineClear(final int x) {
        final int w = this.entryImage.getWidth(this);
        final int h = this.entryImage.getHeight(this);
        for (int i = 0; i < h; i++) {
            if (this.pixelMap[(i * w) + x] != -1) {
                return false;
            }
        }
        return true;
    }
}
