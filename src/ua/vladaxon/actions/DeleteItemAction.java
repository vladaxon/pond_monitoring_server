package ua.vladaxon.actions;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import ua.vladaxon.basicitem.BasicManager;

/**
 * Объект действия, отвечающий за удаление записей.
 * Имеет 2 конструктора: с параметрами имени и короткого описания и конструктор по умолчанию.
 * При приведении в действие вызывается окно подтверждение и после метод удаления 
 * записей указанного менеджера записей.
 */
public class DeleteItemAction extends AbstractAction {

	/**
	 * Конструктор по умолчанию. В качестве имени действия и короткого описания
	 * используются значения по умолчанию.
	 * @param manager Менеджер записей.
	 * @param owner Фрейм владелец диалогового окна подтверждения.
	 * @param table Таблица с записями.
	 */
	public DeleteItemAction(BasicManager<?> manager, JFrame owner, JTable table){
		this(manager, owner, table, defdelname, defshortdescr);
	}
	
	/**
	 * Конструктор с указанным именем и коротким описанием.
	 * @param manager Менеджер записей.
	 * @param owner Фрейм владелец диалогового окна подтверждения.
	 * @param table Таблица с записями.
	 * @param name Имя действия.
	 * @param shortdescription Короткое описание действия.
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
	 * Действие удаления записей.
	 * Если не выбрана ни одна запись - выводится сообщение с ошибкой.
	 * Если выбрана одна или несколько записей - они удаляются.
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
	
	/**Фрейм владелец диалогового окна*/
	private JFrame owner = null;
	/**Менеджер записей*/
	private BasicManager<?> manager = null;
	/**Таблица записей*/
	private JTable table = null;	
	/**Имя действия по умолчанию*/
	private static final String defdelname = "Удалить запись";
	/**Короткое описание по умолчанию*/
	private static final String defshortdescr = "Удаляет выделенную запись";
	/**Сообщение выводимое, если ни одна запись не выделена*/
	private static final String selectitems = "Не выбрана ни одна запись!";
	/**Шаблон подтверждение удаления записей...*/
	private static final String confirmmsg = 
			"Вы действительно хотите удалить запис{0,choice,0#|1#ь|2#и}? ({0})";
	private static final long serialVersionUID = 1L;

}