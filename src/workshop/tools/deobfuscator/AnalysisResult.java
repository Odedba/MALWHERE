package workshop.tools.deobfuscator;

import java.util.HashSet;
import java.util.Set;

/***************************************************************
 * This class represents the result of the Analyzer's analysis 
 ***************************************************************/
public class AnalysisResult {	
	private boolean createsIframe;
	private boolean hasHiddenFrame;
	private boolean writesScript;
	private HashSet<String> urls;

	public AnalysisResult(
			boolean createsIframe,
			boolean hasHiddenFrame,
			boolean writesScript,
			Set<String> urls)
	{	
		this.createsIframe = createsIframe;
		this.hasHiddenFrame = hasHiddenFrame;
		this.writesScript = writesScript;
		this.urls = new HashSet<String>();
		this.urls.addAll(urls);
	}

	/**
	 * @return the createsIframe
	 */
	public boolean createsIframe() {
		return createsIframe;
	}

	/**
	 * @return the hasHiddenFrame
	 */
	public boolean hasHiddenFrame() {
		return hasHiddenFrame;
	}

	/**
	 * @return the writesScript
	 */
	public boolean writesScript() {
		return writesScript;
	}
	
	/**
	 * @return the URLs
	 */
	public Set<String> getUrls(){
		return urls;
	}


}
