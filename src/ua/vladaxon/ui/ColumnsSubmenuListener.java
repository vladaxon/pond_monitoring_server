package ua.vladaxon.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * ���������, ����������� ������������ ��� �������� ������� �������.
 */
public class ColumnsSubmenuListener implements ActionListener {
	
	/**
	 * ����������� �� ���������, ���������� ����������� ������� ������� � ����������� ������
	 * ��� �������� �������.
	 * @param table �������, ������� ����� ����������� ����������.
	 */
	public ColumnsSubmenuListener(JTable table){
		this.table = table;
		TableColumnModel columnmodel = table.getColumnModel();
		columns = new TableColumn[table.getColumnCount()];
		for(int i=0; i<table.getColumnCount(); i++)
			columns[i] = columnmodel.getColumn(i);
	}
	
	/**
	 * ����� ��������� � ���������� �������.
	 * � ����������� �� ���������� ������ ������� ��������������� �������.
	 * ����� ������� ������������ ����� ����� ������ {@link JCheckBoxMenuItem#getActionCommand()}
	 * ��� �������� ��������� �������� ������ �������.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JCheckBoxMenuItem chechbox = (JCheckBoxMenuItem) e.getSource();
		int columnindex = Integer.parseInt(chechbox.getActionCommand());
		if(chechbox.isSelected()){
			table.addColumn(columns[columnindex]);
			int columncount = table.getColumnCount();
			if(columnindex<columncount)
				table.moveColumn(columncount-1, columnindex);
		} else
			table.removeColumn(columns[columnindex]);
	}
	
	/**�������, � ������� �������� ���������*/
	private JTable table = null;
	/**������ �������, ��� �������� �������*/
	private TableColumn[] columns = null;

}