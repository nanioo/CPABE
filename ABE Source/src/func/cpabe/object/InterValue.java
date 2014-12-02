package func.cpabe.object;

public class InterValue {
	
	private String pubKey;			/*public key*/
	private String masKey;
	private String attKey;
	private String priKey;
	private String encrypted;
	public String getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}

	private String plaintext;
	
	public InterValue(){
		pubKey = null;
		masKey = null;
		attKey = null;
		priKey = null;
		encrypted = null;
		plaintext = null;
	}
	
	/**
	 * 
	 * @Title: printToString 
	 * @Description: TODO(show all attributes) 
	 * @param:     
	 * @return: void    
	 * @throws
	 */
	public void printToString(){
		System.out.println("public key is :"+ this.pubKey);
		System.out.println("master key is :"+ this.masKey);
		System.out.println("attribute key is :"+ this.attKey);
		System.out.println("private key is :"+ this.priKey);
		System.out.println("encrypted1 key is :"+ this.encrypted);
		System.out.println("recovery plaintext key is :"+ this.plaintext);
	}
	
	public String getPubKey() {
		return pubKey;
	}

	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}

	public String getMasKey() {
		return masKey;
	}

	public void setMasKey(String masKey) {
		this.masKey = masKey;
	}

	public String getAttKey() {
		return attKey;
	}

	public void setAttKey(String attKey) {
		this.attKey = attKey;
	}

	public String getPriKey() {
		return priKey;
	}

	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}

	public String getPlaintext() {
		return plaintext;
	}

	public void setPlaintext(String plaintext) {
		this.plaintext = plaintext;
	}
}
