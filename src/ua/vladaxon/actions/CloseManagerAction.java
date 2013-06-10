package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ua.vladaxon.basicitem.BasicManager;

/**
 * ������ ��������, ���������� �� �������� ���������.
 * ����� 2 ������������: � ����������� ����� � ��������� �������� � ����������� �� ���������.
 * ��� ���������� � �������� ���������� ����� �������� ���������.
 */
public class CloseManagerAction extends AbstractAction{

	/**
	 * ����������� �� ���������. � �������� ����� �������� � ��������� ��������
	 * ������������ �������� �� ���������.
	 * @param basicmanager �������� �������, ��������� ��� ������ ������ ��������.
	 */
	public CloseManagerAction(BasicManager<?> manager){
		this(manager, defclosename, defshortdescr);
	}
	
	/**
	 * ����������� � ��������� ������ � �������� ���������.
	 * @param basicmanager �������� �������, ��������� ��� ������ ������ ��������.
	 * @param actionname ��� ��������.
	 * @param shortdescrition �������� �������� ��������.
	 */
	public CloseManagerAction(BasicManager<?> manager, String actionname, String shortdescrition){
		super();
		this.putValue(NAME, actionname);
		this.putValue(SHORT_DESCRIPTION, shortdescrition);
		this.manager = manager;
	}
	
	/**
	 * �������� �������� ���������.
	 * @see BasicManager#closeManager()
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		manager.closeManager();
	}
	
	/**�������� �������*/
	private BasicManager<?> manager = null;
	/**��� �������� �� ���������*/
	private static final String defclosename = "�����";
	/**�������� �������� �������� �� ���������*/
	private static final String defshortdescr = "��������� ��������";
	private static final long serialVersionUID = 1L;
	
}