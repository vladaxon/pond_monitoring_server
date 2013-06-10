package ua.vladaxon.basicitem;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.Monitor;
import ua.vladaxon.database.Pond;
import ua.vladaxon.database.User;
import ua.vladaxon.ui.BasicManagerUI;

/**
 * ����������� ����� ��������� �������. ��������� ��������� �������� �������������� ����.
 * ������ ����� ������ ��� ��������� ��� ���������� ��������������, ��������, �����������.
 * ����� ����� ����� ��� ���� ���������� ������ ����������, �������� � ������ ����� �������.
 * @param <T> ������������� ���, ����� ���� ���������: {@link User}, {@link Pond}, {@link Monitor}
 */
public abstract class BasicManager<T extends BasicItem>{
	
	/**
	 * ����������� ������������ ������.
	 * @param dbmanager �������� ��.
	 * @param logger ������.
	 */
	public BasicManager(DBManager dbmanager, ServerLogger logger){
		this.dbmanager = dbmanager;
		this.logger = logger;
	}
	
	/**
	 * ��������� �������� ���������� ��������.
	 * ������� ������, ���������� ������ �� ��������� ������ ������ �� ��, ���������� ���������.
	 */
	public void openManager(){
		if(itemlist.isEmpty()){
			changed = false;
			this.loadItem();			
		}
		ui.show();
	}
	
	/**
	 * �����, ����������� ������ �� ��.
	 */
	public void loadItem(){
		dbmanager.loadItems(this);
	}
	
	/**
	 * �����, ����������� ������ � ��.
	 * ��������� ���������� ������ � ��������� ����� ������.
	 */
	public void updateItem(){
		dbmanager.updateItems(this);
	}
	
	/**
	 * �����, ����������� ������ � ��.
	 */
	public void saveItem(){
		dbmanager.saveItems(this);
	}
	
	/**
	 * ����� ������� ������. ������������ ��� ���������� ����������� ������.
	 * ���� ������ ���� �������� ������������� � ��� ���������� � �� ��������� ������ - ������
	 * ���������, ���� ���������� ������ ������� - ������ ��������� � ������� ����� ������.
	 */
	public void clearItem(){
		itemlist.clear();
	}
	
	/**
	 * ����� ��������� ������ ������� ��� ���������.
	 * ���� ����� ���������� ���������� �� ��������� �� � ������������� ����� ������.
	 * ����� ���� �������� ����� ���������� ������� ���������� ���������.
	 * @param items ������ �������.
	 * @see DBManager#loadUser(UserManager)
	 */
	public void loadItems(List<T> items){
		if(items!=null){
			this.itemlist = items;
			changed = false;
			ui.refreshTable();
		} else {
			ui.refreshTable();
			ui.loadProblem();
			changed = false;
		}
	}
	
	/**
	 * ���������� ���������� �������. ��������� ��� ������ �������.
	 * @return ���������� �������.
	 */
	public int getRowCount(){
		return itemlist.size();
	}
	
	/**
	 * ���������� ������ �������.
	 * ����� ��������� ��������� �� ��� ���������� �������.
	 * @return ������ �������.
	 */
	public List<T> getItems(){
		return itemlist;
	}
	
	/**
	 * �����, ������������ ���������� ������� ��� ������ �������.
	 * ���������� ������ ���������� ����� �������, � ������� ��������, ����� ���������� ����� ������.
	 * @return ���������� ������� �������.
	 */
	public abstract int getColumnCount();
	
	/**
	 * �����, ������������ ��������� ��� �������.
	 * ���������� ������ ���������� ����� �������, � ������� ��������, ����� ���������� ����� ������.
	 * @param columnIndex ����� �������.
	 * @return ��������� ������� �������.
	 */
	public abstract String getHeader(int columnIndex);
	
	/**
	 * �����, ������������ ���� ����������� �������������� ������� �������.
	 * ���������� ������ ���������� ����� �������, � ������� ��������, ����� ���������� ����� ������.
	 * @param columnIndex ����� �������.
	 * @return true - ���� �������������� ���������.
	 */
	public abstract boolean isEditable(int columnIndex);
	
