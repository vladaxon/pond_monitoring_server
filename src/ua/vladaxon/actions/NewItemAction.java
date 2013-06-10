package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.basicitem.BasicManager;

/**
 * ������ ��������, ���������� �� �������� ����� ������. ����� ������������� �����������.
 * ��� ������ �������� - ���������� ����� c������� ����� ������ � ��������� �������.
 */
public class NewItemAction extends AbstractAction{
	
	/**
	 * ����������� � ���������� �����������.
	 * @param manager �������� �������
	 * @param name ��� ��������
	 * @param descr �������� ��������
	 * @param icon ������ ��������
	 */
	public NewItemAction(BasicManager<?> manager, String name, String descr, ImageIcon icon){
		super();
		this.putValue(NAME, name);
		this.putValue(SHORT_DESCRIPTION, descr);
		this.putValue(SMALL_ICON, icon);
		this.manager = manager;
	}
	
	/**
	 * ����������� � ������� �� ���������.
	 * @param manager �������� �������
	 * @param name ��� ��������
	 * @param descr �������� ��������
	 */
	public NewItemAction(BasicManager<?> manager, String name, String descr){
		this(manager, name, descr, newitemicon);
	}
	
	/**
	 * ����������� �� ���������� �� ���������.
	 * @param manager �������� �������
	 */
	public NewItemAction(BasicManager<?> manager){
		this(manager, defname, defdescription, newitemicon);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		manager.createItem();
	}
	
	/**������ ��������� �������*/
	private BasicManager<?> manager = null;
	/**��� �������� �� ���������*/
	private static final String defname = "����� ������";
	/**�������� �������� �� ���������*/
	private static final String defdescription = "���������� ���� ������� �������� ����� ������";
	/**������ �������� �� ���������*/
	public static final ImageIcon newitemicon =
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/add.png"));
	private static final long serialVersionUID = 1L;

}