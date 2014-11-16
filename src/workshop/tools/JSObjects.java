package workshop.tools;

import java.util.Arrays;
import java.util.HashSet;

public class JSObjects {

	private static final HashSet<String> JS_STRING = new HashSet<String>(Arrays.asList
			("String", "length", "prototype", "charAt", "charCodeAt", "concat",
					"fromCharCode", "indexOf", "lastIndexOf", "localeCompare", "match", "replace", "search", 
					"slice", "split", "substr", "substring", "toLocaleLowerCase", "toLocaleUpperCase", "toLowerCase", 
					"toString", "toUpperCase", "trim", "valueOf"));

	private static final HashSet<String> JS_NUMBER = new HashSet<String>(Arrays.asList
			("Number", "MAX_VALUE", "MIN_VALUE", "NEGATIVE_INFINITY", "NaN", "POSITIVE_INFINITY", "prototype", 
					"toExponential", "toFixed", "toPrecision", "toString", "valueOf"));

	private static final HashSet<String> JS_MATH = new HashSet<String>(Arrays.asList
			("Math", "E", "LN2", "LN10", "LOG2E", "LOG10E", "PI", "SQRT1_2", "SQRT2", "abs", "acos", "asin", 
					"atan", "atan2", "ceil", "cos", "exp", "floor", "log", "max", "min", "pow", "random", 
					"round", "sin", "sqrt", "tan"));

	private static final HashSet<String> JS_DATE = new HashSet<String>(Arrays.asList
			("Date", "prototype", "getDate", "getDay", "getFullYear", "getHours", "getMilliseconds", "getMinutes",
					"getMonth", "getSeconds", "getTime", "getTimezoneOffset", "getUTCDate", "getUTCDay", "getUTCFullYear",
					"getUTCHours", "getUTCMilliseconds", "getUTCMinutes", "getUTCMonth", "getUTCSeconds", "getYear",
					"parse", "setDate", "setFullYear", "setHours", "setMilliseconds", "setMinutes", "setMonth", "setSeconds",
					"setTime", "setUTCDate", "setUTCFullYear", "setUTCHours", "setUTCMilliseconds", "setUTCMinutes", "setUTCMonth",
					"setUTCSeconds", "setYear", "toDateString", "toGMTString", "toISOString", "toJSON", "toLocaleDateString",
					"toLocaleTimeString", "toLocaleString", "toString", "toTimeString", "toUTCString", "UTC", "valueOf"));

	private static final HashSet<String> JS_ARRAY = new HashSet<String>(Arrays.asList
			("Array", "length", "prototype", "concat", "indexOf", "join", "lastIndexOf", "pop", "push", "reverse", "shift", 
					"slice", "sort", "splice", "toString", "unshift", "valueOf"));

	private static final HashSet<String> JS_BOOLEAN = new HashSet<String>(Arrays.asList
			("Boolean", "prototype", "toString", "valueOf"));

	private static final HashSet<String> JS_REG_EXP = new HashSet<String>(Arrays.asList
			("RegExp", "global", "ignoreCase", "lastIndex", "multiline", "source", "compile", "exec", "test", "toString"));

	private static final HashSet<String> JS_GLOBAL = new HashSet<String>(Arrays.asList
			("Infinity", "isNaN", "undefined", "decodeURI", "decodeURIComponent", "encodeURI",
					"encodeURIComponent", "escape", "eval", "isFinite", "Number", "parseFloat", "parseInt", "String", "unescape"));



	public static boolean contains(String name){
		return (JS_STRING.contains(name)  || 
				JS_NUMBER.contains(name)  ||
				JS_MATH.contains(name)    ||
				JS_DATE.contains(name)    ||
				JS_ARRAY.contains(name)   ||
				JS_BOOLEAN.contains(name) ||
				JS_REG_EXP.contains(name)  ||
				JS_GLOBAL.contains(name)); 
	}


	public static HashSet<String> getSet(String name){
		switch(name){
		case "String":
			return JS_STRING;
		case "Number":
			return JS_NUMBER;
		case "Math":
			return JS_MATH;
		case "Date":
			return JS_DATE;
		case "Array":
			return JS_ARRAY;
		case "Boolean":
			return JS_BOOLEAN;
		case "RegExp":
			return JS_REG_EXP;
		default: 
			return JS_GLOBAL;
		}
	}


}
