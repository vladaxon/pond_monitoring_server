package ua.vladaxon.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.basicitem.BasicManager;

/**
 * Класс, рендерера таблицы. От стандартного рендерера отличается возможностью
 * изменения фона ячейки в зависимости от состояния записи.
 */
public class ColoredTableCellRenderer extends JTextField implements TableCellRenderer {

	/**
	 * Конструктор рендерера.
	 * @param usermanager Менеджер записей, необходим для определения флага записи.
	 */
	public ColoredTableCellRenderer(BasicManager<?> basicmanager){
		this.basicmanager = basicmanager;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value!=null) {
			this.setBackground(this.getUserFlag(row, isSelected));
			this.setText(value.toString());
			this.setBorder(null);
		}
		return this;
	}
	
	/**
	 * Метод, определяющий фоновый цвет записи.
	 * Цвет зависит от состояния записи(добавлен, модифицирован, удален, нормальный) и
	 * выбрана ли данная запись в таблице.
	 * @param number Номер записи.
	 * @param isSelected Выбрана ли запись(строка) в таблице.
	 * @return Цвет фона для ячейки.
	 */
	private Color getUserFlag(int number, boolean isSelected){
		assert(basicmanager!=null);
		Flag flag = basicmanager.getFlag(number);
		Color colorflag = null;
		switch (flag) {
		case ADDED:{
			colorflag = added;
			break;
		}
		case DELETED:{
			colorflag = deleted;
			break;
		}
		case MODIFIED:{
			colorflag = modified;
			break;
		}
		default:{
			colorflag = normal;
			break;
		}
		}
		if(isSelected)
			return this.mixColors(colorflag, selected);
		return colorflag;
	}
	
	/**
	 * Метод, смешивающий 2 цвета. Смешивает цвет выбора с цветом фона.
	 * Таким образом даже при выделении можно заметить состояние записи(добавлена, модифицирована и т.д.)
	 * @param first Первый цвет.
	 * @param second Второй цвет.
	 * @return Средний цвет из двух указанных.
	 */
	public Color mixColors(Color first, Color second){
		Color result = null;
		int r = (first.getRed() + second.getRed())/2;
		int g = (first.getGreen() + second.getGreen())/2;
		int b = (first.getBlue() + second.getBlue())/2;
		result = new Color(r, g, b);
		return result;
	}
	
	/**Менеджер записей*/
	private BasicManager<?> basicmanager = null;
	/**Цвет фона добавленной записи*/
	public static final Color added = new Color(100, 255, 100);
	/**Цвет фона модифицированной записи*/
	public static final Color modified = new Color(255, 255, 100);
	/**Цвет фона удаленной записи*/
	public static final Color deleted = new Color(255, 100, 100);
	/**Цвет фона выбранной записи*/
	public static final Color selected = new Color(180, 180, 255);
	/**Цвет фона по умолчанию*/
	public static final Color normal = Color.WHITE;
	private static final long serialVersionUID = 1L;

}