package com.example.translate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

//不要忘了给模拟器添加SD卡
public class Main extends AppCompatActivity implements OnClickListener, TextWatcher
{
    //	定义数据库的绝对路径
    private final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/dictionary";//获得外部储存目录的绝对路径
    private AutoCompleteTextView actvWord;//输入控件
    private final String DATABASE_FILENAME = "dictionary.db";//数据库文件名
    private SQLiteDatabase database;//手机的数据库
    private Button btnSelectWord;
    private ListView list_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v->{
            finish();
        });
        verifyStoragePermissions();
        database = openDatabase();
        list_view=(ListView) findViewById(R.id.list_view);
        btnSelectWord = (Button) findViewById(R.id.btnSelectWord);
        actvWord = (AutoCompleteTextView) findViewById(R.id.actvWord);
        btnSelectWord.setOnClickListener(this);
        actvWord.addTextChangedListener(this);
    }


    //词典适配器
//		public class DictionaryAdapter extends CursorAdapter
//		{
//			private LayoutInflater layoutInflater;
//
//			@Override
//			public CharSequence convertToString(Cursor cursor)
//			{
//				return cursor == null ? "" : cursor.getString(cursor
//						.getColumnIndex("_id"));
//			}
//
//			private void setView(View view, Cursor cursor)
//			{
//				TextView tvWordItem = (TextView) view;
//				tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
//			}
//
//			@Override
//			public void bindView(View view, Context context, Cursor cursor)
//			{
//				setView(view, cursor);
//			}
//
//			@Override
//			public View newView(Context context, Cursor cursor, ViewGroup parent)
//			{
//				View view = layoutInflater.inflate(R.layout.word_list_item, null);
//				setView(view, cursor);
//				return view;
//			}
//
//			public DictionaryAdapter(Context context, Cursor c, boolean autoRequery)
//			{
//				super(context, c, autoRequery);
//				layoutInflater = (LayoutInflater) context
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			}
//		}


    //文字变化后
    public void afterTextChanged(Editable s) {
        if(database != null) {
            Cursor cursor = database.rawQuery("" +
                            "select english as _id from t_words where english like ?",
                    new String[]
                            {s.toString() + "%"});
            DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                    cursor, true);
            actvWord.setAdapter(dictionaryAdapter);
        }
    }
    //文字变化前
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }
    //文字变化时
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }
    //按钮点击事件
    public void onClick(View view)
    {
        String sql = "select chinese from t_words where english=?";
        Cursor cursor = database.rawQuery(sql, new String[]
                { actvWord.getText().toString() });
        String result = "未找到该单词.";
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("chinese"));
        }
        new AlertDialog.Builder(this).setTitle("查询结果").setMessage(result)
                .setPositiveButton("关闭", null).show();
//			Intent intent=new Intent();
//			intent.putExtra("English", value)
    }

    //调用数据库
    private SQLiteDatabase openDatabase() {
        try
        {
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists())//如果文件不存在
                dir.mkdir();//建立一个新的目录
            if (!(new File(databaseFilename)).exists())//如果新文件的数据库文件名不存在
            {
                InputStream is = getResources().openRawResource(
                        R.raw.dictionary);//读取R.raw.dictionary
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0)
                {
                    fos.write(buffer, 0, count);
                }

                fos.close();//关闭文件输出流
                is.close();//关闭文件输入流
            }
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                    databaseFilename, null);//从SD获取数据库

            return database;
        }
        catch (Exception e)
        {
        }
        return null;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public void verifyStoragePermissions() {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(Main.this,
                        "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(Main.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    database = openDatabase();
                } else {
                    finish();
                }
                break;
        }
    }
}
