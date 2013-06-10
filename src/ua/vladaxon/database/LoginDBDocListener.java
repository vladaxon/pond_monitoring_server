package ua.vladaxon.database;

import javax.swing.JTextField;

import ua.vladaxon.CorrectFieldListener;
import ua.vladaxon.ui.ColoredJPasswordField;
import ua.vladaxon.ui.ColoredJTextField;
import ua.vladaxon.ui.ColoredTextField;

/**
 * �����, ����������� ������������ ����� ������ � ����.
 * ����� � ���� ����� ��������� ��������� �����, �����, ������ �������������.
 * @see CorrectFieldListener
 */
public class LoginDBDocListener extends CorrectFieldListener{
	
	public LoginDBDocListener(ColoredTextField field){
		this.field = field;
		((JTextField)field).getDocument().addDocumentListener(this);
	}

	@Override
	protected void testInput() {
		String text = null;
		if(field instanceof ColoredJPasswordField)
			text = new String(((ColoredJPasswordField) field).getPassword());
		else
			text = ((ColoredJTextField) field).getText();
		if(text.length()==0){
			field.setDefault();
			correctness = false;
		} else {
			if (text.matches(wordsymb)) {
				field.setDefault();
				correctness = true;
				((JTextField) field).setToolTipText(null);
			} else {
				field.setDenied();
				correctness = false;
				((JTextField) field).setToolTipText(notwordsymbols);
			}
		}
		correctlistener.checkCorrectness();
	}
	
	/**��������� ����, � �������� �������� ���������, ��������� ��� ����� �����*/
	private ColoredTextField field = null;

}