package smart.library.adapters;

import android.widget.*;
import smart.library.models.*;
import android.content.*;
import java.util.*;
import android.view.*;
import smart.library.*;
import android.view.animation.*;
import android.animation.*;
import android.graphics.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.*;
import java.text.*;

public class BookAdapter extends ArrayAdapter<Books>
{
	public Context mCtx;
	public int mRes;
	public ArrayList<Books> mList;

	public BookAdapter(Context ctx, int res, List<Books> list)
	{
		super(ctx, res, list);
		this.mCtx = ctx;
		this.mRes = res;
		this.mList = (ArrayList<Books>) list;
	}

	@Override
	public int getCount()
	{
		return mList.size();
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		Typeface tpb=Typeface.createFromAsset(mCtx.getAssets(), "Montserrat-Bold.ttf");
		Typeface tpr=Typeface.createFromAsset(mCtx.getAssets(), "Montserrat-Regular.ttf");
		FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

		String id=getItem(position).getBId();
		String img=getItem(position).getBImg();
		String name=getItem(position).getBName();
		String date=getItem(position).getBTime();
		String res=getItem(position).getBRes();
		String bro=getItem(position).getBBro();

		ViewHolder vh;
		if (view == null)
		{
			view = (RelativeLayout)LayoutInflater.from(mCtx).inflate(mRes, parent, false);
			vh = new ViewHolder(view);
			view.setTag(vh);
		}
		else
		{
			vh = (ViewHolder)view.getTag();
		}
		vh.bId.setText(id);
		vh.bImg.setImageBitmap(bitMapToImage(img));
		vh.bName.setText(name);
		vh.bName.setTypeface(tpr);
		vh.bName.setSelected(true);

		if (!date.equals("null"))
		{
			float diff=Daybetween(getCurrentDate(), date);
			if (!bro.equals("null"))
			{
				if(user!=null){
					if(user.getUid().equals(bro)){
						vh.imgAvail.setImageResource(R.drawable.icon_star_blue);
					}
					else{
						vh.imgAvail.setImageResource(R.drawable.icon_star_red);
					}
				}
			}
			if (diff == 0)
			{
				if (user != null)
				{
					if (!res.equals("null"))
					{
						if (user.getUid().equals(res))
						{
							vh.imgAvail.setImageResource(R.drawable.icon_dot_blue);
						}
						else
						{
							vh.imgAvail.setImageResource(R.drawable.icon_dot_red);
						}
					}
				}
			}
		}

		ObjectAnimator anim=(ObjectAnimator)AnimatorInflater.loadAnimator(mCtx, R.animator.flipping);
		anim.setTarget(vh.grid);
		anim.setDuration(500);
		anim.start();
		vh.grid.startAnimation(AnimationUtils.loadAnimation(mCtx, R.anim.fade_in));

		return view;
	}
	class ViewHolder
	{
		ImageView bImg,imgAvail;
		TextView bName,bId,bres;
		RelativeLayout grid;
		public ViewHolder(View v)
		{
			grid = v.findViewById(R.id.grid_viewRelativeLayout);
			bId = v.findViewById(R.id.tvg_id);
			bres=v.findViewById(R.id.tv_reserve);
			bImg = v.findViewById(R.id.gv_img);
			bName = v.findViewById(R.id.tvg_name);
			imgAvail = v.findViewById(R.id.img_available);
		}
	}
	public static Bitmap bitMapToImage(String code)
	{
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		byte[] imageBytes=byteArrayOutputStream.toByteArray();
		imageBytes = android.util.Base64.decode(code, android.util.Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
	}
	private String getCurrentDate()
	{
		SimpleDateFormat date_format=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		return date_format.format(new Date());
	}
	public long Daybetween(String date1, String date2)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date Date1 = null,Date2 = null;
		try
		{
			Date1 = sdf.parse(date1);
			Date2 = sdf.parse(date2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);
	}
}
