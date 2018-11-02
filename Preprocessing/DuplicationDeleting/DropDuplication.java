

package DuplicationDeleting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*****************************************
 * drop duplication of RDF datasets
 * drop duplication of modes then merge these modes
 * in the following, size denote the size of modes, 
 * @author Lucy
 *
 */


public class DropDuplication{
	public void dropDuplicateTriple(String oldFile, int size, String newFile){
		try{
			Set<String> set=new HashSet<String>();
			InputStreamReader rd= new InputStreamReader(new FileInputStream(oldFile));
			BufferedReader bw = new BufferedReader(rd);
			String lin="";
			int counk=0;
			int count=0;
			while((lin=bw.readLine())!=null){
				count=count+1;
				set.add(lin);
				if(count%size==0){
					System.out.println("count: "+count);
					merge(set, newFile);
					set.clear();
					
				}
			}
			if(set.size()>0){
				merge(set, newFile);
				set.clear();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void merge(Set<String> set, String newFile){
		try{
			int num=0;
			File f=new File(newFile);
			if(!f.exists()){
				FileWriter fw=new FileWriter(newFile);
				for(String s: set){
					num=num+1;
					fw.write(s+"\r\n");
				}
				fw.close();
			}
			else{
				InputStreamReader rd= new InputStreamReader(new FileInputStream(newFile));
				BufferedReader bw = new BufferedReader(rd);
				String lin="";
				while((lin=bw.readLine())!=null){
					num=num+1;
					if(set.contains(lin)){
						set.remove(lin);
					}
				}
				rd.close();
				bw.close();
				if(set.size()>0){
					FileWriter fw=new FileWriter(newFile, true);
					for(String s: set){
						num=num+1;
						fw.write(s+"\r\n");
					}
					fw.close();
				}
				
			}
			System.out.println("new number of triples without repeat: "+num);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void dropDuplicateTripleForVeryLargeFile(String oldFile, int size, String dirc){
		try{
			Set<String> set=new HashSet<String>();
			int num=0;
			InputStreamReader rd= new InputStreamReader(new FileInputStream(oldFile));
			BufferedReader bw = new BufferedReader(rd);
			String lin="";
			int counk=0;
			int count=0;
			while((lin=bw.readLine())!=null){
				count=count+1;
				set.add(lin);
				if(count%size==0){
					System.out.println("count: "+count);
					List<String> list=new ArrayList<String>();
					list.addAll(set);
					set.clear();
					Collections.sort(list);
					FileWriter fw=new FileWriter(dirc+"/file_"+num+".txt");
					for(int i=0; i<list.size(); i++){
						fw.write(list.get(i)+"\r\n");
					}
					fw.close();
					list.clear();
					System.out.println("finish write!!!");
					num=num+1;
				}
			}	
			bw.close();
			
			List<String> list=new ArrayList<String>();
			list.addAll(set);
			Collections.sort(list);
			FileWriter fw=new FileWriter(dirc+"/file_"+num+".txt");
			for(int i=0; i<list.size(); i++){
				fw.write(list.get(i)+"\r\n");
			}
			fw.close();
			list.clear();
			set.clear();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void merge(String dir, String newFile){
		try{
			int total=0;
			int count=0;
			FileWriter fw=new FileWriter(newFile);
			Map<BufferedReader, readLine> map=new HashMap<BufferedReader, readLine>();
			File folder=new File(dir);
			File[] files=folder.listFiles();
			Set<String> lines=new HashSet<String>();
			for(File f: files){
				InputStreamReader rd= new InputStreamReader(new FileInputStream(f));//¶ÁÈ¡URIÎÄ¼þ
				BufferedReader bw = new BufferedReader(rd);
				readLine rl=new readLine();
				String s=bw.readLine();
				count=count+1;
				rl.line=s;
				if(s==null){
					System.out.println(f.getName());
				}
				lines.add(s);
				map.put(bw, rl);
			}			
			while(true){
				String min=Collections.min(lines);
				fw.write(min+"\r\n");
				total=total+1;
				lines.remove(min);
				Set<BufferedReader> deleteBR=new HashSet<BufferedReader>();
				for(BufferedReader br: map.keySet()){
					if(map.get(br).line.equals(min)){
						String s=br.readLine();
						count=count+1;
						if(count%1000==0){
							System.out.println("count: "+count);
						}
						if(s==null){
							deleteBR.add(br);
						}
						else{
							map.get(br).line=s;
							lines.add(s);
						}
					}
				}	
				for(BufferedReader br: deleteBR){
					map.remove(br);
				}				
				if(map.size()==0){
					break;
				}
			}
			fw.close();
			
			System.out.println("the total number of different triples: "+total);
		}catch(Exception e){
			e.printStackTrace();
		}
	} 
	
	public static void main(String[] args){  
		DropDuplication ddt=new DropDuplication();
		long start=System.currentTimeMillis();
		//ddt.dropDuplicateTripleForVeryLargeFile("E:/BTCData/KB_noSameAs.txt", 5000000, "E:/BTCData/files/"); 
		ddt.merge("E:/BTCData/files/", "E:/BTCData/test.txt"); 
		long end=System.currentTimeMillis();
		System.out.println("used time (seconds): "+(end-start)/1000);
	}
}

class readLine{ 
	public String line;
	public readLine(){
		line="";
	}
}