package ua.vladaxon.connections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.basicitem.BasicItem;
import ua.vladaxon.connections.ConnectionManager.Response;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.Monitor;
import ua.vladaxon.database.Pond;

public class TaskHandler {
	
	/**
	 * Конструктор задачи только из ответа сервера.
	 * Используется сервером при первой отправке документа клиенту.
	 * @param response Ответ сервера на подключение клиента.
	 */
	public TaskHandler(Response response){
		serverresponse = response;
	}
	
	/**
	 * Конструктор задачи из документа, пришедшего от клиента.
	 * @param doc Документ клиента.
	 * @throws Exception передает исключение из методов разбора.
	 */
	public TaskHandler(Document doc) throws Exception{
		Element root = doc.getDocumentElement();
		checkVersion(root);
		checkConnection(root);
		parseRequest(root);
	}
	
	/**
	 * Выполняет полученную задачу. Передает управление подметоду, в зависимости от типа запроса.
	 * @param logger Логгер для вывода сообщений.
	 * @param dbmanager Менеджер БД для выполнения запроса.
	 */
	public void executeTask(ServerLogger logger, DBManager dbmanager){
		switch (request) {
		case AUTHORIZATION:{
			executeAuthorization(logger, dbmanager);
			break;
		}
		case UPDATE:{
			executeUpdate(logger, dbmanager);
			break;
		}
		case POND:{
			executePondRequest(logger, dbmanager);
			break;
		}
		case MONITOR:{
			executeMonitorRequest(logger, dbmanager);
			break;
		}
		default:{
			return;
		}
		}
	}
	
	/**
	 * Формирует выходной документ для отправки клиенту.
	 * @param docbuilder Для создания документа.
	 * @return Документ, готовый к отправке клиенту.
	 */
	public Document formDocument(DocumentBuilder docbuilder){
		Document doc = docbuilder.newDocument();
		Element rootelement = doc.createElement(ROOTNAME);
		doc.appendChild(rootelement);
		rootelement.setAttribute(ROOTVERSIONATTR, ROOTVERSION);
		Element dataelement = doc.createElement(DATAELEMENT);
		rootelement.appendChild(dataelement);
		Element connectionelem = doc.createElement(CONNECTIONELEMENT);
		dataelement.appendChild(connectionelem);
		Text connstatus = doc.createTextNode(serverresponse.toString());
		connectionelem.appendChild(connstatus);
		if (request != RequestType.NONE) {
			// Response
			Element responseelement = doc.createElement(RESPONSEELEMENT);
			dataelement.appendChild(responseelement);
			Element statuselement = doc.createElement(STATEELEMENT);
			Element subjectelement = doc.createElement(SUBJECTELEMENT);
			responseelement.appendChild(statuselement);
			responseelement.appendChild(subjectelement);
			statuselement.appendChild(doc.createTextNode(requestexecuted + ""));
			subjectelement.appendChild(doc.createTextNode(request.toString()));
			if (requestexecuted) {
				switch (request) {
				case AUTHORIZATION: {
					Element pidelement = doc.createElement(PIDELEMENT);
					pidelement.appendChild(doc.createTextNode(userpondid + ""));
					responseelement.appendChild(pidelement);
					break;
				}
				case POND: {
					Element itemselement = doc.createElement(ITEMSELEMENT);
					for (BasicItem pond : outputitems) {
						itemselement.appendChild(((Pond) pond).getElement(doc));
					}
					rootelement.appendChild(itemselement);
					break;
				}
				case MONITOR: {
					Element itemselement = doc.createElement(ITEMSELEMENT);
					for (BasicItem monitor : outputitems) {
						itemselement.appendChild(((Monitor) monitor)
								.getElement(doc));
					}
					rootelement.appendChild(itemselement);
					break;
				}
				default:
					break;
				}
			}
		}
		return doc;
	}
	
	/**
	 * Возвращает флаг отклонение клиента.
	 * @return true - если сервер отклонил подключение клиента.
	 */
	public boolean isReject(){
		return serverresponse == Response.REJECT;
	}
	
	/**
	 * Проверяет версию документа, полученного от клиента.
	 * @param root Корневой элемент документа.
	 * @throws Exception если версии не совпадают.
	 */
	private void checkVersion(Element root) throws Exception{
		String docversion = root.getAttribute(ROOTVERSIONATTR);
		if(!docversion.equals(ROOTVERSION)){
			TaskExceptionFactory.documentVersion();
		}
	}
	
