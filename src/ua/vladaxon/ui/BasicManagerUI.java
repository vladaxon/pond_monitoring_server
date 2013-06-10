package ua.vladaxon.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import ua.vladaxon.Server;
import ua.vladaxon.basicitem.BasicManager;
import ua.vladaxon.basicitem.ItemTableModel;

/**
 * Абстрактный класс, содержащий общие методы и данные, свойственны
 * для всех менеджеров записей.
 */
public abstract class BasicManagerUI {

	/**
	 * Создает объект окна и добавляет слушатель на закрытие(для вывода подтверждения).
	 * Создается таблица, модель к ней, и цветовой рендерер.
	 * @param frametitle Заголовок окна.
	 * @param manager Менеджер записей.
	 */
	public BasicManagerUI(String frametitle, BasicManager<?> manager){
		mainframe = new JFrame(frametitle);
		setWindowListener(manager);
		ItemTableModel tablemodel = new ItemTableModel((BasicManager<?>) manager);
		table = new JTable(tablemodel);
		table.setDefaultRenderer(Object.class, new ColoredTableCellRenderer(manager));
	}

	/**
	 * Метод, обновляющий таблицу с данными.
	 * Задача добавляется в очередь событий и выполняется
	 * из того потока.
	 */
	public void refreshTable(){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				table.clearSelection();
				table.revalidate();
				table.repaint();
			}
		});
	}
	
	/**
	 * Метод, отображающий окно пользовательского интерфейса.
	 */
	public void show(){
		mainframe.pack();
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
	}
	
	/**
	 * Метод, отображающий окно подтверждения выхода.
	 * Окно отображается в том случае, если данные, с которыми работает менеджер,
	 * были изменены.
	 * @return Ответ на диалоговое окно.
	 */
	public int trySave(){
		return JOptionPane.showConfirmDialog(mainframe, confirmsave);
	}
	
	/**
	 * Метод, отображающий окно с сообщением о проблеме загрузки записей из БД.
	 */
	public void loadProblem(){
		JOptionPane.showMessageDialog(mainframe, loadproblem);
	}
	
	/**
	 * Метод, закрывающий окно менеджера.
	 * Делает окно невидимым.
	 */
	public void closeUI(){
		mainframe.setVisible(false);
	}
	
	/**
	 * Метод, возвращающий объект фрейма, необходимый
	 * для создания диалогового окна создания новой записи.
	 * @return Объект фрейма.
	 */
	public JFrame getOwnerFrame(){
		return mainframe;
	}
	
	/**
	 * Метод, создающий подменю отображения колонок таблицы.
	 * Пункты меню зависят от менеджера записей, поэтому метод универсальный.
	 * Может использоватся при построении меню в классах наследниках.
	 * @param manager Менеджер записей, для которого нужно построить подменю.
	 * @return Объект подменю отображения колонок таблицы.
	 * @see ColumnsSubmenuListener
	 */
	protected JMenu columnSubmenu(BasicManager<?> manager){
		JMenu columnsmenu = new JMenu("Колонки");
		columnsmenu.setIcon(BasicManagerUI.colmicon);
		columnsmenu.setFont(menufont);
		ActionListener columnlistener = new ColumnsSubmenuListener(table);
		for(int i=0; i<manager.getColumnCount(); i++){
			JCheckBoxMenuItem colmenuitem = new JCheckBoxMenuItem(manager.getHeader(i));
			colmenuitem.setFont(menufont);
			colmenuitem.setSelected(true);
			colmenuitem.setActionCommand(i +"");
			colmenuitem.addActionListener(columnlistener);
			columnsmenu.add(colmenuitem);
		}
		return columnsmenu;
	}
	
	/**
	 * Метод, устанавливающий максимальную ширину столбца по ширине заголовка.
	 * Применяется когда длина заголовка явно больше длины текста в колонке.
	 * @param column Колонка, которая должна иметь фиксированную ширину
	 * @param gap Дополнительная ширина
	 */
	protected void setFixedWidthByHeader(TableColumn column, int gap){
		String header = column.getHeaderValue().toString();
		FontRenderContext fontrender = new FontRenderContext(new AffineTransform(), true, false);
		Rectangle2D rect = table.getFont().getStringBounds(header, fontrender);
		int size = (int) (gap + Math.round(rect.getWidth()));
		column.setMaxWidth(size);
		column.setPreferredWidth(size);
	}
	
	/**
	 * Метод, создающий слушатель окна для возможности подтверждения выхода
	 * при нажатии кнопки закрытия окна. Если данные были изменены - при выходе
	 * появится окно подтверждения сохранения данных или несохранения.
	 * @param manager Менеджер записей.
	 */
	protected void setWindowListener(final BasicManager<?> manager){
		WindowAdapter adapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				manager.closeManager();
			}
		};
		mainframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainframe.addWindowListener(adapter);
	}
	
	/**Объект таблицы*/
	protected JTable table = null;
	/**Окно менеджера*/
	protected JFrame mainframe = null;	
	/**Сообщение выводимое при подтверждении сохранения записей*/
	private static final String confirmsave = "Сохранить измененные данные?";
	/**Сообщение, выводимое при ошибке загрузки записей*/
	private static final String loadproblem = "Не удалось загрузить записи из БД";
	/**Пункт меню Управление, один объект строки для всех пунктов*/
	protected static final String controlmenu = "Управление";
	/**Пункт меню Вид, один объект строки для всех пунктов*/
	protected static final String viewmenu = "Вид";
	/**Иконка выхода*/
	protected static final ImageIcon exiticon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/cancel.png"));
	/**Иконка обновления*/
	protected static final ImageIcon updticon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/refresh.png"));
	/**Иконка удаления*/
	protected static final ImageIcon delticon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/delete.png"));
	/**Иконка колонок*/
	protected static final ImageIcon colmicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/columns.png"));
	/**Шрифт пунктов меню*/
	protected static final Font menufont = new Font("Arial", Font.PLAIN, 13);

}