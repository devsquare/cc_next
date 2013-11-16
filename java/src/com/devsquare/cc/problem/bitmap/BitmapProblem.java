package com.devsquare.cc.problem.bitmap;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.devsquare.cc.interfaces.Constants;
import com.devsquare.cc.interfaces.Problem;
import com.devsquare.cc.problem.bitmap.interfaces.IReader;

public class BitmapProblem implements Problem<BitmapOutput, BitmapParameter> {
	
	
	
	private Map<String, IReader> readerMap = new HashMap<String, IReader>();
	
	Random randomGen = null;
	ArrayList<Entry<String,IReader>> filelist = null;
	
	private static BitmapProblem bp = new BitmapProblem();
	
	private BitmapProblem(){
		
	}
	
	public static BitmapProblem get(){
		return bp;
	}
	
	public BitmapProblem init() throws IOException{
		String dataDir = Constants.DATA_DIR;
		File f  = new File(dataDir);
		File[] farray = f.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String name = pathname.getName();
				return (name.endsWith("jpg") || name.endsWith("jpeg"));
			}
		});
		
		for(File ff:farray){
			readerMap.put(ff.getName(), new BitmapFileChannelReader(ff));
		}
		
		filelist = new ArrayList<Map.Entry<String,IReader>>(readerMap.size());
		Iterator<Entry<String, IReader>> itr = readerMap.entrySet().iterator();
		while(itr.hasNext()){
			filelist.add(itr.next());
		}
		randomGen = new Random();
		return this;
	}
	
	

	public BitmapOutput getFileChunk(BitmapParameter parameter) throws Exception {
		int position = parameter.getPosition();
		int length = parameter.getLength();
		String fileId = parameter.getFileID();
		IReader reader = readerMap.get(fileId);
		byte b[] = reader.read(position, length);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(BitmapParameter.BYTE, b);
		BitmapOutput bop = new BitmapOutput(map);
		return bop;
	}
	
	public BitmapOutput getRandomFile(){
		int index = randomGen.nextInt(readerMap.size());
		Entry<String, IReader> entry = filelist.get(index);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(BitmapParameter.FILE_ID, entry.getKey());
		map.put(BitmapParameter.FILE_SIZE, entry.getValue().size());
		
		return new BitmapOutput(map);
	}

	@Override
	public BitmapOutput validate(BitmapParameter parameter) {
		
		return null;
	}

}
