package co.edu.unipiloto.forms;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class Login_form extends AppCompatActivity {
    Button usuario;

    private TextInputLayout getUser;
    Context context;
    String TABLE_NAME="Registros_RapiCoop";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        usuario = (Button) findViewById(R.id.usuario);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        getSupportActionBar().setTitle("Login Form");


    }
    public void btn_ingresar (View view){
        Intent i = new Intent(Login_form.this, UsuarioHamburguesa.class);
            startActivity(i);

    }
    public void btn_singupForm(View view){

        startActivity(new Intent(this,Singup_Form.class));
    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }


}
