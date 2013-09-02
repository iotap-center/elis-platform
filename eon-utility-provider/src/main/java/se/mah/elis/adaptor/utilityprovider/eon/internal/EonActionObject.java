package se.mah.elis.adaptor.utilityprovider.eon.internal;

public class EonActionObject {

	private long id;
	private EonActionStatus status;
	private String message;
	
	public EonActionObject(long id, EonActionStatus s, String message) {
		this.id = id;
		this.status = s;
		this.message = message;
	}
	
	public EonActionObject(long id, EonActionStatus s) {
		this(id, s, null);
	}

	public long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public EonActionStatus getStatus() {
		return this.status;
	}
	
	

}
