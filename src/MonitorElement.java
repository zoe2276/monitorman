import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.util.HashMap;

public class MonitorElement extends JPanel implements MouseListener, MouseMotionListener {
    private Container parent;
    private Monitor monitor;
    int startX = -1, startY = -1;
    boolean moving = false;
    int endX = -1, endY = -1;

    public MonitorElement(Container parent, Monitor monitor) {
        // 3x3 grid to hold the size, vp width, and vp height
        super(new GridLayout(3, 3), false);
        this.setParent(parent);
        this.setMonitor(monitor);

        // check if monitor does not have size
        if (this.monitor.getSize() == 0.0) {
            String monitorSize = JOptionPane.showInputDialog(this, "Please enter the size for monitor \"" + this.monitor.getId() + "\":", "Enter monitor size", JOptionPane.PLAIN_MESSAGE);
            this.monitor.setSize(Float.valueOf(monitorSize), true);
        }

        // border
        TitledBorder monitorElemBorder = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), this.monitor.getId());
        monitorElemBorder.setTitleJustification(TitledBorder.CENTER);
        monitorElemBorder.setTitlePosition(TitledBorder.DEFAULT_POSITION);
        
        String monitorLabel = String.valueOf(this.monitor.getSize()) + this.monitor.getUnit();
        JLabel label = new JLabel(monitorLabel, JLabel.CENTER);
        add(label);
        setBorder(monitorElemBorder);
        // setSize(16, 9);

        // parent.add(Box.createRigidArea(new Dimension(0, 12)));
        parent.add(this);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // event listeners
    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mousePressed(MouseEvent e) {
        // track coords when mouse is first pressed
        Point startCoords = e.getPoint();
        startX = startCoords.x;
        startY = startCoords.y;
        moving = true;
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        moving = false;
        int xDiff = endX - startX, yDiff = endY - startY;
        HashMap<String, Integer> currentMonitorPosition = this.monitor.getPosition();
        moveMonitor(currentMonitorPosition.get("x") + xDiff, currentMonitorPosition.get("y") + yDiff);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point endCoords = e.getPoint();
        endX = endCoords.x;
        endY = endCoords.y;
        if (moving) {
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    public void moveMonitor(int x, int y) {
        try {
            this.monitor.setPosition(x, y);
        } catch (IOException e) {
            // looks like we hit it out of bounds! do nothing.
        }
    }

    public Container getParent() {
        return this.parent;
    }
    public void setParent(Container parent) {
        this.parent = parent;
    }

    public Monitor getMonitor() {
        return this.monitor;
    }
    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }
}
