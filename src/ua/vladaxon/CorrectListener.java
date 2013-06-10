package ua.vladaxon;

import javax.swing.JButton;

/**
 * �����, ������ �������� ������ �� ����� ������������� ����� � �����.
 * ��������� ��� �������� �������������� ������, � ����������� �� ������������ ���������� �����.
 */
public class CorrectListener {
	
	/**
	 * ����������� �������, ������������ ����� ������������ �����.
	 * ����������� ��������� ��������� ���������� � ������������ 
	 * ���� �� ���� ���������� ����� ��� ����������� ����������� ������ �������.
	 * @param donebutton ������ �������������.
	 * @param listeners ������ ���������� �����.
	 */
	public CorrectListener(JButton donebutton, CorrectFieldListener... listeners){
		this.donebutton = donebutton;
		this.listeners = listeners;
		for(CorrectFieldListener cfl: listeners)
			cfl.setCorectListener(this);
	}
	
	/**
	 * ����� �������� ����� ������������.
	 * ��������� ����� ������������ ���� ����������������� ���������� � �������� ��� ���������
	 * ������ �������������. ����� ���������� �� ���������� �����.
	 */
	public void checkCorrectness(){
		boolean total = true;
		for(CorrectFieldListener cfl: listeners)
			total = total && cfl.correctness;
		donebutton.setEnabled(total);
	}
	
	/**������ ������������� �����*/
	private JButton donebutton = null;
	/**������ ���������� �����, ������� ����� �������� ����� ��������*/
	private CorrectFieldListener[] listeners = null;

}