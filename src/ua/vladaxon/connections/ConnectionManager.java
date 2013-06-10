package ua.vladaxon.connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;

import ua.vladaxon.PropManager;
import ua.vladaxon.ServerLogger;
import ua.vladaxon.database.DBManager;

/**
 * Менеджер соединений. Управляет объектами пользовательских соединений,
 * создает их и запускает.
 */
public class ConnectionManager implements Runnable{
	
	/**
	 * Создает объект менеджера соединений и запускает в отдельном потоке.
	 * @param propm Менеджер свойств, нужен для количества слотов и порта прослушивания сокета
	 * @param logger Логгер для вывода сообщений
	 * @return Объект менеджера соединений, если он был успешно создан или null в другом случае
	 */
	public static ConnectionManager createInstance
	(PropManager propm, ServerLogger logger, DBManager dbmanager){
		try {
			ConnectionManager cm = new ConnectionManager(propm, logger, dbmanager);
			cm.threadpool.submit(cm);
			return cm;
		} catch (IOException e) {
			logger.setMessage("Менеджер соединений", "Не удалось запустить менеджер соединений");
			return null;
		}
	}
	
	/**
	 * Конструктор менеджера. Создает массив слотов, пул потоков, серверный сокет
	 * и фабрики для обработки xml.
	 * @param propm Менеджер свойств
	 * @param logger Логгер
	 * @throws IOException При создании севрерного сокета.
	 */
	private ConnectionManager(PropManager propm, ServerLogger logger, DBManager dbmanager) throws IOException{
		int slotcount = propm.getClientSlotCount();
		int serverport = propm.getServerPort();
		this.logger = logger;
		this.dbmanager = dbmanager;
		reserveslotcount = (int) Math.ceil((slotcount/100f)*reserveslotratio);
		threadpool = Executors.newFixedThreadPool(slotcount+reserveslotcount+1);//+ServerSocket+Reject
		connections = new UserConnection[slotcount+reserveslotcount];//+Reject
		server = new ServerSocket(serverport);
		docFactory.setValidating(true);
		docFactory.setIgnoringElementContentWhitespace(true);
	}
	
	@Override
	public void run() {
		logger.setMessage("Менеджер соединений", "Сервер пользовательских соединений запущен.");
		while(true){
			try {
				Socket socket = server.accept();
				if(!ConnectionManager.this.addUser(socket)){
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public TransformerFactory getTransFactory() {
		return transFactory;
	}

	public DocumentBuilderFactory getDocFactory() {
		return docFactory;
	}

	public DBManager getDbmanager() {
		return dbmanager;
	}

	public ServerLogger getLogger() {
		return logger;
	}

	/**
	 * Добавляет пользовательское соединение и запускает его.
	 * @param socket Сокет для подключения к киленту.
	 * @return true - если пользовательское соединение запущено.
	 */
	private boolean addUser(Socket socket){
		try {
			UserConnection connection = getUserConnection(socket);
			threadpool.submit(connection);
			return true;
		} catch (Exception e) {
			logger.setMessage("Менеджер соединений", 
					"Исключение при создании нового подключения: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Создает объект подключения к клиенту, в зависимости от состояния слотов.
	 * Если есть свободный слот - создается обычное подключение для работы с клиентом.
	 * Если свободен только резервный слот - создается временное подключения для
	 * оповещения клиента о невозможности работы. Если все слоты заняты - метод возвращает null;
	 * @param socket Сокет для создания подключения.
	 * @return Объект подключения к клиенту или null если все слоты заняты.
	 * @throws Exception При ошибке создания пользовательского подключения или отсутствия свободных слотов
	 */
	private UserConnection getUserConnection(Socket socket) throws Exception{
		int availslot = getFreeSlot();
		if(availslot!=-1){
			if(availslot>(connections.length-1-reserveslotcount)){
				connections[availslot] = new UserConnection(socket, Response.REJECT, this);
			} else {
				if(dbmanager.isConnected()){
					connections[availslot] = new UserConnection(socket, Response.ACCEPT, this);
				} else {
					connections[availslot] = new UserConnection(socket, Response.REJECT, this);
				}
			}
		} else
			throw new Exception("No available slot!");
		return connections[availslot];
	}
	
	/**
	 * Метод, возвращающий номер свободного слота или -1 если все слоты заняты.
	 * Слот является свободным если в нем отсутствует объект подключения клиента
	 * или подключения не является активным.
	 * @return Номер свободного слота.
	 */
	private int getFreeSlot(){
		for(int i=0; i<connections.length; i++){
			if(connections[i]==null || !connections[i].isAlive())
				return i;
		}
		return -1;
	}
	
	/**Фабрика трансформеров для пользовательских соединений*/
	private TransformerFactory transFactory = TransformerFactory.newInstance();
	/**Фабрика обработчиков документов для пользовательских соединений*/
	private DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	/**Серверный сокет, принимающий подключения*/
	private ServerSocket server;
	/**Менеджер БД*/
	private DBManager dbmanager;
	/**Объект логгера*/
	private ServerLogger logger;
	/**Массив слотов подключения*/
	private UserConnection[] connections;
	/**Пул потоков обслуживания клиентов*/
	private ExecutorService threadpool;
	/**Количество резервных слотов*/
	private int reserveslotcount;
	/**Нумерация типов соединений*/
	public static enum Response {ACCEPT, REJECT};
	/**Соотношения резервных слотов */
	private final int reserveslotratio = 10;

}