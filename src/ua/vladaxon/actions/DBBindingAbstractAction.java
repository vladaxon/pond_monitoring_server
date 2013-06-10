package ua.vladaxon.actions;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import ua.vladaxon.database.DBManager;

/**
 * Абстрактный класс действия, который предполагает связывание объекта действия
 * с менеджером БД.
 */
public abstract class DBBindingAbstractAction extends AbstractAction{

	/**
	 * Конструктор связывает строки наследников с предком {@link AbstractAction} методом putValue.
	 * @param name Имя действия
	 * @param shortdescription Краткое описание(всплывающая подсказка)
	 */
	public DBBindingAbstractAction(String name, String shortdescription, ImageIcon icon){
		super();
		this.putValue(NAME, name);
		this.putValue(SHORT_DESCRIPTION, shortdescription);
		this.putValue(SMALL_ICON, icon);
	}

	/**
	 * Метод, связывающий объект действий с менеджером БД.
	 * @param dbmanager Объект менеджера БД.
	 */
	public void bindDB(DBManager dbmanager){
		this.dbmanager = dbmanager;
	}
	
	/**Объект менеджера БД*/
	protected DBManager dbmanager = null;	
	private static final long serialVersionUID = 1L;
	
}