import javax.swing.*;
import java.awt.GridLayout;
import java.util.prefs.*;

public class SettingsPanel extends JPanel {
    private MonitorManagerGui mainframe; // i love the funny

    public SettingsPanel(MonitorManagerGui mainframe) {
        super(new GridLayout(2, 3));

        setMainframe(mainframe);
        
        // get preferences
        Preferences prefs = Preferences.userNodeForPackage(MonitorManagerGui.class);
        final String pkUnit = "pk_unit";

        // unit selector
        String[] units = { "in", "cm" };
        
        // JComboBox<String> inputUnit = new JComboBox<String>(units);
        boolean inputUnitInitialState = prefs.get(pkUnit, "in") == "in" ? true : false;
        SwitchButton inputUnit = new SwitchButton("Unit", "in", "cm", inputUnitInitialState);
        inputUnit.addActionListener(event -> {
            // System.out.println(event.toString());
            String newUnit = inputUnit.isSelected() ? "cm" : "in";
            prefs.put(pkUnit, newUnit);
            
            // inform the parent to refresh its children's labels and computed sizes
            mainframe.refreshUnit();
        });
        add(inputUnit);
    }

    //g/s
    
    public MonitorManagerGui getMainframe() {
        return this.mainframe;
    }
    public void setMainframe(MonitorManagerGui mainframe) {
        this.mainframe = mainframe;
    }
}

class SwitchButton extends JToggleButton {
    private String label = "Switch"; // general label
    private String label0 = "Off"; // "off" state label
    private String label1 = "On"; // "on" state label

    SwitchButton(String label, String label0, String label1, boolean initialState) {
        String currentValue = initialState ? label1 : label0;
        super(currentValue);

        setLabel(label);
        setLabel0(label0);
        setLabel1(label1);

        addItemListener(e -> {
            if (isSelected()) {
                setText(getLabel1());
            } else {
                setText(getLabel0());
            }
        });
    }

    //g/s

    public String getLabel() {
        return this.label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel0() {
        return this.label0;
    }
    public void setLabel0(String label) {
        this.label0 = label;
    }

    public String getLabel1() {
        return this.label1;
    }
    public void setLabel1(String label) {
        this.label1 = label;
    }
}