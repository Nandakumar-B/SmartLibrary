package smart.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import smart.library.models.Profile;

public class AuthActivity extends Activity implements OnClickListener
{
	private FirebaseAuth auth;
	private FirebaseDatabase db;
	private DatabaseReference ref;
	
	TextView tvHead,tvAuth,tvLoad;
	private EditText etName,etPhone,etMail,etPass;
	private Button btn;
	private ImageView loadImg;
	RelativeLayout authPan,loadPan;
	
	Typeface tpb,tpr;
	private Animation zin,zout;
	private AlertDialog dialogLoad;
	
	private boolean isSign=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.auth_activity);
		tvHead=findViewById(R.id.auth_activityTextView);
		tvAuth=findViewById(R.id.txt_auth);
		etName=findViewById(R.id.et_name);
		etPhone=findViewById(R.id.et_phone);
		etMail=findViewById(R.id.et_mail);
		etPass=findViewById(R.id.et_password);
		btn=findViewById(R.id.btn_auth);
		authPan=findViewById(R.id.auth_pan);
		
		tpb=Typeface.createFromAsset(getAssets(),"Montserrat-Bold.ttf");
		tpr=Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
		zin=AnimationUtils.loadAnimation(this,R.anim.zoom_in);
		zout=AnimationUtils.loadAnimation(this,R.anim.zoom_out);
	    
		btn.setOnClickListener(this);
		tvAuth.setOnClickListener(this);
		auth=FirebaseAuth.getInstance();
		
		tvHead.setTypeface(tpb);
		tvAuth.setTypeface(tpr);
		etName.setTypeface(tpr);
		etPhone.setTypeface(tpr);
		etMail.setTypeface(tpr);
		etPass.setTypeface(tpr);
		btn.setTypeface(tpb);
		
		authPan.startAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
	}
	@Override
	public void onClick(View v){
		switch(v.getId()){
			case R.id.txt_auth:
				if(!isSign){
					isSign=true;
					etName.setVisibility(View.GONE);
					etPhone.setVisibility(View.GONE);
					tvHead.setText(R.string.sign_in_to_library);
					btn.setText("Sign In");
					tvAuth.setText(R.string.no_account);
				}
				else{
					isSign=false;
					etName.setVisibility(View.VISIBLE);
					etPhone.setVisibility(View.VISIBLE);
					tvHead.setText("Create a new account");
					btn.setText("Create account");
					tvAuth.setText("Already have an account? Sign in");
				}
				authPan.startAnimation(AnimationUtils.loadAnimation(AuthActivity.this,R.anim.fade_in));
				break;
			case R.id.btn_auth:
				if(isSign){
					if(!Patterns.EMAIL_ADDRESS.matcher(s(etMail)).matches() ||
					   s(etPass).matches("")){
						message("Please check your details");
					}
					else{
						startLoad();
						auth.signInWithEmailAndPassword(s(etMail), s(etPass)).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>(){

								@Override
								public void onComplete(Task<AuthResult> p1)
								{
									if(p1.isSuccessful()){
										stopLoad();
										message("Welcome to Smart Library");
										Intent in=new Intent(AuthActivity.this,MainActivity.class);
										in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(in);
									}else{
										message(p1.getException().getMessage());
										stopLoad();
									}
								}
						});
					}
				}
				else{
					if(s(etName).matches("") ||
					   !Patterns.PHONE.matcher(s(etPhone)).matches() ||
					   !Patterns.EMAIL_ADDRESS.matcher(s(etMail)).matches() ||
					   s(etPass).matches("")){
						message("Please check your details");
					}
					else{
						startLoad();
							auth.createUserWithEmailAndPassword(s(etMail),s(etPass)).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>(){

									@Override
									public void onComplete(Task<AuthResult> task)
									{
										if(task.isSuccessful()){
											final FirebaseUser user=auth.getInstance().getCurrentUser();
											db=FirebaseDatabase.getInstance();
											ref=db.getReference("smart").child("users").child(user.getUid());	
											
											ref.setValue(new Profile("",s(etName),s(etPhone),s(etMail),s(etPass),0,0,0)).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<Void>(){

													@Override
													public void onComplete(Task<Void> p1)
													{
														if(p1.isSuccessful()){
															stopLoad();
															message("Account created successfully");
															Intent in=new Intent(AuthActivity.this,MainActivity.class);
															in.putExtra("uid",user.getUid());
															in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
															startActivity(in);
														}
														else{
															message(p1.getException().getMessage());
															stopLoad();
														}
													}
											});
										}else{
											message(task.getException().getMessage());
											stopLoad();
										}
									}
							});
						}
					}
				break;
		}
	}
	private void startLoad(){
		AlertDialog.Builder adLoad=new AlertDialog.Builder(this);
		View v= LayoutInflater.from(this).inflate(R.layout.load_activity,null);
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
	private String s(EditText et){
		return et.getText().toString();
	}
	private void message(String m){
		Toast.makeText(this,m,Toast.LENGTH_SHORT).show();
	}
}
