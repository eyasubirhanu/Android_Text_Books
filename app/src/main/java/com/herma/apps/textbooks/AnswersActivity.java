package com.herma.apps.textbooks;import android.content.Context;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.Color;import android.graphics.Typeface;import android.graphics.drawable.BitmapDrawable;import android.graphics.drawable.Drawable;import android.graphics.drawable.LevelListDrawable;import android.os.AsyncTask;import android.os.Bundle;import android.text.Html;import android.util.Log;import android.util.TypedValue;import android.view.View;import android.view.ViewGroup;import android.widget.LinearLayout;import android.widget.TextView;import androidx.appcompat.app.AppCompatActivity;import androidx.appcompat.widget.Toolbar;import androidx.core.content.ContextCompat;import com.herma.apps.textbooks.R;import com.herma.apps.textbooks.common.questions.RadioBoxesFragment;import java.io.FileNotFoundException;import java.io.IOException;import java.io.InputStream;import java.net.MalformedURLException;import java.net.URL;import io.reactivex.Completable;import io.reactivex.CompletableObserver;import io.reactivex.android.schedulers.AndroidSchedulers;import io.reactivex.disposables.Disposable;import io.reactivex.schedulers.Schedulers;public class AnswersActivity extends AppCompatActivity{    Context context;    LinearLayout resultLinearLayout;    String[] answerKey, response, responseShouldBe, questions, queId;    @Override    protected void onCreate(Bundle savedInstanceState)    {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_answers);        context = this;        resultLinearLayout = findViewById(R.id.resultLinearLayout);        toolBarInit();        getResult();    }    private void toolBarInit()    {        Toolbar answerToolBar = findViewById(R.id.answerToolbar);        answerToolBar.setNavigationIcon(R.drawable.ic_arrow_back);        answerToolBar.setNavigationOnClickListener(v -> onBackPressed());    }    /*After, getting all result you can/must delete the saved results    although we are clearing the Tables as soon we start the QuestionActivity.*/    private void getResult()    {        Completable.fromAction(() -> {        }).subscribeOn(Schedulers.io())                .observeOn(AndroidSchedulers.mainThread())                .subscribe(new CompletableObserver()                {                    @Override                    public void onSubscribe(Disposable d)                    {                    }                    @Override                    public void onComplete()                    {                        MakeResultView();                    }                    @Override                    public void onError(Throwable e)                    {                    }                });    }    /*Here, JSON got created and send to make Result View as per Project requirement.     * Alternatively, in your case, you make Network-call to send the result to back-end.*/    private void MakeResultView()    {        if (getIntent().getExtras() != null)        {            queId = getIntent().getStringArrayExtra("queId");            questions = getIntent().getStringArrayExtra("questions");//            questions = getIntent().getStringArrayExtra("questions");            answerKey = getIntent().getStringArrayExtra("answerKey");            response = getIntent().getStringArrayExtra("response");            responseShouldBe = getIntent().getStringArrayExtra("responseShouldBe");        }        questionsAnswerView();    }    private void questionsAnswerView()    {        if (answerKey.length > 0)        {            for (int i = 0; i < answerKey.length; i++)            {                // question will be here....                String question = "#"+(i+1) + " " + questions[i];                String correct_answer = "("+answerKey[i]+") " + responseShouldBe[i];                TextView questionTextView = new TextView(context);                questionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);                questionTextView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));                questionTextView.setPadding(40, 30, 16, 30);                questionTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));                questionTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));                questionTextView.setTypeface(null, Typeface.BOLD);//                questionTextView.setText(question + "\n\n "+ "ትክክለኛ መልስ * " +correct_answer);                System.out.println(question + "\n\n "+ "ትክክለኛ መልስ * " +correct_answer);                questionTextView.setText(Html.fromHtml(question + "\n\n "+ "ትክክለኛ መልስ * " +correct_answer , new Html.ImageGetter() {                    @Override                    public Drawable getDrawable(String source) {                        LevelListDrawable d = new LevelListDrawable();                        Drawable empty = getResources().getDrawable(R.drawable.icon);                        d.addLevel(0, 0, empty);                        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());                        source = source.substring(2, source.length()-2);//        System.out.println("source is " + source );//        source.replace("localhost","datascienceplc.com");//        System.out.println("source is " + source );                        new LoadImage().execute(source, d, questionTextView);                        return d;                    }                }, null));                resultLinearLayout.addView(questionTextView);//                if(!answerKey[i].equals("***"+response[i]))                if (!responseShouldBe[i].equals(response[i]))                {                    String answer = response[i] ;                    String formattedAnswer = "የመረጡት መልስ " + "• " + answer; // alt + 7 --> •                    TextView answerTextView = new TextView(context);                    answerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);                    answerTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));                    answerTextView.setPadding(60, 30, 16, 30);                    answerTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));                    answerTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));//                    answerTextView.setText(formattedAnswer);                    answerTextView.setText(Html.fromHtml(formattedAnswer , new Html.ImageGetter() {                        @Override                        public Drawable getDrawable(String source) {                            LevelListDrawable d = new LevelListDrawable();                            Drawable empty = getResources().getDrawable(R.drawable.icon);                            d.addLevel(0, 0, empty);                            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());                            source = source.substring(2, source.length()-2);//        System.out.println("source is " + source );//        source.replace("localhost","datascienceplc.com");//        System.out.println("source is " + source );                            new LoadImage().execute(source, d, answerTextView);                            return d;                        }                    }, null));                    View view = new View(context);                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));                    view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));                    resultLinearLayout.addView(answerTextView);                    resultLinearLayout.addView(view);                }            }        }    }    class LoadImage extends AsyncTask<Object, Void, Bitmap> {        private LevelListDrawable mDrawable;        TextView tv;        @Override        protected Bitmap doInBackground(Object... params) {            String source = (String) params[0];            mDrawable = (LevelListDrawable) params[1];            tv = (TextView) params[2];            Log.d("print from", "doInBackground " + source);            try {                InputStream is = new URL(source).openStream();                return BitmapFactory.decodeStream(is);            } catch (FileNotFoundException e) {                e.printStackTrace();            } catch (MalformedURLException e) {                e.printStackTrace();            } catch (IOException e) {                e.printStackTrace();            }            return null;        }        @Override        protected void onPostExecute(Bitmap bitmap) {            Log.d("Print from post execute", "onPostExecute drawable " + mDrawable);//            Log.d(TAG, "onPostExecute bitmap " + bitmap);            if (bitmap != null) {                BitmapDrawable d = new BitmapDrawable(bitmap);                mDrawable.addLevel(1, 1, d);                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());                mDrawable.setLevel(1);                // i don't know yet a better way to refresh TextView                // mTv.invalidate() doesn't work as expected                CharSequence t = tv.getText();                tv.setText(t);            }        }    }}