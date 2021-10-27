package smart.library;

import android.app.*;
import android.os.*;
import android.graphics.*;
import android.widget.*;
import android.animation.*;
import android.view.animation.*;
import com.google.firebase.auth.*;
import android.content.*;

public class SplashActivity extends Activity
{
	 FirebaseAuth auth;
	 FirebaseUser user;
	
	 TextView tv;
	 ImageView img;
	
	 Typeface tpb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		tv=findViewById(R.id.splash_screenTextView);
		img=findViewById(R.id.splash_screenImageView);
		tpb=Typeface.createFromAsset(getAssets(),"Montserrat-Bold.ttf");
		tv.setTypeface(tpb);
		ObjectAnimator anim=(ObjectAnimator)AnimatorInflater.loadAnimator(this,R.animator.flipping);
		anim.setDuration(2000);
		anim.setTarget(img);
		anim.start();
		tv.setAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
		
		auth=FirebaseAuth.getInstance();
		user= FirebaseAuth.getInstance().getCurrentUser();
		
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				Intent in;
				if(user!=null){
					in = new Intent(SplashActivity.this, MainActivity.class);
				}else{
					in = new Intent(SplashActivity.this, AuthActivity.class);
				}
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(in);
			}
		},2000);
	}
}
