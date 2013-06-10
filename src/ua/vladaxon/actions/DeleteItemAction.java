package ua.vladaxon.actions;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import ua.vladaxon.basicitem.BasicManager;

/**
 * ������ ��������, ���������� �� �������� �������.
 * ����� 2 ������������: � ����������� ����� � ��������� �������� � ����������� �� ���������.
 * ��� ���������� � �������� ���������� ���� ������������� � ����� ����� �������� 
 * ������� ���������� ��������� �������.
 */
public class DeleteItemAction extends AbstractAction {

	/**
	 * ����������� �� ���������. � �������� ����� �������� � ��������� ��������
	 * ������������ �������� �� ���������.
	 * @param manager �������� �������.
	 * @param owner ����� �������� ����������� ���� �������������.
	 * @param table ������� � ��������.
	 */
	public DeleteItemAction(BasicManager<?> manager, JFrame owner, JTable table){
		this(manager, owner, table, defdelname, defshortdescr);
	}
	
	/**
	 * ����������� � ��������� ������ � �������� ���������.
	 * @param manager �������� �������.
	 * @param owner ����� �������� ����������� ���� �������������.
	 * @param table ������� � ��������.
	 * @param name ��� ��������.
	 * @param shortdescription �������� �������� ��������.
	 */
	public DeleteItemAction(BasicManager<?> manager, JFrame owner, 
			JTable table, String name, String shortdescription){
		super();
		this.putValue(NAME, name);
		this.putValue(SHORT_DESCRIPTION, shortdescription);
		this.manager = manager;
		this.owner = owner;
		this.table = table;
	}
	
	/**
	 * �������� �������� �������.
	 * ���� �� ������� �� ���� ������ - ��������� ��������� � �������.
	 * ���� ������� ���� ��� ��������� ������� - ��� ���������.
	 * @see BasicManager#deleteItem(int)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int[] selecteditems = table.getSelectedRows();
		if(selecteditems.length==0)
			JOptionPane.showMessageDialog(owner, selectitems);
		else {
			int result = JOptionPane.showConfirmDialog
					(owner, MessageFormat.format(confirmmsg, selecteditems.length));
			if(result==JOptionPane.YES_OPTION){
				for(int number: selecteditems){
					manager.deleteItem(number);
				}
			}
		}
		table.clearSelection();
	}
	
	/**����� �������� ����������� ����*/
	private JFrame owner = null;
	/**�������� �������*/
	private BasicManager<?> manager = null;
	/**������� �������*/
	private JTable table = null;	
	/**��� �������� �� ���������*/
	private static final String defdelname = "������� ������";
	/**�������� �������� �� ���������*/
	private static final String defshortdescr = "������� ���������� ������";
	/**��������� ���������, ���� �� ���� ������ �� ��������*/
	private static final String selectitems = "�� ������� �� ���� ������!";
	/**������ ������������� �������� �������...*/
	private static final String confirmmsg = 
			"�� ������������� ������ ������� �����{0,choice,0#|1#�|2#�}? ({0})";
	private static final long serialVersionUID = 1L;

}