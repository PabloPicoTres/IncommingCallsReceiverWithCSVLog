package pablo.ad.recuperacionllamadascarmelo.view;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pablo.ad.recuperacionllamadascarmelo.MainActivity;
import pablo.ad.recuperacionllamadascarmelo.R;
import pablo.ad.recuperacionllamadascarmelo.listeners.refreshCalls;
import pablo.ad.recuperacionllamadascarmelo.permissions.PermissionsAppCompatActivity;
import pablo.ad.recuperacionllamadascarmelo.viewmodel.MyViewmodel;

import static pablo.ad.recuperacionllamadascarmelo.MainActivity.TAG;
import static pablo.ad.recuperacionllamadascarmelo.viewmodel.MyViewmodel.memoria;

public class Inicio extends PermissionsAppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, refreshCalls {

    private TextView listado;
    private TextView subtitulo;
    private MyViewmodel viewmodel;

    NavController navController;

    private SharedPreferences sharedPreferences;


    public Inicio() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = new ViewModelProvider(this).get(MyViewmodel.class);
        MainActivity.listener = this;
        viewmodel.setListener(this);

        subtitulo = view.findViewById(R.id.tvSubtitulo);
        listado = view.findViewById(R.id.tvListado);

        navController = Navigation.findNavController(view);

        FloatingActionButton settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_inicio_to_settingsFragment);
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        checkPermissions();

    }

    @Override
    public void refresh() {

        subtitulo.setText("Visto deste : Memoria " + memoria);
        listado.setText(viewmodel.getCallList());
        Log.v(TAG, "memoria:" + memoria);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        memoria = sharedPreferences.getString(key, "interna");
        refresh();
    }



    /* ------------ PERMISSIONS ------------ */

    @Override
    public void allPermissionsOk() {
        refresh();
    }

    @Override
    public void explainPermission(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle( rationaleTitle );

        builder.setMessage( rationaleMsg );
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                request();

            }
        });

        builder.setNegativeButton("Close App", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // close app
                //finish();
                System.exit(0);

            }
        });
        builder.show();
    }

}