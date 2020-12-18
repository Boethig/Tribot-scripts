package scripts.boe_api.framework;

import scripts.boe_api.camera.ACamera;

public abstract class Node {

	protected ACamera aCamera;

	public Node() {}

	public Node(ACamera aCamera) 
	{       
	this.aCamera = aCamera;
	}

	public abstract boolean validate();

	public abstract void execute();

	public abstract String status();
}
