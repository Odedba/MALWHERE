package workshop.tools;

import java.util.Arrays;
import java.util.HashSet;

public class HtmlDOM {
	
	private static final HashSet<String> HTML_DOM = new HashSet<String>(Arrays.asList(
			"document", "addEventListener","adoptNode", "anchors", "applets", "baseURI", "body", "close", 
			"cookie", "createAttribute", "createComment", "createDocumentFragment", "createElement",
			"createTextNode", "doctype", "documentElement", "documentMode", "documentURI", "domain", 
			"domConfig", "embeds", "forms", "getElementById", "getElementsByClassName", "getElementsByName",
			"getElementsByTagName", "head", "images", "implementation", "importNode", "inputEncoding", 
			"lastModified", "links", "normalize", "normalizeDocument", "open", "querySelector", "querySelectorAll",
			"readyState", "referrer", "removeEventListener", "renameNode", "scripts", "strictErrorChecking",
			"title", "URL", "write", "writeln", "attributes", "hasAttributes", "nextSibling", "nodeName", 
			"nodeType", "nodeValue","ownerDocument", "ownerElement", "parentNode", "previousSibling", "textContent"));


	public static boolean contains(String name){
		return HTML_DOM.contains(name);
	}
	

	public static HashSet<String> getSet(){
		return HTML_DOM;
	}
}
