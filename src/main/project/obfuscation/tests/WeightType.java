package main.project.obfuscation.tests;

/**
 * The weight given to a test, according to
 * the level of importance of the test for
 * the total obfuscation score.
 */
public enum WeightType {
	VERY_LOW (1.0),
	LOW (2.0),
	MEDIUM (3.0),
	HIGH (4.0),
	VERY_HIGH (5.0);
	
	private final double weight;
	
	WeightType(double weight){
		this.weight = weight;
	}
	
	public double getWeight() { 
		return weight;
	}
}
