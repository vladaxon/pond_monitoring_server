package ua.vladaxon.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Слушатель, управляющий отображением или скрытием колонок таблицы.
 */
public class ColumnsSubmenuListener implements ActionListener {
	
	/**
	 * Конструктор по умолчанию, производит кэширование колонок таблицы в собственный массив
	 * для быстрого доступа.
	 * @param table Таблица, которая будет управляться слушателем.
	 */
	public ColumnsSubmenuListener(JTable table){
		this.table = table;
		TableColumnModel columnmodel = table.getColumnModel();
		columns = new TableColumn[table.getColumnCount()];
		for(int i=0; i<table.getColumnCount(); i++)
			columns[i] = columnmodel.getColumn(i);
	}
	
	/**
	 * Метод включения и отключения колонок.
	 * В зависимости от вызвавшего флажка убирает соответствующую колонку.
	 * Номер колонки определяется через вызов метода {@link JCheckBoxMenuItem#getActionCommand()}
	 * где хранится строковое значение номера колонки.
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
	
	/**Таблица, с которой работает слушатель*/
	private JTable table = null;
	/**Массив колонок, для быстрого доступа*/
	private TableColumn[] columns = null;

}