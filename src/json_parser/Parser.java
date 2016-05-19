package json_parser;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * This package can be used to parse .json files. The parser returns a map 
 * (key -> value pairs) and retains the hierarchical structure of the JSON object 
 * 
 * @author Ashwin Menon
 * @version 1.0
 * @since 2015-06-15
 * 
 * */


public class Parser {
	
	/**
	 * function passes the json file as a string to the parser which returns a map
	 * @param json_file JSON file
	 * @return map with key -> value pairs
	*/
	
	public HashMap<String , Object > parser(String json_file) throws IOException {

		@SuppressWarnings("resource")
		
		BufferedReader buffer = new BufferedReader(   // loading sample JSON file 
	          new FileReader(json_file));
		
		/* forming a string input for the parser */
		
		int c = 0;
		String json = "";
		while((c = buffer.read()) != -1) {
			char character = (char) c;

			if(character != '\n' && character != ' ') json += character;

		}
		
		
		/* map which stores processed data - calling the parse_json function */
		HashMap<String , Object> JSON_Map = new 
		    HashMap<String , Object>(parse(json)); 
		
		return JSON_Map;
		
	}	
	
	/**
	 * function processes the json data recursively
	 * @param json json file
	 * @return map with json key->value pairs
	*/
	
	private HashMap<String , Object> parse(String json) {
		
		char[] array = json.toCharArray();
		String s = "";
		
		HashMap<String , Object> JSON_Map = new HashMap<String , Object>();
		
		for(int i = 0; i < json.length(); i++) {  // loop over staring characters 
		
		   /* when a key is encountered */
		
		   if(array[i] == '"') {
			  StringBuilder sb = new StringBuilder();
			  sb.append(array[i]);
			   i++;
			   while(array[i] != '"') {
				   sb.append(array[i]);
				   i++;
			   }
			   
			   sb.append(array[i]);
			   s = sb.toString();
			   i+=2;   
			  
			   /* for null values */
			   
			   if(array[i] == 'n') {
				  
				   StringBuilder nul = new StringBuilder();
				   nul.append(array[i]);
				   i++;
				   while(nul.length() != 4) {  // null has four letters 
					   nul.append(array[i]);
					   i++;
				   }
				   
				   
				   String n = nul.toString();
				   
				   /* insert into map */
				   JSON_Map.put(s , new String(n));
				   
			   }
			   
			   /* for string values */
			   
			   if(array[i] == '"') {
				   String key = s;
				   StringBuilder rb = new StringBuilder();
				   rb.append(array[i]);
				   i++;
				   while(array[i] != '"') {
					   rb.append(array[i]);
					   i++;
				   }
				   
				   rb.append(array[i]);
				   s = rb.toString();
				   
				   /* insert to map */
				   JSON_Map.put(key , new String(s));
				      
			   }
			   
			   /* for boolean values */
			   
			   else if(array[i] == 't' || array[i] == 'f') {
				   String key = s;
				   StringBuilder bb = new StringBuilder();
				   while(array[i] != 'e') {  // both true and false end in 'e'
					   
					   bb.append(array[i]);
					   i++;
					   
				   }
				   
				   bb.append(array[i]);
				   s = bb.toString();
				   
				   /* insert into map */
				   JSON_Map.put(key , new String(s));
				   
			   }
			   
			   
			   else if(array[i] == '[') {
				   
				   i++; // access the first array element 
				   
				   /* for an array of strings */
				   
				   if(array[i] == '"') {
					   
					   Vector<String> v = new Vector<String>();
					   StringBuilder as = new StringBuilder();
					   while(array[i] != ']') {
						  
						  if(array[i] == ',' && array[i-1] == '"' && array[i+1] == '"')
							  i++;
						  
						  as.append(array[i]);
						  i++;
						  while(array[i] != '"') {
							  
						   as.append(array[i]);
						   i++;
						
						  }
						  
						  as.append(array[i]);
						  String str = as.toString();
						  v.add(str);
						  as.setLength(0);
						  i++;
						   
					   }
					   
					   /* insert into map */
					   JSON_Map.put(s , new Vector<String>(v));
					   
				   }
				   
				   /* for an array of boolean values */
				   
				   if(array[i] == 't' || array[i] == 'f') {
					   
					   Vector<Boolean> v = new Vector<Boolean>();
					   StringBuilder as = new StringBuilder();
					   while(array[i] != ']') {
						   
						  if(array[i] == ',')
							i++;
						   
						  while(array[i] != 'e') {
							  
						    as.append(array[i]);
						    i++;
						
						  }
						  
						  as.append(array[i]);
						  String str = as.toString();
						  v.add(Boolean.parseBoolean(str)); // convert from string to boolean
						  as.setLength(0);
			              i++;
						   
					   }
					   
					   /* insert into map */
					   JSON_Map.put(s , new Vector<Boolean>(v));
					   
				   }
				   
				   /* for an array of integers or floats */
				   
				   if(array[i] >= '0' && array[i] <= '9') {
					   
					   Vector<Float> v = new Vector<Float>();
					   StringBuilder num = new StringBuilder();
					   while(array[i] != ']') {
						   
						  if(array[i] == ',')
								i++;
						   
						  while((array[i] >= '0' && array[i] <= '9')
								|| ((array[i] == '.') && (array[i+1] >= '0' 
								&& array[i+1] <= '9'))) {  // taking decimal points into consideration
							  
						    num.append(array[i]);
						    i++;
						
						  }
						  
						  String str = num.toString();
						  v.add(Float.valueOf(str));
						  num.setLength(0);
						    
					   }
					   
					   /* insert into map */
					   JSON_Map.put(s , new Vector<Float>(v));
					   
				   }
				   
				   /* for an array of JSON objects */
				   
				   if(array[i] == '{') {
					   
					   // vector of HashMaps for each JSON object 
					   Vector<HashMap<String , Object> > v = new Vector<HashMap<String , Object> >();
					   while(array[i] != ']') {
						  
						  if(array[i] == '{') {
						   int counter = 1;
						   StringBuilder sjson = new StringBuilder();
						   sjson.append(array[i]);
						   while(counter != 0) {  //counter = 0 when the correct closing '}' is found
							  i++;
							  sjson.append(array[i]);
							  if(array[i] == '}')
								  counter--;
							  if(array[i] == '{')
								  counter++;  
						   }
					      
						   // recursively processing each JSON object
						   HashMap<String , Object> subJSONMap = new   
						   HashMap<String , Object>(parse(sjson.toString()));
						   
						   v.add(subJSONMap); // add map to the vector
						   
						 }
						   i++;
						  				   
					   }
					   
					   JSON_Map.put(s , new Vector<HashMap<String , Object>>(v));
					   
				   }
				   
				   
			   }
			   
			   /* if the value is another JSON object */
			   
			   else if(array[i] == '{') {
				   int counter = 1;
				   StringBuilder sjson = new StringBuilder();
				   sjson.append(array[i]);
				   while(counter != 0) {  //counter = 0 when the correct closing '}' is found
					   i++;
					   sjson.append(array[i]);
					   if(array[i] == '}')
						   counter--;
					   if(array[i] == '{')
						   counter++;  
				   }
				   
				   
				   HashMap<String , Object> subJSONMap = new   // recursively process the JSON object
				   HashMap<String , Object>(parse(sjson.toString()));
				   
				   /* insert into map */
				   JSON_Map.put(s , new HashMap<String , Object>(subJSONMap));
				   
			   }
			   
			   
			   /* if the value is an integer or float */
			   
			   else if(array[i] >= '0' && array[i] <= '9') {
				  
				   String key = s;
				   StringBuilder number = new StringBuilder();
				   while((array[i] >= '0' && array[i] <= '9')
				       || ((array[i] == '.') && (array[i+1] >= '0' 
				       && array[i+1] <= '9'))) {  // taking decimal points into consideration
					   
					   number.append(array[i]);
					   i++;
					   
				   }
				   
				   s = number.toString();
				   
				   /* insert into map */
				   JSON_Map.put(key , new Float(Float.valueOf(s))); 
				   
			   }
			   
			   else continue;
			   
		   }
		  		   
		}
				
		return JSON_Map;   // returning final map 
			
	  }
	
    }


