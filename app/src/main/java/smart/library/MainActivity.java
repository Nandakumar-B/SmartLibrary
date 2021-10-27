package smart.library;

import android.app.*;
import android.os.*;
import android.widget.*;
import smart.library.adapters.*;
import smart.library.models.*;
import java.util.*;
import android.view.View.*;
import android.view.*;
import android.content.*;
import android.view.animation.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import java.io.*;
import android.net.*;
import java.text.*;
import android.graphics.Typeface;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends Activity implements OnClickListener
{
	private FirebaseAuth auth;
	private FirebaseUser user;
	private DatabaseReference db;

	private Animation proDown;

	ImageButton btnScan,btnBot,btnAdd,btnUser;
	private ImageView imgDet;
	private GridView gv;
	private ListView lv;
	private ExpandableListView elv,elv1;
	Button btnOut,btnEbook,btnRes;
	private RelativeLayout panPro1,panProSub1,panPro,panProSub,panDet,panDetSub;
	TextView tsv,tvHint,tvPro,tvPro1,
	tvName,tvPhone,tvMail,tvName1,tvPhone1,tvMail1,
	tvAhead,tvAinfo,tvNeg,tvPos,
	tvDTitle,tvDAuthor,tvDAbHead,
	tvDAbout,tvDInfo,tvCash,tvCashInfo,tvCash1,tvCashInfo1;
	private SearchView sv;
	private Typeface tpb,tpr;

	private BookAdapter bad;
	private CounterAdapter cad,cad1;
	private UserAdapter uad;
	private AlertDialog dialog;
	private List<Books> mList;
	private List<String> grpList,grpList1;
	private List<Counter> mList1,mList2,mList3,mList4;
	private List<Profile> usList;
	private HashMap<String,List<Counter>> hash,hash1;

	private String stUrl,stBook="",stUser="null";
	private int exp=15,mon=1,countRes=0,countBor=0;
	private boolean press,isPro,isPro1,isDet,isLog,isAdmin=false;
	private boolean isUsers=false;

	@Override
	public void onBackPressed()
	{
		if (press)
		{
			super.onBackPressed();
			return;
		}
		message("Press again");
		sv.setIconified(true);
		if (isPro && !isDet)
		{
			panProSub.startAnimation(proDown);
		}
		if (isPro1 && !isDet)
		{
			panProSub1.startAnimation(proDown);
		}
		if (isDet)
		{
			panDetSub.startAnimation(proDown);
		}
		press = true;
		new Handler().postDelayed(new Runnable(){
				@Override
				public void run()
				{
					press = false;
				}
			}, 2000);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		gv = findViewById(R.id.mainGridView);
		lv = findViewById(R.id.mainListView);
		elv = findViewById(R.id.mainExpandableListView);
		elv1 = findViewById(R.id.mainExpandableListView1);
		panPro = findViewById(R.id.pan_pro);
		panPro1 = findViewById(R.id.pan_pro1);
		panDet = findViewById(R.id.pan_det);
		panProSub = findViewById(R.id.pan_pro_sub);
		panProSub1 = findViewById(R.id.pan_pro_sub1);
		panDetSub = findViewById(R.id.pan_det_sub);
		btnScan = findViewById(R.id.btn_scan);
		btnBot = findViewById(R.id.btn_bot);
		btnOut = findViewById(R.id.btn_logout);
		btnAdd = findViewById(R.id.btn_add);
		btnUser = findViewById(R.id.btn_users);
		btnEbook = findViewById(R.id.btn_ebook);
		btnRes = findViewById(R.id.btn_reserve);
		tvHint = findViewById(R.id.txt_hint);
		tvPro = findViewById(R.id.txt_pro);
		tvPro1 = findViewById(R.id.txt_pro1);
		tvName = findViewById(R.id.txt_name);
		tvPhone = findViewById(R.id.txt_phone);
		tvMail = findViewById(R.id.txt_mail);
		tvName1 = findViewById(R.id.txt_name1);
		tvPhone1 = findViewById(R.id.txt_phone1);
		tvMail1 = findViewById(R.id.txt_mail1);
		tvDTitle = findViewById(R.id.txt_det_title);
		tvDAuthor = findViewById(R.id.txt_det_author);
		tvDAbHead = findViewById(R.id.txt_det_abhead);
		tvDAbout = findViewById(R.id.txt_det_about);
		tvDInfo = findViewById(R.id.txt_det_info);
		tvCash = findViewById(R.id.txt_cash);
		tvCashInfo = findViewById(R.id.txt_cash_info);
		tvCash1 = findViewById(R.id.txt_cash1);
		tvCashInfo1 = findViewById(R.id.txt_cash_info1);
		sv = findViewById(R.id.mainSearchView);
	    tsv = sv.findViewById(sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
		imgDet = findViewById(R.id.img_det_preview);
		proDown = AnimationUtils.loadAnimation(this, R.anim.move_down);

		tpb = Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf");
		tpr = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
		tvHint.setTypeface(tpb);
		tvPro.setTypeface(tpb);
		tsv.setTypeface(tpr);
		tvPro1.setTypeface(tpb);
		tvName.setTypeface(tpb);
		tvPhone.setTypeface(tpr);
		tvMail.setTypeface(tpr);
		tvName1.setTypeface(tpb);
		tvPhone1.setTypeface(tpr);
		tvMail1.setTypeface(tpr);
		tvDTitle.setTypeface(tpb);
		tvDAuthor.setTypeface(tpr);
		tvDAbHead.setTypeface(tpb);
		tvDAbout.setTypeface(tpr);
		tvDInfo.setTypeface(tpr);
		tvCash.setTypeface(tpb);
		tvCashInfo.setTypeface(tpr);
		tvCash1.setTypeface(tpb);
		tvCashInfo1.setTypeface(tpr);
		btnOut.setTypeface(tpb);
		btnEbook.setTypeface(tpb);
		btnRes.setTypeface(tpr);
		//	String[] dataList={"nandu","chocho","shadow"};
		//	lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList));

		//	gv.startAnimation(AnimationUtils.loadAnimation(this,R.anim.move_up));
		auth = FirebaseAuth.getInstance();
		user = auth.getCurrentUser();
		db = FirebaseDatabase.getInstance().getReference("smart");

		tvHint.setOnClickListener(this);
		tvPro.setOnClickListener(this);
		btnScan.setOnClickListener(this);
		btnBot.setOnClickListener(this);
		btnOut.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		btnUser.setOnClickListener(this);
		btnEbook.setOnClickListener(this);
		btnRes.setOnClickListener(this);
		panPro.setOnClickListener(this);
		panPro1.setOnClickListener(this);
		panDet.setOnClickListener(this);
		panProSub.setOnClickListener(this);
		panProSub1.setOnClickListener(this);
		panDetSub.setOnClickListener(this);
  	    getData("users/" + user.getUid(), "profile");
        getData("books", "books");
		getData("users/" + user.getUid(), "count");

	    mList = new ArrayList<>();
		mList1 = new ArrayList<>();
		mList2 = new ArrayList<>();
		grpList = new ArrayList<>();
		mList3 = new ArrayList<>();
		mList4 = new ArrayList<>();
		grpList1 = new ArrayList<>();
		usList = new ArrayList<>();
		hash = new HashMap<>();
		hash1 = new HashMap<>();

		if (getIntent().getStringExtra("data") != null)
		{
			//message(getIntent().getStringExtra("data"));
			getData("books", "borrow");
		}
		if(getIntent().getStringExtra("reserve")!=null){
			getData("books/"+getIntent().getStringExtra("reserve"),"detail");
			stBook=getIntent().getStringExtra("reserve");
			if (!isDet)
			{
				panDet.setVisibility(View.VISIBLE);
				panDetSub.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.move_up));
				isDet = true;
			}
		}
		lv.setOnItemLongClickListener(new ListView.OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					tvCash1.setText("0");
					TextView t1=p2.findViewById(R.id.txt_user_id);
					if (!isPro1)
					{
						panPro1.setVisibility(View.VISIBLE);
						panProSub1.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.move_up));
						isPro1 = true;
					}
					getUserBookData(t1.getText().toString());
					getData("users/" + t1.getText().toString(), "users");
					return false;
				}
			});

		gv.setOnItemLongClickListener(new ListView.OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					TextView t1=p2.findViewById(R.id.tvg_name);
					TextView t2=p2.findViewById(R.id.tvg_id);
					if (!isDet)
					{
						panDet.setVisibility(View.VISIBLE);
						panDetSub.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.move_up));
						isDet = true;
					}
					//	tvDTitle.setText(t1.getText().toString());
					getData("books/" + t2.getText().toString(), "detail");
					stBook = t2.getText().toString();
					return false;
				}


			});
		elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){

				@Override
				public boolean onChildClick(ExpandableListView p1, View p2, int p3, int p4, long p5)
				{
					TextView t=p2.findViewById(R.id.txt_child_name);
					TextView t1=p2.findViewById(R.id.txt_child_path);
					//if (p3 == 0)
					//{
					if (!isDet)
					{
						panDet.setVisibility(View.VISIBLE);
						panDetSub.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.move_up));
						isDet = true;
					}  
					//		tvDTitle.setText(t.getText().toString());
					getData("books/" + t1.getText().toString(), "detail");
					stBook = t1.getText().toString();
					//	}

					return false;
				}
			});
		elv1.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){

				@Override
				public boolean onChildClick(ExpandableListView p1, View p2, int p3, int p4, long p5)
				{
					TextView t=p2.findViewById(R.id.txt_child_name);
					TextView t1=p2.findViewById(R.id.txt_child_path);
					//if (p3 == 0)
					//{
					if (!isDet)
					{
						panDet.setVisibility(View.VISIBLE);
						panDetSub.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.move_up));
						isDet = true;
					}  
					//		tvDTitle.setText(t.getText().toString());
					getData("books/" + t1.getText().toString(), "detail");
					stBook = t1.getText().toString();
					//	}

					return false;
				}


			});
		sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

				@Override
				public boolean onQueryTextSubmit(String p1)
				{
					return false;
				}

				@Override
				public boolean onQueryTextChange(String txt)
				{
				    if (!isUsers)
					{
						List<Books> list=new ArrayList<>();
						for (Books b:mList)
				    	{
				     		if (b.getBName().toUpperCase().contains(txt.toUpperCase()))
					    	{
							    list.add(b);
						    }
					    	BookAdapter ad=new BookAdapter(MainActivity.this, R.layout.grid_view, list);
						    gv.setAdapter(ad);
					    }
					}
					else
					{
						List<Profile> list1=new ArrayList<>();
						for (Profile p:usList)
						{
							if (p.getName().toUpperCase().contains(txt.toUpperCase()))
							{
								list1.add(p);
							}
						}
						UserAdapter uad1=new UserAdapter(MainActivity.this, R.layout.custom_user, list1);
						lv.setAdapter(uad1);
					}
					return false;
				}
			});
		proDown.setAnimationListener(new Animation.AnimationListener(){
				@Override
				public void onAnimationStart(Animation p1)
				{}
				@Override
				public void onAnimationEnd(Animation p1)
				{
					if (isPro1 && !isDet)
					{
						isPro1 = false;
						panPro1.setVisibility(View.GONE);
					}
					if (isPro && !isDet)
					{
						isPro = false;
						panPro.setVisibility(View.GONE);
					}
					if (isDet)
					{
						isDet = false;
						panDet.setVisibility(View.GONE);
					}
				}
				@Override
				public void onAnimationRepeat(Animation p1)
				{}
			});
    }

	private void getUserBookData(final String userId)
	{
		db.child("books").addValueEventListener(new ValueEventListener(){

				@Override
				public void onDataChange(DataSnapshot p1)
				{
					mList3.clear();
					mList4.clear();
					grpList1.clear();
					int a1=0;
					int b1=0;
					int rs1=0;
					for (DataSnapshot sp:p1.getChildren())
					{
						//message(sp.getKey().toString());
						Books bk=sp.getValue(Books.class);
						if (!bk.getBTime().equals("null"))
						{
							float diff=Daybetween(getCurrentDate(), bk.getBTime());
							if (diff == 0)
							{				
								if (userId.equals(bk.getBRes()))
								{
									a1++;
									mList3.add(new Counter(String.valueOf(a1) + ". " + bk.getBName(), sp.getKey().toString()));
								}
							}
							if (!bk.getBBro().equals("null"))
							{			
								if (userId.equals(bk.getBBro()))
								{
									b1++;
									long d=exp - Daybetween(bk.getBTime(), getCurrentDate());
									if (d >= 0)
									{
										if (d == 1)
										{
											mList4.add(new Counter(String.valueOf(b1) + ". " + bk.getBName() + " ( " + String.valueOf(d) + " day left )", sp.getKey().toString()));
										}
										else
										{
											mList4.add(new Counter(String.valueOf(b1) + ". " + bk.getBName() + " ( " + String.valueOf(d) + " days left )", sp.getKey().toString()));
										}
									}
									else
									{
										rs1 += -d;
										if (-d == 1)
										{
											mList4.add(new Counter(String.valueOf(b1) + ". " + bk.getBName() + " ( Expired " + String.valueOf(-d) + " day ago )", sp.getKey().toString()));
										}
										else
										{
											mList4.add(new Counter(String.valueOf(b1) + ". " + bk.getBName() + " ( Expired " + String.valueOf(-d) + " days ago )", sp.getKey().toString()));
										}
										//message(rs1 * mon + "");
										db.child("users/" + userId + "/price").setValue(rs1 * mon);
										tvCash1.setText(String.valueOf(rs1 * mon));
									}
								}
							}
						}
					}

					grpList1.add("Reserved books (" + String.valueOf(mList3.size()) + ")");
					grpList1.add("Borrowed books (" + String.valueOf(mList4.size()) + ")");
					hash1.put(grpList1.get(0), mList3);
					hash1.put(grpList1.get(1), mList4);

					cad1 = new CounterAdapter(MainActivity.this, grpList1, hash1);
					elv1.setAdapter(cad1);

				}

				@Override
				public void onCancelled(DatabaseError p1)
				{
					message(p1.getMessage());
				}


			});
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.txt_hint:
				sv.setIconified(false);
				break;
			case R.id.btn_add:
				startActivity(new Intent(this, AddActivity.class));
				break;
			case R.id.btn_scan:
				startActivity(new Intent(this, BarcodeActivity.class));
				break;
			case R.id.btn_bot:
				startActivity(new Intent(this, ChatActivity.class));
				break;
			case R.id.txt_pro:
				if (!isPro)
				{
					panPro.setVisibility(View.VISIBLE);
					panProSub.startAnimation(AnimationUtils.loadAnimation(this, R.anim.move_up));
					isPro = true;
				}
				break;
			case R.id.pan_pro:
				if (isPro)
				{
					panProSub.startAnimation(proDown);
				}
				break;
			case R.id.pan_pro1:
				if (isPro1)
				{
					panProSub1.startAnimation(proDown);
				}
				break;
			case R.id.pan_det:
				if (isDet)
				{
					panDetSub.startAnimation(proDown);
				}
				break;
			case R.id.btn_logout:
				showAlert("Logout", "Are you sure to logout?");
				isLog = true;
				break;
			case R.id.btn_ebook:
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(stUrl)));
				break;
			case R.id.btn_reserve:
				if (btnRes.getText().toString().equals("Cancel reservation"))
				{
					//cr--;
					countRes--;
					db.child("books/" + stBook + "/bres").setValue("null");
					db.child("books/" + stBook + "/btime").setValue(addDate(getCurrentDate(),exp-5));
					db.child("users/" + user.getUid() + "/reserve").setValue(countRes);
					//db.child("users/"+user.getUid()+"/res").setValue(cr);
					//db.child("users/"+user.getUid()+"/books/"+stBook).removeValue();
				}
				else if (btnRes.getText().toString().equals("Reserve"))
				{
					if (countRes < 4)
					{
						//  cr++;
						countRes++;
						db.child("books/" + stBook + "/bres").setValue(user.getUid());
						db.child("books/" + stBook + "/btime").setValue(getCurrentDate());
						db.child("users/" + user.getUid() + "/reserve").setValue(countRes);
						//  db.child("users/"+user.getUid()+"/res").setValue(cr);
						// db.child("users/"+user.getUid()+"/books/"+stBook).setValue(stBook);
					}
					else
					{
						message("Maximum 4 reservation is allowed!");
					}
				}
				else if (btnRes.getText().toString().equals("Borrow"))
				{
					if (countBor < 4)
					{
						countBor++;
						message(tvDInfo.getText().toString());
						db.child("books/" + stBook + "/bbro").setValue(user.getUid());
						db.child("books/" + stBook + "/btime").setValue(getCurrentDate());
						db.child("users/" + user.getUid() + "/borrow").setValue(countBor);
						if (tvDInfo.getText().toString().startsWith("You reserved"))
						{
							countRes--;
							db.child("users/" + user.getUid() + "/reserve").setValue(countRes);
							db.child("books/" + stBook + "/bres").setValue("null");
						}
					}
					else
					{
						message("You can borrow 4 books maximum!");
					}
				}else if(btnRes.getText().toString().equals("Return")){
					message(stBook+" : "+stUser);
					db.child("books/"+stBook+"/bbro").setValue("null");
					db.child("books/"+stBook+"/btime").setValue(addDate(getCurrentDate(),exp-5));
					//db.child("users/"+stUser+"/borrow").setValue(countBor);
				}
				panDetSub.startAnimation(proDown);
				break;
			case R.id.alert_neg:
				dialog.dismiss();
				isLog = false;
				break;
			case R.id.btn_users:

				if (!isUsers)
				{
					btnUser.setImageResource(R.drawable.icon_book);
					gv.setVisibility(View.GONE);
					lv.setVisibility(View.VISIBLE);
					lv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
					tvHint.setText("Search users");
					isUsers = true;
					getData("users", "all_users");
				}
				else
				{
					btnUser.setImageResource(R.drawable.icon_users);
					isUsers = false;
					gv.setVisibility(View.VISIBLE);
					gv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
					lv.setVisibility(View.GONE);
					tvHint.setText("Search books");
				}
				break;
			case R.id.alert_pos:
				if (isLog)
				{
					isLog = false;
					auth.signOut();
					dialog.dismiss();
					Intent in=new Intent(this, SplashActivity.class);
					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(in);
				}
				break;
		}
	}
	private void getData(final String root, final String type)
	{
		db.child(root).addValueEventListener(new ValueEventListener(){

				@Override
				public void onDataChange(DataSnapshot p1)
				{
					if (type.equals("profile"))
					{
						Profile us=p1.getValue(Profile.class);
						tvPro.setText(String.valueOf(us.getName().charAt(0)));
						tvPro1.setText(String.valueOf(us.getName().charAt(0)));
						tvName.setText(us.getName());
						tvPhone.setText(us.getPhone());
						tvMail.setText(us.getMail());
						if(us.getMail().toLowerCase().equals("11170109@ammini.edu.in".toLowerCase())){
							btnAdd.setVisibility(View.VISIBLE);
							btnUser.setVisibility(View.VISIBLE);
							isAdmin=true;
						}else{
							isAdmin=false;
							btnAdd.setVisibility(View.GONE);
							btnUser.setVisibility(View.GONE);
						}
					}
					if (type.equals("all_users"))
					{
						usList.clear();
						for (DataSnapshot shot:p1.getChildren())
						{
							Profile us=shot.getValue(Profile.class);
							usList.add(new Profile(shot.getKey(), us.getName(), "null", "null", "null", us.getPrice(), us.getReserve(), us.getBorrow()));
						}
						uad = new UserAdapter(MainActivity.this, R.layout.custom_user, usList);
						lv.setAdapter(uad);
					}
					if (type.equals("count"))
					{
						Profile pf=p1.getValue(Profile.class);
						countRes = pf.getReserve();
						countBor = pf.getBorrow();
					}
					if (type.equals("users"))
					{
						Profile us=p1.getValue(Profile.class);
						tvName1.setText(us.getName());
						tvPhone1.setText(us.getPhone());
						tvMail1.setText(us.getMail());
					}
					if (type.equals("books"))
					{
						mList.clear();
						mList1.clear();
						mList2.clear();
						grpList.clear();
						int rs=0;
						int a=0;
						int b=0;
						for (DataSnapshot sp:p1.getChildren())
						{
							//message(sp.getKey().toString());
							final Books bk=sp.getValue(Books.class);
							mList.add(new Books(sp.getKey().toString(), bk.getBImg(), bk.getBName(), bk.getBAuthor(),
												bk.getBType(), bk.getBLink(), bk.getBRfid(), bk.getBAbout(), bk.getBRes(), bk.getBBro(), bk.getBTime()));
							if(!bk.getBRes().equals("null")&&!bk.getBTime().equals("null")){
									if(Daybetween(bk.getBTime(),getCurrentDate())!=0){
										db.child("books/"+sp.getKey().toString()+"/bres").setValue("null");
										db.child("books/"+sp.getKey().toString()+"/btime").setValue("null");
									}
							}
							
//edited
							
							if (!bk.getBTime().equals("null"))
							{
								float diff=Daybetween(getCurrentDate(), bk.getBTime());
								if (diff == 0)
								{				
						            if (user.getUid().equals(bk.getBRes()))
									{
										a++;
										mList1.add(new Counter(String.valueOf(a) + ". " + bk.getBName(), sp.getKey().toString()));
									}
								}
								if (!bk.getBBro().equals("null"))
								{			
								    if (user.getUid().equals(bk.getBBro()))
									{
										b++;
										long d=exp - Daybetween(bk.getBTime(), getCurrentDate());
										if (d >= 0)
										{
											if (d == 1)
											{
												mList2.add(new Counter(String.valueOf(b) + ". " + bk.getBName() + " ( " + String.valueOf(d) + " day left )", sp.getKey().toString()));
											}
											else
											{
												mList2.add(new Counter(String.valueOf(b) + ". " + bk.getBName() + " ( " + String.valueOf(d) + " days left )", sp.getKey().toString()));
											}
										}
										else
										{
											rs += -d;
										    if (-d == 1)
											{
												mList2.add(new Counter(String.valueOf(b) + ". " + bk.getBName() + " ( Expired " + String.valueOf(-d) + " day ago )", sp.getKey().toString()));
											}
											else
											{
												mList2.add(new Counter(String.valueOf(b) + ". " + bk.getBName() + " ( Expired " + String.valueOf(-d) + " days ago )", sp.getKey().toString()));
											}
											//message(rs*mon+"");
											//tvCash.setText(String.valueOf(rs * mon));
										//	if (user != null)
											//{
												//db.child("users/" + user.getUid() + "/price").setValue(rs * mon);
											//}
										}
									}
								}
							}
							
							
	//editing ended
						}
						bad = new BookAdapter(MainActivity.this, R.layout.grid_view, mList);
						gv.setAdapter(bad);		
//edited
					//	if (user != null)
					//	{
						//	db.child("users/" + user.getUid() + "/reserve").setValue(mList1.size());
						//	db.child("users/" + user.getUid() + "/borrow").setValue(mList2.size());
						//}
						grpList.add("Reserved books (" + String.valueOf(mList1.size()) + ")");
						grpList.add("Borrowed books (" + String.valueOf(mList2.size()) + ")");
						hash.put(grpList.get(0), mList1);
						hash.put(grpList.get(1), mList2);

						cad = new CounterAdapter(MainActivity.this, grpList, hash);
						elv.setAdapter(cad);
//editing stopped
					}
					if (type.equals("borrow"))
					{
						//message(isAdmin?"Return":"Borrow");
						for (DataSnapshot sp:p1.getChildren())
						{
							Books bk=sp.getValue(Books.class);
							//  message(getIntent().getStringExtra("data")+" \n data : "+bk.getBRfid());
							if (getIntent().getStringExtra("data").equals(bk.getBRfid()))
							{
								//message("data matched for " + bk.getBName());
								if (!isDet)
								{
									panDet.setVisibility(View.VISIBLE);
									panDetSub.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_up));
									isDet = true;
								}
								tvDTitle.setText(bk.getBName());
								tvDAuthor.setText(bk.getBAuthor());
								tvDAbout.setText(bk.getBAbout());
								imgDet.setImageBitmap(bitMapToImage(bk.getBImg()));
								stUrl = bk.getBLink();
								if (!bk.getBTime().equals("null"))
								{
									float diff=Daybetween(getCurrentDate(), bk.getBTime());
									if (diff == 0)
									{
										tvDInfo.setTextColor(Color.parseColor("#F82D21"));
										if (user.getUid().equals(bk.getBRes()))
										{
											btnRes.setVisibility(View.VISIBLE);
											btnRes.setText("Borrow");
											tvDInfo.setText("You reserved this book! you can borrow this book now!");			
											stBook = sp.getKey();
										}
										else
										{
											btnRes.setVisibility(View.INVISIBLE);
											//tvDInfo.setText("This book is reserved by someone else.");	
											if (bk.getBRes().equals("null"))
											{
												 if(isAdmin){
													 stBook=sp.getKey();
													 stUser=bk.getBBro();
													 btnRes.setVisibility(View.VISIBLE);
													 btnRes.setText("Return");
												 }
												if (user.getUid().equals(bk.getBBro()))
												{
													tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(exp) + " days left to return the book )");
												}
												else
												{
													tvDInfo.setText("This book is borrowed by someone else!\n(  " + String.valueOf(exp) + " days left to return the book )");
												}
											}
											else
											{
												tvDInfo.setText("This book is reserved by someone else.");	
											}
										}
									}
									else
									{
										if (!bk.getBBro().equals("null"))
										{
											long d=exp - Daybetween(bk.getBTime(), getCurrentDate());
											tvDInfo.setTextColor(Color.parseColor("#F82D21"));
											if (user.getUid().equals(bk.getBBro()))
											{
												
												if(isAdmin){
													stUser=bk.getBBro();
													stBook=sp.getKey();
													btnRes.setVisibility(View.VISIBLE);
													btnRes.setText("Return");
												}
												if (d >= 0)
												{
													if (d == 1)
													{ 
														tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(d) + " day left to return the book )");
													}
													else
													{
														tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(d) + " days left to return the book )");
													}

												}
												else
												{
													if (-d == 1)
													{
														tvDInfo.setText("You borrowed this book!\n( Book return date expired " + String.valueOf(-d) + " day ago )");
													}
													else
													{
														tvDInfo.setText("You borrowed this book!\n( Book return date expired " + String.valueOf(-d) + " days ago )");
													}
												}

											}
											else
											{
												if(isAdmin){
													stUser=bk.getBBro();
													stBook=sp.getKey();
													btnRes.setVisibility(View.VISIBLE);
													btnRes.setText("Return");
												}
												if (d >= 0)
												{
													if (d == 1)
													{
														tvDInfo.setText("This book is borrowed by someone else!\n(  " + String.valueOf(d) + " day left to return the book )");
													}
													else
													{
														tvDInfo.setText("This book is borrowed by someone else!\n(  " + String.valueOf(d) + " days left to return the book )");
													}

												}
												else
												{
													if (-d == 1)
													{
														tvDInfo.setText("This book is borrowed by someone else!\n( Book return date expired " + String.valueOf(-d) + " day ago )");
													}
													else
													{
														tvDInfo.setText("This book is borrowed by someone else!\n( Book return date expired " + String.valueOf(-d) + " days ago )");
													}
												}

											}
										}
										else
										{
											btnRes.setVisibility(View.VISIBLE);
											btnRes.setText("Borrow");
											tvDInfo.setTextColor(Color.parseColor("#79665D"));
											tvDInfo.setText("You can Borrow this book now");
											stBook = sp.getKey();
										}

									}
								}
								else
								{   
								 //   message(bk.getBTime()+" Is the time");
//									if (!bk.getBBro().equals("null"))
//									{  
//										long d=exp - Daybetween(bk.getBTime(), getCurrentDate());
//										tvDInfo.setTextColor(Color.parseColor("#F82D21"));
//										if (user.getUid().equals(bk.getBBro()))
//										{ 
//											btnRes.setVisibility(View.INVISIBLE);
//											if (d >= 0)
//											{
//												if (d == 1)
//												{
//													tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(d) + " day left to return the book )");
//												}
//												else
//												{
//													tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(d) + " days left to return the book )");
//												}
//
//											}
//											else
//											{
//												if (-d == 1)
//												{
//													tvDInfo.setText("You borrowed this book!\n( Book return date expired " + String.valueOf(-d) + " day ago )");
//												}
//												else
//												{
//													tvDInfo.setText("You borrowed this book!\n( Book return date expired " + String.valueOf(-d) + " days ago )");
//												}
//											}
//										}
//										else
//										{
//											btnRes.setVisibility(View.INVISIBLE); 
//
//											if (d >= 0)
//											{
//												if (d == 1)
//												{
//													tvDInfo.setText("This book is borrowed by someone else!\n( " + String.valueOf(d) + " day left to return the book )");
//												}
//												else
//												{
//													tvDInfo.setText("This book is borrowed by someone else!\n( " + String.valueOf(d) + " days left to return the book )");
//												}
//
//											}
//											else
//											{
//												if (-d == 1)
//												{
//													tvDInfo.setText("This book is borrowed by someone else!\n( Book return date expired " + String.valueOf(-d) + " day ago )");
//												}
//												else
//												{
//													tvDInfo.setText("This book is borrowed by someone else!\n( Book return date expired " + String.valueOf(-d) + " days ago )");
//												}
//											}
//										}
//									}
//									else
//									{
										btnRes.setVisibility(View.VISIBLE);
										btnRes.setText("Borrow");
										tvDInfo.setTextColor(Color.parseColor("#79665D"));
										tvDInfo.setText("You can Borrow this book now");
										stBook = sp.getKey();
									//}
								}
							}
						}

					}
					if (type.equals("detail"))
					{
						Books bk=p1.getValue(Books.class);
						tvDTitle.setText(bk.getBName());
						tvDAuthor.setText(bk.getBAuthor());
						tvDAbout.setText(bk.getBAbout());
						imgDet.setImageBitmap(bitMapToImage(bk.getBImg()));
						stUrl = bk.getBLink();
						if (!bk.getBTime().equals("null"))
						{
							float diff=Daybetween(getCurrentDate(), bk.getBTime());
							if (diff == 0)
							{
								tvDInfo.setTextColor(Color.parseColor("#F82D21"));
								if (user.getUid().equals(bk.getBRes()))
								{
									btnRes.setVisibility(View.VISIBLE);
									btnRes.setText("Cancel reservation");
									tvDInfo.setText("Your reservation close today at 11:59 pm");							
								}
								else
								{
									btnRes.setVisibility(View.INVISIBLE);
									//tvDInfo.setText("This book is reserved by someone else.");			
									if (bk.getBRes().equals("null"))
									{
										if (user.getUid().equals(bk.getBBro()))
										{
											tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(exp) + " days left to return the book )");
										}
										else
										{
											tvDInfo.setText("This book is borrowed by someone else!\n(  " + String.valueOf(exp) + " days left to return the book )");
										}
									}
									else
									{
										tvDInfo.setText("This book is reserved by someone else.");	
									}
								}
							}
							else
							{
							  //  message(bk.getBBro());
								if (!bk.getBBro().equals("null"))
								{
									long d=exp - Daybetween(bk.getBTime(), getCurrentDate());
									tvDInfo.setTextColor(Color.parseColor("#F82D21"));
									if (user.getUid().equals(bk.getBBro()))
									{
										btnRes.setVisibility(View.INVISIBLE);
										if (d >= 0)
										{
											if (d == 1)
											{
												tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(d) + " day left to return the book )");
											}
											else
											{
												tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(d) + " days left to return the book )");
											}

										}
										else
										{
											if (-d == 1)
											{
												tvDInfo.setText("You borrowed this book!\n( Book return date expired " + String.valueOf(-d) + " day ago )");
											}
											else
											{
												tvDInfo.setText("You borrowed this book!\n( Book return date expired " + String.valueOf(-d) + " days ago )");
											}
										}
									}
									else
									{
										btnRes.setVisibility(View.INVISIBLE);
										if (d >= 0)
										{
											if (d == 1)
											{
												tvDInfo.setText("This book is borrowed by someone else!\n( " + String.valueOf(d) + " day left to return the book )");
											}
											else
											{
												tvDInfo.setText("This book is borrowed by someone else!\n( " + String.valueOf(d) + " days left to return the book )");
											}

										}
										else
										{
											if (-d == 1)
											{
												tvDInfo.setText("This book is borrowed by someone else!\n( Book return date expired " + String.valueOf(-d) + " day ago )");
											}
											else
											{
												tvDInfo.setText("This book is borrowed by someone else!\n( Book return date expired " + String.valueOf(-d) + " days ago )");
											}
										}
									}
								}
								else
								{
									btnRes.setVisibility(View.VISIBLE);
									btnRes.setText("Reserve");
									tvDInfo.setTextColor(Color.parseColor("#79665D"));
									tvDInfo.setText("You can reserve this book until 11:59 pm today");
								}

							}
						}
						else
						{
							//	message(bk.getBBro());
							if (!bk.getBBro().equals("null"))
							{
								long d=exp - Daybetween(bk.getBTime(), getCurrentDate());
								tvDInfo.setTextColor(Color.parseColor("#F82D21"));
								if (user.getUid().equals(bk.getBBro()))
								{
									btnRes.setVisibility(View.INVISIBLE);
									if (d >= 0)
									{
										if (d == 1)
										{
											tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(d) + " day left to return the book )");
										}
										else
										{
											tvDInfo.setText("You borrowed this book!\n( " + String.valueOf(d) + " days left to return the book )");
										}

									}
									else
									{
										if (-d == 1)
										{
											tvDInfo.setText("You borrowed this book!\n( Book return date expired " + String.valueOf(-d) + " day ago )");
										}
										else
										{
											tvDInfo.setText("You borrowed this book!\n( Book return date expired " + String.valueOf(-d) + " days ago )");
										}
									}
								}
								else
								{
									btnRes.setVisibility(View.INVISIBLE);
									if (d >= 0)
									{
										if (d == 1)
										{
											tvDInfo.setText("This book is borrowed by someone else!\n( " + String.valueOf(d) + " day left to return the book )");
										}
										else
										{
											tvDInfo.setText("This book is borrowed by someone else!\n( " + String.valueOf(d) + " days left to return the book )");
										}

									}
									else
									{
										if (-d == 1)
										{
											tvDInfo.setText("This book is borrowed by someone else!\n( Book return date expired " + String.valueOf(-d) + " day ago )");
										}
										else
										{
											tvDInfo.setText("This book is borrowed by someone else!\n( Book return date expired " + String.valueOf(-d) + " days ago )");
										}
									}
								}
							}
							else
							{
								btnRes.setVisibility(View.VISIBLE);
								btnRes.setText("Reserve");
								tvDInfo.setTextColor(Color.parseColor("#79665D"));
								tvDInfo.setText("You can reserve this book until 11:59 pm today");
							}

						}
					}
				}

				@Override
				public void onCancelled(DatabaseError p1)
				{
					message(p1.getMessage());
				}


			});
	}
	private void showAlert(String title, String message)
	{
		AlertDialog.Builder ad=new AlertDialog.Builder(this);
		View v=getLayoutInflater().from(this).inflate(R.layout.alert_activity, null);
		tvAhead = v.findViewById(R.id.alert_head);
		tvAinfo = v.findViewById(R.id.alert_info);
		tvNeg = v.findViewById(R.id.alert_neg);
		tvPos = v.findViewById(R.id.alert_pos);
		tvNeg.setOnClickListener(this);
		tvPos.setOnClickListener(this);
		tvAhead.setTypeface(tpb);
		tvAinfo.setTypeface(tpr);
		tvNeg.setTypeface(tpr);
		tvPos.setTypeface(tpr);
		tvAinfo.setText(message);
		tvAhead.setText(title);
		ad.setView(v);
		dialog = ad.create();
		if (dialog.getWindow() != null)
		{
			dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			dialog.getWindow().getAttributes().windowAnimations = R.style.zoomAlert;
		}
		dialog.show();
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
	private String addDate(String dt,int dr){
		//String dt = "2012-01-04";  // Start date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(dt));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		c.add(Calendar.DATE, dr);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		String output = sdf1.format(c.getTime());
		return output;
	}
	private void message(String m)
	{
		Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
	}

}
