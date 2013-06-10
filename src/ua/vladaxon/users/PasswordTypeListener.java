package ua.vladaxon.users;

import ua.vladaxon.CorrectFieldListener;
import ua.vladaxon.ui.ColoredJPasswordField;

/**
 * �����, ����������� ������������ ����� �������, ��� �������� ������ ������������.
 * ��������� ��� ���� �� ����������� ���������� �������� � ���������� ���������.
 * ���� ��� ���� ����� �� ����� 4 �������� � �� �������� ��������� - ����������� ������������
 * ������� � ����� �����. ���� ��� ��������� - ������ ��������� ��������� ����������.
 */
public class PasswordTypeListener extends CorrectFieldListener{

	public PasswordTypeListener(ColoredJPasswordField first, ColoredJPasswordField second){
		this.first = first;
		this.second = second;
		first.getDocument().addDocumentListener(this);
		second.getDocument().addDocumentListener(this);
	}
	
	/**
	 * �����, ����������� �������� ������ �� ������������ �����������.
	 * � ������ ��� ���� ����������� �� ������������ ��������� �����������, ����� ���:
	 * �������� �� ����� 4 �������� � ���������� ���������.
	 * ���� ��� ���� ����� ������������� ���� ����������� - ����������� ������������ ���� �������.
	 * ���� ������, ��������� � ��� ����, ��������� - ������ ��������� ��������.
	 * � ����� ���������� ����� �������� ����� ������������.
	 */
	protected void testInput(){
		boolean firststate = checkCorrectness(first);
		boolean secondstate = checkCorrectness(second);
		if(firststate && secondstate){
			String text1 = new String(first.getPassword());
			String text2 = new String(second.getPassword());
			if(text1.equals(text2)){
				correctness = true;
			} else {
				second.setDenied();
				second.setToolTipText(noteq);
				correctness = false;
			}
		} else {
			correctness = false;
			if(!firststate){
				second.setDefault();
				second.setToolTipText(typepassword);
			}
		}
		correctlistener.checkCorrectness();
	}
	
	/**
	 * �����, ����������� ��������� ������ �� ������������ �����������.
	 * ������ �� ������ ���� ������, � ���� ������ 4 ��������.
	 * ������ ����� ��������� ��������� �����, �����, ������ �������������.
	 * @param field ����, ������ �������� �����������
	 * @return true - ���� ������ ������ ���������
	 */
	private boolean checkCorrectness(ColoredJPasswordField field){
		String password = new String(field.getPassword());
		if(password.length()>0){
			if(password.matches(wordsymb)){
				if(password.length()>=4){
					field.setAccepted();
					field.setToolTipText(passtyped);
					return true;
				} else {
					field.setDenied();
					field.setToolTipText(shortpassword);
					return false;					
				}
			} else {
				field.setDenied();
				field.setToolTipText(notwordsymbols);
				return false;
			}
		} else {
			field.setDefault();
			field.setToolTipText(typepassword);
			return false;
		}
	}
	
	/**������ ���� ����� ������*/
	private ColoredJPasswordField first = null;
	/**������ ����, �������������� ����*/
	private ColoredJPasswordField second = null;
	/**����������� ��������� ��� ������ ���� �����*/
	private static final String typepassword = "������� ������";
	/**����������� ��������� ��� ������������� ����� ������*/
	private static final String shortpassword = "������ ������ ��������� �� ������ 4 ��������";
	/**����������� ��������� ���� ������ � ����� �� ���������*/
	private static final String noteq = "������ �� ���������";
	/**����������� ��������� ��� ����� ������, ������� ������������� �����������*/
	private static final String passtyped = "������ ������";

}