package Examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import Examples.Restaurants.Restaurant;

public class Reasoner {

	// The main Class Object holding the Domain knowledge

	// Generate the classes automatically with: Opening a command console and
	// type:
	// Path to YOUR-PROJECTROOT-IN-WORKSPACE\xjc.bat yourschemaname.xsd -d src
	// -p yourclasspackagename

	public Restaurants therestaurant; //This is a candidate for a name change

	public SimpleGUI Myface;

	
	public List restaurantList = new ArrayList();    //This is a candidate for a name change
	public List<Restaurants.Restaurant> totalrestaurant = new ArrayList();  //This is a candidate for a name change
	public List<Restaurants.Restaurant.Food> foodList = new ArrayList(); //This is a candidate for a name change
	public List theLendingList = new ArrayList(); //This is a candidate for a name change
	public List theRecentThing = new ArrayList(); 

	public Vector<String> restaurantsyn = new Vector<String>();  //This is a candidate for a name change
	public Vector<String> foodsyn = new Vector<String>();
	
	public Vector<String> lendingsyn = new Vector<String>();  //This is a candidate for a name change
	public Vector<String> recentobjectsyn = new Vector<String>();

	public String questiontype = "";         // questiontype selects method to use in a query
	public List classtype = new ArrayList(); // classtype selects which class list to query
	public String attributetype = "";        // attributetype selects the attribute to check for in the query

	public Object Currentitemofinterest; // Last Object dealt with
	public Integer Currentindex;         // Last Index used

	public String tooltipstring = "";
	public String URL = "";              // URL for Wordnet site
	public String URL2 = "";             // URL for Wikipedia entry

	public Reasoner(SimpleGUI myface) {

		Myface = myface; // reference to GUI to update Tooltip-Text
		// basic constructor for the constructors sake :)
	}
	public Reasoner() {

		}

	public void initknowledge() { // load all the library knowledge from XML 

		JAXB_XMLParser xmlhandler = new JAXB_XMLParser(); // we need an instance of our parser

		//This is a candidate for a name change
		File xmlfiletoload = new File("restaurants.xml"); // we need a (CURRENT)  file (xml) to load  

		

		restaurantsyn.add("restaurant");
		restaurantsyn.add("restara");
		restaurantsyn.add("restrorant");
		restaurantsyn.add("restrorent");
		restaurantsyn.add("restraurent");
		restaurantsyn.add("restre");
		
		foodsyn.add("food");
		foodsyn.add("lunch");
		foodsyn.add("dinner");
		foodsyn.add("lanch");
		foodsyn.add("foods");
		foodsyn.add("foodsmenu");
		foodsyn.add("foodmenu");
		lendingsyn.add(" lending");   //All of the following is a candidate for a name change

		recentobjectsyn.add(" this");   //All of the following is a candidate for a name change
		recentobjectsyn.add(" that");
		recentobjectsyn.add(" him");
		recentobjectsyn.add(" her");	// spaces to prevent collision with "wHERe"	
		recentobjectsyn.add(" it");

		try {
			FileInputStream readthatfile = new FileInputStream(xmlfiletoload); // initiate input stream

			therestaurant = xmlhandler.loadXML(readthatfile);

			// Fill the Lists with the objects data just generated from the xml

			restaurantList = therestaurant.getRestaurant(); 
			
			Iterator itr = restaurantList.iterator();
			while(itr.hasNext()){
				
				Restaurants.Restaurant res = (Restaurant) itr.next();
				totalrestaurant.add(res);
				
				Iterator local = res.getFood().iterator();
				while(local.hasNext()){
					Restaurants.Restaurant.Food food = (Restaurants.Restaurant.Food) local.next();
					foodList.add(food);
				}
			}
 			
		System.out.println("List reading");
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("error in init");
		}
	}

