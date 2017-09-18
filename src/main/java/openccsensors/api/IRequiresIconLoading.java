package openccsensors.api;

import net.minecraft.client.renderer.texture.IIconRegister;

public interface IRequiresIconLoading {
	void loadIcon(IIconRegister iconRegistry);
}
