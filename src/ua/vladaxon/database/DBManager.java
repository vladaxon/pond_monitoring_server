package ua.vladaxon.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ua.vladaxon.PropManager;
import ua.vladaxon.ServerLogger;
import ua.vladaxon.actions.DBBindingAbstractAction;
import ua.vladaxon.basicitem.BasicItem;
import ua.vladaxon.basicitem.BasicManager;
import ua.vladaxon.monitor.MonitorManager;
import ua.vladaxon.ponds.PondManager;
import ua.vladaxon.users.UserManager;

public class DBManager {
	
	public DBManager(ServerLogger logger, PropManager props, DBBindingAbstractAction[] actions){
		this.logger = logger;
		this.props = props;
		this.actions = actions;
		for(DBBindingAbstractAction b: this.actions)
			b.bindDB(this);
	}
	
	/**
	 * Метод, соединяющий сервер с БД.
	 */
	public void connect(String login, String password){
		try {
			connection = DriverManager.getConnection(props.getDatabaseURL(), login, password);
			String dbname = connection.getMetaData().getDatabaseProductName();
			logger.setMessage(from, String.format(connreceived, dbname));
			actions[0].setEnabled(false);
			actions[1].setEnabled(true);
			actions[2].setEnabled(true);
			actions[3].setEnabled(true);
			actions[4].setEnabled(true);
		} catch (SQLException e) {
			logger.setMessage(from, e.getMessage());
		}
	}

