package cx.it.hyperbadger.spacezombies.launcher;

public class Download {
	private DownloadType type;
	private int version;
	private String name;
	private String url;
	public Download(DownloadType type, String name, String version, String url){
		this.type = type;
		this.name = name;
		this.version = Integer.parseInt(version);
		this.url = url;
	}
	public DownloadType getType(){
		return type;
	}
	public String getName(){
		return name;
	}
	public int getVersion(){
		return version;
	}
	public String getLocation(){
		switch(type){
		case JAR:
			return Main.location+System.getProperty("file.separator")+this.name;
		case NATIVE:
			return Main.location+System.getProperty("file.separator")+"native"+System.getProperty("file.separator")+this.name;
		default:
			return null;
		}
	}
	public String getURL(){
		return url;
	}
}
