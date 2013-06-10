package ua.vladaxon;

import java.awt.EventQueue;

import ua.vladaxon.connections.ConnectionManager;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.ui.ServerUI;

/**
 * Главный класс севрера. Создает основные компоненты.
 */
public class Server {
	
	private ServerLogger logger = null;
	private PropManager props = null;
	private ServerUI serverui = null;
	private DBManager dbmanager = null;
	
	public Server(){
		logger = new ServerLogger();
		props = new PropManager(logger);
		serverui = new ServerUI(logger);
		dbmanager = new DBManager(logger, props, serverui.getActions());
		ConnectionManager.createInstance(props, logger, dbmanager);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {	
			@Override
			public void run() {
				new Server();
			}
		});
	}
	
	static{
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}