package pablo.ad.recuperacionllamadascarmelo.permissions;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;

public abstract class PermissionsAppCompatActivity extends Fragment {


    /*          ---- HOW TO USE ----
    *
    * 1.- Add permissions to manifest and below
    * 2.- Choose the activity when you need to get permissions (Could be used to fragments changing AppCompatActivity and some contexts below)
    * 3.- Change your activity "extends" from AppCompatActivity to PermissionsAppCompatActivity
    * 4.- Call the checkPermissions() method to start requesting the permissions
    * 5.- Implement the method allPermissionsOk(); that is called automatically
    *
    * Made by Pablo Javier Jaimez Cobos
    * */



    public static String[] PERMISSIONS_NEED = {READ_PHONE_STATE, READ_CALL_LOG, READ_CONTACTS};     //Edit here your needed permissions

    public static boolean showRationale = true;                                                     //Turn false if you dont want to show rationale
    public static String rationaleTitle = "Se necesitan "+ PERMISSIONS_NEED.length + " permisos";   //rationale title
    public static String rationaleMsg = "Esta aplicacion necesita ciertos permisos para " +         //RATIONALE EXPLAIN
                                        "poder acceder a su completo funcionamiento. " +
                                        "En caso contrario, puede verse afectado el funcionamiento" +
                                        " o ejecutar el cierre de la aplicacion.";

    public final int REQUEST_CODE_ASK_PERMISSION = 111;                                             //You can also edit this code ( never mind )

    public static boolean isFirstTime = true;


    /*
    *
    *
    *
    *   Si no es la primera vez que se solicitan los permisos,
            la aplicación debe mostrar una explicación detallada por la que es necesario conceder los permisos.
                Si el usuario no concede los permisos la aplicación se debe cerrar.
                Si el usuario concede los permisos,
                    entonces debe mostrar el listado de llamadas que según los ajustes corresponde listar.

    *
    * */

    /*
    *
    * Implement this method on your activity and use it for whatever you want
    * This method is called when you have all the permissions OK ;)
    *
    * */
    public abstract void allPermissionsOk();


    /*
    * RETURNS TRUE IF WE HAVE ALL THE PERMISSIONS AND FALSE IF WE NOT
    * */

    public boolean checkPermissions(){

        boolean tengoPermiso = getUngrantedPermissions();
        if( tengoPermiso ){
            allPermissionsOk();
        } else {
            requestUngrantedPermissions();
        }

        return tengoPermiso;
    }

    private boolean getUngrantedPermissions() {
        boolean tengoPermiso = true;
        if( Build.VERSION.SDK_INT>=Build.VERSION_CODES.M ){
            for(String permiso : PERMISSIONS_NEED){
                if (ContextCompat.checkSelfPermission(getContext(), permiso) == PackageManager.PERMISSION_DENIED) {

                    tengoPermiso = false;

                }
            }
        }
        return tengoPermiso;
    }


    /*
     *   REQUEST THE PERMISSIONS NOT GRANTED
     * */
    public void requestUngrantedPermissions() {

        if (isFirstTime) {

            request();


        } else {

            if ( showRationale ) {
                explainPermission();
            }

        }
    }


    public void request(){
        if( Build.VERSION.SDK_INT>=Build.VERSION_CODES.M ){

            requestPermissions( PERMISSIONS_NEED , REQUEST_CODE_ASK_PERMISSION );


        }
    }


    public abstract void explainPermission();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSION)  {

            if (getUngrantedPermissions()){
                allPermissionsOk();
            } else {
                //denegado
                if ( !isFirstTime) {
                    // close app
                    //finish();
                    System.exit(0);
                } else {
                    isFirstTime = false;
                    checkPermissions();
                }
            }
        }
    }

}
