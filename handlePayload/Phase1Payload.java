package handlePayload;

public class Phase1Payload extends Payload {

	
	
	public Phase1Payload(Byte phase, Byte type, String message) {
		super(phase, type, message);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public byte getPhase() {
		// TODO Auto-generated method stub
		return super.getPhase();
	}

	@Override
	public void setPhase(byte phase) {
		// TODO Auto-generated method stub
		super.setPhase(phase);
	}

	@Override
	public byte getType() {
		// TODO Auto-generated method stub
		return super.getType();
	}

	@Override
	public void setType(byte type) {
		// TODO Auto-generated method stub
		super.setType(type);
	}

	@Override
	public int getMessageLength() {
		// TODO Auto-generated method stub
		return super.getMessageLength();
	}

	@Override
	public void setMessageLength(int messageLength) {
		// TODO Auto-generated method stub
		super.setMessageLength(messageLength);
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}

	@Override
	public void setMessage(String message) {
		// TODO Auto-generated method stub
		super.setMessage(message);
	}
	public void payloadInfoClient(Payload payload) {
	
		
		System.out.println("phase: " + this.phase + " message type: authRequest" + " message length: "+ messageLength);
		
		 
	}
	
	public void payloadInfoServer(Payload payload) {
	
		String temp = "type";
		
		if(type == 1) {
			
			temp="auth_Challange";
			
		}
		else if(type == 2) {
			
			temp="auth_Fail";
			
		}
		else if(type == 3) {
			
			temp="auth_Success";

		}

		System.out.println("phase: " + this.phase + " message type:"+ temp  + " message length: "+ messageLength);
		
		 
	}
	
}
