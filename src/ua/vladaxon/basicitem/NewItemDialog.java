package ua.vladaxon.basicitem;

/**
 * Интерфейс диалогов создания новых записей.
 * Предоставляет методы для запуска диалога и получения результата.
 * @param <T> Объект, с которым работает реализация интерфейса.
 */
public interface NewItemDialog<T extends BasicItem> {
	
	/**
	 * Метод, вызывающий отображение диалогового окна.
	 * @return true - если диалог завершился успешно.
	 */
	public boolean showDialog();
	
	/**
	 * Возвращает созданный объект записи.
	 * @return Созданный объект.
	 */
	public T getItem();

}