	public  Vector<String> generateAnswer(String input) { // Generate an answer (String Vector)

		Vector<String> out = new Vector<String>();
		out.clear();                 // just to make sure this is a new and clean vector
		
		questiontype = "none";

		Integer Answered = 0;        // check if answer was generated

		Integer subjectcounter = 0;  // Counter to keep track of # of identified subjects (classes)
		
		// Answer Generation Idea: content = Questiontype-method(classtype class) (+optional attribute)

		// ___________________________ IMPORTANT _____________________________

		input = input.toLowerCase(); // all in lower case because thats easier to analyse
		
		// ___________________________________________________________________

		String answer = "";          // the answer we return

		// ----- Check for the kind of question (number, location, etc)------------------------------

		if (input.contains("how many")){questiontype = "amount"; input = input.replace("how many", "<b>how many</b>");} 
		if (input.contains("number of")){questiontype = "amount"; input = input.replace("number of", "<b>number of</b>");}
		if (input.contains("amount of")){questiontype = "amount"; input = input.replace("amount of", "<b>amount of</b>");} 
		if (input.contains("count")){questiontype = "amount"; input = input.replace("count", "<b>count</b>");}

		if (input.contains("what kind of")){questiontype = "list"; input = input.replace("what kind of", "<b>what kind of</b>");}
		if (input.contains("list all")){questiontype = "list"; input = input.replace("list all", "<b>list all</b>");}
		if (input.contains("diplay all")){questiontype = "list"; input = input.replace("diplay all", "<b>diplay all</b>");}

		if (input.contains("is there a")){questiontype = "checkfor"; input = input.replace("is there a", "<b>is there a</b>");}
		if (input.contains("i am searching")){questiontype = "checkfor"; input = input.replace("i am searching", "<b>i am searching</b>");}
		if (input.contains("i am looking for")){questiontype = "checkfor"; input = input.replace("i am looking for", "<b>i am looking for</b>");}
		if (input.contains("do you have")&&!input.contains("how many")){questiontype = "checkfor";input = input.replace("do you have", "<b>do you have</b>");}
		if (input.contains("i look for")){questiontype = "checkfor"; input = input.replace("i look for", "<b>i look for</b>");}
		if (input.contains("is there")){questiontype = "checkfor"; input = input.replace("is there", "<b>is there</b>");}
		
		if (input.contains("vegfood") 
				|| input.contains("veg food")
				|| input.contains("nonveg food") 
				|| input.contains("non veg")
				|| input.contains("nonvegfood"))
				

		{
			questiontype = "foodClass";
			System.out.println("Find Location");
		}
		if (input.contains("is there veg food")){questiontype = "foodClass"; input = input.replace("is there veg food", "<b>is there veg food</b>");}
		if (input.contains("is there Nonveg food")){questiontype = "foodClass"; input = input.replace("is there veg food", "<b>is there veg food</b>");}
		if (input.contains("is there vegitarian food")){questiontype = "foodClass"; input = input.replace("is there veg food", "<b>is there veg food</b>");}
		if (input.contains("is there Non vegitarian food")){questiontype = "foodClass"; input = input.replace("is there Non vegitarian food", "<b>is there Non vegitarian food</b>");}
		if (input.contains("vegetarians food list")){questiontype = "foodClassVegList"; input = input.replace("vegitarians food list", "<b>vegitarians food list</b>");}
		if (input.contains("non vegetarians food list")){questiontype = "foodClassNonVegList"; input = input.replace("non vegitarians food list", "<b>non vegitarians food list</b>");}
		if (input.contains("vegetarian food list")){questiontype = "foodClassVegList"; input = input.replace("vegitarian food list", "<b>vegitarian food list</b>");}
		if (input.contains("non vegetarian food list")){questiontype = "foodClassNonVegList"; input = input.replace("non vegitarian food list", "<b>non vegitarian food list</b>");}
		if (input.contains("vegetarian list")){questiontype = "foodClassVegList"; input = input.replace("vegetarian list", "<b>vegetarian list</b>");}
		if (input.contains("non vegetarian list")){questiontype = "foodClassNonVegList"; input = input.replace("non vegetarian list", "<b>non vegetarian list</b>");}
		
		
		
		
		if (input.contains("where") 
				|| input.contains("can't find")
				|| input.contains("can i find") 
				|| input.contains("way to")
				|| input.contains("address of")
				|| input.contains("location of"))

		{
			questiontype = "location";
			System.out.println("Find Location");
		}
		if (input.contains("can i lend") 
				|| input.contains("can i borrow")
				|| input.contains("can i get the book")
				|| input.contains("am i able to")
				|| input.contains("could i lend") 
				|| input.contains("i want to lend")
				|| input.contains("i want to borrow"))

		{
			questiontype = "intent";
			System.out.println("Find FoodAvailability");
		}
		
		if (input.contains("thank you") 
				|| input.contains("bye")
				|| input.contains("thanks")
				|| input.contains("cool thank")) 			

		{
			questiontype = "farewell";
			System.out.println("farewell");
		}


		// ------- Checking the Subject of the Question --------------------------------------

		for (int x = 0; x < restaurantsyn.size(); x++) {   //This is a candidate for a name change
			if (input.contains(restaurantsyn.get(x))) {    //This is a candidate for a name change
				classtype = totalrestaurant;             //This is a candidate for a name change
				
				input = input.replace(restaurantsyn.get(x), "<b>"+restaurantsyn.get(x)+"</b>");
				
				subjectcounter = 1;
				System.out.println("Class type Restaurant recognised.");
			}
		}
		for (int x = 0; x < foodsyn.size(); x++) {  //This is a candidate for a name change
			if (input.contains(foodsyn.get(x))) {   //This is a candidate for a name change
				classtype = foodList;            //This is a candidate for a name change
				
				input = input.replace(foodsyn.get(x), "<b>"+foodsyn.get(x)+"</b>");
				
				subjectcounter = 1;
				System.out.println("Class type Food recognised.");
			}
		}
		
		
	
		System.out.println("subjectcounter = "+subjectcounter);

		if (questiontype == "amount") { // Number of Subject

			Integer numberof = Count(classtype);

			answer=("The number of "
					+ classtype.get(0).getClass().getSimpleName() + "s is "
					+ numberof + ".");

			Answered = 1; // An answer was given

		}

		if (questiontype == "list") { // List all Subjects of a kind

			answer=("You asked for the listing of all "
					+ classtype.get(0).getClass().getSimpleName() + "s. <br>"
					+ "We have the following "
					+ classtype.get(0).getClass().getSimpleName() + "s:"
					+ ListAll(classtype));
			Answered = 1; // An answer was given

		}

		if (questiontype == "checkfor") { // test for a certain Subject instance

			Vector<String> check = CheckFor(classtype, input);
			answer=(check.get(0));
			Answered = 1; // An answer was given
			if (check.size() > 1) {
				Currentitemofinterest = classtype.get(Integer.valueOf(check
						.get(1)));
				System.out.println("Classtype List = "
						+ classtype.getClass().getSimpleName());
				System.out.println("Index in Liste = "
						+ Integer.valueOf(check.get(1)));
				Currentindex = Integer.valueOf(check.get(1));
				theRecentThing.clear(); // Clear it before adding (changing) the
				// now recent thing
				theRecentThing.add(classtype.get(Currentindex));
			}
		}

		// Location Question in Pronomial form "Where can i find it"

		if (questiontype == "location") {   // We always expect a pronomial question to refer to the last
											// object questioned for

			answer=("You can find the "
					+ classtype.get(0).getClass().getSimpleName() + " " + "at "
					+ Location(classtype, input));

			Answered = 1; // An answer was given
		}

		if ((questiontype == "intent" && classtype == totalrestaurant) 
				||(questiontype == "intent" && classtype == theRecentThing)) {

			// Can I lend the book or not (Can I lent "it" or not)
			answer=("You "+ BookAvailable(classtype, input));
			Answered = 1; // An answer was given
		}

		if (questiontype == "farewell") {       // Reply to a farewell
			
			answer=("You are welcome.");

			Answered = 1; // An answer was given
		}
		
		if (questiontype == "foodClass") {   // We always expect a pronomial question to refer to the last
			// object questioned for

answer=("You can find the "
+ classtype.get(0).getClass().getSimpleName() + " " + "at "
+ ListAll(classtype));

Answered = 1; // An answer was given
}
				if (questiontype == "foodClassVegList") {   // We always expect a pronomial question to refer to the last
					// object questioned for
					
					
					
					StringBuilder str = new StringBuilder("List of vegetarians food\n<ul>");
		Iterator<Restaurants.Restaurant.Food> itr = foodList.iterator();
		while(itr.hasNext()){
			
			Restaurants.Restaurant.Food localFood = itr.next();
			
			if(localFood.getClazz().equalsIgnoreCase("vegetarians")){
				
				str.append("<li>"+ localFood.getClazz()+" "+localFood.getName()+" "+localFood.getPrice()+"</li>");
			}
		}
		str.append("</ul>");			
		answer=(str.toString());
		
		Answered = 1; // An answer was given
		}
			if (questiontype == "foodClassNonVegList") {   // We always expect a pronomial question to refer to the last
				Iterator<Restaurants.Restaurant.Food> itr = foodList.iterator();
				StringBuilder str = new StringBuilder("List of Non vegetarians food\n<ul>");
				while(itr.hasNext()){
					
					Restaurants.Restaurant.Food localFood = itr.next();
					
					if(localFood.getClazz().contains("non vegetarians")){
						
						str.append("<li>"+localFood.getClazz() + " "+localFood.getName() +" "+localFood.getPrice()+"</li>");
						
					}
				}
				str.append("</ul>");
				answer=(str.toString());
	
	Answered = 1; // An answer was given
	}
		
		
		
		
		
		if (Answered == 0) { // No answer was given

			answer=("Sorry I didn't understand that.");
		}
		
		

		
		
		

		out.add(input);
		out.add(answer);
		
		return out;
	}

