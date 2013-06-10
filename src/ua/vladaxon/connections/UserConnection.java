package ua.vladaxon.connections;

import java.io.IOException;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.connections.ConnectionManager.Response;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.xml.XMLInputStream;
import ua.vladaxon.xml.XMLOutputStream;

/**
 * ����� ���������� ����������. ��������� ������� � ��������� xml ������.
 * ������, ���������, ������������ ������ ���������� {@link TaskHandler}.
 */
public class UserConnection implements Runnable{
	
	public UserConnection(Socket socket, Response response, ConnectionManager manager) throws Exception{
		this.response = response;
		this.socket = socket;
		this.logger = manager.getLogger();
		this.dbmanager = manager.getDbmanager();
		docBuilder = manager.getDocFactory().newDocumentBuilder();
		transformer = manager.getTransFactory().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"server.dtd");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		docoutstream = new XMLOutputStream(socket.getOutputStream());
		docinstream = new XMLInputStream(socket.getInputStream());
		docstream = new StreamResult(docoutstream);
		socketadress = socket.getLocalSocketAddress().toString().substring(1);
		if(response == Response.ACCEPT)
			logger.setMessage("������"+"("+socketadress+")", "������ ���������.");
	}

	/**
	 * ���������� ���� ���������� �����������.
	 * @return true - ���� ����������� �������.
	 */
	public boolean isAlive(){
		return alive;
	}
	
	/**
	 * �������� ���� ������ �������. ����� ������������� �����������
	 * ������ ����� ���������� �� ������� �������. ����� �����-��������
	 * ������������� � ���� ����������� ��� ����������� ����������.
	 */
	@Override
	public void run() {
		TaskHandler task = new TaskHandler(response);
		alive = true;
		try {
			while (alive) {
				sendResponse(task);
				task = receiveRequest();
			}
		} catch (Exception e) {
			closeConnection(e);
		}
	}
	
	/**
	 * ����� ��������� �������.
	 * @return ������ ������.
	 * @throws Exception ���� �� ������� ������� �������� ��� ��������� ���.
	 */
	private TaskHandler receiveRequest() throws Exception{
		docinstream.reset();
		try {
			docinstream.receive();
		} catch (Exception e) {
			TaskExceptionFactory.connectionReset();
		}
		Document doc = docBuilder.parse(docinstream);
		TaskHandler task = new TaskHandler(doc);
		task.executeTask(logger, dbmanager);
		return task;
	}
	
	/**
	 * ��������� ����� � ���� ��������� � ���������� ��� �������.
	 * ���� ������ �������� ����������� ������� - ����� �������� ���������� �����������.
	 * @param task ������ ������ ��� ������������ ������.
	 * @throws Exception ��� ������������� ������������ ��������� ��� ��� ��������.
	 */
	private void sendResponse(TaskHandler task) throws Exception{
		Document doc = task.formDocument(docBuilder);
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, docstream);
		try {
			docoutstream.send();
		} catch (Exception e) {
			TaskExceptionFactory.connectionReset();
		}
		docoutstream.reset();
		if(task.isReject()){
			if(dbmanager.isConnected()){
				TaskExceptionFactory.clientRejected();
			} else {
				TaskExceptionFactory.dBDisconnected();
			}
		}
	}
	
	/**
	 * ��������� ���������� �� �������, ���������� ����������� ������.
	 * @param reason �������� ������� ���������� � ������� ����������.
	 */
	private void closeConnection(Exception reason){
		logger.setMessage("������"+"("+socketadress+")", reason.getMessage());
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		alive = false;
	}
	
	/**����������� ���������� ��� �������*/
	private DocumentBuilder docBuilder;
	/**����������� ��� �������� ����������*/
	private Transformer transformer = null;
	/**�������� ���� ������*/
	private DBManager dbmanager;
	/**������*/
	private ServerLogger logger;
	/**����� ����������� �����������*/
	private Socket socket;
	/**����� ���������*/
	private StreamResult docstream;
	/**����� �������� ����������*/
	private XMLOutputStream docoutstream;
	/**����� ��������� ����������*/
	private XMLInputStream docinstream;
	/**��� ������� ������ �������*/
	private Response response;
	/**���� ���������� �����������*/
	private volatile boolean alive = false;
	/**��������� �������� ������ ������ �������*/
	private String socketadress;

}