package ua.vladaxon.users;

import java.util.Set;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.basicitem.BasicManager;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.User;

/**
 * Менеджер записей пользователей. Управляет записями в БД. Позволяет
 * создавать новые записи, редактировать существующие или удалять.
 * @see BasicManager
 */
public class UserManager extends BasicManager<User>{
	
	public UserManager(DBManager dbmanager, ServerLogger logger) {
		super(dbmanager, logger);
		ui = new UserManagerUI(this);
		newitem = new NewUser(ui.getOwnerFrame(), this);
	}
	
	/**
	 * Метод, проверяющий указаный логин на уникальность. Необходим для создания 
	 * уникального логина для нового пользователя. Проверяются все записи: которые
	 * присутствуют в БД и те, которые созданы, но еще не занесены в БД.
	 * @param login Логин для проверки.
	 * @return true - если логин уникальный.
	 */
	public boolean isUniqueLogin(String login){
		for(User u: itemlist)
			if(u.getLogin().equals(login))
				return false;
		return true;
	}
	
	/**
	 * Метод, выполняющий запрос менеджеру БД на получение множества
	 * номеров ставков, к которым не привязаны пользователи.
	 * Вызывается перед созданием новой записи пользователя.
	 */
	public void requestFreePondId(){
		dbmanager.loadFreePondId(this);
	}
	
	/**
	 * Метод, передающий диалогу создания нового пользователя
	 * множество номеров ставков, к которым не привязан пользователь
	 * Множество необходимо для создания пользователя т.к. при создании
	 * он сразу должен быть привязан к ставку. Данный метод вызывает
	 * менеджер БД из потока выполнения запроса. Менеджер пользователей
	 * удаляет номера, по которым уже созданы записи, находящиеся
	 * в добавочном списке, но еще не добавленные в БД, затем передают
	 * измененное множеству диалогу создания записи.
	 * @param freeidset Множество номеров ставков.
	 */
	public void setFreePondId(Set<Integer> freeidset){
		for(User u: itemlist)
			if(u.getFlag()==Flag.ADDED)
				freeidset.remove(u.getValue(4));
		((NewUser) newitem).setFreePondId(freeidset);
	}

	@Override
	public int getColumnCount() {
		return User.getColumnCount();
	}

	@Override
	public String getHeader(int columnIndex) {
		return User.getHeader(columnIndex);
	}

	@Override
	public boolean isEditable(int columnIndex) {
		return User.isEditable(columnIndex);
	}

}