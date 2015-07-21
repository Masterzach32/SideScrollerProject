package net.masterzach32.sidescroller.api;

/**
 * 
 *
 */
public interface IUpdatable {

	public String getLocalVersion();
	
	public String getServerVersionURL();
	
	public String getUpdateURL();
	
	public String getDownloadURL();
	
}