	// Methods to generate answers for the different kinds of Questions
	
	// Answer a question of the "Is a book or "it (meaning a book) available ?" kind

	public String BookAvailable(List thelist, String input) {
        return "";
        } 
		
	// Answer a question of the "How many ...." kind 
	
	public Integer Count(List thelist) { // List "thelist": List of Class Instances (e.g. theBookList)

		//URL = "http://en.wiktionary.org/wiki/"		

		URL = "http://wordnetweb.princeton.edu/perl/webwn?o2=&o0=1&o8=1&o1=1&o7=&o5=&o9=&o6=&o3=&o4=&s="
				+ classtype.get(0).getClass().getSimpleName().toLowerCase();
		URL2 = "http://en.wikipedia.org/wiki/"
				+ classtype.get(0).getClass().getSimpleName().toLowerCase();
		System.out.println("URL = "+URL);
		tooltipstring = readwebsite(URL);
		String html = "<html>" + tooltipstring + "</html>";
		Myface.setmytooltip(html);
		Myface.setmyinfobox(URL2);

		return thelist.size();
	}

	// Answer a question of the "What kind of..." kind
	
	
	public String ListAll(List thelist) {

		String listemall = "<ul>";

		if (thelist == totalrestaurant) {                                  //This is a candidate for a name change
			for (int i = 0; i < thelist.size(); i++) {
				Restaurants.Restaurant curbook = (Restaurants.Restaurant) thelist.get(i);                  //This is a candidate for a name change
				listemall = listemall + "<li>" + (curbook.getName() + "</li>");    //This is a candidate for a name change
			}
		}

		if (thelist == foodList) {                                //This is a candidate for a name change
			for (int i = 0; i < thelist.size(); i++) {
				Restaurants.Restaurant.Food curmem = (Restaurants.Restaurant.Food) thelist.get(i);               //This is a candidate for a name change
				listemall = listemall + "<li>"                         //This is a candidate for a name change
						+ (curmem.getClazz() + " " + curmem.getName() +" " + curmem.getPrice()  + "</li>");  //This is a candidate for a name change
			}
		}
		
		
		
			
		/*if (thelist == theCatalogList) {                               //This is a candidate for a name change
			for (int i = 0; i < thelist.size(); i++) {
				Catalog curcat = (Catalog) thelist.get(i);             //This is a candidate for a name change
				listemall = listemall 
						+ "<li>" + (curcat.getName() + "</li>");      //This is a candidate for a name change
			}
		}
		
		if (thelist == theLendingList) {                               //This is a candidate for a name change
			for (int i = 0; i < thelist.size(); i++) {
				Lending curlend = (Lending) thelist.get(i);             //This is a candidate for a name change
				listemall = listemall + "<li>" 
						+ (curlend.getIsbn() + "</li>");                //This is a candidate for a name change
			}
		}*/
		
		listemall += "</ul>";

		URL = "http://wordnetweb.princeton.edu/perl/webwn?o2=&o0=1&o8=1&o1=1&o7=&o5=&o9=&o6=&o3=&o4=&s="
				+ classtype.get(0).getClass().getSimpleName().toLowerCase();
		URL2 = "http://en.wikipedia.org/wiki/"
				+ classtype.get(0).getClass().getSimpleName().toLowerCase();
		System.out.println("URL = "+URL);
		tooltipstring = readwebsite(URL);
		String html = "<html>" + tooltipstring + "</html>";
		Myface.setmytooltip(html);
		Myface.setmyinfobox(URL2);
		
		return listemall;
	}

	
	
	
	
