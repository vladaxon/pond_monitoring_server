package ua.vladaxon.connections;

public abstract class TaskExceptionFactory {
	
	/**
	 * ������� ���������� ��� ��������� ������� ���������.
	 * @return ����������.
	 */
	public static void documentVersion() throws Exception{
		throw new VersionDocumentException();
	}
	
	/**
	 * ������� ���������� ���� ������ �������� ����������� �������.
	 * @return ����������.
	 */
	public static void clientRejected() throws Exception{
		throw new ClientRejectedException();
	}
	
	/**
	 * ������� ���������� ���� ������ �������� ����������� �������.
	 * @return ����������.
	 */
	public static void dBDisconnected() throws Exception{
		throw new DBDisconnectedEception();
	}
	
	/**
	 * ������� ���������� ��� ������� ������� �� ���������� �� �������.
	 * @return ����������.
	 */
	public static void clientDisconnected() throws Exception{
		throw new ClientDisconnectException();
	}
	
	/**
	 * ������� ���������� ��� ������� ����� � ��������.
	 * @return ����������.
	 */
	public static void connectionReset() throws Exception{
		throw new ConnectionResetException();
	}
	
	private static class VersionDocumentException extends Exception{

		@Override
		public String getMessage() {
			return "������ �������� ��-�� ������������� ������ ��������� ������.";
		}
		
		private static final long serialVersionUID = 1L;
		
	}
	
	private static class ClientRejectedException extends Exception{

		@Override
		public String getMessage() {
			return "������ ��� �������� �������� ��-�� ���������� ���������� ������.";
		}

		private static final long serialVersionUID = 1L;
		
	}
	
	private static class DBDisconnectedEception extends Exception{

		@Override
		public String getMessage() {
			return "������ ��� �������� �������� ��-�� ������������� ���� ������.";
		}

		private static final long serialVersionUID = 1L;
		
	}
	
	private static class ClientDisconnectException extends Exception{

		@Override
		public String getMessage() {
			return "������ �������� �� ������������ �������.";
		}

		private static final long serialVersionUID = 1L;
		
	}
	
	private static class ConnectionResetException extends Exception{

		@Override
		public String getMessage() {
			return "���������� ���������.";
		}
		
		private static final long serialVersionUID = 1L;

	}

}