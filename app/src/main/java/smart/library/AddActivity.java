package smart.library;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.text.*;
import android.graphics.*;
import android.net.*;
import android.content.*;
import java.io.*;
import android.provider.*;
import android.*;
import android.content.pm.*;
import android.view.animation.*;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import smart.library.models.*;

public class AddActivity extends Activity implements OnClickListener
{
	private EditText etTitle,etAuthor,etLink,etRfid,
	etType,etAbout;
	private TextView tvLoad;
	private Button btn;
	private ImageView img,loadImg;
	private RelativeLayout loadPan;

	private Typeface tpb,tpr;
	private Animation zin,zout;
	private FirebaseAuth auth;
	private DatabaseReference ref;
	private FirebaseStorage st;
	private StorageReference stRef;

	private Uri filePath;
	private final int PICK_IMG=71;
	private Bitmap bit;
	private AlertDialog dialogLoad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_activity);

		etTitle = findViewById(R.id.add_title);
		etAuthor = findViewById(R.id.add_author);
		etLink = findViewById(R.id.add_link);
		etRfid=findViewById(R.id.add_rfid);
		etType = findViewById(R.id.add_type);
		etAbout = findViewById(R.id.add_about);
		btn = findViewById(R.id.btn_upload);
		img = findViewById(R.id.add_preview);
		
		zin=AnimationUtils.loadAnimation(this,R.anim.zoom_in);
		zout=AnimationUtils.loadAnimation(this,R.anim.zoom_out);
		
		tpb = Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf");
		tpr = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
		
		auth=FirebaseAuth.getInstance();
		ref=FirebaseDatabase.getInstance().getReference("smart/books");
		st=FirebaseStorage.getInstance();
		stRef=st.getReference();

		etTitle.setTypeface(tpr);
		etAuthor.setTypeface(tpr);
		etLink.setTypeface(tpr);
		etRfid.setTypeface(tpr);
		etType.setTypeface(tpr);
		etAbout.setTypeface(tpr);
		btn.setTypeface(tpb);
		
		btn.setOnClickListener(this);
		img.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.add_preview:
				checkPer();
				break;
			case R.id.btn_upload:
				if (c(etTitle) || c(etAbout) || c(etAuthor) ||
					c(etLink) || c(etType) || c(etRfid) || filePath==null)
				{
					message("All fields are required!");
				}
				else
				{
					uploadData();
				}
				break;
		}
	}
	private void uploadData(){
		if(filePath!=null){
			startLoad();
	//	message(getFileToByte(bit));
	//	img.setImageBitmap(bitMapToImage(getFileToByte(bit)));
		ref.push().setValue(new Books("null",getFileToByte(bit),s(etTitle),s(etAuthor),s(etType),s(etLink),s(etRfid),s(etAbout),"null","null","null"))
				.addOnCompleteListener(AddActivity.this, new OnCompleteListener<Void>(){

					@Override
					public void onComplete(Task<Void> p1)
					{
						stopLoad();
						message("Data uploaded!");
						clearInputs();
					}
		});
		}
	}
	public static String getFileToByte(Bitmap bmp){
		//Bitmap bmp = null;
		ByteArrayOutputStream bos = null;
		byte[] bt = null;
		String encodeString = null;
		try{
			//bmp = BitmapFactory.decodeFile(filePath);
			bos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bt = bos.toByteArray();
			encodeString = android.util.Base64.encodeToString(bt, android.util.Base64.DEFAULT);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return encodeString;
	}
	private void clearInputs(){
		etTitle.setText("");
		etLink.setText("");
		etAuthor.setText("");
		etRfid.setText("");
		etAbout.setText("");
		etType.setText("");
	}
	public static Bitmap bitMapToImage(String code){
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		byte[] imageBytes=byteArrayOutputStream.toByteArray();
		imageBytes=android.util.Base64.decode(code,android.util.Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
	}
	private void checkPer()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
			{
				String[] permission={Manifest.permission.READ_EXTERNAL_STORAGE};
				requestPermissions(permission, 1);
			}
			else{
				chooseImage();
			}
		}
		else{
			chooseImage();
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (requestCode == 1)
		{
			if (grantResults[0] == -1)
			{
				if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
				{
					message("Storage Permission is required!");
					checkPer();
				}
				else
				{
				    message("please give permission");
					Intent in=new Intent();
					in.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					Uri uri=Uri.fromParts("package", this.getPackageName(), null);
					in.setData(uri);
					startActivity(in);
				}
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMG && resultCode == RESULT_OK && data != null && data.getData() != null)
		{
			filePath = data.getData();
			
			try
			{
			    bit=MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
			//	message(getFileToByte(bit));
				img.setImageBitmap(bit);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				message(e.getMessage());
			}
		}
	}
	private void startLoad(){
		AlertDialog.Builder adLoad=new AlertDialog.Builder(this);
		View v=getLayoutInflater().from(this).inflate(R.layout.load_activity,null);
		adLoad.setView(v);
		adLoad.setCancelable(false);
		loadPan=v.findViewById(R.id.load_pan1);
		loadImg=v.findViewById(R.id.img_load1);
		tvLoad=v.findViewById(R.id.txt_load1);
		loadPan.setVisibility(View.VISIBLE);
		loadImg.startAnimation(zin);
		tvLoad.setTypeface(tpb);
		dialogLoad=adLoad.create();
		zin.setAnimationListener(new Animation.AnimationListener(){

				@Override
				public void onAnimationStart(Animation p1){}
				@Override
				public void onAnimationEnd(Animation p1)
				{
					loadImg.startAnimation(zout);
				}
				@Override
				public void onAnimationRepeat(Animation p1){}
			});
		zout.setAnimationListener(new Animation.AnimationListener(){

				@Override
				public void onAnimationStart(Animation p1){}
				@Override
				public void onAnimationEnd(Animation p1)
				{
					loadImg.startAnimation(zin);
				}
				@Override
				public void onAnimationRepeat(Animation p1){}
			});
			if(dialogLoad.getWindow()!=null){
				dialogLoad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			}
		dialogLoad.show();
	}
	private void stopLoad(){
		dialogLoad.dismiss();
	}
	private void chooseImage()
	{
		Intent in=new Intent();
		in.setType("image/*");
		in.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(in, "Select Picture"), PICK_IMG);
	}
	private boolean c(EditText et)
	{
		return TextUtils.isEmpty(s(et)) || s(et).matches(" ");
	}
	private String s(EditText et)
	{
		return et.getText().toString();
	}
	private void message(String m)
	{
		Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
	}
}
