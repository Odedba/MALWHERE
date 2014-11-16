package workshop.tools.deobfuscator;

/*********************************************************************
 * This class represents a single result given by the JSDeobfuscator
 *********************************************************************/
public class DeobfuscatorResult {

	public enum Type {
		DOCUMENT_WRITE, 
		DOCUMENT_WRITELN, 
		CREATE_ELEMENT, 
		EVAL, 
		SET_TIMEOUT, 
		ENCODE_URI_COMPONENT,
		DOCUMENT_GET_ELEMENT_BY_ID,
		OBJECT_ASSIGN,
		ALERT};

		private Type type;
		private String info;

		/**
		 * Constructor of DeobfuscatorResults.
		 * 
		 * @param label - The label from which to derive this instances type.
		 * @param info  - The info.
		 */
		public DeobfuscatorResult(String label, String info){
			if(label.startsWith("DOCUMENT_WRITE")){
				this.type = Type.DOCUMENT_WRITE;
			}
			else if(label.startsWith("DOCUMENT_WRITELN")){
				this.type = Type.DOCUMENT_WRITELN;
			}
			else if(label.startsWith("CREATE_ELEMENT")){
				this.type = Type.CREATE_ELEMENT;
			}
			else if(label.startsWith("EVAL")){
				this.type = Type.EVAL;
			}
			else if(label.startsWith("SET_TIMEOUT")){
				this.type = Type.SET_TIMEOUT;
			}
			else if(label.startsWith("ENCODE_URI_COMPONENT")){
				this.type = Type.ENCODE_URI_COMPONENT;
			}
			else if(label.startsWith("DOCUMENT_GET_ELEMENT_BY_ID")){
				this.type = Type.DOCUMENT_GET_ELEMENT_BY_ID;
			}
			else if(label.startsWith("OBJECT_ASSIGN")){
				this.type = Type.OBJECT_ASSIGN;
			}
			else{
				this.type = Type.ALERT;
			}

			this.info = info;
		}

		/**
		 * Getter for type.
		 * 
		 * @return This type.
		 */
		public Type getType(){
			return type;
		}

		/**
		 * Getter for info.
		 * 
		 * @return This info.
		 */
		public String getInfo(){
			return info;
		}

		@Override
		public String toString(){
			return "INFO: " + type + ": "+info;
		}

		@Override
		public int hashCode() {
			final int prime = 773;
			int result = 1;
			result = prime * result + type.hashCode();
			result = prime * result + info.hashCode();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DeobfuscatorResult other = (DeobfuscatorResult) obj;
			return (info.equals(other.info) && type == other.type);
		}


}
