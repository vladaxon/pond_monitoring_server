package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ua.vladaxon.basicitem.BasicManager;

/**
 * ������ ��������, ���������� �� ���������� �������.
 * ����� 2 ������������: � ����������� ����� � ��������� �������� � ����������� �� ���������.
 * ��� ���������� � �������� ���������� ����� ���������� ������� ���������� ��������� �������.
 */
public class UpdateItemAction extends AbstractAction{

	/**
	 * ����������� � ��������� ������ � �������� ���������.
	 * @param basicmanager �������� �������, ��������� ��� ������ ������ ����������.
	 * @param actionname ��� ��������.
	 * @param shortdescrition �������� �������� ��������.
	 */
	public UpdateItemAction(BasicManager<?> manager, String actionname, String shortdescrition){
		super();
		this.putValue(NAME, actionname);
		this.putValue(SHORT_DESCRIPTION, shortdescrition);
		this.manager = manager;
	}
	
	/**
	 * ����������� �� ���������. � �������� ����� �������� � ��������� ��������
	 * ������������ �������� �� ���������.
	 * @param basicmanager �������� �������, ��������� ��� ������ ������ ����������.
	 */
	public UpdateItemAction(BasicManager<?> manager){
		this(manager, defupdname, defshortdescr);
	}
	
	/**
	 * �������� ���������� �������.
	 * @see BasicManager#updateItem(BasicManager)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		manager.updateItem();
	}
	
	/**�������� �������*/
	private BasicManager<?> manager = null;
	/**��� �������� �� ���������*/
	private static final String defupdname = "�������� ������";
	/**�������� �������� �������� �� ���������*/
	private static final String defshortdescr = "������� ���������� ������ � ��";
	private static final long serialVersionUID = 1L;
	
}