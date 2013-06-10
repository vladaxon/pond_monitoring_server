package ua.vladaxon.basicitem;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.Monitor;
import ua.vladaxon.database.Pond;
import ua.vladaxon.database.User;
import ua.vladaxon.ui.BasicManagerUI;

/**
 * Абстрактный класс менеджера записей. Позволяет управлять записями универсального типа.
 * Данный класс служит как супекласс для менеджеров пользователями, ставками, мониторинга.
 * Класс имеет общие для всех менеджеров методы добавление, удаление и других общих методов.
 * @param <T> Универсальный тип, может быть следующим: {@link User}, {@link Pond}, {@link Monitor}
 */
public abstract class BasicManager<T extends BasicItem>{
	
	/**
	 * Конструктор абстрактного класса.
	 * @param dbmanager Менеджер БД.
	 * @param logger Логгер.
	 */
	public BasicManager(DBManager dbmanager, ServerLogger logger){
		this.dbmanager = dbmanager;
		this.logger = logger;
	}
	
	/**
	 * Открывает менеджер управления записями.
	 * Удаляет списки, производит запрос на получение нового списка из БД, отображает интерфейс.
	 */
	public void openManager(){
		if(itemlist.isEmpty()){
			changed = false;
			this.loadItem();			
		}
		ui.show();
	}
	
	/**
	 * Метод, загружающий записи из БД.
	 */
	public void loadItem(){
		dbmanager.loadItems(this);
	}
	
	/**
	 * Метод, обновляющий записи с БД.
	 * Сохраняет измененные записи и загружает новый список.
	 */
	public void updateItem(){
		dbmanager.updateItems(this);
	}
	
	/**
	 * Метод, сохраняющий записи в БД.
	 */
	public void saveItem(){
		dbmanager.saveItems(this);
	}
	
	/**
	 * Метод очистки списка. Используется для достижения целостности данных.
	 * Если данные были изменены пользователем и при сохранении в БД произошла ошибка - данные
	 * останутся, если сохранение прошло успешно - список очищается с помощью этого метода.
	 */
	public void clearItem(){
		itemlist.clear();
	}
	
	/**
	 * Метод установки списка записей для менеджера.
	 * Этот метод асинхронно вызывается из менеджера БД и устанавливает новый список.
	 * После чего вызывает метод обновления таблицы интерфейса менеджера.
	 * @param items Список записей.
	 * @see DBManager#loadUser(UserManager)
	 */
	public void loadItems(List<T> items){
		if(items!=null){
			this.itemlist = items;
			changed = false;
			ui.refreshTable();
		} else {
			ui.refreshTable();
			ui.loadProblem();
			changed = false;
		}
	}
	
	/**
	 * Возвращает количество записей. Необходим для модели таблицы.
	 * @return Количество записей.
	 */
	public int getRowCount(){
		return itemlist.size();
	}
	
	/**
	 * Возвращает список записей.
	 * Метод необходим менеджеру БД для обновления записей.
	 * @return Список записей.
	 */
	public List<T> getItems(){
		return itemlist;
	}
	
	/**
	 * Метод, возвращающий количество колонок для модели таблицы.
	 * Наследники класса делегируют метод объекта, с которым работают, через реализацию этого метода.
	 * @return Количество колонок таблицы.
	 */
	public abstract int getColumnCount();
	
	/**
	 * Метод, возвращающий заголовки для таблицы.
	 * Наследники класса делегируют метод объекта, с которым работают, через реализацию этого метода.
	 * @param columnIndex Номер столбца.
	 * @return Заголовок столбца таблицы.
	 */
	public abstract String getHeader(int columnIndex);
	
	/**
	 * Метод, возвращающий флаг возможности редактирования данного столбца.
	 * Наследники класса делегируют метод объекта, с которым работают, через реализацию этого метода.
	 * @param columnIndex Номер столбца.
	 * @return true - если редактирование разрешено.
	 */
	public abstract boolean isEditable(int columnIndex);
	
