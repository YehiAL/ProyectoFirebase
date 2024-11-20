package com.example.proyectofirebaseya;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText txtCodigo, txtNombre, txtDueno, txtDireccion;
    private ListView lista;
    private Spinner spMascota;
    //Variable de la conexion a FireStore
    private FirebaseFirestore db;
    //Datos del spinner
    String[] TiposMascotas = {"Perro","Gato","Pajaro"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CargarListaFirestore();
        db = FirebaseFirestore.getInstance();

        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombre = findViewById(R.id.txtNombre);
        txtDueno = findViewById(R.id.txtDueno);
        txtDireccion = findViewById(R.id.txtDireccion);



        spMascota = findViewById(R.id.spMascota);
        lista = findViewById(R.id.lista);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TiposMascotas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMascota.setAdapter(adapter);


    }

    public void enviarDatosFirestore(View view){
        String codigo = txtCodigo.getText().toString();
        String nombre = txtNombre.getText().toString();
        String dueno = txtDueno.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String tipoMascota = spMascota.getSelectedItem().toString();

        Map<String, Object> mascota = new HashMap<>();
        mascota.put("codigo",codigo);
        mascota.put("nombre",nombre);
        mascota.put("dueno",dueno);
        mascota.put("direccion",direccion);
        mascota.put("tipoMascota",tipoMascota);

        db.collection("mascotas")
                .document(codigo)
                .set(mascota)
                .addOnSuccessListener(aVoid->{
                    Toast.makeText(MainActivity.this,"Datos enviadors a Firestore correctamente",Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e->{
                    Toast.makeText(MainActivity.this,"Error al enviar datos a Firestore:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarLista(View view){
        CargarListaFirestore();
    }

    private void CargarListaFirestore() {
    }
}