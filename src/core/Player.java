package core;

public class Player {

	// Fields.
	private String name;
	private String type;
	private String color;
	private int simulationNumber;

	// Constructor.
	public Player(String name, String type, String color, int simulationNo) {
		this.name = name;
		this.type = type;
		this.color = color;
		this.simulationNumber = simulationNo;
	}
	
	// Methods.
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return the simulationNumber
	 */
	public int getSimulationNumber() {
		return this.simulationNumber;
	}
}
