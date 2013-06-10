package ua.vladaxon.ui;

import javax.swing.JTextField;

/**
 * ����������� ����� ����������� ������������� �����.
 * ��������� ������������ ��� � ����������� �� �������(�������������, �������������, �� ���������)
 */
public class ColoredJTextField extends JTextField implements ColoredTextField{
	
	/**
	 * �����, ��������������� ������������� ���� ����.
	 */
	public void setAccepted(){
		this.setBackground(accepted);
	}
	
	/**
	 * �����, ��������������� ������������� ���� ����.
	 */
	public void setDenied(){
		this.setBackground(denied);
	}
	
	/**
	 * �����, ��������������� ���� ���� �� ���������.
	 */
	public void setDefault(){
		this.setBackground(defcolor);
	}
	
	private static final long serialVersionUID = 1L;

}