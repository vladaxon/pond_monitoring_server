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
 * Класс клиентское соединение. Управляет приемом и передачей xml файлов.
 * Разбор, обработку, формирование ответа организует {@link TaskHandler}.
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
			logger.setMessage("Клиент"+"("+socketadress+")", "Клиент подключен.");
	}

	/**
	 * Возвращает флаг активности подключения.
	 * @return true - если подключение активно.
	 */
	public boolean isAlive(){
		return alive;
	}
	
	/**
	 * Основной цикл обмена данными. Перед зацикливанием формируется
	 * первый ответ посылаемый от сервера клиенту. Далее прием-передача
	 * зацыкливается и цикл завершается при возбуждении исключения.
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
	 * Метод получения запроса.
	 * @return Объект задачи.
	 * @throws Exception Если не удалось принять документ или разобрать его.
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
	 * Формирует ответ в виде документа и отправляет его клиенту.
	 * Если сервер отклонил подключение клиента - после передачи соединение разрывается.
	 * @param task Объект задачи для формирования ответа.
	 * @throws Exception При невозможности формирования документа или его отправки.
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
	 * Отключает соединения от клиента, производит завершающие работы.
	 * @param reason Передает причину отключения в объекте исключения.
	 */
	private void closeConnection(Exception reason){
		logger.setMessage("Клиент"+"("+socketadress+")", reason.getMessage());
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		alive = false;
	}
	
	/**Конструктор документов для разбора*/
	private DocumentBuilder docBuilder;
	/**Трансформер для отправки документов*/
	private Transformer transformer = null;
	/**Менеджер базы данных*/
	private DBManager dbmanager;
	/**Логгер*/
	private ServerLogger logger;
	/**Сокет клиентского подключения*/
	private Socket socket;
	/**Поток документа*/
	private StreamResult docstream;
	/**Поток отправки документов*/
	private XMLOutputStream docoutstream;
	/**Поток получения документов*/
	private XMLInputStream docinstream;
	/**Тип первого ответа клиенту*/
	private Response response;
	/**Флаг активности подключения*/
	private volatile boolean alive = false;
	/**Строковое значение адреса сокета клиента*/
	private String socketadress;

}