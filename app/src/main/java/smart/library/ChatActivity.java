package smart.library;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import smart.library.adapters.*;
import smart.library.models.*;
import android.view.inputmethod.*;
import android.graphics.*;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;
import com.google.firebase.auth.*;

public class ChatActivity extends Activity
{
	private RecyclerView lv;
	private EditText et;
	private ImageButton btn;

	private ChatAdapter ad;
	private ArrayList<Chats> mList;

	private Typeface tpb,tpr;

	private FirebaseAuth auth;
	private FirebaseUser user;
	private FirebaseDatabase db;
	private DatabaseReference ref;

	private int totBook=0;
	private String[] askFirst={"can","could","would","may","need"};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_view);
		lv = findViewById(R.id.chat_list);
		et = findViewById(R.id.et_chat);
		btn = findViewById(R.id.btn_send);

		auth = FirebaseAuth.getInstance();
		user = auth.getCurrentUser();
		db = FirebaseDatabase.getInstance();
		ref = db.getReference("smart");
	    readData("books", "books", null);
		tpb = Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf");
		tpr = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
		et.setTypeface(tpr);
		mList = new ArrayList<>();
		ad = new ChatAdapter(ChatActivity.this, mList);
		lv.setLayoutManager(new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false));
		lv.setAdapter(ad);
		setChat("Hi , iam Chatbot ! how can i help you ?", false, "null","null");

		btn.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					String m=et.getText().toString();
					if (et.getText().toString().equals(""))
					{
						message("Please enter something");
					}
					else
					{
						setChat(m, true, "null","null");
						if (!isLastVisible())
						{
							lv.smoothScrollToPosition(ad.getItemCount() - 1);
						}
						checkChat(m);
						et.setText("");
						et.onEditorAction(EditorInfo.IME_ACTION_DONE);
					}

				}
			});
	}
	private void checkChat(String m)
	{
		readData("keyword", "books", m.toLowerCase());
		if (m.equals("hi"))
		{
			setChat("hello", false, "null","null");
		}
		else if (m.contains("how many books are available ?"))
		{
			setChat("Total " + String.valueOf(totBook) + " books are available!", false, "null","null");
		}
	}
	private void setChat(String m, boolean t, String link,String id)
	{
		Chats chat=new Chats(m, t, link,id);
		mList.add(chat);
	}
	boolean isLastVisible()
	{
		LinearLayoutManager man=(LinearLayoutManager)lv.getLayoutManager();
		int p=man.findLastVisibleItemPosition();
		int n=lv.getAdapter().getItemCount();
		return (p >= n);
	}
	private void readData(final String type, String path, final String info)
	{
		ref.child(path).addValueEventListener(new ValueEventListener(){

				@Override
				public void onDataChange(DataSnapshot p1)
				{
					if (type.equals("books"))
					{
						for (DataSnapshot shot:p1.getChildren())
						{
							totBook++;
							Books bk=shot.getValue(Books.class);
						}
					}
					if (type.equals("keyword"))
					{
						for (DataSnapshot shot:p1.getChildren())
						{
							Books bk=shot.getValue(Books.class);
							if (info.contains(bk.getBName().toLowerCase())||
							    info.contains(bk.getBAuthor().toLowerCase())||
								info.contains(bk.getBType().toLowerCase()))
							{
								String link=bk.getBLink();
								String resId="null";
								String msg="";//The book "+bk.getBName()+" ";
								if (!bk.getBBro().equals("null"))
								{           //borrow!=null
								    resId="null";
									if (bk.getBBro().equals(user.getUid()))
									{
										msg += "You have borrowed the book " + bk.getBName() + " on " + bk.getBTime();
									}
									else
									{
										msg += "On " + bk.getBTime() + ", another person borrowed the book " + bk.getBName();
									}
								}
								else
								{                                       //borrow=null
									if (!bk.getBRes().equals("null"))
									{        //reserve!=null
										if (bk.getBRes().equals(user.getUid()))
										{
											resId=bk.getBRes()+","+shot.getKey();
											msg += "You have reserved the book " + bk.getBName();
										}
										else
										{
											resId="null";
											msg += "The book " + bk.getBName() + " was reserved by another.";
										}
									}
									else
									{                                 //reserve=null
									    resId="res"+","+shot.getKey();
										msg += "The book " + bk.getBName() + " is available in the library.  You can now borrow or reserve this book.";
									}
								}
								msg+="\n\n Author : "+bk.getBAuthor()+"\n Genre : "+bk.getBType();
								setChat(msg, false, link,resId);
							}      //if for book name
							else
							{
								if (info.contains(bk.getBAuthor().toLowerCase()))
								{ 

								}
							}

						}//for loop for firebase

					}
				}

				@Override
				public void onCancelled(DatabaseError p1)
				{
					message(p1.getMessage());
				}
			});
	}
	private void message(String m)
	{
		Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
	}

}
