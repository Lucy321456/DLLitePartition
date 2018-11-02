package VirtualABoxConstruction;

import java.util.ArrayList;
import java.util.List;


class CMTask implements Runnable{
	private String abox;
	private String indAssertionPath;
	private String mapFile;
	private String sizeFile;
	
	public CMTask(String abox, String indAssertionPath, String mapFile, String sizeFile){
		this.abox=abox;
		this.indAssertionPath=indAssertionPath;
		this.mapFile=mapFile;
		this.sizeFile=sizeFile;
	}
	
	public void run(){
		CreateOneVirtualABox.extractInd(abox, indAssertionPath, mapFile, sizeFile);
		Thread.yield();
	}
}

public class CreateMVirtualABox{
	public void extractInd(List<String> aboxs, List<String> indAssertionPaths, List<String> mapFiles, List<String> sizeFiles){
		for(int i=0; i<aboxs.size(); i++){
			new Thread(new CMTask(aboxs.get(i), indAssertionPaths.get(i), mapFiles.get(i), sizeFiles.get(i)));
		}
	}
	
	public static void main(String[] args){
		CreateMVirtualABox CMVA=new CreateMVirtualABox();
		List<String> aboxs=new ArrayList<String>();
		List<String> indAssertionPath=new ArrayList<String>();
		List<String> mapFiles=new ArrayList<String>();
		List<String> sizeFiles=new ArrayList<String>();
		for(int i=0; i<30; i++){
			aboxs.add("E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/SubABox_"+i+".txt");
			indAssertionPath.add("E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/VABox"+i+"/");
			mapFiles.add("E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/CPMap"+i+".txt");
			sizeFiles.add("E:/Experiment/DLLitePartition/Data/dbpedia/KBPartition/KBPartition/SizeOfDB"+i+".txt");
		}
		CMVA.extractInd(aboxs, indAssertionPath, mapFiles, sizeFiles);
	}
}