	/**
	 * Проверяет состояние соединения с клиентом.
	 * @param root Корневой элемент документа.
	 * @throws Exception если клиент послал команду отключения.
	 */
	private void checkConnection(Element root) throws Exception{
		Element data = (Element) root.getFirstChild();
		String connstr = getString((Element) data.getFirstChild());
		if(!connstr.equals(Response.ACCEPT.toString())){
			TaskExceptionFactory.clientDisconnected();
		}
	}
	
	/**
	 * Разбирает тип запроса от клиента и дополнительные параметры.
	 * Если принят запрос на получение записей - должно присутствовать
	 * условие для разбора документа
	 * @param root
	 */
	private void parseRequest(Element root){
		Element data = (Element) root.getFirstChild();
		NodeList datachilds = data.getChildNodes();
		if(datachilds.getLength()==2){
			Element requestnode = (Element) datachilds.item(1);
			NodeList requestchilds = requestnode.getChildNodes();
			request = RequestType.getByName(getString((Element) requestchilds.item(0)).toUpperCase());
			switch (request) {
			case WRONG: {
				return;
			}
			case UPDATE: {
				if(getClientParams((Element) requestchilds.item(1), (Element) requestchilds.item(2))){
					parseItems(root);
				} else {
					request = RequestType.WRONG;
				}
				break;
			}
			case AUTHORIZATION: {
				if(!getClientParams((Element) requestchilds.item(1), (Element) requestchilds.item(2))){
					request = RequestType.WRONG;
				}
				break;
			}
			default:
				break;
			}
		} else {
			request = RequestType.NONE;
		}
	}
	
	/**
	 * Считывает данные для авторизации клиента. Выделено в отдельный метод
	 * поскольку используется в 2 случаях: авторизации и занесение данных.
	 * @param username Элемент с именем пользователя.
	 * @param password Элемент с паролем пользователя.
	 * @return true - если данные получены успешно.
	 */
	private boolean getClientParams(Element username, Element password){
		this.username = getString(username);
		try {
			this.password = Integer.parseInt(getString(password));
		} catch (NumberFormatException e) {
			return false;
		}
		return this.username!=null;
	}
	
	/**
	 * Разбирает часть документа с записями.
	 * @param root Корневой элемент документа.
	 */
	private void parseItems(Element root){
		NodeList rootchilds = root.getChildNodes();
		if(rootchilds.getLength()==2){
			NodeList itemschilds = rootchilds.item(1).getChildNodes();
			getItemType((Element)rootchilds.item(1));
			switch (itemtype) {
			case POND:{
				try {
					updatepond = new Pond((Element) itemschilds.item(0));
				} catch (Exception e) {
					request = RequestType.WRONG;
				}
				break;
			}
			case MONITOR:{
				inputitems = new ArrayList<Monitor>();
				for(int i=0; i<itemschilds.getLength(); i++){
					try {
						Monitor item = new Monitor((Element) itemschilds.item(i));
						inputitems.add(item);
					} catch (Exception e) {
						request = RequestType.WRONG;
					}
			}
				break;
			}
			}
		} else {
			request = RequestType.WRONG;
		}
	}
	
	/**
	 * Возвращает тип записей, полученных от пользователя.
	 * @param items Элемент с записями.
	 * @return Тип записей или null.
	 */
	private ItemType getItemType(Element items){
		String strtype = ((Element)items.getFirstChild()).getTagName().toUpperCase();
		try {
			itemtype = ItemType.valueOf(strtype);
		} catch (Exception e) {
			request = RequestType.WRONG;
			return null;
		}
		return itemtype;
	}
	
	/**
	 * Возвращает номер ставка, за которым привязан пользователь с указанным
	 * именем и паролем.
	 * @param logger Логгер для вывода сообщений об ошибке.
	 * @param dbmanager Менеджер БД для выполнения запроса.
	 */
	private void executeAuthorization(ServerLogger logger, DBManager dbmanager){
		if(dbmanager.isConnected()){
			userpondid = dbmanager.getUserPondID(username, password);
			requestexecuted = true;
		} else {
			logger.setMessage("Обработчик запросов", "Не удалось выполнить запрос из-за недоступности БД!");
		}
	}
	
