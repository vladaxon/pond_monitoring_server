package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ua.vladaxon.basicitem.BasicManager;

/**
 * Объект действия, отвечающий за обновление записей.
 * Имеет 2 конструктора: с параметрами имени и короткого описания и конструктор по умолчанию.
 * При приведении в действие вызывается метод обновления записей указанного менеджера записей.
 */
public class UpdateItemAction extends AbstractAction{

	/**
	 * Конструктор с указанным именем и коротким описанием.
	 * @param basicmanager Менеджер записей, необходим для вызова метода обновления.
	 * @param actionname Имя действия.
	 * @param shortdescrition Короткое описание действия.
	 */
	public UpdateItemAction(BasicManager<?> manager, String actionname, String shortdescrition){
		super();
		this.putValue(NAME, actionname);
		this.putValue(SHORT_DESCRIPTION, shortdescrition);
		this.manager = manager;
	}
	
	/**
	 * Конструктор по умолчанию. В качестве имени действия и короткого описания
	 * используются значения по умолчанию.
	 * @param basicmanager Менеджер записей, необходим для вызова метода обновления.
	 */
	public UpdateItemAction(BasicManager<?> manager){
		this(manager, defupdname, defshortdescr);
	}
	
	/**
	 * Действие обновления записей.
	 * @see BasicManager#updateItem(BasicManager)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		manager.updateItem();
	}
	
	/**Менеджер записей*/
	private BasicManager<?> manager = null;
	/**Имя действия по умолчанию*/
	private static final String defupdname = "Обновить записи";
	/**Короткое описание действия по умолчанию*/
	private static final String defshortdescr = "Заносит измененные записи в БД";
	private static final long serialVersionUID = 1L;
	
}