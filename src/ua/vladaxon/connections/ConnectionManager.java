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
 * �������� ����������. ��������� ��������� ���������������� ����������,
 * ������� �� � ���������.
 */
public class ConnectionManager implements Runnable{
	
	/**
	 * ������� ������ ��������� ���������� � ��������� � ��������� ������.
	 * @param propm �������� �������, ����� ��� ���������� ������ � ����� ������������� ������
	 * @param logger ������ ��� ������ ���������
	 * @return ������ ��������� ����������, ���� �� ��� ������� ������ ��� null � ������ ������
	 */
	public static ConnectionManager createInstance
	(PropManager propm, ServerLogger logger, DBManager dbmanager){
		try {
			ConnectionManager cm = new ConnectionManager(propm, logger, dbmanager);
			cm.threadpool.submit(cm);
			return cm;
		} catch (IOException e) {
			logger.setMessage("�������� ����������", "�� ������� ��������� �������� ����������");
			return null;
		}
	}
	
	/**
	 * ����������� ���������. ������� ������ ������, ��� �������, ��������� �����
	 * � ������� ��� ��������� xml.
	 * @param propm �������� �������
	 * @param logger ������
	 * @throws IOException ��� �������� ���������� ������.
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
		logger.setMessage("�������� ����������", "������ ���������������� ���������� �������.");
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
	 * ��������� ���������������� ���������� � ��������� ���.
	 * @param socket ����� ��� ����������� � �������.
	 * @return true - ���� ���������������� ���������� ��������.
	 */
	private boolean addUser(Socket socket){
		try {
			UserConnection connection = getUserConnection(socket);
			threadpool.submit(connection);
			return true;
		} catch (Exception e) {
			logger.setMessage("�������� ����������", 
					"���������� ��� �������� ������ �����������: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * ������� ������ ����������� � �������, � ����������� �� ��������� ������.
	 * ���� ���� ��������� ���� - ��������� ������� ����������� ��� ������ � ��������.
	 * ���� �������� ������ ��������� ���� - ��������� ��������� ����������� ���
	 * ���������� ������� � ������������� ������. ���� ��� ����� ������ - ����� ���������� null;
	 * @param socket ����� ��� �������� �����������.
	 * @return ������ ����������� � ������� ��� null ���� ��� ����� ������.
	 * @throws Exception ��� ������ �������� ����������������� ����������� ��� ���������� ��������� ������
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
	 * �����, ������������ ����� ���������� ����� ��� -1 ���� ��� ����� ������.
	 * ���� �������� ��������� ���� � ��� ����������� ������ ����������� �������
	 * ��� ����������� �� �������� ��������.
	 * @return ����� ���������� �����.
	 */
	private int getFreeSlot(){
		for(int i=0; i<connections.length; i++){
			if(connections[i]==null || !connections[i].isAlive())
				return i;
		}
		return -1;
	}
	
	/**������� ������������� ��� ���������������� ����������*/
	private TransformerFactory transFactory = TransformerFactory.newInstance();
	/**������� ������������ ���������� ��� ���������������� ����������*/
	private DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	/**��������� �����, ����������� �����������*/
	private ServerSocket server;
	/**�������� ��*/
	private DBManager dbmanager;
	/**������ �������*/
	private ServerLogger logger;
	/**������ ������ �����������*/
	private UserConnection[] connections;
	/**��� ������� ������������ ��������*/
	private ExecutorService threadpool;
	/**���������� ��������� ������*/
	private int reserveslotcount;
	/**��������� ����� ����������*/
	public static enum Response {ACCEPT, REJECT};
	/**����������� ��������� ������ */
	private final int reserveslotratio = 10;

}