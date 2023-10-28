package eriksUtil;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class hintTextField extends JTextField implements FocusListener{

	private static final long serialVersionUID = 1L;
	private String hint;
	
	public hintTextField(String hint){
		super(hint);
		this.hint = hint;
		super.addFocusListener(this);
	}
	@Override
	public void focusGained(FocusEvent e) {
		if(this.getText().isEmpty()){
			super.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(this.getText().isEmpty()){
			super.setText(hint);
		}
	}
	
	public String getText(){
		String typed = super.getText();
		
		return typed.equals(hint) ? "" : typed;
	}

}
