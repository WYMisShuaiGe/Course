package com.example.course;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.course.R.layout.activity_course;

public class CourseActivity extends AppCompatActivity {


    private TextView mo,tue,we,th,fr,textView;
    private ImageView iv;

    private String mocourse="";
    private String tucourse="";
    private String wecourse="";
    private String thcourse="";
    private String frcourse="";
    private String mocourse1="";
    private String tucourse1="";
    private String wecourse1="";
    private String thcourse1="";
    private String frcourse1="";

    private  AutoCompleteTextView acTextView;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private HttpClient client=new DefaultHttpClient();
    private List<Teachers> Teacherlist=new ArrayList<Teachers>();
    private String TeacherName="";
    private String Teacheridentity="";
    private String Teacherid="";
    private Bitmap img_yzm;

    private List<Courses> les=new ArrayList<Courses>();
    private  EditText editTextYZM;

    GestureDetector myGestureDetector;

    ProgressDialog dialogWithProgress;
    ViewFlipper viewFlipper;
    Animation slideLeftIn;
    Animation slideLeftOut;
    Animation slideRightIn;
    Animation slideRightOut;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_course);

        acTextView = (AutoCompleteTextView) findViewById(R.id.acTextView);
        mo = (TextView) findViewById(R.id.mo);
        tue = (TextView) findViewById(R.id.tue);
        we = (TextView) findViewById(R.id.we);
        th = (TextView) findViewById(R.id.th);
        fr = (TextView) findViewById(R.id.fr);
        //IV=(ImageView)findViewById(R.id.IV);
        //ET=(EditText)findViewById(R.id.ET);
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        acTextView.setThreshold(1);
        acTextView.setAdapter(adapter);
        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TeacherName = parent.getItemAtPosition(position).toString();
                for (Teachers t : Teacherlist) {
                    Teacheridentity=t.getIdentityCode();
                    Teacherid=t.getTeacherId();
                   // Log.i("Tom",Teacherid);
                    if (t.getTeachername().equals(TeacherName)) {

                        if (Teacheridentity.equals("0")) {
                            getyzm();
                            t.setIdentityCode("1");
                        } else if(Teacheridentity.equals("1")) {
                            getTeachercourse();
                        }
                        break;
                    }
                }
            }
        });

        getTeacher();

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        //手势
        myGestureDetector = new GestureDetector(new myGestureDetector());
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }
    private void getTeachercourse() {

        new Thread() {
            @Override
            public void run() {
                 mocourse1="";
                 tucourse1="";
                 wecourse1="";
                 thcourse1="";
                 frcourse1="";
                HttpUriRequest request = new HttpGet("http://192.168.1.102:8080/WebCourse/getTeachercourse?id=" +Teacherid);

                try {
                    HttpResponse response=client.execute(request);

                    String se = EntityUtils.toString(response.getEntity(), "gb2312");
                    Log.i("Tom", se);

                    JSONArray jsa = new JSONArray(se);
                    Log.i("Tom", jsa.getString(0));
                    JSONObject obj=null;
                    String [] aa=jsa.getString(0).split(",");
                    Bundle bundle=null;
                    //ArrayList<String> msg=new ArrayList<String>();
                    Message msg=new Message();

                    for (int m=0;m<aa.length;m++){
                        Log.i("Toooooooom", aa[m]);
                         obj= new JSONObject(aa[m]);
                        if (obj.has("1"))
                        {
                            mocourse1+=obj.getString("1")+"。   ";
                          //  mo.setText(mocourse);
                        }
                        if (obj.has("2")) {
                            Log.i("Tom", obj.getString("2"));
                            tucourse1+=obj.getString("2")+"。   ";
                            //tue.setText(tucourse);
                        }if (obj.has("3")) {
                            Log.i("Tom", obj.getString("3"));
                            wecourse1+=obj.getString("3")+"。   ";
                            //tue.setText(tucourse);
                        }
                        if (obj.has("4")){
                            Log.i("Tom", obj.getString("4"));
                            thcourse1+=obj.getString("4")+"。   ";
                          //  th.setText(thcourse);
                        }
                        if (obj.has("5")) {
                            Log.i("Tom", obj.getString("5"));
                            frcourse1+=obj.getString("5")+"。   ";
                            //tue.setText(tucourse);
                        }
                    }
                 //  Log.i("Toom", mocourse + tucourse + thcourse);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout linearLayout = new LinearLayout(CourseActivity.this);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);

                            TextView textView0=new TextView(CourseActivity.this);
                            TextView textView1=new TextView(CourseActivity.this);
                            TextView textView2=new TextView(CourseActivity.this);
                            TextView textView3=new TextView(CourseActivity.this);
                            TextView textView4=new TextView(CourseActivity.this);
                            TextView textView5=new TextView(CourseActivity.this);
                            textView0.setText(TeacherName + "的课表");
                            textView1.setText("星期一：" + mocourse1);
                            textView2.setText("星期二：" + tucourse1);
                            textView3.setText("星期三：" + wecourse1);
                            textView4.setText("星期四：" + thcourse1);
                            textView5.setText("星期五：" + frcourse1);

                            linearLayout.addView(textView0);
                            linearLayout.addView(textView1);
                            linearLayout.addView(textView2);
                            linearLayout.addView(textView3);
                            linearLayout.addView(textView4);
                            linearLayout.addView(textView5);
                            viewFlipper.addView(linearLayout);
                            viewFlipper.showNext();

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getyzm() {
        dialogWithProgress = new ProgressDialog(CourseActivity.this);
        dialogWithProgress.setTitle("验证码");
        dialogWithProgress.setMessage("正在加载中...");
        dialogWithProgress.setIndeterminate(false);

        dialogWithProgress.setCancelable(true);
        dialogWithProgress.show();
        final android.os.Handler handler=new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                dialogWithProgress.dismiss();
                LayoutInflater inflater = LayoutInflater.from(CourseActivity.this);
                View view = inflater.inflate(R.layout.layout_yzm, (ViewGroup) findViewById(R.id.layout_root));
                //获取布局中的控件
                iv = (ImageView)view.findViewById(R.id.iv);
                textView = (TextView)view.findViewById(R.id.textView);
                editTextYZM = (EditText)view.findViewById(R.id.editTextYZM);
                iv.setImageBitmap((Bitmap) msg.getData().getParcelable("Bitmap"));
                AlertDialog.Builder builder2 = new AlertDialog.Builder(CourseActivity.this);
                builder2.setView(view);
                builder2.setTitle("验证码").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    getcoursebyYZM();
                    }
                }).create().show();

            }
        };
        new Thread(){

            @Override
            public void run() {
                HttpUriRequest request=new HttpGet("http://192.168.1.102:8080/WebCourse/getyzm");
                try {
                    HttpResponse reponse = client.execute(request);
                    InputStream in = reponse.getEntity().getContent();
                    img_yzm = BitmapFactory.decodeStream(in);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Bitmap", img_yzm);
                    Message msg = new Message();
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    private void getcoursebyYZM() {
        dialogWithProgress = new ProgressDialog(CourseActivity.this);
        dialogWithProgress.setTitle("课程表");
        dialogWithProgress.setMessage("正在加载中...");
        dialogWithProgress.setIndeterminate(false);

        dialogWithProgress.setCancelable(true);
        dialogWithProgress.show();
        new Thread(){
            @Override
            public void run() {


                String yzm=editTextYZM.getText().toString();
                Log.i("Tom", yzm);
                Log.i("Tom",Teacherid);
                HttpUriRequest request=new HttpGet("http://192.168.1.102:8080/WebCourse/getcoursebyYZM?id="+Teacherid+"&text_yzm="+yzm);
                try {
                    HttpResponse response=client.execute(request);
                    String se = EntityUtils.toString(response.getEntity());
                    Log.i("Tom",se);
                    JSONArray jsa = new JSONArray(se);
                //    Log.i("TTTT",jsa.get(m).toString());
                    mocourse=jsa.get(0).toString();
                    tucourse=jsa.get(1).toString();
                    wecourse=jsa.get(2).toString();
                    thcourse=jsa.get(3).toString();
                    frcourse=jsa.get(4).toString();
                    Log.i("mo",mocourse+"0000000000");
                    Log.i("tu",tucourse+"00000000");
                    Log.i("we",wecourse+"00000");
                    Log.i("th",thcourse+"0000000");
                    Log.i("fr",frcourse+"00");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!mocourse.equals(""))
                            {
                                try {
                                    String[] sb=mocourse.split(",");
                                    mocourse = "";
                                    for (int n=0;n<sb.length;n++) {
                                        JSONObject obj = new JSONObject(sb[n]);
                                        mocourse += obj.getString("1") + "。";
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!tucourse.equals(""))
                            {
                                try {
                                    String[] sb=tucourse.split(",");
                                    tucourse = "";
                                    for (int n=0;n<sb.length;n++) {
                                        JSONObject obj = new JSONObject(sb[n]);
                                        tucourse += obj.getString("2") + "。";
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!wecourse.equals(""))
                            {
                                try {
                                    String[] sb=wecourse.split(",");
                                    wecourse = "";
                                    for (int n=0;n<sb.length;n++) {
                                        JSONObject obj = new JSONObject(sb[n]);
                                        wecourse += obj.getString("3") + "。";
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!thcourse.equals(""))
                            {
                                try {
                                    String[] sb=thcourse.split(",");
                                    thcourse = "";
                                    for (int n=0;n<sb.length;n++) {
                                        JSONObject obj = new JSONObject(sb[n]);
                                        thcourse += obj.getString("4") + "。";
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!frcourse.equals(""))
                            {
                                try {
                                    String[] sb=frcourse.split(",");
                                    frcourse = "";
                                    for (int n=0;n<sb.length;n++) {
                                        JSONObject obj = new JSONObject(sb[n]);
                                        frcourse += obj.getString("5") + "。";
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            LinearLayout linearLayout = new LinearLayout(CourseActivity.this);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            TextView textView0=new TextView(CourseActivity.this);
                            TextView textView1=new TextView(CourseActivity.this);
                            TextView textView2=new TextView(CourseActivity.this);
                            TextView textView3=new TextView(CourseActivity.this);
                            TextView textView4=new TextView(CourseActivity.this);
                            TextView textView5=new TextView(CourseActivity.this);
                            textView0.setText( TeacherName+"的课表");
                            textView1.setText("星期一：" + mocourse);
                            textView2.setText("星期二：" + tucourse);
                            textView3.setText("星期三：" + wecourse);
                            textView4.setText("星期四：" + thcourse);
                            textView5.setText("星期五：" + frcourse);

                            linearLayout.addView(textView0);
                            linearLayout.addView(textView1);
                            linearLayout.addView(textView2);
                            linearLayout.addView(textView3);
                            linearLayout.addView(textView4);
                            linearLayout.addView(textView5);
                            viewFlipper.addView(linearLayout);
                            viewFlipper.showNext();

                            dialogWithProgress.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    private void getTeacher() {
        new Thread()
        {
            @Override
            public void run() {
                //HttpClient client=new DefaultHttpClient();
                HttpUriRequest request=new HttpGet("http://192.168.1.102:8080/WebCourse/getTeacher");
                try {
                    HttpResponse reponse = client.execute(request);
                   //
                    String se = EntityUtils.toString(reponse.getEntity());
                    Log.i("Tom",se);
                    JSONArray jsa = new JSONArray(se);

                    for(int m=0;m<jsa.length();m++)
                    {
                        JSONObject js ;
                        Teachers teacher =new Teachers();
                        js =jsa.getJSONObject(m);
                        teacher.setTeacherId(js.getString("teacherId"));
                        teacher.setTeachername(js.getString("teachername"));
                        teacher.setIdentityCode(js.getString("identityCode"));
                     //   Log.i("chyr", teacher.getTeachername() + "..." + teacher.getTeacherId() + "..." + teacher.getIdentityCode());
                        Teacherlist.add(teacher);
                    }
                    for(int i=0;i<Teacherlist.size();i++)
                    {
                        Teachers t=Teacherlist.get(i);
                        list.add(t.getTeachername().toString());
                    }
                    //Log.i(TAG, "run ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class myGestureDetector extends GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;
        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if(e1.getX()-e2.getX()>SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                    Toast.makeText(CourseActivity.this,"从右往左滑动！",Toast.LENGTH_LONG).show();
                    viewFlipper.setInAnimation(slideLeftIn);
                    viewFlipper.setOutAnimation(slideLeftOut);
                    viewFlipper.showNext();
                }
                if(e2.getX()-e1.getX()>SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                    Toast.makeText(CourseActivity.this,"从左往右滑动！", Toast.LENGTH_LONG).show();
                    viewFlipper.setInAnimation(slideRightIn);
                    viewFlipper.setOutAnimation(slideRightOut);
                    viewFlipper.showPrevious();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}