	// Answer a question of the "Do you have..." kind 
	
	public Vector<String> CheckFor(List thelist, String input) {

		Vector<String> yesorno = new Vector<String>();
		if (classtype.isEmpty()){
			yesorno.add("Class not recognised. Please specify if you are searching for a Resaturants, Food, name, or order?");
		} else {
			yesorno.add("No we don't have such a "
				+ classtype.get(0).getClass().getSimpleName());
		}

		Integer counter = 0;

		if (thelist == totalrestaurant) {                         //This is a candidate for a name change

			for (int i = 0; i < thelist.size(); i++) {

              Restaurants.Restaurant curbook = (Restaurants.Restaurant) thelist.get(i);                           //This is a candidate for a name change

				if (input.contains(curbook.getName().toLowerCase())            //This is a candidate for a name change
						|| input.contains(curbook.getAddress().toLowerCase())      //This is a candidate for a name change
						|| input.contains(curbook.getType().toLowerCase())) {  //This is a candidate for a name change

					counter = i;
					yesorno.set(0, "Yes we have such Restaurant");                  //This is a candidate for a name change
					yesorno.add(counter.toString());
					i = thelist.size() + 1; // force break
				}
			}
		}

		if (thelist == foodList) {                                      //This is a candidate for a name change
			for (int i = 0; i < thelist.size(); i++) {
				Restaurants.Restaurant.Food curmem = (Restaurants.Restaurant.Food) thelist.get(i);                      //This is a candidate for a name change
				if (input.contains(curmem.getClazz().toLowerCase())         //This is a candidate for a name change
						|| input.contains(curmem.getName().toLowerCase()) //This is a candidate for a name change
						|| input.contains(curmem.getPrice().toLowerCase())) {  //This is a candidate for a name change

					counter = i;
					yesorno.set(0, "Yes we have such type of Food ");               //This is a candidate for a name change
					yesorno.add(counter.toString());
					i = thelist.size() + 1;
				}
			}
		}

		if (classtype.isEmpty()) {
			System.out.println("Not class type given.");
		} else {
			URL = "http://wordnetweb.princeton.edu/perl/webwn?o2=&o0=1&o8=1&o1=1&o7=&o5=&o9=&o6=&o3=&o4=&s="
				+ classtype.get(0).getClass().getSimpleName().toLowerCase();
			URL2 = "http://en.wikipedia.org/wiki/"
				+ classtype.get(0).getClass().getSimpleName().toLowerCase();
			System.out.println("URL = "+URL);
			tooltipstring = readwebsite(URL);
			String html = "<html>" + tooltipstring + "</html>";
			Myface.setmytooltip(html);
			Myface.setmyinfobox(URL2);
		}
	
		return yesorno;
	}

