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
	 * ����������� ������ ������ �� ������ �������.
	 * ������������ �������� ��� ������ �������� ��������� �������.
	 * @param response ����� ������� �� ����������� �������.
	 */
	public TaskHandler(Response response){
		serverresponse = response;
	}
	
	/**
	 * ����������� ������ �� ���������, ���������� �� �������.
	 * @param doc �������� �������.
	 * @throws Exception �������� ���������� �� ������� �������.
	 */
	public TaskHandler(Document doc) throws Exception{
		Element root = doc.getDocumentElement();
		checkVersion(root);
		checkConnection(root);
		parseRequest(root);
	}
	
	/**
	 * ��������� ���������� ������. �������� ���������� ���������, � ����������� �� ���� �������.
	 * @param logger ������ ��� ������ ���������.
	 * @param dbmanager �������� �� ��� ���������� �������.
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
	 * ��������� �������� �������� ��� �������� �������.
	 * @param docbuilder ��� �������� ���������.
	 * @return ��������, ������� � �������� �������.
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
	 * ���������� ���� ���������� �������.
	 * @return true - ���� ������ �������� ����������� �������.
	 */
	public boolean isReject(){
		return serverresponse == Response.REJECT;
	}
	
	/**
	 * ��������� ������ ���������, ����������� �� �������.
	 * @param root �������� ������� ���������.
	 * @throws Exception ���� ������ �� ���������.
	 */
	private void checkVersion(Element root) throws Exception{
		String docversion = root.getAttribute(ROOTVERSIONATTR);
		if(!docversion.equals(ROOTVERSION)){
			TaskExceptionFactory.documentVersion();
		}
	}
	
	/**
	 * ��������� ��������� ���������� � ��������.
	 * @param root �������� ������� ���������.
	 * @throws Exception ���� ������ ������ ������� ����������.
	 */
	private void checkConnection(Element root) throws Exception{
		Element data = (Element) root.getFirstChild();
		String connstr = getString((Element) data.getFirstChild());
		if(!connstr.equals(Response.ACCEPT.toString())){
			TaskExceptionFactory.clientDisconnected();
		}
	}
	
	/**
	 * ��������� ��� ������� �� ������� � �������������� ���������.
	 * ���� ������ ������ �� ��������� ������� - ������ ��������������
	 * ������� ��� ������� ���������
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
	 * ��������� ������ ��� ����������� �������. �������� � ��������� �����
	 * ��������� ������������ � 2 �������: ����������� � ��������� ������.
	 * @param username ������� � ������ ������������.
	 * @param password ������� � ������� ������������.
	 * @return true - ���� ������ �������� �������.
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
	 * ��������� ����� ��������� � ��������.
	 * @param root �������� ������� ���������.
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
	 * ���������� ��� �������, ���������� �� ������������.
	 * @param items ������� � ��������.
	 * @return ��� ������� ��� null.
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
	 * ���������� ����� ������, �� ������� �������� ������������ � ���������
	 * ������ � �������.
	 * @param logger ������ ��� ������ ��������� �� ������.
	 * @param dbmanager �������� �� ��� ���������� �������.
	 */
	private void executeAuthorization(ServerLogger logger, DBManager dbmanager){
		if(dbmanager.isConnected()){
			userpondid = dbmanager.getUserPondID(username, password);
			requestexecuted = true;
		} else {
			logger.setMessage("���������� ��������", "�� ������� ��������� ������ ��-�� ������������� ��!");
		}
	}
	
	/**
	 * ��������� ��������� ������ � ��.
	 * @param logger ������ ��� ������ ��������� �� ������.
	 * @param dbmanager �������� �� ��� ���������� �������.
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
			logger.setMessage("���������� ��������", "�� ������� ��������� ������ ��-�� ������������� ��!");
		}
	}
	
	/**
	 * ��������� ���������� ������ ������. ���������� �����������
	 * ������ � ������ ���� ������������ �������� � ����� ������.
	 * @param dbmanager �������� ��.
	 */
	private void executePondUpdate(DBManager dbmanager){
		if(updatepond.getId()==userpondid){
			dbmanager.setItems(Arrays.asList(updatepond));
			requestexecuted = true;
		}
	}
	
	/**
	 * ��������� ���������� ������� �����������. ���������� �����������
	 * ������ � ������ ���� ��� ������ ������������� ������ ������������.
	 * @param dbmanager �������� ��.
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
	 * ��������� ������ ������� ��� �������� �������.
	 * @param logger ������ ��� ������ ��������� �� ������.
	 * @param dbmanager �������� �� ��� ���������� �������.
	 */
	private void executePondRequest(ServerLogger logger, DBManager dbmanager){
		if(dbmanager.isConnected()){
			outputitems = dbmanager.getAllPonds();
			requestexecuted = true;
		} else {
			logger.setMessage("���������� ��������", "�� ������� ��������� ������ ��-�� ������������� ��!");
		}
	}
	
	/**
	 * ��������� ������ ����������� ��� �������� �������.
	 * @param logger ������ ��� ������ ��������� �� ������.
	 * @param dbmanager �������� �� ��� ���������� �������.
	 */
	private void executeMonitorRequest(ServerLogger logger, DBManager dbmanager){
		if(dbmanager.isConnected()){
			outputitems = dbmanager.getAllMonitors();
			requestexecuted = true;
		} else {
			logger.setMessage("���������� ��������", "�� ������� ��������� ������ ��-�� ������������� ��!");
		}
	}
	
	/**
	 * ���������� �������� ��������.
	 * @param element ������� �� ���������.
	 * @return ��������� �������� ��������.
	 */
	public static String getString(Element element){
		Text text = (Text) element.getFirstChild();
		return text.getData().trim();
	}
	
	/**��� �������*/
	private RequestType request = RequestType.NONE;
	/**���� ���������� �������*/
	private boolean requestexecuted = false;
	/**����� ������ ������������*/
	private int userpondid = -1;
	/**��� ���������� �������*/
	private ItemType itemtype;
	/**��� ������������ ��� �����������(���� ���������� �������)*/
	private String username;
	/**������ ������������ ��� �����������(���� ���������� �������)*/
	private int password;
	/**������ ��������� ��� �������� �������*/
	private List<? extends BasicItem> outputitems;
	/**������ ��������� ��������� �� �������*/
	private List<Monitor> inputitems;
	/**������ ������, ���������� ���������*/
	private Pond updatepond;
	/**��������� ���������� �������*/
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
	/**��������� �������� �������*/
	private static enum ItemType {POND, MONITOR}
	/**������ ����������� ���������*/
	private static String ROOTVERSION = "1";

}