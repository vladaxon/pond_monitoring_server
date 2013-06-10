package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.basicitem.BasicManager;

/**
 * Объект действия, отвечающий за создание новой записи. Имеет перегруженный конструктор.
 * При вызове действия - вызывается метод cоздания новой записи у менеджера записей.
 */
public class NewItemAction extends AbstractAction{
	
	/**
	 * Конструктор с указанными параметрами.
	 * @param manager Менеджер записей
	 * @param name Имя действия
	 * @param descr Описание действия
	 * @param icon Иконка действия
	 */
	public NewItemAction(BasicManager<?> manager, String name, String descr, ImageIcon icon){
		super();
		this.putValue(NAME, name);
		this.putValue(SHORT_DESCRIPTION, descr);
		this.putValue(SMALL_ICON, icon);
		this.manager = manager;
	}
	
	/**
	 * Конструктор с иконкой по умолчанию.
	 * @param manager Менеджер записей
	 * @param name Имя действия
	 * @param descr Описание действия
	 */
	public NewItemAction(BasicManager<?> manager, String name, String descr){
		this(manager, name, descr, newitemicon);
	}
	
	/**
	 * Конструктор со значениями по умолчанию.
	 * @param manager Менеджер записей
	 */
	public NewItemAction(BasicManager<?> manager){
		this(manager, defname, defdescription, newitemicon);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		manager.createItem();
	}
	
	/**Объект менеджера записей*/
	private BasicManager<?> manager = null;
	/**Имя действия по умолчанию*/
	private static final String defname = "Новая запись";
	/**Описание действия по умолчанию*/
	private static final String defdescription = "Отображает окно диалога создания новой записи";
	/**Иконка действия по умолчанию*/
	public static final ImageIcon newitemicon =
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/add.png"));
	private static final long serialVersionUID = 1L;

}