	//  Method to retrieve the location information from the object (Where is...) kind

	public String Location(List classtypelist, String input) {

		List thelist = classtypelist;
		String location = "";

		// if a pronomial was used "it", "them" etc: Reference to the recent thing

		if (thelist == theRecentThing && theRecentThing.get(0) != null) {

			if (theRecentThing.get(0).getClass().getSimpleName()
					.toLowerCase().equals("restaurant")) {                  //This is a candidate for a name change

				Restaurants.Restaurant curbook = (Restaurants.Restaurant) theRecentThing.get(0);          //This is a candidate for a name change
				location = (curbook.getAddress() + " ");             //This is a candidate for a name change

			}

			if (theRecentThing.get(0).getClass().getSimpleName()
					.toLowerCase().equals("food")) {               //This is a candidate for a name change

				Restaurants.Restaurant.Food curmem = (Restaurants.Restaurant.Food) theRecentThing.get(0);      //This is a candidate for a name change
				location = (curmem.getClazz() + " " + curmem.getName() + " " + curmem  //This is a candidate for a name change
						.getPrice());                                    //This is a candidate for a name change

			}

			}

		// if a direct noun was used (food, restaurant, etc)

		else {

			if (thelist == totalrestaurant) {                         //This is a candidate for a name change

				int counter = 0;

				for (int i = 0; i < thelist.size(); i++) {

					Restaurants.Restaurant curbook = (Restaurants.Restaurant) thelist.get(i);         //This is a candidate for a name change

					if (input.contains(curbook.getName().toLowerCase())            //This is a candidate for a name change
							|| input.contains(curbook.getAddress().toLowerCase())      //This is a candidate for a name change
							) {  //This is a candidate for a name change

						counter = i;
						location = (curbook.getAddress() + " ");
						Currentindex = counter;
						theRecentThing.clear(); 									// Clear it before adding (changing) theRecentThing
						classtype = totalrestaurant;                                    //This is a candidate for a name change
						theRecentThing.add(classtype.get(Currentindex));
						i = thelist.size() + 1; 									// force break
					}
				}
			}

			if (thelist == foodList) {                                         //This is a candidate for a name change

				int counter = 0;

				for (int i = 0; i < thelist.size(); i++) {

					Restaurants.Restaurant.Food curmember = (Restaurants.Restaurant.Food) thelist.get(i);         				  //This is a candidate for a name change

					if (input.contains(curmember.getClazz().toLowerCase())              //This is a candidate for a name change
							|| input.contains(curmember.getName().toLowerCase())      //This is a candidate for a name change
							|| input.contains(curmember.getPrice().toLowerCase())) {   //This is a candidate for a name change

						counter = i;
						location = (curmember.getClazz() + " ");
						Currentindex = counter;
						theRecentThing.clear(); 										// Clear it before adding (changing) the
						classtype = foodList;            	 						//This is a candidate for a name change
						theRecentThing.add(classtype.get(Currentindex));
						i = thelist.size() + 1; 				             	        // force break
					}
				}
			}

		}

		URL = "http://wordnetweb.princeton.edu/perl/webwn?o2=&o0=1&o8=1&o1=1&o7=&o5=&o9=&o6=&o3=&o4=&s="
				+ classtype.get(0).getClass().getSimpleName().toLowerCase();
		URL2 = "http://en.wikipedia.org/wiki/"
				+ classtype.get(0).getClass().getSimpleName().toLowerCase();
		System.out.println("URL = "+URL);
		tooltipstring = readwebsite(URL);
		String html = "<html>" + tooltipstring + "</html>";
		Myface.setmytooltip(html);
		Myface.setmyinfobox(URL2);

		return location;
	}

