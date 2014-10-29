package operation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class AbsReadFile {
	
	/*
     * ��ȡĳ���ļ����µ������ļ�
     */
    public boolean readfile(String filepath) throws FileNotFoundException, IOException {
            try {

                    File file = new File(filepath);
                    if (!file.isDirectory()) {
                            dealWithFile(file);

                    } else if (file.isDirectory()) {
                            //System.out.println("�ļ���");
                            String[] filelist = file.list();
                            for (int i = 0; i < filelist.length; i++) {
                                    File readfile = new File(filepath + "\\" + filelist[i]);
                                    if (!readfile.isDirectory()) {
                                    	dealWithFile(readfile);
                                    } else if (readfile.isDirectory()) {
                                            readfile(filepath + "\\" + filelist[i]);
                                    }
                            }

                    }

            } catch (FileNotFoundException e) {
                    System.out.println("readfile()   Exception:" + e.getMessage());
            }
            return true;
    }
	/**
	 * @param file
	 */
	public abstract void dealWithFile(File file);


}
