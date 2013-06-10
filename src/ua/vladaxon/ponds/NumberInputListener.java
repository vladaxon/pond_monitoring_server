package ua.vladaxon.ponds;

import ua.vladaxon.CorrectFieldListener;
import ua.vladaxon.ui.ColoredJTextField;

/**
 * ������ ��������� ������������ ����� � ����. ��������� ������������
 * ��� �������� ����� ������ ������ � ������ �� ������������� ����� �������� ��������.
 */
public class NumberInputListener extends CorrectFieldListener{
	
	/**
	 * ����������� �������.
	 * @param field ����������� ����
	 * @param emptallowed ���� ���������� ������� ����
	 * @param negallowed ���� ���������� �������������� �����
	 */
	public NumberInputListener(ColoredJTextField field, boolean emptallowed, boolean negallowed) {
		this.idfield = field;
		this.emptallowed = emptallowed;
		if(negallowed)
			localregex = negativorpositiv;
		else
			localregex = nonegativ;
		field.getDocument().addDocumentListener(this);
		idfield.setToolTipText(notnumber);
		correctness = emptallowed;
	}

	/**
	 * �������� �� ���� �����.
	 */
	@Override
	protected void testInput() {
		String strid = idfield.getText();
		if(strid.length()==0){
			if(emptallowed){
				idfield.setDefault();
				idfield.setToolTipText(notnumber);
				correctness = true;
			} else {
				idfield.setDenied();
				idfield.setToolTipText(notnumber);
				correctness = false;
			}
		} else {
			if(strid.matches(localregex)){
				idfield.setAccepted();
				idfield.setToolTipText(number);
				correctness = true;
			} else {
				idfield.setDenied();
				idfield.setToolTipText(notnumber);
				correctness = false;
			}
		}
		correctlistener.checkCorrectness();
	}
	
	/**���� ����� ������*/
	private ColoredJTextField idfield = null; 
	/**���� ���������� ������� ����*/
	private boolean emptallowed = false;
	/**���������, ������������ ��� ��������*/
	private String localregex = null;

}