	/**
	 * Выполняет занесение данных в БД.
	 * @param logger Логгер для вывода сообщений об ошибке.
	 * @param dbmanager Менеджер БД для выполнения запроса.
	 */
	private void executeUpdate(ServerLogger logger, DBManager dbmanager){
		if(dbmanager.isConnected()){
			userpondid = dbmanager.getUserPondID(username, password);
			if(userpondid!=-1){
				switch (itemtype) {
				case POND:{
					executePondUpdate(dbmanager);
					break;
				}
				case MONITOR:{
					executeMonitorUpdate(dbmanager);
					break;
				}
				}
			}
		} else {
			logger.setMessage("Обработчик запросов", "Не удалось выполнить запрос из-за недоступности БД!");
		}
	}
	
	/**
	 * Выполняет обновление записи ставка. Обновление выполняется
	 * только в случае если пользователь привязан к этому ставку.
	 * @param dbmanager Менеджер БД.
	 */
	private void executePondUpdate(DBManager dbmanager){
		if(updatepond.getId()==userpondid){
			dbmanager.setItems(Arrays.asList(updatepond));
			requestexecuted = true;
		}
	}
	
	/**
	 * Выполняет обновление записей мониторинга. Обновление выполняется
	 * только в случае если все записи соответствуют номеру пользователя.
	 * @param dbmanager Менеджер БД.
	 */
	private void executeMonitorUpdate(DBManager dbmanager){
		for(Monitor m: inputitems){
			if(!m.getValue(0).equals(userpondid)){
				return;
			}
		}
		dbmanager.setItems(inputitems);
		requestexecuted = true;
	}
	
	/**
	 * Загружает список ставков для передачи клиенту.
	 * @param logger Логгер для вывода сообщений об ошибке.
	 * @param dbmanager Менеджер БД для выполнения запроса.
	 */
	private void executePondRequest(ServerLogger logger, DBManager dbmanager){
		if(dbmanager.isConnected()){
			outputitems = dbmanager.getAllPonds();
			requestexecuted = true;
		} else {
			logger.setMessage("Обработчик запросов", "Не удалось выполнить запрос из-за недоступности БД!");
		}
	}
	
	/**
	 * Загружает список мониторинга для передачи клиенту.
	 * @param logger Логгер для вывода сообщений об ошибке.
	 * @param dbmanager Менеджер БД для выполнения запроса.
	 */
	private void executeMonitorRequest(ServerLogger logger, DBManager dbmanager){
		if(dbmanager.isConnected()){
			outputitems = dbmanager.getAllMonitors();
			requestexecuted = true;
		} else {
			logger.setMessage("Обработчик запросов", "Не удалось выполнить запрос из-за недоступности БД!");
		}
	}
	
	/**
	 * Возвращает значение элемента.
	 * @param element Элемент со значением.
	 * @return Строковое значение элемента.
	 */
	public static String getString(Element element){
		Text text = (Text) element.getFirstChild();
		return text.getData().trim();
	}
	
	/**Тип запроса*/
	private RequestType request = RequestType.NONE;
	/**Флаг выполнения запроса*/
	private boolean requestexecuted = false;
	/**Номер ставка пользователя*/
	private int userpondid = -1;
	/**Тип полученных записей*/
	private ItemType itemtype;
	/**Имя пользователя для авторизации(если добавление записей)*/
	private String username;
	/**Пароль пользователя для авторизации(если добавление записей)*/
	private int password;
	/**Список элементов для отправки клиенту*/
	private List<? extends BasicItem> outputitems;
	/**Список элементов полученых от клиента*/
	private List<Monitor> inputitems;
	/**Объект ставка, подлежащий изменению*/
	private Pond updatepond;
	/**Состояние соединения сервера*/
	private Response serverresponse = Response.ACCEPT;
	private static final String ROOTNAME = "server";
	private static final String ROOTVERSIONATTR = "version";
	private static final String DATAELEMENT = "data";
	private static final String CONNECTIONELEMENT = "connection";
	private static final String RESPONSEELEMENT = "response";
	private static final String STATEELEMENT = "state";
	private static final String SUBJECTELEMENT = "subject";
	private static final String PIDELEMENT = "pid";
	private static final String ITEMSELEMENT = "items";
	/**Нумерация элемента запроса*/
	private static enum ItemType {POND, MONITOR}
	/**Версия обработчика документа*/
	private static String ROOTVERSION = "1";

}