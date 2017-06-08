package minetweaker.runtime.providers;

import minetweaker.runtime.IScriptIterator;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * @author Stan
 */
public class ScriptIteratorZip implements IScriptIterator {

    private final File file;
    private final ZipFile zipFile;
    private final Iterator<ZipEntry> entries;
    private ZipEntry current;

    public ScriptIteratorZip(File file) throws IOException {
        this.file = file;

        zipFile = new ZipFile(file);
        List<ZipEntry> entriesList = new ArrayList<ZipEntry>();
        Enumeration<? extends ZipEntry> original = zipFile.entries();
        while(original.hasMoreElements()) {
            ZipEntry entry = original.nextElement();
            if(entry.getName().startsWith("scripts/") && entry.getName().endsWith(".zs")) {
                entriesList.add(entry);
            }
        }

        entries = entriesList.iterator();
    }

    @Override
    public String getGroupName() {
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }

    @Override
    public boolean next() {
        if(entries.hasNext()) {
            current = entries.next();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return current.getName().substring("scripts/".length());
    }

    @Override
    public InputStream open() throws IOException {
        return zipFile.getInputStream(current);
    }
}
