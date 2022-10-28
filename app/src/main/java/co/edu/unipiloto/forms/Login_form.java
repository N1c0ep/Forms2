package co.edu.unipiloto.forms;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class Login_form extends AppCompatActivity {
    Button usuario;

    private TextInputLayout getUser;
    private TextInputLayout userName;
    private TextInputLayout password;
    private Cursor fila;
    Context context;
    String TABLE_NAME="Registros_RapiCoop";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        usuario = (Button) findViewById(R.id.usuario);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        getSupportActionBar().setTitle("Login Form");

        password=(TextInputLayout)findViewById(R.id.user_password);
        userName=(TextInputLayout)findViewById(R.id.user_login);


    }
    public void InicioSesion(View v){
        /*Creamos un objeto de la clase DBHelper e
        instanciamos el constructor y damos el nonbre de
         la base de datos y la version*/
        RapiCoopDatabaseHelper admin=new RapiCoopDatabaseHelper(this,"instituto",null,1);
        /*Abrimos la base de datos como escritura*/
        SQLiteDatabase db=admin.getWritableDatabase();
        /*Creamos dos variables string y capturamos los datos
         ingresado por el usuario y lo convertimos a string*/
        String usuario=userName.getEditText().getText().toString();
        String contrasena=password.getEditText().getText().toString();
        /*inicializamos al cursor y llamamos al objeto de la base
        de datos para realizar un sentencia query where donde
         pasamos las dos variables nombre de usuario y password*/
        fila=db.rawQuery("select USERNAME,PASSWORD from Registros_RapiCoop where USERNAME='"+
                usuario+"' and PASSWORD='"+contrasena+"'",null);
        /*Realizamos un try catch para captura de errores*/
        try {
            /*Condicional if preguntamos si cursor tiene algun dato*/
            if(fila.moveToFirst()){
                //capturamos los valores del cursos y lo almacenamos en variable
                String usua=fila.getString(0);
                String pass=fila.getString(1);
                //preguntamos si los datos ingresados son iguales
                if (usuario.equals(usua)&&contrasena.equals(pass)){
                    //si son iguales entonces vamos a otra ventana
                    //Menu es una nueva actividad empty
                    Intent ven=new Intent(this, UsuarioHamburguesa.class);
                    //lanzamos la actividad
                    startActivity(ven);

                }
            }//si la primera condicion no cumple entonces que envie un mensaje toast
            else {
                Toast toast=Toast.makeText(this,"Datos incorrectos",Toast.LENGTH_LONG);
                //mostramos el toast
                toast.show();
            }

        } catch (Exception e) {//capturamos los errores que ubieran
            Toast toast=Toast.makeText(this,"Error" + e.getMessage(),Toast.LENGTH_LONG);
            //mostramos el mensaje
            toast.show();
        }
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
