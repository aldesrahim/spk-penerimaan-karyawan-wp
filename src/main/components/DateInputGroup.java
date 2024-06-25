package main.components;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Dimension;
import java.util.Date;

/**
 *
 */
public class DateInputGroup extends InputGroup {
    public DateInputGroup() {
        super(new DateInputField());
        
        setMinimumSize(UIScale.scale(panelSize));
    }

    @Override
    public final DateInputField getInputField() {
        return (DateInputField) super.getInputField();
    }

    @Override
    public String getInputValue() {
        return getInputField().getText();
    }
    
    public Date getDate() {
        return getInputField().getDate();
    }
    
    public void setDate(Date date) {
        getInputField().setDate(date);
    }

    @Override
    public void setPanelSize(Dimension defaultSize) {
        setMinimumSize(UIScale.scale(defaultSize));
        
        super.setPanelSize(defaultSize);
    }
    
    
}
