package smart.library.adapters;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import smart.library.*;
import smart.library.models.*;
import android.view.animation.*;
import android.graphics.*;
import android.net.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHolder>
{
	 FirebaseAuth auth;
	 FirebaseUser user;
	 FirebaseDatabase db;
	 DatabaseReference ref;
	
	Context mCtx;
	List<Chats> mList;
	
	private int lastPos=-1;
	private String bookId="null";
	
	class CustomViewHolder extends RecyclerView.ViewHolder{
		TextView tv;
		LinearLayout lv;
		Button btnLeft,btnRight;
		public CustomViewHolder(View v){
			super(v);
			lv=v.findViewById(R.id.chat_LinearLayout);
			tv=v.findViewById(R.id.txt_chat);
			btnLeft=v.findViewById(R.id.chat_leftButton);
			btnRight=v.findViewById(R.id.chat_rightButton);
			auth=FirebaseAuth.getInstance();
			user=auth.getCurrentUser();
			db=FirebaseDatabase.getInstance();
			ref=db.getReference("smart");
		}
	}
	public ChatAdapter(Context ctx,List<Chats> list){
		this.mCtx=ctx;
		this.mList=list;
	}
	@NonNull
	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		return new CustomViewHolder(LayoutInflater.from(mCtx).inflate(viewType,parent,false));
	}

	@Override
	public void onBindViewHolder(final CustomViewHolder vh,final int position)
	{

	   final String bookLink=mList.get(position).getBookLink();
	   final String data=mList.get(position).getResId();
	   String resId="null";
	   if(data.contains(",")&&!data.equals("null")){
		   resId=data.split(",")[0];
		   bookId=data.split(",")[1];
	   }
		vh.tv.setText(mList.get(position).getMessage());
		vh.btnLeft.setVisibility(bookLink.equals("null")?View.GONE:View.VISIBLE);
		vh.btnRight.setVisibility(resId.equals("null")?View.GONE:View.VISIBLE);
		if(resId.equals(user.getUid())){
			vh.btnRight.setText("CANCEL RESERVATION");
		}else if(resId.equals("res")){
			vh.btnRight.setText("RESERVE");
		}
		vh.btnLeft.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mCtx.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(bookLink)));
				}
		});
		vh.btnRight.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
						//message("Do reservation on "+bookId);
						Intent in=new Intent(mCtx,MainActivity.class);
						in.putExtra("reserve",bookId);
						mCtx.startActivity(in);
				}
		});
		vh.tv.setTypeface(Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Regular.ttf"));
		vh.btnLeft.setTypeface(Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Regular.ttf"));
		vh.btnRight.setTypeface(Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Regular.ttf"));
		//setAnimation(vh.tv,position,mList.get(position).getMe());
		setAnimation(vh.lv,position,mList.get(position).getMe());
	}
    private void setAnimation(View v,int pos,boolean isMe){
		if(pos>lastPos){
			if(isMe){
				v.startAnimation(AnimationUtils.loadAnimation(mCtx,R.anim.scale_left));
			}
			else{
				v.startAnimation(AnimationUtils.loadAnimation(mCtx,R.anim.scale_right));
			}
			lastPos=pos;
		}
	}
	@Override
	public int getItemCount()
	{
		return mList.size();
	}

	@Override
	public int getItemViewType(int position)
	{
		if(mList.get(position).getMe()){
			return R.layout.chat_right;
		}
		return R.layout.chat_left;
	}
	private void message(String m){
		Toast.makeText(mCtx,m,Toast.LENGTH_SHORT).show();
	}
}
