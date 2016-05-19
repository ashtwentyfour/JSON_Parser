package json_parser;

import java.io.*;

public class Driver {

   public static void main(String args[]) throws IOException {
       
       Parser p = new Parser();
       System.out.println(p.parser(args[0]));       
   }

}
