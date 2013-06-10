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
	 * �����, ����������� ������ � ��.
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
	 * �����, ����������� ������ �� ��.
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
	 * ��������� ���������� � ��.
	 * @return true - ���� ���������� �� �����������.
	 */
	public boolean isConnected(){
		return connection!=null;
	}
	
	/**
	 * �����, ����������� ����������� ������ �� ��������� ������ �� ��.
	 * ��� ������ � ������ ������� �� ��������� �������, ������� ������ ������ �����.
	 * ������ ����������� ���������� � ������������ ����� �������� �������� ������.
	 * ����� �������� ������ �� ��, ��� ������������� � ��������� ��� � ����������
	 * ����������� ����� ��������� �������, ������� ��������� ���������� ������ �
	 * ��������� �������. ������� ��������� ������ � ������� {@link FutureTask} �����
	 * �� ��������, ��������� � ��� ���������� ������������� ���������� �������� �
	 * ��������� ������ � ������� ����� ���������� ������, ���� ������������ ��
	 * ������� ����������� ����������� ������������� ���� ��� �������� � ������� ����.
	 * @param manager �������� �������.
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
	 * �����, ����������� ����������� ������ �� ���������� ��.
	 * ��������������� ���������� ����� ���������� ������� � �� � �������� ������ ������.
	 * ���� ���������� ����������� �������� - ����� ������ �� �����������.
	 * � ����� ���������� {@link DBManager#loadItems(BasicManager)}
	 * @param <T>
	 * @param manager �������� �������.
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
	 * �����, ����������� ����������� ������ �� ���������� ������� � ��.
	 * ���� ���������� ������� ������ ������� - ������ � ���������� ������� ���������.
	 * ��� ��������� ��������� ������ ��� ������ ������ � ��.
	 * ����� ���������� {@link DBManager#loadItems(BasicManager)}
	 * @param manager �������� �������.
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
	 * �����, ���������� ����������� �� �� ������ ������� �������,
	 * � ������� �� �������� ������������. ������ ��������� ��� ��������
	 * �������� ����� ������ ������������.
	 * @param manager �������� �������������, �������� ���������� ��������� �������.
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
	 * �����, ���������� ����������� �� �� ������ ���� ������� �������.
	 * ��������� ��� �������� ����� ������ �����������.
	 * @param manager �������� �����������, �������� ���������� ��������� �������.
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
	 * ���������� ����� ������, ������� �������� �� ������������� � ���������
	 * ������� � �������. ������������ ��� ����������� �������.
	 * @param username ����� ������������.
	 * @param password ������������� ������ ������������
	 * @return ����� ������ ������������ ��� -1 ���� ������ ������������ ��� � ��.
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
	 * �����, ����������� �� �� ������ �������������.
	 * @return ������ �������� �������������. ��� ��������� ���������� ���������� null
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
	 * �����, ����������� �� �� ������ �������.
	 * @return ������ �������� �������.
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
	 * �����, ����������� �� �� ������ �����������.
	 * @return ������ �������� �����������.
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
	 * �����, ����������� ������ �������, ������� ����� �������� ��� �������� � ��.
	 * ����������� ����� ���� �������� ������� � � ����������� �� ����� �����������
	 * �������������� ������, ����� ���� ����������� � �������� ����������.
	 * @param list �������� ������ �������.
	 * @param loadproblem ���������, ��� ������ ����������.
	 * @return true - ���� ���������� ������ �������.
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
	 * �����, ������������ ��������� ������� �������.
	 * ����� ���������� ��������� ���� �������
	 * ��� ������ ������������� � �������������.
	 * @param free true - ���� ����� ������ ��������� ������
	 * @return ��������� ������� �������
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
	
	/**������ �������� ��������, ��������� � ���������� ��*/
	private DBBindingAbstractAction[] actions;
	/**������ ���������� � ��*/
	private Connection connection;
	/**������ ���������� ��� ������� � �����������*/
	private ReentrantReadWriteLock dblock = new ReentrantReadWriteLock();
	/**������ �������, ��������� ��� ������� ���������*/
	private ServerLogger logger;
	/**�������� �������*/
	private PropManager props;
	/**��� ������� ��� ���������� ��������*/
	private ExecutorService querypool = Executors.newCachedThreadPool();
	/**������, ������������ ������ ���� �������������*/
	private static final String getallusersquery = "SELECT * FROM users ORDER BY login";
	/**������, ������������ ������ ���� ������� � ��� � ������� ����������� ������������, ���� �� ����*/
	private static final String getallpondsquery = "SELECT ponds.*, users.uname, users.tel " +
			"FROM ponds LEFT OUTER JOIN users ON pond=pid ORDER BY pid;";
	/**������, ������������ ������ ���� �������*/
	private static final String getallmonitorquery = "SELECT * FROM monitor ORDER BY mdate";
	/**������, ������������ ������ �������, ������� �� ����� ����������� �������������*/
	private static final String getfreepondidquery = 
			"SELECT pid FROM ponds WHERE pid NOT IN(SELECT pond FROM users);";
	/**������, ������������ ��� ������ �������*/
	private static final String getallpondidquery = "SELECT pid FROM ponds;";
	/**���������� ����� ������ �� ����� ������������ � ������*/
	private static final String GETUSERPONDID = "SELECT pond FROM users WHERE login='%s' AND passw=%d;";
	/**��� ������� � ����*/
	private static final String from = "�������� ��";
	 private static final String connreceived = "���������� � �� %s �����������";
	 private static final String conlost = "���������� � �� �������";
	 private static final String dbproblem = "��������� ������ ��� ���������� ������� � ���� ������!";
	 private static final String userloadproblemmsg = 
			 "��������� ������ ��� �������� ������� ������������� �� ��!";
	 private static final String pondloadproblemmsg = 
			 "��������� ������ ��� �������� ������� ������� �� ��!";
	 private static final String monitorloadproblemmsg = 
			 "��������� ������ ��� �������� ������� ����������� �� ��!";
	
}