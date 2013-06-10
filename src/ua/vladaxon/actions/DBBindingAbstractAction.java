package ua.vladaxon.actions;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import ua.vladaxon.database.DBManager;

/**
 * ����������� ����� ��������, ������� ������������ ���������� ������� ��������
 * � ���������� ��.
 */
public abstract class DBBindingAbstractAction extends AbstractAction{

	/**
	 * ����������� ��������� ������ ����������� � ������� {@link AbstractAction} ������� putValue.
	 * @param name ��� ��������
	 * @param shortdescription ������� ��������(����������� ���������)
	 */
	public DBBindingAbstractAction(String name, String shortdescription, ImageIcon icon){
		super();
		this.putValue(NAME, name);
		this.putValue(SHORT_DESCRIPTION, shortdescription);
		this.putValue(SMALL_ICON, icon);
	}

	/**
	 * �����, ����������� ������ �������� � ���������� ��.
	 * @param dbmanager ������ ��������� ��.
	 */
	public void bindDB(DBManager dbmanager){
		this.dbmanager = dbmanager;
	}
	
	/**������ ��������� ��*/
	protected DBManager dbmanager = null;	
	private static final long serialVersionUID = 1L;
	
}