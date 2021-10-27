package smart.library;

import android.app.*;
import com.google.firebase.database.*;

public class OfflineApp extends Application
{

	@Override
	public void onCreate()
	{
		super.onCreate();
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
	}
	
}
