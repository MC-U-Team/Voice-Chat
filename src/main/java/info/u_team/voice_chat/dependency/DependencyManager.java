package info.u_team.voice_chat.dependency;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import info.u_team.voice_chat.VoiceChatMod;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;

public class DependencyManager {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Marker MARKER = MarkerManager.getMarker("Load");
	
	public static void construct() {
		LOGGER.info(MARKER, "Load dependencies");
		final String devPath = System.getProperty("voicechat.dev");
		if (devPath == null) {
			findJarFilesInJar("dependencies/internal", path -> addToInternalDependencies(createInternalURL(path)));
		}
		LOGGER.info(MARKER, "Finished loading dependencies");
	}
	
	private static void findJarFilesInJar(String folder, Consumer<Path> consumer) {
		final ModFile modfile = ModList.get().getModFileById(VoiceChatMod.MODID).getFile();
		try (Stream<Path> stream = Files.walk(modfile.findResource("/" + folder))) {
			stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
		} catch (final IOException ex) {
			LOGGER.error(MARKER, "When searching for jar files in jar an exception occured.", ex);
		}
	}
	
	private static URL createInternalURL(Path path) {
		final String url = "modjar://" + VoiceChatMod.MODID + path;
		LOGGER.debug(MARKER, "Load url" + url);
		try {
			return new URL(url);
		} catch (final MalformedURLException ex) {
			LOGGER.error(MARKER, "Could not create url from internal path", ex);
		}
		return null;
	}
	
	private static void addToInternalDependencies(URL url) {
		try {
			final URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(systemClassLoader, url);
		} catch (final Exception ex) {
			LOGGER.error(MARKER, "Method addURL on system classloader could not be invoked", ex);
		}
	}
}
