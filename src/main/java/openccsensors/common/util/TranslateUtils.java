package openccsensors.common.util;


public class TranslateUtils {
	@SuppressWarnings("deprecation")
	public static String translateToLocal(String key) {
		return net.minecraft.util.text.translation.I18n.translateToLocal(key);
	}
}
