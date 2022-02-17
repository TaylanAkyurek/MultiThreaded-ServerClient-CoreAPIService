package handlePayload;

import java.io.Serializable;

public class Payload implements Serializable{
	
	 public byte phase;
	 public byte type;
	 public int messageLength;
	 public String message;
	
	 
	 
	 public Payload(byte phase, byte type, int messageLength, String message) {
		super();
		this.phase = phase;
		this.type = type;
		this.messageLength = messageLength;
		this.message = message;
	}

	public Payload(byte phase, byte type, String message) {
	        this(phase, type, message.length(), message);
	 }

	 public String toString() {
	        return message;
	 }

	public byte getPhase() {
		return phase;
	}

	public void setPhase(byte phase) {
		this.phase = phase;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void payloadInfoClient(Payload payload) {
		// TODO Auto-generated method stub
		System.out.println(this.toString());
		
	}
	public void payloadInfoServer(Payload payload) {
		// TODO Auto-generated method stub
		System.out.println(this.toString());

	}
	
	 
	
}
