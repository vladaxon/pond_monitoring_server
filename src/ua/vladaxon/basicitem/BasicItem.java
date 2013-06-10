package ua.vladaxon.basicitem;

import ua.vladaxon.database.Monitor;
import ua.vladaxon.database.Pond;
import ua.vladaxon.database.User;

/**
 * Абстрактный, базовый класс, содержащий в себе флаг записи, абстрактные методы для модифицирования
 * и взятия данных, методы для модели таблицы.
 * @see User
 * @see Pond
 * @see Monitor
 */
public abstract class BasicItem {
	
	/**
	 * Возвращает значение указанного поля. Наследники реализуют этот метод
	 * в зависимости от данных с которыми они работают.
	 * @param field Номер поля.
	 * @return Значение поля.
	 */
	public abstract Object getValue(int field);
	
	/**
	 * Метод изменения поля объекта. В зависимости от поля значение может изменится или нет.
	 * Наследники реализуют этот метод в зависимости от данных, с которыми они работают.
	 * @param field Номер поля.
	 * @param value Новое значение.
	 * @return true - если данные были изменены. false - если новое значение совпадает со старым.
	 */
	public abstract boolean modifyValue(int field, Object value);
	
	/**
	 * Метод, формирующий запрос для БД, в зависимости от состояния объекта.
	 * Запрос необходим для добавления, изменения, удаления записи из БД.
	 * Запрос формируется в соответствии с флагом объекта.
	 * @return Запрос для БД.
	 */
	public abstract String getQuery();
	

	/**
	 * Возвращает флаг записи.
	 * @return Флаг записи.
	 */
	public Flag getFlag() {
		return flag;
	}

	/**
	 * Устанавливает флаг записи.
	 * @param flag Флаг записи.
	 */
	public void setFlag(Flag flag) {
		this.flag = flag;
	}
	
	/**Флаг записи*/
	protected Flag flag = Flag.NORMAL;
	/**Нумерация флагов*/
	public static enum Flag {NORMAL, ADDED, MODIFIED, DELETED};

}