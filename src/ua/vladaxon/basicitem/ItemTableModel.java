package ua.vladaxon.basicitem;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ItemTableModel implements TableModel{

	private BasicManager<?> manager = null;
	
	public ItemTableModel(BasicManager<?> manager){
		this.manager = manager;
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return manager.getColumnCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return manager.getHeader(columnIndex);
	}

	@Override
	public int getRowCount() {
		return manager.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		BasicItem item = manager.getItem(rowIndex);
		if(item!=null)
			return item.getValue(columnIndex);
		else
			return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return manager.isEditable(columnIndex);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		manager.modifyItem(rowIndex, columnIndex, aValue);
	}

}