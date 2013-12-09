package se.mah.elis.adaptor.building.ninjablock.beans;

public class LastDataBean {
    private String DA;
    private String timestamp;
  
    public float getValueAsFloat() {
    	return Float.parseFloat(DA);
    }
    
    public String getStringValue()	{
    	return DA;
    }
}
