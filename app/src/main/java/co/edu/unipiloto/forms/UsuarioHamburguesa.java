package co.edu.unipiloto.forms;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UsuarioHamburguesa extends AppCompatActivity {

    TextView tvDatos;
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        tvDatos = findViewById(R.id.textView2);

 //       Bundle recibeDatos = getIntent().getExtras();
//        String [] info = recibeDatos.getStringArray("keyDatos");

        //tvDatos.setText(info[0]+"\nNombre: " +info[1]+"\nDescripcion: "+ info[2] +"\nPrecio: $"+ info[3]+"\nCantidad: "+info[4]);
    }
}
