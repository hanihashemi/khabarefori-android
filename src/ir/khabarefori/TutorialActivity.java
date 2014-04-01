package ir.khabarefori;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by hani on 4/1/14.
 */
public class TutorialActivity extends Activity implements View.OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

        Button btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.finish();
    }
}
