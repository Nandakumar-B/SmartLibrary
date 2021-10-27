package smart.library.models;

public class Profile
{
	private String id,name,phone,mail,pass;
	private int price,reserve,borrow;
	public Profile(String id,String name,String phone,String mail,String pass,int price,int reserve,int borrow){
		this.id=id;
		this.name=name;
		this.mail=mail;
		this.phone=phone;
		this.pass=pass;
		this.price=price;
		this.reserve=reserve;
		this.borrow=borrow;
	}
	public Profile(){}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return id;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setMail(String mail){
		this.mail=mail;
	}
	public String getMail(){
		return mail;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setPass(String pass){
		this.pass=pass;
	}
	public String getPass(){
		return pass;
	}
	public void setPrice(int price)
	{
		this.price = price;
	}
	public int getPrice()
	{
		return price;
	}
	public void setReserve(int reserve)
	{
		this.reserve = reserve;
	}
	public int getReserve()
	{
		return reserve;
	}
	
	public void setBorrow(int borrow)
	{
		this.borrow = borrow;
	}
	public int getBorrow()
	{
		return borrow;
	}
}