	/**
	 * Возвращает запись за указанным номером.
	 * Метод необходим для модели таблицы.
	 * @param rownumber Номер записи(совпадает с номером строки)
	 * @return Объект записи(тип зависит от менеджера наследника этого класса)
	 */
	public T getItem(int rownumber){
		if(rownumber<itemlist.size())
			return itemlist.get(rownumber);
		else
			return null;
	}
	
	/**
	 * Возвращает флаг указанной записи. Флаг значит состояние записи и используется
	 * для подсвечивания записи определенным цветом, в зависимости от состояния.
	 * @param rownumber Номер записи.
	 * @return Флаг записи.
	 */
	public Flag getFlag(int rownumber){
		return itemlist.get(rownumber).getFlag();
	}
	
	/**
	 * Метод, запускающий процесс создания новой записи.
	 * Наследники в конструкторе должны создать экземпляр диалога
	 * создания новой записи в зависимости от данных, с которыми
	 * они работают.
	 */
	public void createItem() {
		if(newitem.showDialog())
			addItem(newitem.getItem());
	}
	
	/**
	 * Добавляет в список новую запись, тип которой зависит от класса наследника.
	 * @param newitem Новая запись.
	 */
	public void addItem(T newitem){
		itemlist.add(newitem);
		changed = true;
		ui.refreshTable();
	}
	
	/**
	 * Метод модификации записи из таблицы. Вычисляет необходимую строку и передает
	 * новое значение и номер поля объекту, который нужно изменить. Если объект изменил
	 * своё значение поднимается флаг изменения. Объект может отменить изменение, если
	 * новое значение эквивалентно старому.
	 * @param number Номер записи в списке.
	 * @param field Номер поля в записи.
	 * @param value Новое значение.
	 */
	public void modifyItem(int number, int field, Object value) {
		changed |= itemlist.get(number).modifyValue(field, value);
		ui.refreshTable();
	}
	
	/**
	 * Метод, удаляющий запись указанного номера.
	 * Если запись была загружена из БД - она помечается на удаление.
	 * Если запись была создана и отсутствует в БД - удаляется сразу.
	 * Помеченные на удаления записи будут удалены из БД при синхронизации.
	 * @param number Номер записи, подлежащей удалению.
	 */
	public void deleteItem(int number){
		T item = itemlist.get(number);
		if(item.getFlag()==Flag.NORMAL)
			item.setFlag(Flag.DELETED);
		else
			itemlist.remove(number);
		changed = true;
		ui.refreshTable();
	}
	
	/**
	 * Метод закрытия менеджера записей. Если в процессе работы данные были изменены
	 * менеджер выводит окно с предложением сохранить измененные записи. Если пользователь
	 * выбирает "Да" - менеджер закрывает окно и запускает асинхронную задачу на сохранение записей.
	 * Если пользователь выбирает "Нет" - менеджер закрывает окно и сразу обнуляет списки.
	 * Если пользователь нажимает "Отмена" - работа менеджера продолжается. Если в процессе работы
	 * данные не были изменены - окно подтверждения не появляется.
	 */
	public void closeManager(){
		if(!changed){
			ui.closeUI();
			itemlist.clear();
		} else {
			int result = ui.trySave();
			switch (result) {
			case JOptionPane.YES_OPTION:{
				ui.closeUI();
				this.saveItem();
				break;				
			}
			case JOptionPane.NO_OPTION:{
				ui.closeUI();
				itemlist.clear();
				break;
			}
			default:
				return;
			}
		}		
	}
	
	/**Объект менеджера БД*/
	protected DBManager dbmanager = null;
	/**Объект логгера*/
	protected ServerLogger logger = null;
	/**Список записей универсального типа*/
	protected List<T> itemlist = new ArrayList<T>();
	/**Абстрактный класс пользовательского интерфейса пользователя*/
	protected BasicManagerUI ui = null;
	/**Объект диалога создания новой записи*/
	protected NewItemDialog<T> newitem = null;
	/**Флаг показывающий произошли ли какие-либо изменения с открытия*/
	protected boolean changed = false;

}