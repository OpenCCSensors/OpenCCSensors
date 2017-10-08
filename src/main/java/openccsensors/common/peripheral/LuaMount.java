package openccsensors.common.peripheral;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Nonnull;

import dan200.computercraft.api.filesystem.IMount;
import openccsensors.OpenCCSensors;

public class LuaMount implements IMount {

	@Override
	public boolean exists(@Nonnull String path) throws IOException {
		File file = new File(new File(OpenCCSensors.EXTRACTED_LUA_PATH), path);
		return file.exists();
	}

	@Override
	public boolean isDirectory(@Nonnull String path) throws IOException {
		File file = new File(new File(OpenCCSensors.EXTRACTED_LUA_PATH), path);
		return file.isDirectory();
	}

	@Override
	public void list(@Nonnull String path, @Nonnull List<String> contents) throws IOException {
		File directory = new File(new File(OpenCCSensors.EXTRACTED_LUA_PATH), path);
		for (File file : directory.listFiles()) {
			contents.add(file.getName());
		}
	}

	@Override
	public long getSize(@Nonnull String path) throws IOException {
		File file = new File(new File(OpenCCSensors.EXTRACTED_LUA_PATH), path);
		return file.length();
	}

	@Nonnull
	@Override
	public InputStream openForRead(@Nonnull String path) throws IOException {
		File file = new File(new File(OpenCCSensors.EXTRACTED_LUA_PATH), path);
		return new FileInputStream(file);
	}
}