	/**
	 * Метод, отключающий сервер от БД.
	 */
	public void disconnect(){
		logger.setMessage(from, conlost);
		try {
			connection.close();
			connection = null;
			actions[0].setEnabled(true);
			actions[1].setEnabled(false);
			actions[2].setEnabled(false);
			actions[3].setEnabled(false);
			actions[4].setEnabled(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Проверяет соединение с БД.
	 * @return true - если соединение не установлено.
	 */
	public boolean isConnected(){
		return connection!=null;
	}
	
	/**
	 * Метод, формирующий асинхронную задачу на получения списка из БД.
	 * Тип данных в списке зависит от менеджера записей, который вызвал данный метод.
	 * Задача запускается асинхронно и определяется какой менеджер запустил задачу.
	 * После загрузки данных из БД, они преобразуются в объектный тип и вызывается
	 * специальный метод менеджера записей, которых сохраняет полученный список и
	 * обновляет таблицу. Вариант получения данных с помощью {@link FutureTask} здесь
	 * не подходит, поскольку с ним невозможно принудительно оповестить менеджер о
	 * получении данных и таблица будет оставаться пустая, пока пользователь не
	 * вызовет перерисовку компонентов растягиванием окна или нажатием в область окна.
	 * @param manager Менеджер записей.
	 */
	public void loadItems(final BasicManager<?> manager){
		Runnable task = new Runnable() {
			@Override
			public void run() {
				if (manager instanceof UserManager)
					((UserManager) manager).loadItems(DBManager.this.getAllUsers());
				if (manager instanceof PondManager)
					((PondManager) manager).loadItems(DBManager.this.getAllPonds());
				if (manager instanceof MonitorManager)
					((MonitorManager) manager).loadItems(DBManager.this.getAllMonitors());
			}
		};
		querypool.submit(task);
	}
	
	/**
	 * Метод, формирующий асинхронную задачу на обновление БД.
	 * Последовательно вызывается метод сохранения записей в БД и загрузка нового списка.
	 * Если сохранение завершилось неудачей - новый список не загружается.
	 * В целом аналогичен {@link DBManager#loadItems(BasicManager)}
	 * @param <T>
	 * @param manager Менеджер записей.
	 */
	public <T extends BasicItem> void updateItems(final BasicManager<T> manager){
		Runnable task = new Runnable() {
			@Override
			public void run() {
				if(manager instanceof UserManager){
					if(DBManager.this.setItems(manager.getItems()))
						((UserManager)manager).loadItems(DBManager.this.getAllUsers());
				}
				if(manager instanceof PondManager){
					if(DBManager.this.setItems(manager.getItems()))
							((PondManager)manager).loadItems(DBManager.this.getAllPonds());
				}
				if(manager instanceof MonitorManager){
					if(DBManager.this.setItems(manager.getItems()))
							((MonitorManager)manager).loadItems(DBManager.this.getAllMonitors());
				}
			}
		};
		querypool.submit(task);
	}
	
	/**
	 * Метод, формирующий асинхронную задачу на сохранение записей в БД.
	 * Если сохранение записей прошло успешно - списки в менеджерах записей удаляются.
	 * Это позволяет сохранить данные при ошибке записи в БД.
	 * Метод аналогичен {@link DBManager#loadItems(BasicManager)}
	 * @param manager Менеджер записей.
	 */
	public <T extends BasicItem> void saveItems(final BasicManager<T> manager){
		Runnable task = new Runnable() {
			@Override
			public void run() {
				if(manager instanceof UserManager)
					if(DBManager.this.setItems(manager.getItems()))
						manager.clearItem();
				if(manager instanceof PondManager)
					if(DBManager.this.setItems(manager.getItems()))
						manager.clearItem();
				if(manager instanceof MonitorManager)
					if(DBManager.this.setItems(manager.getItems()))
						manager.clearItem();
			}
		};
		querypool.submit(task);
	}
	
	/**
	 * Метод, асинхронно загружающий из БД список номеров ставков,
	 * к которым не привязан пользователь. Список необходим для процесса
	 * создания новой записи пользователя.
	 * @param manager Менеджер пользователей, которому отсылается множество номеров.
	 */
	public void loadFreePondId(final UserManager manager){
		Runnable task = new Runnable() {
			@Override
			public void run() {
				manager.setFreePondId(getPondsIDs(true));
			}
		};
		querypool.submit(task);
	}
	
	/**
	 * Метод, асинхронно загружающий из БД список всех номеров ставков.
	 * Необходим для создания новой записи мониторинга.
	 * @param manager Менеджер мониторинга, которому отсылается множество номеров.
	 */
	public void loadAllPondId(final MonitorManager manager){
		Runnable task = new Runnable() {
			@Override
			public void run() {
				manager.setAllPondId(getPondsIDs(false));
			}
		};
		querypool.submit(task);
	}
	
	/**
	 * Возвращает номер ставка, который привязан за пользователем с указанным
	 * логином и паролем. Используется для авторизации клиента.
	 * @param username Логин пользователя.
	 * @param password Зашифрованный пароль пользователя
	 * @return Номер ставка пользователя или -1 если такого пользователя нет в БД.
	 */
	public int getUserPondID(String username, int password){
		int pondid = -1;
		if(connection!=null){
			ResultSet result = null;
			Statement statement = null;
			try {
				dblock.readLock().lock();
				statement = connection.createStatement();
				result = statement.executeQuery(String.format(GETUSERPONDID, username, password));
				if(result.next()){
					pondid = result.getInt("pond");
				}
			} catch (SQLException e) {
				this.disconnect();
				e.printStackTrace();	
			} finally {
				try {
					if(statement!=null)
						statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dblock.readLock().unlock();
			}			
		}	
		return pondid;
	}

	/**
	 * Метод, загружающий из БД список пользователей.
	 * @return Массив объектов пользователей. При неудачном завершении возвращает null
	 */
	private List<User> getAllUsers(){
		List<User> users = new ArrayList<User>();
		if(connection!=null){
			ResultSet result = null;
			Statement statement = null;
			try {
				dblock.readLock().lock();
				statement = connection.createStatement();
				result = statement.executeQuery(getallusersquery);
				while(result.next())
					users.add(new User(result));
			} catch (SQLException e) {
				logger.setMessage(from, userloadproblemmsg);
				this.disconnect();
				e.printStackTrace();	
			} finally {
				try {
					if(statement!=null)
						statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dblock.readLock().unlock();
			}			
		}	
		return users;
	}
	
	/**
	 * Метод, загружающий из БД список ставков.
	 * @return Массив объектов ставков.
	 */
	public List<Pond> getAllPonds(){
		List<Pond> ponds = new ArrayList<Pond>();
		if(connection!=null){
			ResultSet result = null;
			Statement statement = null;
			try {
				dblock.readLock().lock();
				statement = connection.createStatement();
				result = statement.executeQuery(getallpondsquery);
				while(result.next()){
					ponds.add(new Pond(result));
				}	
			} catch (SQLException e) {
				logger.setMessage(from, pondloadproblemmsg);
				this.disconnect();
				e.printStackTrace();	
			} finally {
				try {
					if(statement!=null)
						statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dblock.readLock().unlock();
			}			
		}	
		return ponds;
	}
	
	/**
	 * Метод, загружающий из БД список мониторинга.
	 * @return Массив объектов мониторинга.
	 */
	public List<Monitor> getAllMonitors(){
		List<Monitor> monitors = new ArrayList<>();
		if(connection!=null){
			ResultSet result = null;
			Statement statement = null;
			try {
				dblock.readLock().lock();
				statement = connection.createStatement();
				result = statement.executeQuery(getallmonitorquery);
				while(result.next()){
					monitors.add(new Monitor(result));
				}	
			} catch (SQLException e) {
				logger.setMessage(from, monitorloadproblemmsg);
				this.disconnect();
				e.printStackTrace();	
			} finally {
				try {
					if(statement!=null)
						statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dblock.readLock().unlock();
			}			
		}	
		return monitors;
	}
	
	/**
	 * Метод, формирующий список записей, которые нужно изменить или добавить в БД.
	 * Проверяются флаги всех объектов записей и в зависимости от флага формируется
	 * соответсвующий запрос, после чего добавляется в пакетное обновление.
	 * @param list Основной список записей.
	 * @param loadproblem Сообщение, при ошибке сохранения.
	 * @return true - если сохранение прошло успешно.
	 */
	public <T extends BasicItem> boolean setItems(List<T> list){
		if(connection!=null){
			Statement statement = null;
			try {
				dblock.writeLock().lock();
				statement = connection.createStatement();
				for (T i : list) {
					String query = i.getQuery();
					if (query != null)
						statement.addBatch(query);
				}
				statement.executeBatch();
				return true;
			} catch (SQLException e) {
				logger.setMessage(from, dbproblem);
				this.disconnect();
				e.printStackTrace();
				return false;
			} finally {
				try {
					if(statement!=null)
						statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dblock.writeLock().unlock();
			}
		}
		return false;
	}
	
	/**
	 * Метод, возвращающий множество номеров ставков.
	 * Может возвращать множество всех номеров
	 * или только непривязанных к пользователям.
	 * @param free true - если нужны только свободные ставки
	 * @return Множество номеров ставков
	 */
	private Set<Integer> getPondsIDs(boolean free){
		Set<Integer> idset = null;
		if(connection!=null){
			ResultSet result = null;
			Statement statement = null;
			try {
				dblock.readLock().lock();
				statement = connection.createStatement();
				if(free)
					result = statement.executeQuery(getfreepondidquery);
				else
					result = statement.executeQuery(getallpondidquery);
				idset = new HashSet<Integer>();
				while(result.next())
					idset.add(result.getInt("pid"));
				result.close();
			} catch (SQLException e) {
				logger.setMessage(from, dbproblem);
				this.disconnect();
				e.printStackTrace();	
			} finally {
				try {
					if(statement!=null)
						statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dblock.readLock().unlock();
			}			
		}	
		return idset;
	}
	
	/**Массив объектов действий, связанных с менеджером БД*/
	private DBBindingAbstractAction[] actions;
	/**Объект соединения с БД*/
	private Connection connection;
	/**Объект блокировки для ставков и мониторинга*/
	private ReentrantReadWriteLock dblock = new ReentrantReadWriteLock();
	/**Объект логгера, необходим для ведения сообщений*/
	private ServerLogger logger;
	/**Менеджер свойств*/
	private PropManager props;
	/**Пул потоков для выполнения запросов*/
	private ExecutorService querypool = Executors.newCachedThreadPool();
	/**Запрос, возвращающий список всех пользователей*/
	private static final String getallusersquery = "SELECT * FROM users ORDER BY login";
	/**Запрос, возвращающий список всех ставков и имя и телефон привязаного пользователя, если он есть*/
	private static final String getallpondsquery = "SELECT ponds.*, users.uname, users.tel " +
			"FROM ponds LEFT OUTER JOIN users ON pond=pid ORDER BY pid;";
	/**Запрос, возвращающий список всех ставков*/
	private static final String getallmonitorquery = "SELECT * FROM monitor ORDER BY mdate";
	/**Запрос, возвращающий номера ставков, которые не имеют привязанных пользователей*/
	private static final String getfreepondidquery = 
			"SELECT pid FROM ponds WHERE pid NOT IN(SELECT pond FROM users);";
	/**Запрос, возвращающий все номера ставков*/
	private static final String getallpondidquery = "SELECT pid FROM ponds;";
	/**Возвращает номер ставка по имени пользователя и паролю*/
	private static final String GETUSERPONDID = "SELECT pond FROM users WHERE login='%s' AND passw=%d;";
	/**Имя объекта в логе*/
	private static final String from = "Менеджер БД";
	 private static final String connreceived = "Соединение с БД %s установлено";
	 private static final String conlost = "Соединение с БД утеряно";
	 private static final String dbproblem = "Произошла ошибка при выполнении запроса к базе данных!";
	 private static final String userloadproblemmsg = 
			 "Произошла ошибка при загрузке записей пользователей из БД!";
	 private static final String pondloadproblemmsg = 
			 "Произошла ошибка при загрузке записей ставков из БД!";
	 private static final String monitorloadproblemmsg = 
			 "Произошла ошибка при загрузке записей мониторинга из БД!";
	
}