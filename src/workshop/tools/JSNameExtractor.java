package workshop.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class handles script names, based on the AST of the script.
 * The static method it contains extracts and lists all names in script. 
 */
public class JSNameExtractor {
	    /*
	     *  Keeping a hash-table with the desired outputs prevents the program 
	     *  from repeating the computation over and over for the same file.
	     */		
		private static HashMap<String, List<String>> namesMap = new HashMap<String, List<String>>();

		/**
		 * Extracts all names from the script and returns as a list of Strings.
		 * 
		 * @param sourcePath - Path to source file.
		 * @return list of all names from the script.
		 */
		public static List<String> extractNames(String sourcePath){
			if(namesMap.containsKey(sourcePath)){
				return namesMap.get(sourcePath);
			}
			List<String> names = new ArrayList<String>();
			Iterator<String> it = RhinoASTBuilder.getAST(sourcePath).iterator();
			while(it.hasNext()){
				String line = it.next();

				int type_index = line.indexOf(' ') + 1;

				if(type_index >= 0 && line.length() > type_index && line.substring(type_index).startsWith("NAME: ")){
					names.add(line.substring((type_index) + ("NAME: ").length()));
				}
			}
			namesMap.put(sourcePath, names);
			return names;
		}
		
		/**
		 * Creates a list of all script's names with no duplicates.
		 * 
		 * @param sourcePath - Path to source file.
		 * @return list of all script's names with no duplicates.
		 */
		public static List<String> extractNamesNoDuplicates(String sourcePath) {
			List<String> l = extractNames(sourcePath);
			Set<String> s = new HashSet<String>();
			s.addAll(l);
			List<String> names = new ArrayList<String>();
			names.addAll(s);
			return names;	
		}
}