	public String testit() {   // test the loaded knowledge by querying for books written by dostoyjewski

		String answer = "";

		System.out.println("Book List = " + totalrestaurant.size());  //This is a candidate for a name change

		for (int i = 0; i < totalrestaurant.size(); i++) {   // check each book in the List, //This is a candidate for a name change

			Restaurants.Restaurant curbook = (Restaurants.Restaurant) totalrestaurant.get(i);    // cast list element to Book Class //This is a candidate for a name change												
			System.out.println("Testing Book" + curbook.getName());

			if (curbook.getName().equalsIgnoreCase("Pan Africa Market")) {     // check for the author //This is a candidate for a name change

				answer = "A Restrant located at"+curbook.getAddress()+ ".";
			}
		}
		return answer;
	}
	public static void main(String[] args) {
		
		new Reasoner().testit();
	}

	public String readwebsite(String url) {

		String webtext = "";
		try {
			BufferedReader readit = new BufferedReader(new InputStreamReader(
					new URL(url).openStream()));

			String lineread = readit.readLine();

			System.out.println("Reader okay");

			while (lineread != null) {
				webtext = webtext + lineread;
				lineread = readit.readLine();				
			}

			// Hard coded cut out from "wordnet website source text": 
			//Check if website still has this structure   vvvv ...definitions...  vvvv 		
			
			webtext = webtext.substring(webtext.indexOf("<ul>"),webtext.indexOf("</ul>"));                                 //               ^^^^^^^^^^^^^^^^^              

			webtext = "<table width=\"700\"><tr><td>" + webtext
					+ "</ul></td></tr></table>";

		} catch (Exception e) {
			webtext = "Not yet";
			System.out.println("Error connecting to wordnet");
		}
		return webtext;
	}
}
