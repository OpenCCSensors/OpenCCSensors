package openccsensors.api;

public interface ISensorAccess {
	ISensorEnvironment getSensorEnvironment();

	boolean isTurtle();
}
