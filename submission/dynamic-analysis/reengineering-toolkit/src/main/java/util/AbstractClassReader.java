package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Given a root directory, process all of the Class files within it
 * and its subdirectory, and call the loadClass method, passing the class file
 * as a parameter.
 *
 * loadClass to be overridden depending on the type of class-loader to be used.
 */

public abstract class AbstractClassReader<T> {

    public List<T> processDirectory(File directory, String pkgname) {
        ArrayList<T> classes = new ArrayList<T>();
        String prefix = pkgname+getPrefix();
        if(pkgname.equals(""))
            prefix = "";

        // Get the list of the files contained in the package
        String[] files = directory.list();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            String className = null;

            // we are only interested in .class files
            if (fileName.endsWith(".class")) {
                // removes the .class extension
                className = prefix+fileName.substring(0, fileName.length() - 6);
            }
            if (className != null) {
                T loaded = loadClass(className);
                if(loaded!=null)
                    classes.add(loaded);
            }

            //If the file is a directory recursively class this method.
            File subdir = new File(directory, fileName);
            if (subdir.isDirectory()) {

                classes.addAll(processDirectory(subdir, prefix + fileName));
            }
        }
        return classes;
    }

    protected abstract String getPrefix();

    protected abstract T loadClass(String className);
}
