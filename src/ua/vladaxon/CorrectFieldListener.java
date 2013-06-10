package ua.vladaxon;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * ������� ����� ��� ���������� ������������ �����.
 * ��������� ��������� ��������� ��������� � ��������������
 * ����� � 1 ����� �������� ������������ �����.
 */
public abstract class CorrectFieldListener implements DocumentListener{

	@Override
	public void changedUpdate(DocumentEvent e) {
		this.testInput();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		this.testInput();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		this.testInput();
	}

	/**
	 * ����� ����� �������� ������������.
	 */
	protected abstract void testInput();

	/**
	 * ���������� ������� ��������� ������������ �����.
	 * @return <b>true</b> - ���� ����� � ���� ������������� �����������.
	 */
	public boolean getCorrectness(){
		return correctness;
	}
	
	/**
	 * �����, �������������� ������ ����� ������������ ���� �����.
	 * @param correctlistener ��������� ������������.
	 */
	public void setCorectListener(CorrectListener correctlistener){
		this.correctlistener = correctlistener;
	}
	
	/**��������� ����� ������������*/
	protected CorrectListener correctlistener = null;
	/**���� ������������*/
	protected boolean correctness = false;	
	/**���������� ��������� �������� �����*/
	protected static final String wordsymb = "[a-zA-Z_0-9]*";
	/**���������� ��������� ��������������� �����*/
	protected static final String nonegativ = "[0-9]*";
	/**���������� ��������� ��������������� �����*/
	protected static final String negativorpositiv = "-?[0-9]+";
	/**��������� ��������� ��� ����� ������������ ��������*/
	protected static final String notwordsymbols = 
			"���� ������ ��������� ��������� �����, ����� ��� ������ �������������";
	/**���������, ��������� ��� ������������ ����� �����*/
	protected static final String notnumber = "����� �� ������������� �����������";
	/**��������� ��� ���������� ����� �����*/
	protected static final String number = "����� �������";
	
}