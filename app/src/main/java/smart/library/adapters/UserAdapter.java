package smart.library.adapters;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import smart.library.*;
import smart.library.models.*;
import android.graphics.*;
import android.view.animation.*;

public class UserAdapter extends ArrayAdapter<Profile>
{
   private Context mCtx;
   private int mRes;
   private List<Profile> mList;
   
   public UserAdapter(Context ctx,int res,List<Profile> list){
	   super(ctx,res,list);
	   this.mCtx=ctx;
	   this.mRes=res;
	   this.mList=list;
   }

   @Override
   public View getView(int position, View v, ViewGroup parent)
   {
	   Typeface tpb,tpr;
	   tpb=Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Bold.ttf");
	   tpr=Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Regular.ttf");
	   ViewHolder vh;
	   if(v==null){
		   v=LayoutInflater.from(mCtx).inflate(R.layout.custom_user,parent,false);
		   vh=new ViewHolder(v);
		   v.setTag(vh);
	   }else{
		   vh=(ViewHolder)v.getTag();
	   }
	   vh.tvName.setTypeface(tpb);
	   vh.tvPay.setTypeface(tpr);
	   vh.tvPrice.setTypeface(tpb);
	   vh.tvInfo.setTypeface(tpr);
	   vh.tvId.setText(getItem(position).getId());
	   vh.tvName.setText(getItem(position).getName());
	   vh.tvPrice.setText("\u20B9 "+String.valueOf(getItem(position).getPrice()));
	   vh.tvInfo.setText("Books - Reserved : "+String.valueOf(getItem(position).getReserve())+" | Borrowed : "+String.valueOf(getItem(position).getBorrow()));
	   vh.pan.startAnimation(AnimationUtils.loadAnimation(mCtx,R.anim.zoom_in));
	   return v;
   }
   class ViewHolder{
	   TextView tvId,tvName,tvPrice,tvInfo,tvPay;
	   LinearLayout pan;
	   ViewHolder(View v){
		   pan=v.findViewById(R.id.custom_userLinearLayout);
		   tvId=v.findViewById(R.id.txt_user_id);
		   tvName=v.findViewById(R.id.txt_user_name);
		   tvPrice=v.findViewById(R.id.txt_user_price);
		   tvPay=v.findViewById(R.id.txt_user_pay);
		   tvInfo=v.findViewById(R.id.txt_user_info);
	   }
   }
}
