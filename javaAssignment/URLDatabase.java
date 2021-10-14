package javaAssignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import org.json.simple.*;

public class URLDatabase {
    //initialising commands.
	public static final String storeUrlCommand= "storeurl";
	public static final String geturlCommand = "get";
	public static final String getCountCommand = "count";
	public static final String getListUrlCommand = "list";
	public static final String getCommandList = "commands";
	public static final String exitCommand = "exit";
	public static final String userPromptCommand = "Please enter the command.(type 'commands' to see available commands.)";
    public static final String unAvailableCommandPrompt = "Unavailable Command.Please try again.(type 'commands' to see available commands.)";
	public static final String getUrlInof = "Please enter command with url";
	public static final String duplicateUrlInfo = "Url already exists.";
	public static final String urlNotStoredInfo = "Url is not found.";
	public static final String urlEmptyInfo = "You havent saved any url.";
	public static final String storeUrlCompleteInfo = "Stored successfully";
    public static void main (String args[]) {
		System.out.println(userPromptCommand);
		//scanner to read from console/commandline.
		Scanner scanner =  new Scanner(System.in);
		//url map containing url and its data(unique key and usage count)
		Map<String,ArrayList<String>> urlMap = new HashMap<String,ArrayList<String>>();
		try {
			//read until exit
			while(true) {    		
				String input= scanner.nextLine().trim();
				ArrayList<String> urlData=new ArrayList<String>(); //store unique key and usage count
				String command=""; //input command by user. upon unavailable commands,prompted again.
				String url=""; //input url by user
				int count=0;   //usage count
				
    		    //getting command and url from user input
				if(input!=null && input.length()>0) {
					String[] inputArray=input.split(" ");
					command=inputArray[0];
					url=(inputArray.length==2)?inputArray[1]:"";	
				}
				//if user wants to list available commands
				if(command.equals(getCommandList)) {
					availableCommands();
				}
				//storeurl command
				else if(command.equals(storeUrlCommand)){
					if(url.length()==0) {
						printInfo(getUrlInof);
						continue;
					}
					storeUrl(url, urlMap, urlData, count);
				}
				//get command to display url key and increment usage count
				else if(command.equals(geturlCommand)){
					if(url.length()==0) {
						printInfo(getUrlInof);
						continue;
					}
					getUrlInfo(url,urlMap,urlData);
				}
				//get usage count
				else if(command.equals(getCountCommand)){
					if(url.length()==0) {
						printInfo(getUrlInof);
						continue;
					}
					geturlCount(url,urlMap,urlData);
				}
				//list all urls
				else if(command.equals(getListUrlCommand)){	
					listUrl(urlMap);
				}
				//exit 
				else if(command.equals(exitCommand)) {	
					printInfo("Exiting.....");
					break;
				}
				//exit if any any unavailable command is entered.
				else {
					printInfo(unAvailableCommandPrompt);
				}

			}

		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			scanner.close(); //closing scanner to avoid memory leak.
		}
	}

	/*
	 * Method to list available commands
	 */
	public static void availableCommands() {
		System.out.println(storeUrlCommand);
		System.out.println(geturlCommand);
		System.out.println(getCountCommand);
		System.out.println(getListUrlCommand);
		System.out.println(exitCommand);
		System.out.println(userPromptCommand);	
	}
	
	/*
	 * method to store urls
	 * @param url
	 * @param urlMap to store urls
	 * @param urlData
	 * @param count to get usage count
	 */
	public static void storeUrl(String url,Map<String,ArrayList<String>> urlMap,ArrayList<String> urlData, int count) {
		if(urlMap.containsKey(url)) {
			printInfo(duplicateUrlInfo);
		} else {
			urlData.add(getUniqueShortKey(url));
			urlData.add(Integer.toString(count));
			urlMap.put(url,urlData);
			printInfo(storeUrlCompleteInfo);
		}
	}
	
	
	/*
	 * method to get url key 
	 * @param url 
	 * @param urlMap - existing urls
	 * @param urlData
	 */
	public static void getUrlInfo(String url,Map<String,ArrayList<String>> urlMap,ArrayList<String> urlData) {
		if(!urlMap.isEmpty()) {    //checking whether urlMap is empty
			urlData=urlMap.get(url);
			if(urlData!=null) {
				if(urlData.size()>1) {
					int countAfterIncrement = Integer.parseInt(urlData.get(1))+1;
					String uniqueKey = urlData.get(0);
					System.out.println("key corresponding to "+url+" is :"+uniqueKey);
					urlData.set(1,Integer.toString(countAfterIncrement));
					urlMap.put(url, urlData);
				} else {
					printInfo(urlNotStoredInfo);
				}
			} else {
				printInfo(urlNotStoredInfo);
			}
		} else {
			printInfo(urlNotStoredInfo);
		}
	}
	

	/*
	 * method to get the usage count
	 */
	public static void geturlCount(String url,Map<String,ArrayList<String>> urlMap,ArrayList<String> urlData) {
		if(!urlMap.isEmpty() && urlMap.get(url)!=null) { 
			System.out.println("The usage count for "+url+" is "+urlMap.get(url).get(1));
			
			} else {
				printInfo(urlNotStoredInfo);
			
			}
	}
	
	/*
	 * method to list urls alonng with usage count
	 * @param urlMap
	 */
	public static void listUrl(Map<String,ArrayList<String>> urlMap) {
		if(!urlMap.isEmpty()) {
			JSONObject json = new JSONObject();
			for(Map.Entry<String, ArrayList<String>> e : urlMap.entrySet()) {			
				ArrayList<String> urldetails = (ArrayList<String>)e.getValue();
				json.put(e.getKey(),  urldetails.get(1));
			}
			System.out.println(json);
		} else {
			printInfo(urlEmptyInfo);
		}
	}
	
	
	
	/*
	 * method to create unique url key.
	 * @param url
 	* @Returns the unique key
 	*/
	public static String getUniqueShortKey(String url) {
		StringBuilder sb=new StringBuilder();
		int lengthOfUniqueKey=5; //setting length for key
		ArrayList<String> urlArray= new ArrayList<String>(Arrays.asList(url.split("")));

		for(int i=0;i<lengthOfUniqueKey;i++) {
			int num=(int)Math.round(Math.random() * (url.length()-1) + 0); //finding random index 
			sb.append(urlArray.get(num)); //creating key with elements at random index. Uniqueness is ensured.
		}
		return sb.toString();
	}
	
	/*
	 * method for user alerts
	 */
	public static void printInfo(String info){
		System.out.println(info);
	}
	
}
