package workshop.tools;

import java.util.Arrays;
import java.util.HashSet;

public class BrowserBOM {

	private static final HashSet<String> BROWSER_BOM = new HashSet<String>(Arrays.asList(
			"closed", "defaultStatus", "document", "frames", "history", "innerHeight",
			"innerWidth", "length", "location", "name", "navigator", "opener", "outerHeight",
			"outerWidth", "pageXOffset", "pageYOffset", "parent", "screen", "screenLeft",
			"screenTop", "screenX", "screenY", "self", "status", "top", "alert", "atob",
			"blur", "btoa", "clearInterval", "clearTimeout", "close", "confirm", "createPopup",
			"focus", "moveBy", "moveTo", "open", "print", "prompt", "resizeBy", "resizeTo",
			"scroll", "scrollBy", "scrollTo", "setInterval", "setTimeout", "stop"));


	public static boolean contains(String name){
		return BROWSER_BOM.contains(name);
	}


	public static HashSet<String> getSet(){
		return BROWSER_BOM;
	}

}

