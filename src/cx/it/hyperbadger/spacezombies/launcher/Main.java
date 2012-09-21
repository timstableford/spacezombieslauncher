package cx.it.hyperbadger.spacezombies.launcher;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
	public static final String loginURL = "http://spacezombies.hyperbadger.it.cx/login.php";
	public static final String title = "SpaceZombies";
	private final String versionFile = "version.xml";
	public static final String location = System.getProperty("user.home")+System.getProperty("file.separator")+Main.title;;
	private ArrayList<Download> oldFile;
	private static final String versionURL = "http://spacezombies.hyperbadger.it.cx/download/download.php?os="+System.getProperty("os.name").toLowerCase();
	private static String[] args;
	private GUI gui;
	private Progress progress;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new Main();
		Main.args = args;
	}
	public Main() throws IOException{
		gui = new GUI();
		progress = (Progress)gui;
		setupGame();
		progress.setTitle("SpaceZombies Launcher - Launching Game");
		launchGame(null);
		gui.dispose();
	}
	private void launchGame(ArrayList<String> parameters) throws IOException{
		ProcessBuilder processBuilder = new ProcessBuilder();
		ArrayList<String> commands = new ArrayList<String>();
		commands.add("java");
		commands.add("-jar");
		if(Main.args!=null&&Main.args.length>0){
			for(String a: Main.args){
				commands.add(a);
			}
		}
		commands.add(location+System.getProperty("file.separator")+"spacezombies.jar");
		if(parameters!=null&&parameters.size()>0){
			for(String s: parameters){
				commands.add(s);
			}
		}
		//commands.addAll(Arrays.asList(args));
		System.out.println(commands);
		processBuilder.command(commands);
		processBuilder.start();
	}
	private void setupGame() throws IOException{
		//check and create directory structure
		progress.setMax(2);
		progress.setCurrent(0);
		progress.setTitle("SpaceZombies Launcher - Checking directories");
		File layerOne = new File(Main.location);
		if(!layerOne.exists()){
			layerOne.mkdir();
		}
		progress.setCurrent(1);
		File nativeLocation = new File(Main.location+System.getProperty("file.separator")+"native");
		if(!nativeLocation.exists()){
			nativeLocation.mkdir();
		}
		File resourceLocation = new File(Main.location+System.getProperty("file.separator")+"resources");
		if(!resourceLocation.exists()){
			resourceLocation.mkdir();
		}
		progress.setCurrent(2);
		progress.setTitle("SpaceZombies Launcher - Checking for Updates");
		progress.setCurrent(0);
		//load old file versions
		oldFile = new ArrayList<Download>();
		File vF = new File(location+System.getProperty("file.separator")+versionFile);
		if(vF.exists()){
			readXML(oldFile, vF);
		}
		progress.setCurrent(1);
		//new file
		File temp = File.createTempFile("version", "xml");
		URL place = new URL(Main.versionURL);
		FileUtils.copyURLToFile(place, temp);
		ArrayList<Download> newFile = new ArrayList<Download>();
		readXML(newFile,temp);
		progress.setCurrent(2);
		//get files for update
		ArrayList<Download> r = getFilesForUpdate(oldFile, newFile);
		if(r.size()>0){
			progress.setTitle("SpaceZombies Launcher - Updating");
			progress.setMax(r.size());
			int i=0;
			progress.setCurrent(i);
			FileUtils.copyFile(temp, vF);
			System.out.println("Updating");
			for(Download d: r){
				System.out.println("Updating "+d.getName());
				URL c = new URL(d.getURL());
				FileUtils.copyURLToFile(c, new File(d.getLocation()));
				i++;
				progress.setCurrent(i);
			}
			System.out.println("Update Complete");
		}
		progress.setTitle("SpaceZombies Launcher - Checking Files");
		progress.setMax(1);
		progress.setCurrent(0);
		checkFiles(oldFile);
		progress.setCurrent(1);
	}
	private void checkFiles(ArrayList<Download> down) throws MalformedURLException, IOException{
		for(Download d: down){
			File a = new File(d.getLocation());
			if(!a.exists()){
				System.out.println("File does not exist "+d.getName()+": Downloading");
				FileUtils.copyURLToFile(new URL(d.getURL()), a);
			}
		}
	}
	private void readXML(ArrayList<Download> dA, File file){
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			//natives
			processNatives(doc, dA);
			//jars
			processJars(doc,dA);
		  } catch (Exception e) {
			e.printStackTrace();
		  }
	}
	private void processJars(Document doc,ArrayList<Download> dA){
		NodeList nList = doc.getElementsByTagName("jar");
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				dA.add(new Download(DownloadType.JAR,
						getTagValue("name", eElement),
						getTagValue("version", eElement),
						getTagValue("path", eElement)
						));
			}
		}
	}
	private void processResources(Document doc,ArrayList<Download> dA){
		NodeList nList = doc.getElementsByTagName("resource");
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				dA.add(new Download(DownloadType.RESOURCE,
						getTagValue("name", eElement),
						getTagValue("version", eElement),
						getTagValue("path", eElement)
						));
			}
		}
	}
	private void processNatives(Document doc,ArrayList<Download> dA){
		NodeList nList = doc.getElementsByTagName("native");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				dA.add(new Download(DownloadType.NATIVE,
						getTagValue("name", eElement),
						getTagValue("version", eElement),
						getTagValue("path", eElement)
						));
			}
		}
	}
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}
	private ArrayList<Download> getFilesForUpdate(ArrayList<Download> old, ArrayList<Download> newer){
		ArrayList<Download> r = new ArrayList<Download>();
		for(Download d: newer){
			Download a = findDownload(old,d);
			if(a==null||a.getVersion()<d.getVersion()){
				r.add(d);
			}
		}
		return r;
	}
	private Download findDownload(ArrayList<Download> d, Download c){
		for(Download a: d){
			if(a.getName().equals(c.getName())){
				return a;
			}
		}
		return null;
	}
}
