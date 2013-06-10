package ua.vladaxon.connections;

public abstract class TaskExceptionFactory {
	
	/**
	 * Создает исключение при различных версиях документа.
	 * @return Исключение.
	 */
	public static void documentVersion() throws Exception{
		throw new VersionDocumentException();
	}
	
	/**
	 * Создает исключение если сервер отклонил подключение клиента.
	 * @return Исключение.
	 */
	public static void clientRejected() throws Exception{
		throw new ClientRejectedException();
	}
	
	/**
	 * Создает исключение если сервер отклонил подключение клиента.
	 * @return Исключение.
	 */
	public static void dBDisconnected() throws Exception{
		throw new DBDisconnectedEception();
	}
	
	/**
	 * Создает исключение при запросе клиента об отключении от сервера.
	 * @return Исключение.
	 */
	public static void clientDisconnected() throws Exception{
		throw new ClientDisconnectException();
	}
	
	/**
	 * Создает исключение при разрыве связи с клиентом.
	 * @return Исключение.
	 */
	public static void connectionReset() throws Exception{
		throw new ConnectionResetException();
	}
	
	private static class VersionDocumentException extends Exception{

		@Override
		public String getMessage() {
			return "Клиент отключен из-за несовместимой версии протокола обмена.";
		}
		
		private static final long serialVersionUID = 1L;
		
	}
	
	private static class ClientRejectedException extends Exception{

		@Override
		public String getMessage() {
			return "Клиент был отклонен сервером из-за недостатка клиентских слотов.";
		}

		private static final long serialVersionUID = 1L;
		
	}
	
	private static class DBDisconnectedEception extends Exception{

		@Override
		public String getMessage() {
			return "Клиент был отклонен сервером из-за недоступности базы данных.";
		}

		private static final long serialVersionUID = 1L;
		
	}
	
	private static class ClientDisconnectException extends Exception{

		@Override
		public String getMessage() {
			return "Клиент отключен по собственному запросу.";
		}

		private static final long serialVersionUID = 1L;
		
	}
	
	private static class ConnectionResetException extends Exception{

		@Override
		public String getMessage() {
			return "Соединение разорвано.";
		}
		
		private static final long serialVersionUID = 1L;

	}

}