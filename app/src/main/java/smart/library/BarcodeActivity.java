package smart.library;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.google.android.gms.vision.*;
import com.google.android.gms.vision.barcode.*;
import java.io.*;
import android.provider.*;
import android.net.*;

import androidx.annotation.RequiresApi;

public class BarcodeActivity extends Activity
{
	private SurfaceView sv;
	private TextView tv;

	private BarcodeDetector bd;
	private CameraSource cs;

	private String intentData="";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode_activity);
		sv = findViewById(R.id.surfaceView);
		tv = findViewById(R.id.txt_barcode);
	}
	private void initDetAndSrc()
	{
		message("Barcode scanning started!");
		bd = new BarcodeDetector.Builder(this)
			.setBarcodeFormats(Barcode.ALL_FORMATS)
			.build();
		cs = new CameraSource.Builder(this, bd)
			.setRequestedPreviewSize(1920, 1080)
			.setAutoFocusEnabled(true)
			.build();
		sv.getHolder().addCallback(new SurfaceHolder.Callback(){

				@Override
				public void surfaceCreated(SurfaceHolder holder)
				{
					checkPer();
//					try
//					{
//						if (ActivityCompat.checkSelfPermission(BarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
//						{
//							cs.start(sv.getHolder());
//						}
//						else
//						{
//							ActivityCompat.requestPermissions(BarcodeActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//						}
//					}
//					catch (IOException e)
//					{
//						e.printStackTrace();
//						message(e.getMessage());
//					}
				}

				@Override
				public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4)
				{
					// TODO: Implement this method
				}

				@Override
				public void surfaceDestroyed(SurfaceHolder p1)
				{
					cs.stop();
				}


			});
		bd.setProcessor(new Detector.Processor<Barcode>(){

				@Override
				public void release()
				{
					message("To prevent memory leaks barcode scanner has been stopped");
				}

				@Override
				public void receiveDetections(Detector.Detections<Barcode> detections)
				{
					final SparseArray<Barcode> barcodes=detections.getDetectedItems();
					if (barcodes.size() != 0)
					{
						tv.post(new Runnable(){

								@Override
								public void run()
								{
									if (barcodes.valueAt(0).email != null)
									{
										tv.removeCallbacks(null);
										intentData = barcodes.valueAt(0).email.address;
										tv.setText(intentData);
									}
									else
									{
										intentData = barcodes.valueAt(0).displayValue;
										tv.setText(intentData);
										Intent in=new Intent(BarcodeActivity.this,MainActivity.class);
										in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
										in.putExtra("data",tv.getText().toString());
										startActivity(in);
									}
								}

							});
					}
				}
			});
	}
	private void checkPer()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
			{
				String[] permission={Manifest.permission.CAMERA};
				requestPermissions(permission, 1);
			}
			else{
				//chooseImage();
				try{
				   cs.start(sv.getHolder());
				   }catch(IOException e){
					   message(e.getMessage());
				   }
			}
		}
		else{
			//chooseImage();
			try{
				cs.start(sv.getHolder());
			}catch(IOException e){
				message(e.getMessage());
			}
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
				if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
				{
					message("Camera Permission is required for scanning!");
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
    protected void onPause()
	{
        super.onPause();
        cs.release();
    }

    @Override
    protected void onResume()
	{
        super.onResume();
        initDetAndSrc();


    }
	private void message(String m)
	{
		Toast.makeText(BarcodeActivity.this, m, Toast.LENGTH_SHORT).show();
	}
}
