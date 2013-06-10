package ua.vladaxon.ui;

import java.awt.Color;

/**
 * ���������, ���������� �������� ��������� ������������ 
 * ��� � ����������� �� �������(�������������, �������������, �� ���������)
 */
public interface ColoredTextField {
	
	/**
	 * �����, ��������������� ������������� ���� ����.
	 */
	public void setAccepted();
	
	/**
	 * �����, ��������������� ������������� ���� ����.
	 */
	public void setDenied();
	
	/**
	 * �����, ��������������� ���� ���� �� ���������.
	 */
	public void setDefault();
	
	/**���� ���� ��������������� �������������� �������*/
	public static final Color denied = new Color(255, 130, 130);
	/**���� ���� ��������������� �������������� �������*/
	public static final Color accepted = new Color(130, 255, 130);
	/**���� ���� �� ���������*/
	public static final Color defcolor = Color.WHITE;

}