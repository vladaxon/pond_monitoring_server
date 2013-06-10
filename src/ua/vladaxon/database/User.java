package ua.vladaxon.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ua.vladaxon.basicitem.BasicItem;

/**
 * Класс, объект которого отражает сущность пользователя.
 * Имеет поля логина и пароля для входа в систему.
 * Имеет поля имени и телефона который можно изменять.
 * Каждый пользователь должен быть привязан к ставку, поэт
 * Объект имеет специальный флаг, обозначающий состояние этого объекта.
 * Флаг необходим для менеджера БД чтобы синхронизировать измененный список с БД.
 */
public class User extends BasicItem{
	
	/**
	 * Конструктор объекта пользователя из указанных полей.
	 * @param login Логин пользователя
	 * @param password Захэшированный пароль
	 * @param uname Имя пользователя
	 * @param telephone Телефон пользователя
	 * @param pond Номер ставка, к которому привязан пользователь
	 * @param flag Флаг объекта
	 */
	public User(String login, int password, String uname, String telephone, int pond, Flag flag) {
		this.login = login;
		this.password = password;
		this.uname = uname;
		this.telephone = telephone;
		this.pond = pond;
		this.flag = flag;
	}
	
	/**
	 * Конструктор объекта пользователя из записи БД.
	 * Конструктор достает последовательно все поля и присваивает их объекту.
	 * Флаг объекта устанавливается как нормальный.
	 * @param result Запись БД
	 * @throws SQLException Если произошло исключение при обработке записи
	 */
	public User(ResultSet result) throws SQLException{
		login = result.getString("login");
		password = result.getInt("passw");
		uname = result.getString("uname");
		telephone = result.getString("tel");
		pond = result.getInt("pond");
		flag = Flag.NORMAL;
	}
	
	@Override
	public boolean modifyValue(int field, Object value) {
		if(field==3){
			String newvalue = value.toString();
			if(!newvalue.equals(telephone)){
				telephone = value.toString();
				if(flag!=Flag.ADDED)
					flag = Flag.MODIFIED;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Object getValue(int field) {
		switch(field){
		case 0:
			return login;
		case 1:
			return password;
		case 2:
			return uname;
		case 3:
			return telephone;
		case 4:
			return pond;
		default:
			return null;
		}
	}

	@Override
	public String getQuery() {
		switch (flag) {
		case NORMAL:
			return null;
		case ADDED:
			return String.format(addquery, login, password, uname, telephone, pond);
		case MODIFIED:
			return String.format(updatequery, telephone, login);
		default:
			return String.format(deletequery, login);
		}
	}
	
	/**
	 * Возвращает логин пользователя.
	 * Метод необходим для быстрой проверки уникальности логина.
	 * @return Логин пользователя.
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * Метод, возвращающий заголовок колонки для поля.
	 * @param column Номер колонки(поля)
	 * @return Строковый заголовок для таблицы.
	 */
	public static String getHeader(int column) {
		return headers[column];
	}
	
	/**
	 * Метод, возвращающий количество колонок в таблице.
	 * Количество зависит от количества заголовков.
	 * @return Количество столбцов.
	 */
	public static int getColumnCount() {
		return headers.length;
	}
	
	/**
	 * Метод, возвращающий флаг возможности редактирования данного поля.
	 * @param column Номер столбца.
	 * @return true - если разрешено редактирование.
	 */
	public static boolean isEditable(int column){
		return editable[column];
	}

	@Override
	public String toString() {
		return "User [login=" + login + ", password=" + password + ", uname="
				+ uname + ", telephone=" + telephone + ", pond=" + pond + "]";
	}
	
	/**Логин пользователя*/
	private String login = null;
	/**Пароль в зашифрованном виде*/
	private int password = -1;
	/**Имя пользователя*/
	private String uname = null;
	/**Телефон пользователя*/
	private String telephone = null;
	/**Номер ставка, к которому привязан пользователь*/
	private int pond = -1;
	/**Массив заголовков для таблицы*/
	private static final String[] headers = {"Логин","Пароль","Имя","Телефон","Ставок"};
	/**Массив флагов редактирования полей*/
	private static final boolean[] editable = {false,false,false,true,false};
	/**Шаблон запроса на добавление записи*/
	private static final String addquery = "INSERT INTO users VALUES ('%s',%d,'%s','%s',%d)";
	/**Шаблон запроса на изменение записи*/
	private static final String updatequery = "UPDATE users SET tel='%s' WHERE login='%s'";
	/**Шаблон запроса на удаление записи*/
	private static final String deletequery = "DELETE FROM users WHERE login='%s'";

}