package smart.library.models;

public class Counter
{
	private String name,path;
	
	public Counter(String name,String path){
		this.name=name;
		this.path=path;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setPath(String path){
		this.path=path;
	}
	public String getPath(){
		return path;
	}
}
