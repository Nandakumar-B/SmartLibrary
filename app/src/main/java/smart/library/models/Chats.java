package smart.library.models;

public class Chats
{
	public String msg,bookLink,resId;
	public boolean isMe;
	
	public Chats(String msg,boolean isMe,String bookLink,String resId){
		this.msg=msg;
		this.isMe=isMe;
		this.bookLink=bookLink;
		this.resId=resId;
	}
	public void setMessage(String msg){
		this.msg=msg;
	}
	public String getMessage(){
		return msg;
	}
	public void setMe(boolean isMe){
		this.isMe=isMe;
	}
	public boolean getMe(){
		return isMe;
	}
	public void setBookLink(String bookLink){
		this.bookLink=bookLink;
	}
	public String getBookLink(){
		return bookLink;
	}
	public void setResId(String resId){
		this.resId=resId;
	}
	public String getResId(){
		return resId;
	}
}
