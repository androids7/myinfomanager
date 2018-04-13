package cn.edu.info_manage.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import cn.edu.info_manage.R;
import cn.edu.info_manage.domain.Blog;
import cn.edu.info_manage.domain.Contacts;
import cn.edu.info_manage.domain.Orders;
import cn.edu.info_manage.domain.User;
import cn.edu.info_manage.util.ContactsService;
import cn.edu.info_manage.util.BlogService;
import cn.edu.info_manage.util.UserService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlogActivity extends Activity implements OnItemClickListener{
	private static final String TAG = "BlogActivity";
	
	private User user_info;
	private ListView listView;
	private List<Blog> infos;
	private BlogService bService;
	private File mCurrentPhotoFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int id = intent.getExtras().getInt("id",-1);
		String name = intent.getExtras().getString("name");
		if(id<0 && name==null){
			Toast.makeText(this, "请登录", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		setContentView(R.layout.ac_blog);
		UserService userService = new UserService(this);
		user_info = userService.getUserInfoByID(id);
		userService.closeDB();
		
		listView = (ListView) findViewById(R.id.lv_contacts);
	}
	@Override
	protected void onResume() {
		super.onResume();
		bService = new BlogService(this);
		infos = bService.getAllBlog(user_info.getId());
		BlogAdapter adapter = new BlogAdapter(this, infos);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);/**/
	}
	public void btText(View view){
		View inflate = getLayoutInflater().inflate(R.layout.blog_add_text, null);
		final EditText summary = (EditText) inflate.findViewById(R.id.subject);
		final EditText content = (EditText) inflate.findViewById(R.id.content);
		
		new AlertDialog.Builder(this)
			.setTitle("添加文本日志                                           ")
			.setView(inflate)
			.setPositiveButton("保存", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					bService.insertBlog(new Blog(-1, content.getText().toString(),
							summary.getText().toString(), "", "", user_info.getId(), null,
							System.currentTimeMillis()+""));
					onResume();
				}
			})
			.setNegativeButton("舍弃", null)
			.show();
	}
	public void btImage(View view){
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			User.PHOTO_DIR.mkdir();
			mCurrentPhotoFile = new File(User.PHOTO_DIR, getPhotoFileName()); // 用当前时间给取得的图片命名

			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			Uri fromFile = Uri.fromFile(mCurrentPhotoFile);
			String string = fromFile.toString();
			Log.i("gp", string);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, 123);

		} else {
			Toast.makeText(this, "未检测到SD卡!", Toast.LENGTH_LONG);
		}
		
	}
	public void btVideo(View view){
		Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);   
		mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);   
		mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory());   
		startActivityForResult(mIntent, 125);   
	}
	public void btAudio(View view){
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);  
		startActivityForResult(intent, 124);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Blog blog = infos.get(position);
		if(!"".equals(blog.getWorld())){
			View inflate = getLayoutInflater().inflate(R.layout.blog_add_text, null);
			final EditText summary = (EditText) inflate.findViewById(R.id.subject);
			final EditText content = (EditText) inflate.findViewById(R.id.content);
			
			summary.setText(blog.getSummary());
			content.setText(blog.getWorld());
			new AlertDialog.Builder(this)
				.setTitle("添加文本日志                                           ")
				.setView(inflate)
				.setPositiveButton("保存更改", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						bService.updateBlog(new Blog(blog.getId(), content.getText().toString(),
								summary.getText().toString(), "", "", user_info.getId(), null,
								System.currentTimeMillis()+""));
						onResume();
					}
				})
				.setNegativeButton("舍弃更改", null)
				.setNeutralButton("删除", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						bService.delBlog(blog.getId());
						onResume();
					}
				})
				.show();
		}
		else if(!"".equals(blog.getAudio())){
			new AlertDialog.Builder(this)
			.setPositiveButton("查看", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri mUri = Uri.parse(blog.getAudio());
					intent.setDataAndType(mUri, "audio/*");
					startActivity(intent);
				}
			})
			.setNeutralButton("删除", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					bService.delBlog(blog.getId());
					onResume();
				}
			})
			.show();
		}
		else if(!"".equals(blog.getVideo())){
			new AlertDialog.Builder(this)
			.setPositiveButton("查看", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri mUri = Uri.parse(blog.getVideo());
					intent.setDataAndType(mUri, "video/*");
					startActivity(intent);
				}
			})
			.setNeutralButton("删除", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					bService.delBlog(blog.getId());
					onResume();
				}
			})
			.show();
		}
		else if(!"".equals(blog.getPoto())){
			new AlertDialog.Builder(this)
			.setPositiveButton("查看", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri mUri = Uri.parse("file://" + blog.getPoto());
					intent.setDataAndType(mUri, "image/*");
					startActivity(intent);
				}
			})
			.setNeutralButton("删除", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					bService.delBlog(blog.getId());
					onResume();
				}
			})
			.show();
			
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bService!=null)
			bService.closeDB();
	}
	// 用当前时间命名图片
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		  super.onActivityResult(requestCode, resultCode, data); 
		    if (resultCode != RESULT_OK)  
		        return; 
		    switch (requestCode) {
			case 123:
				bService.insertBlog(new Blog(-1, "", "", "", "", user_info.getId(),
						mCurrentPhotoFile.toString(), System.currentTimeMillis()+""));
				break;
			case 124:
				Uri uri = data.getData();
				bService.insertBlog(new Blog(-1, "", "", uri.toString(), "", user_info.getId(),
						"", System.currentTimeMillis()+""));
				break;
			case 125:
				Uri uri2 = data.getData();
				bService.insertBlog(new Blog(-1, "", "", "", uri2.toString(), user_info.getId(),
						"", System.currentTimeMillis()+""));
			default:
				break;
			}
		    
	}
	private byte[] getImageData(Bitmap bitmap){
		int size = bitmap.getWidth()*bitmap.getHeight()*4;
		ByteArrayOutputStream out = new ByteArrayOutputStream(size);
		try {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
}
