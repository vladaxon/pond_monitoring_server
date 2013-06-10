package ua.vladaxon.users;

import ua.vladaxon.CorrectFieldListener;
import ua.vladaxon.ui.ColoredJTextField;

/**
 * �����, ������ �������� ������ �� ������������� ������ ��� ���������� ������ ������������.
 * ����� ������ ��������� ������ ��������� �����, �����, ������ �������������.
 * ����� ������ ���� ���������� � ����� �� ����� 4 ����������� ��������.
 * @see CorrectFieldListener
 */
public class BusyLoginListener extends CorrectFieldListener{
	
	public BusyLoginListener(ColoredJTextField loginfield, UserManager manager){
		this.loginfield = loginfield;
		this.manager = manager;
		loginfield.getDocument().addDocumentListener(this);
		loginfield.setToolTipText(typelogin);
	}
	
	/**
	 * �������� ������ �� ������������ ��������.
	 * ����� ����� ��������� ��������� �����, �����, ������ �������������.
	 * ����� ������ ����� �� ����� 4 �������� � ���� ���������� �����
	 * ������������ �������, ������������ ��������� �������� ������� �������������.
	 */
	protected void testInput(){
		String text = loginfield.getText();
		if(text.length()>0){
			if(text.matches(wordsymb)){
				if(text.length()>=4){
					if(manager.isUniqueLogin(text)){
						loginfield.setAccepted();
						loginfield.setToolTipText(loginunic);
						correctness = true;
					} else {
						loginfield.setDenied();
						loginfield.setToolTipText(notunic);
						correctness = false;
					}
				} else {
					loginfield.setDenied();
					loginfield.setToolTipText(shortlogin);
					correctness = false;					
				}
			} else {
				loginfield.setDenied();
				loginfield.setToolTipText(notwordsymbols);
				correctness = false;
			}
		} else {
			loginfield.setDefault();
			loginfield.setToolTipText(typelogin);
			correctness = false;
		}
		correctlistener.checkCorrectness();
	}
	
	/**���� ����� ������*/
	private ColoredJTextField loginfield = null;
	/**�������� �������������, ������������� �������� ������������*/
	private UserManager manager = null;
	/**����������� ��������� ��� ������ ���� �����*/
	private static final String typelogin = "������� �����";
	/**����������� ��������� ��� ������������� ����� ������*/
	private static final String shortlogin = "����� ������ ��������� �� ����� 4 ��������";
	/**����������� ���������, ���� �������� ����� �� ��������*/
	private static final String notunic = "����� ����� ��� ���������������";
	/**����������� ��������� ��� ����� ������, ������� ������������� �����������*/
	private static final String loginunic = "����� ����� ��������";

}