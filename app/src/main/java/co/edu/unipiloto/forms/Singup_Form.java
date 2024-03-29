package co.edu.unipiloto.forms;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class Singup_Form extends AppCompatActivity {

    TextView tv;
    private RapiCoopDatabaseHelper myDb;
    private TextInputLayout editFullName;
    private TextInputLayout editUserName;
    private TextInputLayout editEmail;
    private TextInputLayout editPassword;
    private Spinner editTipoUsuario;
    private Button btnAddData;
    private Button btnViewAll;
    private String genero;
    private View view;
    TextView direccion;
    int anio, mes , dia;
    Calendar cal =  Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup_form);
        getSupportActionBar().setTitle("Formulario Registro");
        myDb=new RapiCoopDatabaseHelper(this, "instituto", null, 1);
        tv=findViewById(R.id.button_fecha);
        direccion = (TextView) findViewById(R.id.txtDireccion);

        editFullName=(TextInputLayout)findViewById(R.id.editText_fullname);
        editUserName=(TextInputLayout)findViewById(R.id.editText_userName);
        editEmail=(TextInputLayout)findViewById(R.id.editText_email);
        editPassword=(TextInputLayout)findViewById(R.id.editText_password);
        editTipoUsuario=(Spinner) findViewById(R.id.color);

        btnAddData=(Button)findViewById(R.id.button_registrar);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
    }
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

        direccion.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void abrir_calendario(View view) {

        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH);
        dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(Singup_Form.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fecha = dayOfMonth+"/"+month+"/"+year;
                tv.setText(fecha);
                anio=year;
                mes=month;
                dia=dayOfMonth;
            }
        }, anio, mes, dia);

        dpd.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btn_login_form(View view){

        int edad = edad_user(anio,mes,dia);
        String tipoUsuario= editTipoUsuario.getSelectedItem().toString();
        if(edad>=18){
            User user=new User();
            user.setFullName(editFullName.getEditText().getText().toString());
            user.setUserName(editUserName.getEditText().getText().toString());
            user.setEmail(editEmail.getEditText().getText().toString());
            user.setPassword(editPassword.getEditText().getText().toString());
            user.setRol(editTipoUsuario.getItemAtPosition(editTipoUsuario.getSelectedItemPosition()).toString());
            user.setAno_nacimiento(edad);
            user.setDireccion(direccion.getText().toString());
            user.setGenero(genero);
            boolean isInserted=myDb.insertData(user);
            if(isInserted)
                Toast.makeText( Singup_Form.this,  "Informacion registrada", Toast.LENGTH_LONG).show();
            else
                Toast.makeText( Singup_Form.this,  "Informacion no registrada", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,Login_form.class));
        }
        else if(tipoUsuario.equals("Comprador")){
            startActivity(new Intent(this,Login_form.class));
        }
        else{
            showMessage("Error","Su edad es"+edad+"Debe ser mayor de edad para registrase como vendedor o domiciliario");
        }
    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        Singup_Form mainActivity;
        public Singup_Form getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(Singup_Form mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            String sLatitud = String.valueOf(loc.getLatitude());
            String sLongitud = String.valueOf(loc.getLongitude());

            this.mainActivity.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado

        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }








    public void onRadioButtonClicked(View view){
        boolean checked =((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.radioButton_hombre:
                if(checked)
                    genero="hombre";
                    break;

            case R.id.radioButton_mujer:
                if(checked)
                    genero="mujer";
                    break;
            case R.id.radioButton_otro:
                if(checked)
                    genero="otro";
                    break;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public int edad_user(int anio, int mes, int dia){
        Period edad = Period.between(LocalDate.of(anio, mes, dia), LocalDate.now());

                return edad.getYears();

    }
}
