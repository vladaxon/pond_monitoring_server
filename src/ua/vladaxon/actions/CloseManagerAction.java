package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ua.vladaxon.basicitem.BasicManager;

/**
 * Объект действия, отвечающий за закрытие менеджера.
 * Имеет 2 конструктора: с параметрами имени и короткого описания и конструктор по умолчанию.
 * При приведении в действие вызывается метод закрытия менеджера.
 */
public class CloseManagerAction extends AbstractAction{

	/**
	 * Конструктор по умолчанию. В качестве имени действия и короткого описания
	 * используются значения по умолчанию.
	 * @param basicmanager Менеджер записей, необходим для вызова метода закрытия.
	 */
	public CloseManagerAction(BasicManager<?> manager){
		this(manager, defclosename, defshortdescr);
	}
	
	/**
	 * Конструктор с указанным именем и коротким описанием.
	 * @param basicmanager Менеджер записей, необходим для вызова метода закрытия.
	 * @param actionname Имя действия.
	 * @param shortdescrition Короткое описание действия.
	 */
	public CloseManagerAction(BasicManager<?> manager, String actionname, String shortdescrition){
		super();
		this.putValue(NAME, actionname);
		this.putValue(SHORT_DESCRIPTION, shortdescrition);
		this.manager = manager;
	}
	
	/**
	 * Действие закрытия менеджера.
	 * @see BasicManager#closeManager()
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		manager.closeManager();
	}
	
	/**Менеджер записей*/
	private BasicManager<?> manager = null;
	/**Имя действия по умолчанию*/
	private static final String defclosename = "Выход";
	/**Короткое описание действия по умолчанию*/
	private static final String defshortdescr = "Закрывает менеджер";
	private static final long serialVersionUID = 1L;
	
}