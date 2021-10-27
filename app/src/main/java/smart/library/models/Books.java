package smart.library.models;
import android.graphics.*;
import java.io.*;

public class Books
{
	private String bId,bImg,bName,bAuthor,bType,bLink,bRfid,bAbout,bRes,bBro,bTime;
	
	public Books(){
		
	}
	public Books(String bId,String bImg,String bName,String bAuthor,String bType,String bLink,String bRfid,String bAbout,String bRes,String bBro,String bTime){
		this.bId=bId;
		this.bImg=bImg;
		this.bName=bName;
		this.bAuthor=bAuthor;
		this.bType=bType;
		this.bLink=bLink;
		this.bRfid=bRfid;
		this.bAbout=bAbout;
		this.bRes=bRes;
		this.bBro=bBro;
		this.bTime=bTime;
	}
	public void setBId(String bId){
		this.bId=bId;
	}
	public String getBId(){
		return bId;
	}
	public void setBName(String bName){
		this.bName=bName;
	}
	public String getBName(){
		return bName;
	}
	public void setBImg(String bImg){
		this.bImg=bImg;
	}
	public String getBImg(){
		return bImg;
	}
	public void setBAuthor(String bAuthor){
		this.bAuthor=bAuthor;
	}
	public String getBAuthor(){
		return bAuthor;
	}
	public void setBLink(String bLink){
		this.bLink=bLink;
	}
	public String getBLink(){
		return bLink;
	}
	public void setBRfid(String bRfid){
		this.bRfid=bRfid;
	}
	public String getBRfid(){
		return bRfid;
	}
	public void setBAbout(String bAbout){
		this.bAbout=bAbout;
	}
	public String getBAbout(){
		return bAbout;
	}
	public void setBType(String bType){
		this.bType=bType;
	}
	public String getBType(){
		return bType;
	}
	public void setBRes(String bRes){
		this.bRes=bRes;
	}
	public String getBRes(){
		return bRes;
	}
	public void setBBro(String bBro){
		this.bBro=bBro;
	}
	public String getBBro(){
		return bBro;
	}
	public void setBTime(String bTime){
		this.bTime=bTime;
	}
	public String getBTime(){
		return bTime;
	}
	
}
