package com.example.proyectofirebaseya;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        //Mapa con los datos a enviar
        Map<String, Object> mascota = new HashMap<>();
        mascota.put("codigo",codigo);
        mascota.put("nombre",nombre);
        mascota.put("dueno",dueno);
        mascota.put("direccion",direccion);
        mascota.put("tipoMascota",tipoMascota);
        //Enviar datos a Firestore
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

    public void cargarLista(View view){
        CargarListaFirestore();
    }

    public void CargarListaFirestore() {
        //obtener instancia
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       //Consulta a la coleccion mascotas
       db.collection("mascotas")
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()){
                           //lista para almacenar las cadenas de informacion
                           List<String> listaMascotas = new ArrayList<>();

                           //Recorre los datos obtenidos ordenandolos en lista
                           for (QueryDocumentSnapshot document : task.getResult()){
                               String linea = "||" + document.getString("codigo") + "||"+
                                       document.getString("nombre") + "||"+
                                       document.getString("dueno") + "||"+
                                       document.getString("direccion");
                               listaMascotas.add(linea);
                           }
                           //Crear un arrayAdapter para la listaMascotas y pasarlo al layout
                           ArrayAdapter<String> adaptador = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,listaMascotas);
                           lista.setAdapter(adaptador);
                       }else{
                           Log.e("tag","Error al obtener datos de Firestore",task.getException());
                       }
                   }
               });
    }
}