package ua.vladaxon.connections;

/**
 * Перечисление типов запросов. Предназначен для разбора и выполнения
 * запросов от клиента.
 */
public enum RequestType {
	
	POND,MONITOR,UPDATE,AUTHORIZATION,WRONG,NONE;
	
	/**
	 * Возвращает элемент по имени или WRONG если нет такого объекта.
	 * @param name Имя объекта для поиска.
	 * @return Объект, соответствующий имени или WRONG в противном случае.
	 */
	public static RequestType getByName(String name){
		try{
			return valueOf(name);
		} catch (IllegalArgumentException e) {
			return WRONG;
		}
	}
	
}