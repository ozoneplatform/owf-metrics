package ozone.owf;

import java.io.Serializable;

public class Configuration implements Serializable {

	private static final long serialVersionUID = 1L;
	private String casLocation = null;
	private long log4jWatchTime = 30000;
    private String logoutURL = "";
	private String isTestMode = null;
    private String alternateHostName = "";
    private String testSwarmUrl = null;
    private String serverVersion = null;
    private String defaultTheme = null;
    private String lastLoginText = "(last login: [lastLoginDate])";
    private String lastLoginDateFormat = "F j, Y, g:i A";

    public long getLog4jWatchTime() {
		return log4jWatchTime;
	}

	public void setLog4jWatchTime(long log4jWatchTime) {
		this.log4jWatchTime = log4jWatchTime;
	}

	public String getCasLocation() {
		return casLocation;
	}

	public void setCasLocation(String casLocation) {
		this.casLocation = casLocation;
	}

    public String getLogoutURL() {
    	return logoutURL;
    }

    public void setLogoutURL(String logoutURL) {
    	this.logoutURL = logoutURL;
    }
	
	public String getIsTestMode() {
	    return isTestMode;
	}
	
	public void setIsTestMode(String isTestMode) {
	    this.isTestMode = isTestMode;
	}
	
    public String getAlternateHostName() {
      return alternateHostName;
    }
  
    public void setAlternateHostName(String alternateHostName) {
    	this.alternateHostName = alternateHostName;
    }

    public String getTestSwarmUrl() {
    	return testSwarmUrl;
    }

    public void setTestSwarmUrl(String testSwarmUrl) {
    	this.testSwarmUrl = testSwarmUrl;
    }

	public String getServerVersion() {
	    return serverVersion;
	}
	
	public void setServerVersion(String serverVersion) {
	    this.serverVersion = serverVersion;
	}

	/**
	 * @return the defaultTheme
	 */
	public String getDefaultTheme() {
		return defaultTheme;
	}

	/**
	 * @param defaultTheme the defaultTheme to set
	 */
	public void setDefaultTheme(String defaultTheme) {
		this.defaultTheme = defaultTheme;
	}
  
	public String getLastLoginText() {
		return lastLoginText;
	}
   
	public void setLastLoginText(String in) {
		this.lastLoginText = in;
	}
   
	public String getLastLoginDateFormat() {
		return lastLoginDateFormat;
	}
   
	public void setLastLoginDateFormat(String in) {
		this.lastLoginDateFormat = in;
	}
}
