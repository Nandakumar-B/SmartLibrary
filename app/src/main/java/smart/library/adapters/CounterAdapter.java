package smart.library.adapters;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import smart.library.models.*;
import smart.library.*;
import android.graphics.*;
import android.view.animation.*;

public class CounterAdapter extends BaseExpandableListAdapter
{
	private Context mCtx;
	private List<String> mList;
	private HashMap<String,List<Counter>> mItemList;
	
	public CounterAdapter(Context ctx,List<String> list,HashMap<String,List<Counter>> itemList){
		this.mCtx=ctx;
		this.mList=list;
		this.mItemList=itemList;
	}
	@Override
	public int getGroupCount()
	{
		return mList.size();
	}

	@Override
	public int getChildrenCount(int p1)
	{
		return mItemList.get(getGroup(p1)).size();
	}

	@Override
	public Object getGroup(int p1)
	{
		return mList.get(p1);
	}

	@Override
	public Object getChild(int p1, int p2)
	{
		return mItemList.get(getGroup(p1)).get(p2);
	}

	@Override
	public long getGroupId(int p1)
	{
		return p1;
	}

	@Override
	public long getChildId(int p1, int p2)
	{
		return p2;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(int groupPos, boolean isExpand, View v, ViewGroup parent)
	{
	    if(v==null){
			v=LayoutInflater.from(mCtx).inflate(R.layout.group_view,null);
		}
		TextView tvName=v.findViewById(R.id.txt_group_name);
		tvName.setTypeface(Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Bold.ttf"));
		tvName.setText((String)getGroup(groupPos));
		return v;
	}

	@Override
	public View getChildView(int groupPos, int childPos, boolean isLastChild, View v, ViewGroup parent)
	{
		if(v==null){
			v=LayoutInflater.from(mCtx).inflate(R.layout.child_view,null);
		}
		RelativeLayout pan=v.findViewById(R.id.child_viewRelativeLayout);
		TextView tvName=v.findViewById(R.id.txt_child_name);
		TextView tvPath=v.findViewById(R.id.txt_child_path);
		
		Counter cn=(Counter)getChild(groupPos,childPos);
		tvName.setTypeface(Typeface.createFromAsset(mCtx.getAssets(),"Montserrat-Regular.ttf"));
		tvName.setText(cn.getName());
		tvPath.setText(cn.getPath());
		pan.startAnimation(AnimationUtils.loadAnimation(mCtx,R.anim.fade_in));
		return v;
	}

	@Override
	public boolean isChildSelectable(int p1, int p2)
	{
		return true;
	}
	
}
