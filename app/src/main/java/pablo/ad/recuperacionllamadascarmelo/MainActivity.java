package pablo.ad.recuperacionllamadascarmelo;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import pablo.ad.recuperacionllamadascarmelo.listeners.refreshCalls;
import pablo.ad.recuperacionllamadascarmelo.permissions.PermissionsAppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "xyzyx";
    public static refreshCalls listener;
    /*TODO:
     * -Hebras para guardar los archivos y para buscar el nombre del contacto
     * -Ajustes con shared preferences
     * -Busqueda por numero
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }



}