	/**
	 * ���������� ������ �� ��������� �������.
	 * ����� ��������� ��� ������ �������.
	 * @param rownumber ����� ������(��������� � ������� ������)
	 * @return ������ ������(��� ������� �� ��������� ���������� ����� ������)
	 */
	public T getItem(int rownumber){
		if(rownumber<itemlist.size())
			return itemlist.get(rownumber);
		else
			return null;
	}
	
	/**
	 * ���������� ���� ��������� ������. ���� ������ ��������� ������ � ������������
	 * ��� ������������� ������ ������������ ������, � ����������� �� ���������.
	 * @param rownumber ����� ������.
	 * @return ���� ������.
	 */
	public Flag getFlag(int rownumber){
		return itemlist.get(rownumber).getFlag();
	}
	
	/**
	 * �����, ����������� ������� �������� ����� ������.
	 * ���������� � ������������ ������ ������� ��������� �������
	 * �������� ����� ������ � ����������� �� ������, � ��������
	 * ��� ��������.
	 */
	public void createItem() {
		if(newitem.showDialog())
			addItem(newitem.getItem());
	}
	
	/**
	 * ��������� � ������ ����� ������, ��� ������� ������� �� ������ ����������.
	 * @param newitem ����� ������.
	 */
	public void addItem(T newitem){
		itemlist.add(newitem);
		changed = true;
		ui.refreshTable();
	}
	
	/**
	 * ����� ����������� ������ �� �������. ��������� ����������� ������ � ��������
	 * ����� �������� � ����� ���� �������, ������� ����� ��������. ���� ������ �������
	 * ��� �������� ����������� ���� ���������. ������ ����� �������� ���������, ����
	 * ����� �������� ������������ �������.
	 * @param number ����� ������ � ������.
	 * @param field ����� ���� � ������.
	 * @param value ����� ��������.
	 */
	public void modifyItem(int number, int field, Object value) {
		changed |= itemlist.get(number).modifyValue(field, value);
		ui.refreshTable();
	}
	
	/**
	 * �����, ��������� ������ ���������� ������.
	 * ���� ������ ���� ��������� �� �� - ��� ���������� �� ��������.
	 * ���� ������ ���� ������� � ����������� � �� - ��������� �����.
	 * ���������� �� �������� ������ ����� ������� �� �� ��� �������������.
	 * @param number ����� ������, ���������� ��������.
	 */
	public void deleteItem(int number){
		T item = itemlist.get(number);
		if(item.getFlag()==Flag.NORMAL)
			item.setFlag(Flag.DELETED);
		else
			itemlist.remove(number);
		changed = true;
		ui.refreshTable();
	}
	
	/**
	 * ����� �������� ��������� �������. ���� � �������� ������ ������ ���� ��������
	 * �������� ������� ���� � ������������ ��������� ���������� ������. ���� ������������
	 * �������� "��" - �������� ��������� ���� � ��������� ����������� ������ �� ���������� �������.
	 * ���� ������������ �������� "���" - �������� ��������� ���� � ����� �������� ������.
	 * ���� ������������ �������� "������" - ������ ��������� ������������. ���� � �������� ������
	 * ������ �� ���� �������� - ���� ������������� �� ����������.
	 */
	public void closeManager(){
		if(!changed){
			ui.closeUI();
			itemlist.clear();
		} else {
			int result = ui.trySave();
			switch (result) {
			case JOptionPane.YES_OPTION:{
				ui.closeUI();
				this.saveItem();
				break;				
			}
			case JOptionPane.NO_OPTION:{
				ui.closeUI();
				itemlist.clear();
				break;
			}
			default:
				return;
			}
		}		
	}
	
	/**������ ��������� ��*/
	protected DBManager dbmanager = null;
	/**������ �������*/
	protected ServerLogger logger = null;
	/**������ ������� �������������� ����*/
	protected List<T> itemlist = new ArrayList<T>();
	/**����������� ����� ����������������� ���������� ������������*/
	protected BasicManagerUI ui = null;
	/**������ ������� �������� ����� ������*/
	protected NewItemDialog<T> newitem = null;
	/**���� ������������ ��������� �� �����-���� ��������� � ��������*/
	protected boolean changed = false;

}