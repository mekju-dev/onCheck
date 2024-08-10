package com.wgu.testing123;

import static com.wgu.testing123.databaseFragment.dispatchAdapter;
import static com.wgu.testing123.databaseFragment.dispatchList;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class addDispatcher extends AppCompatActivity {
    dbHelper db = new dbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_dispatcher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            return insets;
        });

        //Save Button
        TextView saveAddDispatcher = findViewById(R.id.saveAddDispatcher);

        saveAddDispatcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText dispatcherFirstName = findViewById(R.id.addDispatcherFirstName);
                EditText dispatcherLastName = findViewById(R.id.addDispatcherLastName);
                EditText dispatcherUsername = findViewById(R.id.addDispatcherUsername);
                EditText dispatcherPass = findViewById(R.id.addDispatcherPass);

                if (dispatcherFirstName.getText().toString().isEmpty() || dispatcherLastName.getText().toString().isEmpty() || dispatcherUsername.getText().toString().isEmpty()|| dispatcherPass.getText().toString().isEmpty()) {
                    Toast.makeText(addDispatcher.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }else {
                   String text = db.insertDispatch(dispatcherFirstName.getText().toString(), dispatcherLastName.getText().toString(), dispatcherUsername.getText().toString(), dispatcherPass.getText().toString());
                   db.insertAuth(dispatcherUsername.getText().toString(), 0);
                   if(text.isEmpty()){

                       if (!(db.getDispatchIdFromUser(dispatcherUsername.getText().toString())== -1)) {
                           Dispatch dispatch = new Dispatch(db.getDispatchIdFromUser(dispatcherUsername.getText().toString()), dispatcherFirstName.getText().toString(), dispatcherLastName.getText().toString(), dispatcherUsername.getText().toString(), dispatcherPass.getText().toString());
                           if (dispatchList == null) {
                               dispatchList = new ArrayList<Dispatch>();

                           }dispatchList.add(dispatch);
                           if (!(dispatchAdapter == null)) {

                               dispatchAdapter.setData(generateDispatchList());
                               dispatchAdapter.notifyDataSetChanged();
                           }
                       }

                       /*
                       bundle.putInt("DispatcherID", dispatch.getId());
                       bundle.putString("DispatcherFirst", dispatch.getFirstName());
                       bundle.putString("DispatcherLast", dispatch.getLastName());
                       bundle.putString("DispatchUsername", dispatch.getUsername());
                       bundle.putString("DispatchPassword", dispatch.getPass());

                       databaseFragment fragobj = new databaseFragment();
                       fragobj.setArguments(bundle);
                       //Intent intent = new Intent(addDispatcher.this, databaseFragment.class);


                        */
                    onRestart();
                    finish();
                    }else {
                       Toast.makeText(addDispatcher.this, text, Toast.LENGTH_SHORT).show();
                   }
                }
            }
        });



        //Cancel Button
        ImageView exitAddDispatcher = findViewById(R.id.exitAddDispatcher);

        exitAddDispatcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRestart();
                finish();
            }
        });
    }
    public List<Dispatch> generateDispatchList() {
        db = new dbHelper(this);
        Cursor result = db.getAllDispatchers();
        List<Dispatch> dispatchers = new ArrayList<>();

        if (result.getCount() == 0) {
            Toast.makeText(this, "No Dispatcher Found", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            while (result.moveToNext()) {
                Dispatch dispatch = new Dispatch(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4));

                dispatchers.add(dispatch);
                //Toast.makeText(getActivity(), result.getString(0), Toast.LENGTH_SHORT).show();
            }
        }
        return dispatchers;
    }

}