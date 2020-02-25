package info.u_team.voice_chat;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.logging.log4j.*;

import cpw.mods.modlauncher.TransformingClassLoader;
import info.u_team.voice_chat.config.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;

@Mod(VoiceChatMod.MODID)
public class VoiceChatMod {
	
	public static final String MODID = "voicechat";
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public VoiceChatMod() {
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CONFIG);
		//
		// System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		// System.out.println(VoiceChatMod.class.getClassLoader());
		//
		// findJarFilesInJar("dependencies/internal", path -> addToInternalDependencies(createInternalURL(path)));
		//
		// int a = Opus.OPUS_SET_PHASE_INVERSION_DISABLED_REQUEST;
		// System.out.println("JAAAAAAAAAAAAAAAAAJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		// System.out.println(Opus.class);
		// System.out.println(a);
		
		try {
			final File file = new File("C:\\Users\\mail\\Desktop\\test.jar");
			final URL url = file.toURI().toURL();
			addToInternalDependencies(url);
			
			System.out.println(url);
			
			Class<?> clazz = Class.forName("test.Shift");
			Method method = clazz.getDeclaredMethod("main", String[].class);
			System.out.println("INVOKE");
			
			System.out.println(method);
			String[] params = new String[] {"Test", "YOLO"};
			method.invoke(null, (Object) params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void findJarFilesInJar(String folder, Consumer<Path> consumer) {
		final ModFile modfile = ModList.get().getModFileById(MODID).getFile();
		try (Stream<Path> stream = Files.walk(modfile.findResource("/" + folder))) {
			stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
		} catch (final IOException ex) {
		}
	}
	
	private static URL createInternalURL(Path path) {
		final String url = "modjar://" + MODID + path;
		try {
			return new URL(url);
		} catch (final MalformedURLException ex) {
		}
		return null;
	}
	
	private static void addToInternalDependencies(URL url) {
		try {
			final TransformingClassLoader transformingClassLoader = (TransformingClassLoader) VoiceChatMod.class.getClassLoader();
			final Field field = TransformingClassLoader.class.getDeclaredField("delegatedClassLoader");
			field.setAccessible(true);
			final URLClassLoader delegateClassLoader = (URLClassLoader) field.get(transformingClassLoader);
			final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(delegateClassLoader, url);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
