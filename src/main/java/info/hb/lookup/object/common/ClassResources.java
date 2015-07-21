package info.hb.lookup.object.common;

import java.io.File;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;

/**
 * it worth to be a separate project
 *
 * c = ClassResources(YourClass.class, "fonts") c.names();
 *
 * fonts is a resources directory:
 *
 * com/example/project/YourClass.class
 *
 * com/example/project/fonts
 *
 * TODO make it separate project
 */
public class ClassResources {

	public Class<?> c;
	public File path;

	public ClassResources(Class<?> c) {
		this.c = c;
	}

	public ClassResources(Class<?> c, File path) {
		this.c = c;
		this.path = path;
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public List<String> names() {
		return getResourceListing(c, path);
	}

	/**
	 * enter sub directory
	 *
	 * @param path
	 * @return
	 */
	public ClassResources dir(File path) {
		return new ClassResources(c, new File(this.path, path.getPath()));
	}

	// 1) under debugger, /Users/axet/source/mircle/play/target/classes/
	//
	// 2) app packed as one jar, mac osx wihtout debugger path -
	// /Users/axet/source/mircle/mircle/macosx/Mircle.app/Contents/Resources/Java/mircle.jar
	// case above 1) works prefectly
	//
	// 3) if it is a separate library packed with maven under debugger
	// /Users/axet/.m2/repository/com/github/axet/play/0.0.3/play-0.0.3.jar
	File getPath(Class<?> cls) {
		CodeSource src = cls.getProtectionDomain().getCodeSource();

		if (src == null)
			return null;

		return new File(src.getLocation().getPath());
	}

	String getClassPath(Class<?> c) {
		return new File(c.getCanonicalName().replace('.', File.separatorChar)).getParent();
	}

	String getClassPath(Class<?> c, String path) {
		return new File(c.getCanonicalName().replace('.', File.separatorChar)).getParent() + File.separator + path;
	}

	List<String> getResourceListing(Class<?> clazz, File path) {
		try {
			// clazz.getClassLoader().getResource(pp) may return system library
			// if path is common (Like "/com")
			File pp = getPath(clazz);

			String strPath = path.getPath();

			if (pp.isDirectory()) {
				if (strPath.startsWith(File.separator))
					pp = new File(pp, strPath);
				else
					pp = new File(pp, getClassPath(clazz, strPath));

				File r = new File(pp.toURI());

				if (!r.exists())
					throw new RuntimeException("File not found: " + r);

				String[] ss = r.list();
				if (ss == null)
					return new ArrayList<String>();

				if (ss.length == 0)
					throw new RuntimeException("Font directory is empy: " + r);

				return Arrays.asList(ss);
			}

			if (pp.isFile()) {
				String p;

				if (strPath.startsWith(File.separator))
					p = StringUtils.removeStart(strPath, File.separator);
				else
					p = getClassPath(clazz, strPath);

				JarFile jar = new JarFile(pp);
				Enumeration<JarEntry> entries = jar.entries();
				jar.close();
				Set<String> result = new HashSet<>();

				boolean f = false;
				while (entries.hasMoreElements()) {
					String name = entries.nextElement().getName();
					if (name.startsWith(p)) {
						f = true;
						String a = StringUtils.removeStart(name, p);
						a = StringUtils.removeStart(a, File.separator);
						a = a.trim();

						int e = StringUtils.indexOfAny(a, new char[] { '/', '\\' });
						if (e != -1)
							a = a.substring(0, e);

						if (!a.isEmpty())
							result.add(a);
					}
				}

				if (!f)
					throw new RuntimeException("Jar file: " + pp + " has no entry: " + p);

				if (result.isEmpty())
					throw new RuntimeException("Jar file: " + pp + " has empty folder: " + p);

				return new ArrayList<String>(result);
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return null;
	}

}
