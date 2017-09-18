package openccsensors.api;

import dan200.computercraft.api.peripheral.IComputerAccess;

public interface IMethodCallback {
	String getMethodName();

	Object execute(IComputerAccess item, Object[] args) throws Exception;
}
