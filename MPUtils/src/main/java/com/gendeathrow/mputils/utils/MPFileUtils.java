package com.gendeathrow.mputils.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.compress.utils.CharsetNames;

public class MPFileUtils {

    public static final String NEW_LINE;

    static
    {
        NEW_LINE = System.getProperty("line.separator");
    }
    
	
	public static void createSaveTextFile(File dest, List<String> lines) throws IOException {
		
    	if (dest.getParentFile() != null)
    		dest.getParentFile().mkdirs();

		if (!dest.exists())
			dest.createNewFile();
		
        if (dest.canWrite())
        {
        	try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(dest), CharsetNames.UTF_8))) {
        		lines.forEach(line->{
					try {
						writer.write(line);
						writer.write(NEW_LINE);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

        	}
        }
	}
	

	public static String readFile(String path) throws IOException 
	{
		return readFile(new File(path));
	}
	
	public static String readFile(File checkFile) throws IOException {
		
		if(checkFile.exists()) {
			byte[] encoded = Files.readAllBytes(Paths.get(checkFile.getPath()));
			return new String(encoded, CharsetNames.UTF_8);
		}
		else
			return "";
	}
	
}
