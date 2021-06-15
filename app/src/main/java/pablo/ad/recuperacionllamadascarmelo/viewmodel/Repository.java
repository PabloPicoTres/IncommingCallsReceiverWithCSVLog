package pablo.ad.recuperacionllamadascarmelo.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.LayoutDirection;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pablo.ad.recuperacionllamadascarmelo.MainActivity;
import pablo.ad.recuperacionllamadascarmelo.listeners.refreshCalls;
import pablo.ad.recuperacionllamadascarmelo.model.Llamada;

import static pablo.ad.recuperacionllamadascarmelo.MainActivity.TAG;

public class Repository {




    private Context context;

    private static refreshCalls listener;

    public Repository(Context context){
        this.context = context;
    }



    public List<Llamada> leerArchivos(){
        List<Llamada> callLogs = new ArrayList<>();
        switch (MyViewmodel.memoria){
            case "interna":
                callLogs = leerArchivoInterna();
                break;
            case "externa":
                callLogs = leerArchivoExterna();
                break;
        }
        return callLogs;
    }

    @NotNull
    private List<Llamada> leerArchivoInterna() {
        List<Llamada> callLog = new ArrayList<>();
        File f = new File(context.getFilesDir(),"historial.csv");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while ((linea = br.readLine()) != null) {
                callLog.add(csvInternaToLlamada(linea, "; "));
            }
            br.close();
        } catch(IOException e) {
        }
        return callLog;
    }

    @NotNull
    private List<Llamada> leerArchivoExterna() {
        List<Llamada> callLog = new ArrayList<>();
        File f = new File(context.getExternalFilesDir(null),"llamadas.csv");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while ((linea = br.readLine()) != null) {
                callLog.add(csvExternaToLlamada(linea, "; "));
            }
            br.close();
        } catch(IOException e) {
        }
        return callLog;
    }


    public boolean guardarArchivo( @NotNull List<Llamada> callLogs) {


        boolean result = true;
        //Como necesitamos ordenar para la memoria externa pero no para la interna,
        //necesitamos dos bucles, primero la interna sin ordenar, ordenamos, y luego la externa

        File f = new File(context.getExternalFilesDir(null),"llamadas.csv");
        FileWriter fwf = null;
        File i = new File(context.getFilesDir(),"historial.csv");
        FileWriter fwi = null;

        //Grabamos la memoria interna:
        try {

            fwi = new FileWriter(i);

            for(Llamada call: callLogs) {

                fwi.write(call.toCsvInterna() + "\n");
                Log.v(MainActivity.TAG, "GUARDANDO EN INTERNA: " + call.toCsvInterna());
            }

            fwi.flush();
            fwi.close();

        } catch (IOException e) {
            result = false;
        }

        //ordenamos el arraylist:
        Collections.sort(callLogs); //ordena usando el compareTo() de Llamada


        //Grabamos la memoria externa:
        try {

            fwf = new FileWriter(f);

            for(Llamada call: callLogs) {
                fwf.write(call.toCsvExterna() + "\n");
                Log.v(MainActivity.TAG, "GUARDADO EN EXTERNA: " + call.toCsvExterna());
            }

            fwf.flush();
            fwf.close();

        } catch (IOException e) {
            result = false;
        }

        return result;

    }


    public void añadirLlamadaYGuardarArchivo(Llamada newCall){
        Log.v(MainActivity.TAG, "añadirLlamadaYGuardaArchivo");
        List<Llamada> callLogs;
        callLogs = leerArchivos();
        callLogs.add(newCall);
        guardarArchivo(callLogs);
    }


    public String getTextoCallLogs(){
        StringBuilder sb = new StringBuilder();

        List<Llamada> callLogs = new ArrayList<>();
        Log.v("xyzyx",MyViewmodel.memoria);

        switch (MyViewmodel.memoria){
            case "interna":
                callLogs = leerArchivoInterna();
                break;
            case "externa":
                callLogs = leerArchivoExterna();
                break;
        }

        Log.v(MainActivity.TAG + "CALL LOGS READ", callLogs.toString());

        if(!callLogs.isEmpty()){
            int i = 1;
            for(Llamada call: callLogs){
                sb.append(i+ ": " + call.toCsvInterna() + "\n");
                i++;
            }
        }

        return sb.toString();
    }

    public String getContactName(String phone) {
        String name = "Desconocido";

        String[] data = new String[]{ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String order = ContactsContract.Data.DISPLAY_NAME + " ASC";
        String selectionNumber = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";

        Cursor cursor =  context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                data,
                selectionNumber,
                null,
                order);
        while(cursor.moveToNext()){
            if (cursor.getString(1).equals(phone)){
                name = cursor.getString(0);
            }
        }
        cursor.close();

        return name;
    }

    private static Llamada csvInternaToLlamada(String csv, String separator) {
        Llamada call = new Llamada();
        String[] partes = csv.split(separator);

        if(partes.length == 3) {
            Log.v(MainActivity.TAG, "Nombre: " + partes[2].trim() +
                    "Telefono: " + partes[1].trim() +
                    "Fecha: " + partes[0].trim());
            call = new Llamada(partes[0].trim(), partes[1].trim(),  partes[2].trim());
        }
        return call;
    }


    private static Llamada csvExternaToLlamada(String csv, String separator) {
        Llamada call = new Llamada();
        String[] partes = csv.split(separator);
        if(partes.length == 3) {
            Log.v(MainActivity.TAG, "Nombre: " + partes[0].trim() +
                    "Telefono: " + partes[2].trim() +
                    "Fecha: " + partes[1].trim());
            call = new Llamada(partes[1].trim(), partes[2].trim(), partes[0].trim());
        }
        return call;
    }

    public void newCall(String tlf){
        Log.v(TAG, "numero recibido: " + tlf);

        Date dateTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String date = formatter.format(dateTime);

        String name = getContactName(tlf);

        Llamada llamada = new Llamada(date, tlf, name);

        Log.v(TAG, "Añadimos la llamada: " + llamada);

        añadirLlamadaYGuardarArchivo( llamada );

        if (listener != null){
            listener.refresh();
        }

    }

    public void setListener(refreshCalls listener){
        this.listener = listener;
    }
}
